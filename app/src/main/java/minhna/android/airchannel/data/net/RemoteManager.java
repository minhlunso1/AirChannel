package minhna.android.airchannel.data.net;

import javax.inject.Inject;
import javax.inject.Singleton;

import minhna.android.airchannel.data.pojo.ChannelResponse;
import rx.Single;

/**
 * Created by Minh on 11/11/2017.
 */

@Singleton
public class RemoteManager {
    private final RemoteInterface remoteInterface;

    @Inject
    public RemoteManager(RemoteInterface remoteInterface) {
        this.remoteInterface = remoteInterface;
    }

    public Single<ChannelResponse> getChannels() {
        return remoteInterface.getChannels();
    }
}
