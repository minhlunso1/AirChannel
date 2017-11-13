package minhna.android.airchannel.data.net;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import javax.inject.Inject;
import javax.inject.Singleton;

import minhna.android.airchannel.app.AK;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.data.model.ChannelResponse;
import minhna.android.airchannel.data.model.EventResponse;
import minhna.android.airchannel.data.model.Profile;
import rx.Observable;
import rx.Single;

/**
 * Created by Minh on 11/11/2017.
 */

@Singleton
public class RemoteManager {
    private final RemoteInterface remoteInterface;
    private DatabaseReference firebaseRemote;
    @Inject
    FirebaseAuth mAuth;

    @Inject
    public RemoteManager(RemoteInterface remoteInterface) {
        this.remoteInterface = remoteInterface;
    }

    public Single<ChannelResponse> getChannels() {
        return remoteInterface.getChannels();
    }

    public Observable<EventResponse> getEvents(String channelIds, String start, String end) {
        return remoteInterface.getEvents(channelIds, start, end);
    }

    public DatabaseReference getFirebaseRemote() {
        if (firebaseRemote == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            firebaseRemote = FirebaseDatabase.getInstance().getReference();
        }
        return firebaseRemote;
    }

    public void updateProfile(Profile profile) {
        if (mAuth.getCurrentUser() != null)
            getFirebaseRemote().child(mAuth.getCurrentUser().getUid())
                    .child(AK.PROFILE).setValue(profile);
    }

    public Query getFavChannelQuery(String channelField) {
        return getFirebaseRemote().child(mAuth.getCurrentUser().getUid()).child(AK.FAV_LIST)
                .orderByChild(channelField);
    }

    public void removeFavoriteChannel(String channelId) {
        getFirebaseRemote().child(mAuth.getCurrentUser().getUid())
                .child(AK.FAV_LIST).child(channelId).removeValue();
    }

    public void insertChannel(Channel channel) {
        getFirebaseRemote().child(mAuth.getCurrentUser().getUid())
                .child(AK.FAV_LIST).child(String.valueOf(channel.getChannelId())).setValue(channel);
    }
}
