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

package io.olvid.messenger.discussion

import android.content.DialogInterface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import io.olvid.engine.Logger
import io.olvid.messenger.App
import io.olvid.messenger.AppSingleton
import io.olvid.messenger.R
import io.olvid.messenger.customClasses.LocationIntegrationSelectorDialog
import io.olvid.messenger.customClasses.LocationIntegrationSelectorDialog.OnIntegrationSelectedListener
import io.olvid.messenger.customClasses.SecureAlertDialogBuilder
import io.olvid.messenger.databases.AppDatabase
import io.olvid.messenger.databases.entity.Message
import io.olvid.messenger.databases.entity.jsons.JsonLocation
import io.olvid.messenger.discussion.location.FullscreenMapDialogFragment
import io.olvid.messenger.discussion.message.copyLocationToClipboard
import io.olvid.messenger.services.UnifiedForegroundService.LocationSharingSubService
import io.olvid.messenger.settings.SettingsActivity
import io.olvid.messenger.settings.SettingsActivity.LocationIntegrationEnum
import io.olvid.messenger.settings.SettingsActivity.LocationIntegrationEnum.BASIC
import io.olvid.messenger.settings.SettingsActivity.LocationIntegrationEnum.CUSTOM_OSM
import io.olvid.messenger.settings.SettingsActivity.LocationIntegrationEnum.MAPS
import io.olvid.messenger.settings.SettingsActivity.LocationIntegrationEnum.NONE
import io.olvid.messenger.settings.SettingsActivity.LocationIntegrationEnum.OSM

