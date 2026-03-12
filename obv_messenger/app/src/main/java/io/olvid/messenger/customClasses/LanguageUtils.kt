/*
 *  Olvid for Android
 *  Copyright © 2019-2026 Olvid SAS
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

package io.olvid.messenger.customClasses

import java.util.Locale

object LanguageUtils {
	private val languageToId = mapOf(
		"af" to 2,
		"ar" to 3,
		"ca" to 4,
		"cs" to 5,
		"da" to 6,
		"de" to 7,
		"el" to 8,
		"en" to 0,
		"es" to 9,
		"fa" to 10,
		"fi" to 11,
		"fr" to 1,
		"hi" to 12,
		"hu" to 13,
		"hr" to 14,
		"it" to 15,
		"iw" to 16,
		"ja" to 17,
		"ko" to 18,
		"nl" to 19,
		"no" to 20,
		"pl" to 21,
		"pt" to 22,
		"pt-rBR" to 23,
		"ro" to 24,
		"ru" to 25,
		"sk" to 26,
		"sl" to 27,
		"sv" to 28,
		"tr" to 29,
		"uk" to 30,
		"vi" to 31,
		"zh" to 32,
		"zh-rTW" to 33
	)

	fun getCurrentLanguageId(): Int {
		val locale = Locale.getDefault()
		// Handle special cases like pt-BR -> pt-rBR
		val languageTag = locale.toLanguageTag().replaceFirst("-", "-r")

		return languageToId[languageTag] // Try full tag first
			?: languageToId[locale.language] // Fallback to language code
			?: 0 // Default to English (id 0)
	}
}
