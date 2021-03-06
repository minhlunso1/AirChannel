package minhna.android.airchannel.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import minhna.android.airchannel.data.model.SortType;
import minhna.android.airchannel.data.net.RemoteManager;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.view.custom.FabSheetView;
import minhna.android.airchannel.presenter.BasePresenter;
import minhna.android.airchannel.presenter.PresenterChannel;
import minhna.android.airchannel.databinding.ActivityChannelBinding;

/**
 * Created by Minh on 11/10/2017.
 */

public class ChannelActivity extends BaseActivity implements PresenterChannel.IChannel {
    FabSheetView fabSheet;

    @Inject
    RemoteManager remoteManager;
    @Inject
    LocalManager localManager;
    private List<Channel> list;
    ActivityChannelBinding binding;
    private PresenterChannel presenter;

    @Override
    protected BasePresenter initPresenter() {
        return presenter = new PresenterChannel(this, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        this.getViewComponent().inject(this);
        presenter.bindComponent(localManager, remoteManager);
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
        presenter.loadChannelsFromServer();
    }

    private void setChannelList(List<Channel> list) {
        ChannelAdapter adapter = new ChannelAdapter(list, localManager, remoteManager);
        binding.contentChannel.rvChannel.setAdapter(adapter);
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
                    if (localManager.getProfile() != null)
                        presenter.updateSortType(SortType.ID);
                    if (fabSheet != null)
                        fabSheet.dismiss();
                    canFinishMain = true;
                    Collections.sort(list, Channel.IDComparator);
                    setChannelList(list);
                });
                sortName.setOnClickListener(v1 -> {
                    if (localManager.getProfile() != null)
                        presenter.updateSortType(SortType.NAME);
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

    @Override
    public void onLoadChannelDone(List<Channel> channels, boolean fromRemote) {
        binding.contentChannel.pb.hide();
        binding.contentChannel.cvChannel.setVisibility(View.VISIBLE);
        if (fromRemote && list.size() == channels.size())
            return;
        list.clear();
        list.addAll(channels);
        setChannelList(list);
    }

    @Override
    public void onLoadingChannel() {
        binding.contentChannel.pb.show();
    }

    @Override
    public void onError(String error) {
        binding.contentChannel.pb.hide();
        toggleSnackbar(binding.fabSort,true, error);
    }
}
