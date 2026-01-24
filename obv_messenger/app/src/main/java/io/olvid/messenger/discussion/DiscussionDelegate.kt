package io.olvid.messenger.discussion

import androidx.fragment.app.FragmentActivity

interface DiscussionDelegate {
    fun markMessagesRead()

    fun doNotMarkAsReadOnPause()

    fun scrollToMessage(messageId: Long)

    fun replyToMessage(messageId: Long, rawNewMessageText: String)

    fun initiateMessageForward(activity: FragmentActivity, messageId: Long, openDialogCallback: Runnable?)

    // bookmarked == null means message is not bookmarkable
    fun selectMessage(messageId: Long, forwardable: Boolean, bookmarked: Boolean?)

    // called after a new message is posted (to trigger a scroll to bottom if necessary)
    fun messageWasSent()
}