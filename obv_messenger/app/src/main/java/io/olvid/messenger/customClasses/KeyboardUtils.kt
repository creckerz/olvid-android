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

package io.olvid.messenger.customClasses

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.core.content.edit
import androidx.preference.PreferenceManager

object KeyboardUtils {

    const val KEYBOARD_PORTRAIT_HEIGHT_KEY = "KEYBOARD_PORTRAIT_HEIGHT"
    const val KEYBOARD_LANDSCAPE_HEIGHT_KEY = "KEYBOARD_LANDSCAPE_HEIGHT"

    fun getHeight(context: Context, orientation: Int): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                KEYBOARD_LANDSCAPE_HEIGHT_KEY
            } else {
                KEYBOARD_PORTRAIT_HEIGHT_KEY
            },
            0
        )
    }

    fun saveHeight(context: Context, orientation: Int, height: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putInt(
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    KEYBOARD_LANDSCAPE_HEIGHT_KEY
                } else {
                    KEYBOARD_PORTRAIT_HEIGHT_KEY
                }, height
            )
        }
    }
}

// https://developer.squareup.com/blog/showing-the-android-keyboard-reliably/
fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     * */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }


    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}