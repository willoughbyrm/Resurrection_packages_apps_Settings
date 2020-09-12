/*Copyright (C) 2015 The ResurrectionRemix Project
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/
package com.android.settings.rr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.app.Fragment;
import androidx.preference.Preference;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.provider.SearchIndexableResource;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.settings.rr.utils.RRUtils;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.android.settings.rr.Preferences.*;
import com.android.settings.gestures.SystemNavigationPreferenceController;
import android.provider.Settings;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
@SearchIndexable
public class RecentsSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

   private SystemSettingMasterSwitchPreference mSlimRecents;
   private static final String SLIM_RECENTS_KEY = "use_slim_recents";
   private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.rr_recents);
        mContext = getActivity();
        boolean mHwKeysState = Settings.Secure.getInt(mContext.getContentResolver(),
                              Settings.Secure.HARDWARE_KEYS_ENABLE, 1) != 0;
        mSlimRecents = (SystemSettingMasterSwitchPreference) findPreference(SLIM_RECENTS_KEY);
        if (SystemNavigationPreferenceController.isEdgeToEdgeEnabled(mContext) 
            || SystemNavigationPreferenceController.isSwipeUpEnabled(mContext)
            || !mHwKeysState) {
            mSlimRecents.setEnabled(false);
            mSlimRecents.setSummary(R.string.navbar_not_active);
        } else {
             mSlimRecents.setEnabled(true);
             mSlimRecents.setSummary(R.string.slim_recents_summary);
        }
        int anim = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.RR_CONFIG_ANIM, 0);
        try {
            if (anim == 0) {
                removePreference("animation");
            } else if (anim == 1) {
                removePreference("preview");
            } else if (anim == 2) {
                removePreference("animation");
                removePreference("preview");
            }
        } catch (Exception e) {}
    }

     @Override
     public void onResume() {
         super.onResume();
     }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.RESURRECTED;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean enabled) {
                ArrayList<SearchIndexableResource> result =
                    new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.rr_recents;
                    result.add(sir);
                    return result;
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                List<String> keys = super.getNonIndexableKeys(context);
                return keys;
            }
        };
}
