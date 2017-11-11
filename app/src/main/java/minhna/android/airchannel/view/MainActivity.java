package minhna.android.airchannel.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import minhna.android.airchannel.R;
import minhna.android.airchannel.adapter.FavChannelAdapter;
import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.data.net.RemoteManager;
import minhna.android.airchannel.view.custom.FabSheetView;
import minhna.android.airchannel.viewmodel.FavViewModel;

/**
 * In this test, I want to make it simple so I do not bind Activity with ViewModel.
 * Prefer to use android.arch.lifecycle library for MVVM in real project.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        FavViewModel.IFavViewMode{
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.rl_channel)
    View rlChannel;
    @BindView(R.id.cv_tv_guide)
    View cvTVGuide;
    @BindView(R.id.cv_fav)
    View cvFav;
    @BindView(R.id.rv_fav)
    RecyclerView rvFav;

    FabSheetView fabSheet;
    TextView tvSSOInfo;

    @Inject
    RemoteManager remoteManager;
    @Inject
    LocalManager localManager;
    List<Channel> list;
    FavChannelAdapter adapter;
    MenuItem navSSOAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        this.getViewComponent().inject(this);

        setupView();
    }

    private void setupView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvSSOInfo = headerView.findViewById(R.id.tv_sso_info);
        Menu navMenu = navigationView.getMenu();
        navSSOAction = navMenu.findItem(R.id.nav_sso);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupEvents();
        setupFavList();
    }

    private void setupFavList() {
        list = new ArrayList<>();
        list = localManager.getFavChannelList();
        for (Channel tmp:list)
            localManager.getFavChannelMap().put(tmp.getChannelId(), tmp);
        updateFavView(list.size());
    }

    private void updateFavView(int size) {
        if (size > 0) {
            cvFav.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            rvFav.setLayoutManager(new LinearLayoutManager(this));
            setChannelList(list);
        } else {
            cvFav.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }
    }

    private void setupEvents() {
        RxView.clicks(rlChannel)
                .takeUntil(preDestroy())
                .subscribe(aVoid -> ChannelActivity.go(getBaseContext()));
        fab.setOnClickListener(view -> {
            canFinishMain = false;
            if (fabSheet == null) {
                View sheet = getLayoutInflater().inflate(R.layout.sheet_sort, null, false);
                fabSheet = new FabSheetView.Builder(fab, sheet).build();
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            if (canFinishMain)
                super.onBackPressed();
            else {
                if (fabSheet != null)
                    fabSheet.dismiss();
                canFinishMain = true;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_sso) {
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setChannelList(List<Channel> list) {
        adapter = new FavChannelAdapter(list, localManager, this);
        rvFav.setAdapter(adapter);
    }

    @Override
    public void onRemoveFav(int channelId, int position) {
        if (list.get(position).getChannelId() == channelId) {
            list.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }
}
