package minhna.android.airchannel.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import minhna.android.airchannel.R;
import minhna.android.airchannel.adapter.ChannelAdapter;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.net.RemoteManager;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.view.custom.FabSheetView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import minhna.android.airchannel.databinding.ActivityChannelBinding;

/**
 * Created by Minh on 11/10/2017.
 */

public class ChannelActivity extends BaseActivity {
    FabSheetView fabSheet;

    @Inject
    RemoteManager remoteManager;
    @Inject
    LocalManager localManager;
    List<Channel> list;
    ActivityChannelBinding binding;

    public static void go(Context context) {
        Intent intent = new Intent(context, ChannelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        this.getViewComponent().inject(this);
        setupView();
        loadChannels();
    }

    @Override
    public void onBackPressed() {
        if (canFinishMain)
            super.onBackPressed();
        else {
            if (fabSheet != null)
                fabSheet.dismiss();
            canFinishMain = true;
        }
    }

    private void loadChannels() {
        list = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.contentChannel.rvChannel.setLayoutManager(layoutManager);
        binding.contentChannel.cvChannel.setVisibility(View.INVISIBLE);

        remoteManager.getChannels()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    List<Channel> list = response.channels;
                    Collections.sort(list, Channel.IDComparator);
                    return list;
                })
                .takeUntil(preDestroy())
                .doOnSubscribe(() -> binding.contentChannel.pb.show())
                .subscribe(channels -> {
                    binding.contentChannel.pb.hide();
                    binding.contentChannel.cvChannel.setVisibility(View.VISIBLE);
                    list.addAll(channels);
                    list = formatList(list);
                    setChannelList(list);
                }, error -> {
                    binding.contentChannel.pb.hide();
                    error.printStackTrace();
                    toggleSnackbar(true, error.getMessage());
                });
    }

    private void setChannelList(List<Channel> list) {
        ChannelAdapter adapter = new ChannelAdapter(list, localManager);
        binding.contentChannel.rvChannel.setAdapter(adapter);
    }

    private List<Channel> formatList(List<Channel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (localManager.getFavChannelMap().get(list.get(i).getChannelId()) == null)
                list.get(i).imgFavRes = R.mipmap.ic_non_fav;
            else
                list.get(i).imgFavRes = R.mipmap.ic_on_fav;
        }
        return list;
    }

    private void toggleSnackbar(boolean toShow, String message) {
        if (toShow) {
            snackbar = Snackbar.make(binding.fabSort, message, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        } else {
            if (snackbar != null && snackbar.isShown())
                new Handler().postDelayed(() -> {
                    snackbar.dismiss();
                }, 500);
        }
    }

    private void setupView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.fabSort.setOnClickListener(v -> {
            canFinishMain = false;
            if (fabSheet == null) {
                View sheet = getLayoutInflater().inflate(R.layout.sheet_sort, null, false);
                fabSheet = new FabSheetView.Builder(binding.fabSort, sheet).build();
                fabSheet.show();
                View sortId = sheet.findViewById(R.id.v1);
                View sortName = sheet.findViewById(R.id.v2);

                sortId.setOnClickListener(v1 -> {
                    if (fabSheet != null)
                        fabSheet.dismiss();
                    canFinishMain = true;
                    Collections.sort(list, Channel.IDComparator);
                    setChannelList(list);
                });
                sortName.setOnClickListener(v1 -> {
                    if (fabSheet != null)
                        fabSheet.dismiss();
                    canFinishMain = true;
                    Collections.sort(list, Channel.NameComparator);
                    setChannelList(list);
                });
            } else
                fabSheet.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
