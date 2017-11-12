package minhna.android.airchannel.presenter;

import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.net.RemoteManager;

/**
 * Created by Minh on 11/12/2017.
 */

public class BaseDataPresenter extends BasePresenter {
    protected LocalManager localManager;
    protected RemoteManager remoteManager;

    protected void bindComponent(LocalManager localManager, RemoteManager remoteManager) {
        this.localManager = localManager;
        this.remoteManager = remoteManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localManager = null;
        remoteManager = null;
    }

    public void updateSortType(int type) {
        if (localManager.getProfile() != null) {
            localManager.getProfile().setSortType(type);
            updateProfile();
            localManager.saveSortType(type);
        }
    }

    public void updateProfile() {
        remoteManager.updateProfile(localManager.getProfile());
    }
}
