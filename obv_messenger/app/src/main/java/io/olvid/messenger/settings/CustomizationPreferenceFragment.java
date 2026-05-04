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

package io.olvid.messenger.settings;

import android.app.LocaleManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.DropDownPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import io.olvid.engine.Logger;
import io.olvid.messenger.App;
import io.olvid.messenger.R;
import io.olvid.messenger.customClasses.ImageViewPreference;
import io.olvid.messenger.customClasses.MultilineSummaryPreferenceCategory;
import io.olvid.messenger.fragments.dialog.LedColorPickerDialogFragment;

public class CustomizationPreferenceFragment extends PreferenceFragmentCompat {
    FragmentActivity activity;
    ImageViewPreference appIconPreference;
    ImageViewPreference outboundBubbleColorPreference;
    ImageViewPreference inboundBubbleColorPreference;
    ImageViewPreference outboundFontColorPreference;
    ImageViewPreference outboundFontColorDarkPreference;
    ImageViewPreference inboundFontColorPreference;
    ImageViewPreference inboundFontColorDarkPreference;

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (appIconPreference != null && appIconPreference.isVisible()) {
                appIconPreference.setImageResource(App.currentIcon.getIcon());
            }
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_preferences_customization, rootKey);
        activity = requireActivity();
        PreferenceScreen screen = getPreferenceScreen();
        if (screen == null) {
            return;
        }

        {
            appIconPreference = screen.findPreference(SettingsActivity.PREF_KEY_APP_ICON);
            if (appIconPreference != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (App.currentIcon != null) {
                        appIconPreference.setImageResource(App.currentIcon.getIcon());
                        appIconPreference.removeElevation();
                    }
                } else {
                    screen.removePreference(appIconPreference);
                }
            }
        }

        {
            final MultilineSummaryPreferenceCategory languagePreferenceCategory = screen.findPreference(SettingsActivity.PREF_KEY_APP_LANGUAGE_CATEGORY);
            final ListPreference languagePreference = screen.findPreference(SettingsActivity.PREF_KEY_APP_LANGUAGE);
            if (languagePreferenceCategory != null && languagePreference != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    screen.removePreference(languagePreferenceCategory);
                    screen.removePreference(languagePreference);
                } else {
                    languagePreferenceCategory.setOnClickListener(v -> {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("mailto:lang@olvid.io?subject=" + getString(R.string.mail_subject_olvid_translation)));
                            startActivity(intent);
                        } catch (Exception e) {
                            Logger.x(e);
                        }
                    });


                    LocaleManager localeManager = activity.getSystemService(LocaleManager.class);
                    LocaleList localeList = localeManager.getApplicationLocales();
                    if (!localeList.isEmpty()) {
                        String lang = localeList.get(0).getLanguage();
                        String country = localeList.get(0).getCountry();
                        switch (lang) {
                            case "fr":
                                languagePreference.setValue("fr");
                                break;
                            case "en":
                                languagePreference.setValue("en");
                                break;
                            case "af":
                                languagePreference.setValue("af");
                                break;
                            case "ar":
                                languagePreference.setValue("ar");
                                break;
                            case "ca":
                                languagePreference.setValue("ca");
                                break;
                            case "cs":
                                languagePreference.setValue("cs");
                                break;
                            case "da":
                                languagePreference.setValue("da");
                                break;
                            case "de":
                                languagePreference.setValue("de");
                                break;
                            case "el":
                                languagePreference.setValue("el");
                                break;
                            case "es":
                                languagePreference.setValue("es");
                                break;
                            case "fa":
                                languagePreference.setValue("fa");
                                break;
                            case "fi":
                                languagePreference.setValue("fi");
                                break;
                            case "hi":
                                languagePreference.setValue("hi");
                                break;
                            case "hu":
                                languagePreference.setValue("hu");
                                break;
                            case "hr":
                                languagePreference.setValue("hr");
                                break;
                            case "it":
                                languagePreference.setValue("it");
                                break;
                            case "iw":
                                languagePreference.setValue("iw");
                                break;
                            case "ja":
                                languagePreference.setValue("ja");
                                break;
                            case "ko":
                                languagePreference.setValue("ko");
                                break;
                            case "nl":
                                languagePreference.setValue("nl");
                                break;
                            case "no":
                                languagePreference.setValue("no");
                                break;
                            case "pl":
                                languagePreference.setValue("pl");
                                break;
                            case "pt":
                                if (country.equalsIgnoreCase("BR")) {
                                    languagePreference.setValue("pt-rBR");
                                } else {
                                    languagePreference.setValue("pt");
                                }
                                break;
                            case "ro":
                                languagePreference.setValue("ro");
                                break;
                            case "ru":
                                languagePreference.setValue("ru");
                                break;
                            case "sk":
                                languagePreference.setValue("sk");
                                break;
                            case "sl":
                                languagePreference.setValue("sl");
                                break;
                            case "sv":
                                languagePreference.setValue("sv");
                                break;
                            case "tr":
                                languagePreference.setValue("tr");
                                break;
                            case "uk":
                                languagePreference.setValue("uk");
                                break;
                            case "vi":
                                languagePreference.setValue("vi");
                                break;
                            case "zh":
                                if (country.equalsIgnoreCase("TW")) {
                                    languagePreference.setValue("zh-rTW");
                                } else {
                                    languagePreference.setValue("zh");
                                }
                                break;
                            default:
                                languagePreference.setValue("default");
                                break;
                        }
                    } else {
                        languagePreference.setValue("default");
                    }


                    languagePreference.setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
                        if (newValue instanceof String) {
                            switch ((String) newValue) {
                                case "fr":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("fr"));
                                    break;
                                case "en":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("en"));
                                    break;
                                case "af":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("af"));
                                    break;
                                case "ar":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("ar"));
                                    break;
                                case "ca":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("ca"));
                                    break;
                                case "cs":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("cs"));
                                    break;
                                case "da":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("da"));
                                    break;
                                case "de":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("de"));
                                    break;
                                case "el":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("el"));
                                    break;
                                case "es":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("es"));
                                    break;
                                case "fa":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("fa"));
                                    break;
                                case "fi":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("fi"));
                                    break;
                                case "hi":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("hi"));
                                    break;
                                case "hr":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("hr"));
                                    break;
                                case "hu":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("hu"));
                                    break;
                                case "it":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("it"));
                                    break;
                                case "iw":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("iw"));
                                    break;
                                case "ja":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("ja"));
                                    break;
                                case "ko":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("ko"));
                                    break;
                                case "nl":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("nl"));
                                    break;
                                case "no":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("no"));
                                    break;
                                case "pl":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("pl"));
                                    break;
                                case "pt":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("pt"));
                                    break;
                                case "pt-rBR":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("pt-br"));
                                    break;
                                case "ro":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("ro"));
                                    break;
                                case "ru":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("ru"));
                                    break;
                                case "sk":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("sk"));
                                    break;
                                case "sl":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("sl"));
                                    break;
                                case "sv":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("sv"));
                                    break;
                                case "tr":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("tr"));
                                    break;
                                case "uk":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("uk"));
                                    break;
                                case "vi":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("vi"));
                                    break;
                                case "zh":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("zh"));
                                    break;
                                case "zh-rTW":
                                    localeManager.setApplicationLocales(LocaleList.forLanguageTags("zh-tw"));
                                    break;
                                default:
                                    localeManager.setApplicationLocales(LocaleList.getEmptyLocaleList());
                                    break;
                            }
                        }
                        return false;
                    });
                }
            }
        }

        {
            final SwitchPreference darkModeSwitchPreference = screen.findPreference(SettingsActivity.PREF_KEY_DARK_MODE);
            final ListPreference darkModeListPreference = screen.findPreference(SettingsActivity.PREF_KEY_DARK_MODE_API29);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (darkModeSwitchPreference != null) {
                    screen.removePreference(darkModeSwitchPreference);
                }
                if (darkModeListPreference != null) {
                    darkModeListPreference.setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
                        if (newValue instanceof String) {
                            switch ((String) newValue) {
                                case "Dark":
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    break;
                                case "Light":
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    break;
                                case "Auto":
                                default:
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            }
                        }
                        return true;
                    });
                }
            } else {
                if (darkModeListPreference != null) {
                    screen.removePreference(darkModeListPreference);
                }
                if (darkModeSwitchPreference != null) {
                    darkModeSwitchPreference.setOnPreferenceChangeListener((Preference preference, Object checked) -> {
                        if (checked instanceof Boolean) {
                            if ((Boolean) checked) {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            } else {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            }
                        }
                        return true;
                    });
                }
            }
        }

        {
            Preference.OnPreferenceChangeListener listener = (Preference preference, Object newValue) -> {
                Intent scaleChangedIntent = new Intent(SettingsActivity.ACTIVITY_RECREATE_REQUIRED_ACTION);
                scaleChangedIntent.setPackage(App.getContext().getPackageName());
                // we delay sending this intent so we are sure the setting is updated when activities are recreated
                new Handler(Looper.getMainLooper()).postDelayed(() -> LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(scaleChangedIntent), 200);
                return true;
            };

            final DropDownPreference fontScalePreference = screen.findPreference(SettingsActivity.PREF_KEY_FONT_SCALE);
            if (fontScalePreference != null) {
                fontScalePreference.setOnPreferenceChangeListener(listener);
            }
            final DropDownPreference screenScalePreference = screen.findPreference(SettingsActivity.PREF_KEY_SCREEN_SCALE);
            if (screenScalePreference != null) {
                screenScalePreference.setOnPreferenceChangeListener(listener);
            }
        }

        {
            outboundBubbleColorPreference = screen.findPreference(SettingsActivity.PREF_KEY_OUTBOUND_BUBBLE_COLOR);
            if (outboundBubbleColorPreference != null) {
                outboundBubbleColorPreference.setOnPreferenceClickListener(preference -> {
                    LedColorPickerDialogFragment picker = LedColorPickerDialogFragment.newInstance();
                    picker.setInitialColor(SettingsActivity.getOutboundBubbleColor());
                    picker.setOnLedColorSelectedListener(color -> {
                        SettingsActivity.setOutboundBubbleColor(color);
                        updateOutboundBubbleColorImage();
                    });
                    picker.show(getChildFragmentManager(), "outbound_bubble_color");
                    return true;
                });
                updateOutboundBubbleColorImage();
            }

            inboundBubbleColorPreference = screen.findPreference(SettingsActivity.PREF_KEY_INBOUND_BUBBLE_COLOR);
            if (inboundBubbleColorPreference != null) {
                inboundBubbleColorPreference.setOnPreferenceClickListener(preference -> {
                    LedColorPickerDialogFragment picker = LedColorPickerDialogFragment.newInstance();
                    picker.setInitialColor(SettingsActivity.getInboundBubbleColor());
                    picker.setOnLedColorSelectedListener(color -> {
                        SettingsActivity.setInboundBubbleColor(color);
                        updateInboundBubbleColorImage();
                    });
                    picker.show(getChildFragmentManager(), "inbound_bubble_color");
                    return true;
                });
                updateInboundBubbleColorImage();
            }
        }

        {
            outboundFontColorPreference = screen.findPreference(SettingsActivity.PREF_KEY_OUTBOUND_FONT_COLOR);
            if (outboundFontColorPreference != null) {
                outboundFontColorPreference.setOnPreferenceClickListener(preference -> {
                    LedColorPickerDialogFragment picker = LedColorPickerDialogFragment.newInstance();
                    picker.setInitialColor(SettingsActivity.getOutboundFontColor());
                    picker.setOnLedColorSelectedListener(color -> {
                        SettingsActivity.setOutboundFontColor(color);
                        updateColorImage(outboundFontColorPreference, SettingsActivity.getOutboundFontColor());
                    });
                    picker.show(getChildFragmentManager(), "outbound_font_color");
                    return true;
                });
                updateColorImage(outboundFontColorPreference, SettingsActivity.getOutboundFontColor());
            }

            outboundFontColorDarkPreference = screen.findPreference(SettingsActivity.PREF_KEY_OUTBOUND_FONT_COLOR_DARK);
            if (outboundFontColorDarkPreference != null) {
                outboundFontColorDarkPreference.setOnPreferenceClickListener(preference -> {
                    LedColorPickerDialogFragment picker = LedColorPickerDialogFragment.newInstance();
                    picker.setInitialColor(SettingsActivity.getOutboundFontColorDark());
                    picker.setOnLedColorSelectedListener(color -> {
                        SettingsActivity.setOutboundFontColorDark(color);
                        updateColorImage(outboundFontColorDarkPreference, SettingsActivity.getOutboundFontColorDark());
                    });
                    picker.show(getChildFragmentManager(), "outbound_font_color_dark");
                    return true;
                });
                updateColorImage(outboundFontColorDarkPreference, SettingsActivity.getOutboundFontColorDark());
            }

            inboundFontColorPreference = screen.findPreference(SettingsActivity.PREF_KEY_INBOUND_FONT_COLOR);
            if (inboundFontColorPreference != null) {
                inboundFontColorPreference.setOnPreferenceClickListener(preference -> {
                    LedColorPickerDialogFragment picker = LedColorPickerDialogFragment.newInstance();
                    picker.setInitialColor(SettingsActivity.getInboundFontColor());
                    picker.setOnLedColorSelectedListener(color -> {
                        SettingsActivity.setInboundFontColor(color);
                        updateColorImage(inboundFontColorPreference, SettingsActivity.getInboundFontColor());
                    });
                    picker.show(getChildFragmentManager(), "inbound_font_color");
                    return true;
                });
                updateColorImage(inboundFontColorPreference, SettingsActivity.getInboundFontColor());
            }

            inboundFontColorDarkPreference = screen.findPreference(SettingsActivity.PREF_KEY_INBOUND_FONT_COLOR_DARK);
            if (inboundFontColorDarkPreference != null) {
                inboundFontColorDarkPreference.setOnPreferenceClickListener(preference -> {
                    LedColorPickerDialogFragment picker = LedColorPickerDialogFragment.newInstance();
                    picker.setInitialColor(SettingsActivity.getInboundFontColorDark());
                    picker.setOnLedColorSelectedListener(color -> {
                        SettingsActivity.setInboundFontColorDark(color);
                        updateColorImage(inboundFontColorDarkPreference, SettingsActivity.getInboundFontColorDark());
                    });
                    picker.show(getChildFragmentManager(), "inbound_font_color_dark");
                    return true;
                });
                updateColorImage(inboundFontColorDarkPreference, SettingsActivity.getInboundFontColorDark());
            }
        }
    }

    private void updateOutboundBubbleColorImage() {
        if (outboundBubbleColorPreference != null) {
            String colorString = SettingsActivity.getOutboundBubbleColor();
            if (colorString == null || colorString.length() != 7 || !colorString.startsWith("#")) {
                outboundBubbleColorPreference.setColor((Integer) null);
            } else {
                try {
                    int color = Integer.parseInt(colorString.substring(1), 16);
                    outboundBubbleColorPreference.setColor(color);
                } catch (NumberFormatException e) {
                    outboundBubbleColorPreference.setColor((Integer) null);
                }
            }
        }
    }

    private void updateInboundBubbleColorImage() {
        if (inboundBubbleColorPreference != null) {
            String colorString = SettingsActivity.getInboundBubbleColor();
            if (colorString == null || colorString.length() != 7 || !colorString.startsWith("#")) {
                inboundBubbleColorPreference.setColor((Integer) null);
            } else {
                try {
                    int color = Integer.parseInt(colorString.substring(1), 16);
                    inboundBubbleColorPreference.setColor(color);
                } catch (NumberFormatException e) {
                    inboundBubbleColorPreference.setColor((Integer) null);
                }
            }
        }
    }

    private void updateColorImage(ImageViewPreference preference, String colorString) {
        if (preference != null) {
            if (colorString == null || colorString.length() != 7 || !colorString.startsWith("#")) {
                preference.setColor((Integer) null);
            } else {
                try {
                    int color = Integer.parseInt(colorString.substring(1), 16);
                    preference.setColor(color);
                } catch (NumberFormatException e) {
                    preference.setColor((Integer) null);
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.almostWhite));
    }
}
