package minhna.android.airchannel.data.net;

import minhna.android.airchannel.data.model.ChannelResponse;
import minhna.android.airchannel.data.model.EventResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

/**
 * Created by Minh on 11/11/2017.
 */

public interface RemoteInterface {
    @GET("ams/v3/getChannelList")
    Single<ChannelResponse> getChannels();

    @GET("ams/v3/getEvents")
    Observable<EventResponse> getEvents(@Query("channelId") String channelIds,
                                        @Query("periodStart") String periodStart,
                                        @Query("periodEnd") String periodEnd);
}
