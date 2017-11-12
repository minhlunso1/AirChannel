package minhna.android.airchannel.presenter;

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

public class PresenterMain extends BaseDataPresenter {
    private IMain iMain;
    public interface IMain extends BaseIView {
        void onGetFavDone(List<Channel> list, boolean fromRemote);
    }

    public PresenterMain(Context context, IMain iMain) {
        this.context = context;
        this.iMain = iMain;
    }

    @Override
    public void bindComponent(LocalManager localManager, RemoteManager remoteManager) {
        super.bindComponent(localManager, remoteManager);
    }

    public void getFavList() {
        if (localManager.getProfile() != null) {
            //load from local first for better UX
            executeFavLocalLoading();
            Query favChannelQuery = remoteManager.getFavChannelQuery(
                    Channel.getChannelField(localManager.getProfile().getSortType()));
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
                    Profile profile = dataSnapshot.child(mAuth.getCurrentUser().getUid())
                            .child(AK.PROFILE).getValue(Profile.class);
                    if (profile == null)//not on CRUD remote database yet
                        updateProfile();
                    else {
                        localManager.setProfile(profile);
                        localManager.saveSSOId(profile.getEmail());
                        localManager.saveSortType(profile.getSortType());
                        getFavList();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    iMain.onError(error.getMessage());
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iMain = null;
    }
}
