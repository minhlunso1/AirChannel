package minhna.android.airchannel.data.local;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import minhna.android.airchannel.app.AK;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.data.model.Profile;
import minhna.android.airchannel.data.model.SortType;
import minhna.android.airchannel.injection.annotation.ApplicationContext;

/**
 * Created by Minh on 11/9/2017.
 */

@Singleton
public class LocalManager {
    private DBHelper mDbHelper;
    private AP mSharedPrefsHelper;
    private HashMap<Integer, Channel> favChannelMap;
    private Profile profile;
    private List<Channel> channelList;

    @Inject
    public LocalManager(@ApplicationContext Context context, DBHelper dbHelper, AP sharedPrefsHelper) {
        mDbHelper = dbHelper;
        mSharedPrefsHelper = sharedPrefsHelper;
    }

    public void saveSSOId(String id) {
        mSharedPrefsHelper.saveData(AK.SSO_ID, id);
    }

    public String getSSOId() {
        return mSharedPrefsHelper.getStringData(AK.SSO_ID);
    }

    public void saveSortType(@SortType int type) {
        mSharedPrefsHelper.saveData(AK.SORT_TYPE, type);
    }

    public int getSortType() {
        return mSharedPrefsHelper.getIntData(AK.SORT_TYPE);
    }

    public Long insertChannel(Channel user) throws Exception {
        return mDbHelper.insertChannel(user);
    }

    public boolean deleteChannel(int id) throws Exception {
        return mDbHelper.deleteChannel(id);
    }

    private int deleteAllChannels() {
        return mDbHelper.deleteAllChannels();
    }

    public void clearData() {
        getFavChannelMap().clear();
        deleteAllChannels();
        profile = null;
        mSharedPrefsHelper.clearPrefs();
    }

    public List<Channel> getFavChannelList() throws NullPointerException {
        return mDbHelper.getAllChannel();
    }

    public HashMap getFavChannelMap() {
        if (favChannelMap == null)
            favChannelMap = new HashMap<>();
        return favChannelMap;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Channel> getChannelList() {
        if (channelList == null)
            channelList = new ArrayList<>();
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }
}
