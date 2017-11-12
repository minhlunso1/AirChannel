package minhna.android.airchannel.data.net;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;

import minhna.android.airchannel.data.model.ChannelResponse;
import rx.Single;

/**
 * Created by Minh on 11/11/2017.
 */

@Singleton
public class RemoteManager {
    private final RemoteInterface remoteInterface;
    private DatabaseReference firebaseRemote;

    @Inject
    public RemoteManager(RemoteInterface remoteInterface) {
        this.remoteInterface = remoteInterface;
    }

    public Single<ChannelResponse> getChannels() {
        return remoteInterface.getChannels();
    }

    public DatabaseReference getFirebaseRemote() {
        if (firebaseRemote == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            firebaseRemote = FirebaseDatabase.getInstance().getReference();
        }
        return firebaseRemote;
    }
}
