package minhna.android.airchannel.view.presenter;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import minhna.android.airchannel.app.AK;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.data.model.Profile;
import minhna.android.airchannel.data.net.RemoteManager;

/**
 * Created by Minh on 11/12/2017.
 */

public class PresenterMain extends BasePresenter {
    private IMain iMain;
    private LocalManager localManager;
    private RemoteManager remoteManager;

    public interface IMain extends BaseIView {
        void onGetFavDone(List<Channel> list, boolean fromRemote);
    }

    public PresenterMain(Context context, IMain iMain) {
        this.context = context;
        this.iMain = iMain;
    }

    public void bindComponent(LocalManager localManager, RemoteManager remoteManager) {
        this.localManager = localManager;
        this.remoteManager = remoteManager;
    }

    public void getFavList(FirebaseAuth mAuth) {
        if (localManager.getProfile() != null && mAuth != null && mAuth.getCurrentUser() != null) {
            //load from local first for better UX
            executeFavLocalLoading();
            Query favChannelQuery = remoteManager.getFirebaseRemote().child(mAuth.getCurrentUser().getUid())
                    .child(AK.FAV_LIST)
                    .orderByChild(Channel.getChannelField(localManager.getProfile().getSortType()));
            favChannelQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Channel> list = new ArrayList<>();
                    localManager.getFavChannelMap().clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Channel channel = snapshot.getValue(Channel.class);
                        list.add(channel);
                        localManager.getFavChannelMap().put(channel.getChannelId(), channel);
                    } iMain.onGetFavDone(list, true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    iMain.onError(databaseError.getMessage());
                }
            });
        } else
            executeFavLocalLoading();
    }

    private void executeFavLocalLoading() {
        List<Channel> list = formatList();
        iMain.onGetFavDone(list, false);
    }

    private List<Channel> formatList() {
        List<Channel> list = loadFavLocal();
        if (localManager.getProfile() != null)
            Collections.sort(list, Channel.getChannelComparator(
                    localManager.getProfile().getSortType()));
        return list;
    }

    private List<Channel> loadFavLocal() {
        if (localManager.getFavChannelMap().size() > 0)
            return new ArrayList<>(localManager.getFavChannelMap().values());
        else {
            List<Channel> list = localManager.getFavChannelList();
            for (Channel tmp : list)
                localManager.getFavChannelMap().put(tmp.getChannelId(), tmp);
            return list;
        }
    }

    public void setupProfile(FirebaseAuth mAuth) {
        if (mAuth != null && mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail() != null) {
            localManager.setProfile(new Profile());
            localManager.getProfile().setEmail(mAuth.getCurrentUser().getEmail());
            remoteManager.getFirebaseRemote().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Profile profile = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(Profile.class);
                    if (profile == null)
                        updateProfile(mAuth);
                    else {
                        localManager.setProfile(profile);
                        localManager.saveSSOId(profile.getEmail());
                        localManager.saveSortType(profile.getSortType());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    iMain.onError(error.getMessage());
                }
            });
        }
    }

    private void updateProfile(FirebaseAuth mAuth) {
        remoteManager.getFirebaseRemote().child(mAuth.getCurrentUser().getUid()).setValue(localManager.getProfile());
    }

    public void updateSortType(int type, FirebaseAuth mAuth) {
        localManager.getProfile().setSortType(type);
        updateProfile(mAuth);
        localManager.saveSortType(type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iMain = null;
        localManager = null;
        remoteManager = null;
    }
}
