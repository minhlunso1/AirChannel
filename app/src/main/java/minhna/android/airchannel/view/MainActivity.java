package minhna.android.airchannel.view;

import android.content.Intent;
import android.net.Uri;;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
import minhna.android.airchannel.data.model.Profile;
import minhna.android.airchannel.data.model.SortType;
import minhna.android.airchannel.data.net.RemoteManager;
import minhna.android.airchannel.view.custom.FabSheetView;
import minhna.android.airchannel.presenter.BasePresenter;
import minhna.android.airchannel.presenter.PresenterMain;
import minhna.android.airchannel.viewmodel.FavViewModel;

/**
 * In this test, I want to make it simple so I do not bind Activity with ViewModel and use MVP instead.
 * Prefer to use android.arch.lifecycle library for MVVM in real project.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        FavViewModel.IFavViewMode, PresenterMain.IMain {
    private final int RC_SIGN_IN = 100;

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
    @BindView(R.id.tv_channel)
    View tvChannel;
    @BindView(R.id.tv_tv_guide)
    View tvOnAir;

    FabSheetView fabSheet;
    TextView tvSSOInfo;

    @Inject
    RemoteManager remoteManager;
    @Inject
    LocalManager localManager;
    @Inject
    FirebaseAuth mAuth;
    private List<Channel> list = new ArrayList<>();
    private FavChannelAdapter adapter;
    private MenuItem navSSOAction;
    private GoogleSignInClient mGoogleSignInClient;
    private PresenterMain presenter;
    private boolean canGetList = true;

    @Override
    protected BasePresenter initPresenter() {
        return presenter = new PresenterMain(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        this.getViewComponent().inject(this);
        presenter.bindComponent(localManager, remoteManager);

        setupView();
        setupProfileFromLocal();
        setupGoogleSSO();
    }

    //I load local data for better UX in case of slow net speed
    private void setupProfileFromLocal() {
        if (localManager.getSSOId() != null) {
            localManager.setProfile(new Profile(localManager.getSSOId(), localManager.getSortType()));
            tvSSOInfo.setText(localManager.getSSOId());
            navSSOAction.setTitle(R.string.action_sign_out);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateSSOUI(currentUser);
            if (currentUser != null)
                presenter.setupProfile(mAuth);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupEvents();
        if (canGetList)
            presenter.getFavList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                canGetList = false;
            } catch (ApiException e) {
                e.printStackTrace();
                showSnackbar(fab, getString(R.string.suggest_update_google), Snackbar.LENGTH_LONG);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog(null);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        showSnackbar(fab, getString(R.string.alert_sign_in_successfully), Snackbar.LENGTH_SHORT);
                        clearFavData(mAuth.getCurrentUser());
                        presenter.setupProfile(mAuth);
                    } else
                        showSnackbar(fab, task.getException().getMessage(), Snackbar.LENGTH_INDEFINITE);
                });
    }

    private void clearFavData(FirebaseUser firebaseUser) {
        localManager.clearData();
        list.clear();
        setChannelList(list);
        updateFavView(list.size(), false);
        updateSSOUI(firebaseUser);
    }

    private void updateSSOUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            tvSSOInfo.setText(R.string.label_anonymous);
            navSSOAction.setTitle(R.string.action_sign_in);
        } else {
            tvSSOInfo.setText(currentUser.getEmail());
            navSSOAction.setTitle(R.string.action_sign_out);
        }
    }

    private void setupGoogleSSO() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupView() {
        rvFav.setLayoutManager(new LinearLayoutManager(this));
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

    private void updateFavView(int size, boolean whenRemove) {
        if (size > 0) {
            cvFav.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            if (!whenRemove)
                setChannelList(list);
        } else {
            cvFav.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }
    }

    private void setupEvents() {
        //Faster when start activity from outsite activity so I use RxBinding
        RxView.clicks(rlChannel)
                .takeUntil(preDestroy())
                .subscribe(aVoid -> startNewTaskWith(this, ChannelActivity.class));
        RxView.clicks(cvTVGuide)
                .takeUntil(preDestroy())
                .subscribe(aVoid -> startNewTaskWith(this, OnAirActivity.class));
        fab.setOnClickListener(view -> {
            canFinishMain = false;
            if (fabSheet == null) {
                View sheet = getLayoutInflater().inflate(R.layout.sheet_sort, null, false);
                fabSheet = new FabSheetView.Builder(fab, sheet).build();
                fabSheet.show();
                View sortId = sheet.findViewById(R.id.v1);
                View sortName = sheet.findViewById(R.id.v2);

                sortId.setOnClickListener(v1 -> {
                    if (mAuth != null && mAuth.getCurrentUser() != null && localManager.getProfile() != null)
                        presenter.updateSortType(SortType.ID);
                    if (fabSheet != null)
                        fabSheet.dismiss();
                    canFinishMain = true;
                    Collections.sort(list, Channel.IDComparator);
                    setChannelList(list);
                });
                sortName.setOnClickListener(v1 -> {
                    if (mAuth != null && mAuth.getCurrentUser() != null && localManager.getProfile() != null)
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
            if (item.getTitle().equals(getString(R.string.action_sign_in))) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            } else {
                    FirebaseAuth.getInstance().signOut();
                    showSnackbar(fab, getString(R.string.alert_signed_out), Snackbar.LENGTH_SHORT);
                    clearFavData(null);
                    canGetList = true;
            }
        } else if (id == R.id.nav_name) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(getString(R.string.author_linkedin)));
            startActivity(i);
        } else if (id == R.id.nav_email) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.author_email));
            startActivity(Intent.createChooser(intent, getString(R.string.action_send_email)));
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void setChannelList(List<Channel> list) {
        adapter = new FavChannelAdapter(list, localManager, remoteManager,this);
        rvFav.setAdapter(adapter);
    }

    @Override
    public void onRemoveFav(int channelId, int position) {
        if (list.get(position).getChannelId() == channelId) {
            //to make adapter get right position after remove
            list.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, list.size());
            //
            updateFavView(list.size(), true);
        }
    }

    @Override
    public void onGetFavDone(List<Channel> newList, boolean fromRemote) {
        if (fromRemote && list != null && list.size() == newList.size())
            return;
        this.list = newList;
        updateFavView(list.size(), false);
    }

    @Override
    public void onError(String message) {
        showSnackbar(fab, message, Snackbar.LENGTH_INDEFINITE);
    }
}
