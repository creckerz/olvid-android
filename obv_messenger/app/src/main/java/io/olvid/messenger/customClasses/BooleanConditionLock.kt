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

import io.olvid.engine.Logger
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock


class BooleanConditionLock(initialState: Boolean) {
    val lock = ReentrantLock()
    val condition: Condition = lock.newCondition()
    var isTrue: Boolean
    init {
        isTrue = initialState
    }

    fun waitUntilTrue() {
        lock.lock()
        try {
            while (!isTrue) {
                Logger.e("Waiting for true")
                condition.await()
            }
        } catch (_: Exception) {
        } finally {
            lock.unlock()
        }
    }

    fun set(value: Boolean) {
        lock.lock()
        try {
            isTrue = value
            if (value) {
                condition.signalAll()
            }
        } catch (_: Exception) {
        } finally {
            lock.unlock()
        }
    }
}