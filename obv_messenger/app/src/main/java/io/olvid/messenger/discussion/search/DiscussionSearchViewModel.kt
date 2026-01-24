/*
 *  Olvid for Android
 *  Copyright © 2019-2025 Olvid SAS
 *
 *  This file is part of Olvid for Android.
 *
 *  Olvid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License, version 3,
 *  as published by the Free Software Foundation.
 *
 *  Olvid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with Olvid.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.olvid.messenger.discussion.search

import android.content.Context
import androidx.annotation.ColorRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.olvid.messenger.R
import io.olvid.messenger.customClasses.StringUtils
import io.olvid.messenger.customClasses.StringUtils2.Companion.computeHighlightRanges
import io.olvid.messenger.customClasses.fullTextSearchEscape
import io.olvid.messenger.databases.AppDatabase
import io.olvid.messenger.databases.GlobalSearchTokenizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscussionSearchViewModel : ViewModel() {
    private var currentPosition: Int = 0
    var focusSearchOnOpen by mutableStateOf(true)
    var searchExpanded by mutableStateOf(false)
    var searchText by mutableStateOf("")
    var filterRegexes by mutableStateOf<List<Regex>?>(null)

    var hasNext by mutableStateOf(false)
    var hasPrevious by mutableStateOf(false)

    var matchedMessageAndFyleIds by mutableStateOf<List<Pair<Long, Long?>>>(emptyList()) // messageId, fyleId

    var initialFoundItem by mutableStateOf<Long?>(null)

    fun reset() {
        currentPosition = 0
        focusSearchOnOpen = true
        searchExpanded = false
        searchText = ""
        filterRegexes = null
        hasNext = false
        hasPrevious = false
        matchedMessageAndFyleIds = emptyList()
        initialFoundItem = null
    }

    fun next(): Long? {
        matchedMessageAndFyleIds.getOrNull(currentPosition)?.first?.let { currentMessageId ->
            // search for the largest smaller index that points to a different messageId
            var pos = currentPosition + 1
            while (pos < matchedMessageAndFyleIds.size && matchedMessageAndFyleIds[pos].first == currentMessageId) {
                pos++
            }

            if (pos < matchedMessageAndFyleIds.size) {
                currentPosition = pos
                updateHasNextAndPrevious()
                return matchedMessageAndFyleIds[currentPosition].first
            }
        }
        return null
    }

    fun previous(): Long? {
        matchedMessageAndFyleIds.getOrNull(currentPosition)?.first?.let { currentMessageId ->
            // search for the largest smaller index that points to a different messageId
            var pos = currentPosition - 1
            while (pos >= 0 && matchedMessageAndFyleIds[pos].first == currentMessageId) {
                pos--
            }

            if (pos >= 0) {
                currentPosition = pos
                updateHasNextAndPrevious()
                return matchedMessageAndFyleIds[currentPosition].first
            }
        }
        return null
    }

    private fun updateHasNextAndPrevious() {
        // only show next/previous if there is an item in the list AND this item points to a different message
        hasNext = (currentPosition in 0..< matchedMessageAndFyleIds.lastIndex)
                && (matchedMessageAndFyleIds.last().first != matchedMessageAndFyleIds[currentPosition].first)
        hasPrevious = (currentPosition in 1.. matchedMessageAndFyleIds.lastIndex)
                && (matchedMessageAndFyleIds.first().first != matchedMessageAndFyleIds[currentPosition].first)
    }

    @Synchronized
    fun filter(
        discussionId: Long,
        filterString: String?,
        firstVisibleMessageId: Long,
        messageIdToSetAsCurrent: Long? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            filterRegexes = filterString
                ?.trim()
                ?.split("\\s+".toRegex())
                ?.filter { it.isNotEmpty() }
                ?.map {
                    Regex(
                        """(\b|(?<=_)(?!_))${Regex.escape(StringUtils.unAccent(it))}""",
                        RegexOption.IGNORE_CASE
                    )
                }
            if (filterString.isNullOrBlank()) {
                matchedMessageAndFyleIds = emptyList()
                currentPosition = 0
                updateHasNextAndPrevious()
            } else {
                val tokenizedQuery =
                    GlobalSearchTokenizer.tokenize(filterString).fullTextSearchEscape()
                val result = AppDatabase.getInstance().globalSearchDao()
                    .discussionSearch(discussionId, tokenizedQuery).map { it.id to it.fyleId }
                if (matchedMessageAndFyleIds != result || messageIdToSetAsCurrent != null) {
                    matchedMessageAndFyleIds = result
                    val found: Boolean
                    // if the discussion was opened with a target message, always select this one
                    if (messageIdToSetAsCurrent != null) {
                        result.indexOfFirst { it.first == messageIdToSetAsCurrent }.let {
                            if (it == -1) {
                                found = false
                            } else {
                                found = true
                                currentPosition = it
                                updateHasNextAndPrevious()
                            }
                        }
                    } else {
                        found = false
                    }


                    // if the discussion wasn't opened with a target message or if the target message was not found,
                    // - pick the next visible message in the discussion (the forwardMatch)
                    // - if no match, pick the message 0 (the most recent)
                    if (!found) {
                        val forwardMatch =
                            matchedMessageAndFyleIds.indexOfLast { messageAndFyleId -> messageAndFyleId.first >= firstVisibleMessageId } // TODO: comparing messageIds is a little risky, it would be better to compare their sortIndex...
                        currentPosition = if (forwardMatch != -1) forwardMatch else 0
                        updateHasNextAndPrevious()
                    }

                    // if the current position is in the right interval (it should always be!!) instruct the discussion to scroll
                    if (currentPosition in 0..< matchedMessageAndFyleIds.size) {
                        initialFoundItem = matchedMessageAndFyleIds[currentPosition].first
                    }
                }
            }
        }
    }

    fun highlightColored(
        context: Context,
        content: AnnotatedString,
        @ColorRes textColor: Int = R.color.black,
        backgroundAlpha: Float = 1f
    ): AnnotatedString {
        return AnnotatedString.Builder(content).apply {
            filterRegexes?.let {
                computeHighlightRanges(content.toString(), it).forEach { range ->
                    addStyle(
                        SpanStyle(
                            background = Color(
                                ContextCompat.getColor(
                                    context,
                                    R.color.searchHighlightColor
                                )
                            ).copy(alpha = backgroundAlpha),
                            color = Color(ContextCompat.getColor(context, textColor))
                        ),
                        range.first,
                        range.second
                    )
                }
            }
        }.toAnnotatedString()
    }

    fun highlight(context: Context, content: AnnotatedString): AnnotatedString {
        return highlightColored(context, content)
    }
}