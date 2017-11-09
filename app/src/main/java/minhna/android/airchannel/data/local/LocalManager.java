package minhna.android.airchannel.data.local;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import minhna.android.airchannel.app.AK;
import minhna.android.airchannel.data.pojo.Channel;
import minhna.android.airchannel.injection.annotation.ApplicationContext;

/**
 * Created by Minh on 11/9/2017.
 */

@Singleton
public class LocalManager {

    private Context mContext;
    private DBHelper mDbHelper;
    private AP mSharedPrefsHelper;

    @Inject
    public LocalManager(@ApplicationContext Context context, DBHelper dbHelper, AP sharedPrefsHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mSharedPrefsHelper = sharedPrefsHelper;
    }

    public void saveSSOId(String id) {
        mSharedPrefsHelper.saveData(AK.SSO_ID, id);
    }

    public String getSSOId() {
        return mSharedPrefsHelper.getStringData(AK.SSO_ID);
    }

    public Long createChannel(Channel user) throws Exception {
        return mDbHelper.insertChannel(user);
    }

    public List<Channel> getFavChannelList() throws NullPointerException {
        return mDbHelper.getAllChannel();
    }
}