class LocationMessageHandler(
    private val activity: DiscussionActivity,
    private val discussionViewModel: DiscussionViewModel
) {

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun onLocationClick(message: Message) {
        when (SettingsActivity.locationIntegration) {
            OSM, CUSTOM_OSM, MAPS -> {
                openMap(message)
            }

            BASIC -> {
                // if basic integration is configured
                if (message.hasAttachments()) {
                    // if have a preview: show preview
                    App.runThread {
                        val fyleAndStatuses =
                            AppDatabase.getInstance()
                                .fyleMessageJoinWithStatusDao()
                                .getFylesAndStatusForMessageSync(message.id)

                        discussionViewModel.markAsReadOnPause = false
                        if (fyleAndStatuses.size == 1) {
                            App.openDiscussionGalleryActivity(
                                activity,
                                discussionViewModel.discussionId ?: -1,
                                message.id,
                                fyleAndStatuses[0].fyle.id,
                                true,
                                false
                            )
                        } else {
                            // in case we don't have a single attachment, simply open the message gallery... This should never happen :)
                            App.openMessageGalleryActivity(
                                activity,
                                message.id,
                                -1,
                                false
                            )
                        }
                    }
                } else {
                    try {
                        message.jsonLocation?.let {
                            AppSingleton.getJsonObjectMapper()
                                .readValue(it, JsonLocation::class.java)
                        }?.let { jsonLocation ->
                            // else : open in a third party app
                            App.openLocationInMapApplication(
                                activity,
                                jsonLocation.truncatedLatitudeString,
                                jsonLocation.truncatedLongitudeString,
                                message.contentBody
                            ) { discussionViewModel.markAsReadOnPause = false }
                        }
                    } catch (e : Exception) {
                        Logger.x(e)
                    }
                }
            }

            NONE -> {
                // if no integration is configured, offer to choose an integration
                LocationIntegrationSelectorDialog(
                    activity,
                    false,
                    object : OnIntegrationSelectedListener {
                        override fun onIntegrationSelected(
                            integration: LocationIntegrationEnum,
                            customOsmServerUrl: String?
                        ) {
                            SettingsActivity.setLocationIntegration(
                                integration.string,
                                customOsmServerUrl
                            )
                            // re-run onClick if something was selected
                            if (integration == OSM || integration == MAPS || integration == BASIC || integration == CUSTOM_OSM) {
                                openMap(message)
                            }
                        }
                    }).show()
            }
        }
    }

    private fun openLocationPreviewInGallery(message: Message) {
        App.runThread {
            val fyleAndStatuses = AppDatabase.getInstance().fyleMessageJoinWithStatusDao()
                .getFylesAndStatusForMessageSync(message.id)
            discussionViewModel.markAsReadOnPause = false
            if (fyleAndStatuses.size == 1) {
                App.openDiscussionGalleryActivity(
                    activity,
                    discussionViewModel.discussionId ?: -1,
                    message.id,
                    fyleAndStatuses[0].fyle.id,
                    true,
                    false
                )
            } else {
                // in case we don't have a single attachment, simply open the message gallery... This should never happen :)
                App.openMessageGalleryActivity(activity, message.id, -1, false)
            }
        }
    }

    fun openMap(message: Message? = null) {
        // if a map integration is configured: open fullscreen map (behaviour will change depending on message.locationType)
        FullscreenMapDialogFragment.newInstance(
            message,
            discussionViewModel.discussionId,
            null,
            SettingsActivity.locationIntegration
        )?.show(
            activity.supportFragmentManager,
            DiscussionActivity.FULL_SCREEN_MAP_FRAGMENT_TAG
        )
    }

    // can be accessed by long clicking on basic integration or on a preview
    fun showLocationContextMenu(
        message: Message,
        view: View,
        truncatedLatitudeString: String,
        truncatedLongitudeString: String
    ) {
        val locationMessagePopUp = PopupMenu(activity, view)
        val inflater = locationMessagePopUp.menuInflater
        inflater.inflate(R.menu.popup_location_message, locationMessagePopUp.menu)

        // if your sharing message: add a red stop sharing button
        if (message.isCurrentSharingOutboundLocationMessage) {
            val stopSharingItem =
                locationMessagePopUp.menu.findItem(R.id.popup_action_location_message_stop_sharing)
            if (stopSharingItem != null) {
                stopSharingItem.isVisible = true
                val spannableString = SpannableString(stopSharingItem.title)
                spannableString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            activity,
                            R.color.red
                        )
                    ), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                stopSharingItem.title = spannableString
            }
        }


        // if there is no preview, do not show open preview button
        if (message.totalAttachmentCount == 0) {
            val openPreviewItem =
                locationMessagePopUp.menu.findItem(R.id.popup_action_location_message_open_preview)
            openPreviewItem?.isVisible = false
        }

        locationMessagePopUp.setOnMenuItemClickListener { item: MenuItem ->
            val itemId = item.itemId
            when (itemId) {
                R.id.popup_action_location_message_open_third_party_app -> {
                    App.openLocationInMapApplication(
                        activity,
                        truncatedLatitudeString,
                        truncatedLongitudeString,
                        message.contentBody
                    ) { discussionViewModel.markAsReadOnPause = false }
                }

                R.id.popup_action_location_message_copy_coordinates -> {
                    App.getContext().copyLocationToClipboard(truncatedLatitudeString, truncatedLongitudeString)
                }

                R.id.popup_action_location_message_open_preview -> {
                    openLocationPreviewInGallery(message)
                }

                R.id.popup_action_location_message_stop_sharing -> {
                    val builder =
                        SecureAlertDialogBuilder(
                            activity,
                            R.style.CustomAlertDialog
                        )
                            .setTitle(R.string.title_stop_sharing_location)
                            .setMessage(R.string.label_stop_sharing_location)
                            .setPositiveButton(R.string.button_label_stop) { _: DialogInterface?, _: Int ->
                                LocationSharingSubService.stopSharingInDiscussion(
                                    discussionViewModel.discussionId ?: -1, false
                                )
                            }
                            .setNegativeButton(R.string.button_label_cancel, null)
                    builder.create()
                        .show()
                }

                R.id.popup_action_location_message_change_integration -> {
                    LocationIntegrationSelectorDialog(
                        activity,
                        true,
                        object : OnIntegrationSelectedListener {
                            override fun onIntegrationSelected(
                                integration: LocationIntegrationEnum,
                                customOsmServerUrl: String?
                            ) {
                                SettingsActivity.setLocationIntegration(
                                    integration.string,
                                    customOsmServerUrl
                                )
                            }
                        }).show()
                }
            }
            true
        }
        locationMessagePopUp.show()
    }
}