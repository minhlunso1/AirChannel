package minhna.android.airchannel.data.net;

import minhna.android.airchannel.data.model.ChannelResponse;
import retrofit2.http.GET;
import rx.Single;

/**
 * Created by Minh on 11/11/2017.
 */

public interface RemoteInterface {
    @GET("/ams/v3/getChannelList")
    Single<ChannelResponse> getChannels();
}
