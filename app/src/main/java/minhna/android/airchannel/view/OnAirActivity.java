package minhna.android.airchannel.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import minhna.android.airchannel.R;
import minhna.android.airchannel.adapter.EventAdapter;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Event;
import minhna.android.airchannel.data.model.SortType;
import minhna.android.airchannel.data.net.RemoteManager;
import minhna.android.airchannel.databinding.ActivityOnairBinding;
import minhna.android.airchannel.presenter.BasePresenter;
import minhna.android.airchannel.presenter.PresenterOnAir;
import minhna.android.airchannel.view.custom.FabSheetView;

/**
 * Created by Minh on 11/12/2017.
 */

public class OnAirActivity extends BaseActivity implements PresenterOnAir.IOnAir {
    FabSheetView fabSheet;

    @Inject
    RemoteManager remoteManager;
    @Inject
    LocalManager localManager;
    private List<Event> list;
    private PresenterOnAir presenter;
    ActivityOnairBinding binding;

    @Override
    protected BasePresenter initPresenter() {
        return presenter = new PresenterOnAir(this, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onair);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        this.getViewComponent().inject(this);
        presenter.bindComponent(localManager, remoteManager);
        setupView();
        loadEvents();
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
                    Collections.sort(list, Event.IDComparator);
                    setEventList(list);
                });
                sortName.setOnClickListener(v1 -> {
                    if (localManager.getProfile() != null)
                        presenter.updateSortType(SortType.NAME);
                    if (fabSheet != null)
                        fabSheet.dismiss();
                    canFinishMain = true;
                    Collections.sort(list, Event.NameComparator);
                    setEventList(list);
                });
            } else
                fabSheet.show();
        });
    }

    @Override
    public void onError(String message) {
        binding.contentOnair.pb.hide();
        toggleSnackbar(binding.fabSort,true, message);
    }

    @Override
    public void onLoadShowDone(List<Event> events, boolean fromRemote) {
        binding.contentOnair.pb.hide();
        list.clear();
        list.addAll(events);
        setEventList(list);
    }

    private void loadEvents() {
        list = new ArrayList<>();
        binding.contentOnair.rvOnair.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        presenter.loadEventsFromServer();
    }

    private void setEventList(List<Event> list) {
        EventAdapter adapter = new EventAdapter(list);
        binding.contentOnair.rvOnair.setAdapter(adapter);
    }

    @Override
    public void onLoadingEvent() {
        binding.contentOnair.pb.show();
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
