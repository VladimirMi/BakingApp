package io.github.vladimirmi.bakingapp.presentation.detail;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.BaseActivity;
import io.github.vladimirmi.bakingapp.presentation.detail.ingredients.IngredientsFragment;
import io.github.vladimirmi.bakingapp.presentation.detail.step.StepPagerAdapter;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;
import io.github.vladimirmi.bakingapp.utils.Utils;

/**
 * An activity representing a single Recipe entity detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MasterActivity}.
 */
@SuppressWarnings("WeakerAccess")
public class DetailActivity extends BaseActivity {

    public static final int CONTROLLER_SHOW_TIMEOUT_MS = 3000;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.detail_container) FrameLayout detailContainer;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.player_view) PlayerView playerView;
    @BindView(R.id.player_thumb) ImageView playerThumb;


    private DetailViewModel viewModel;
    private boolean isLandscapeMode;
    private boolean isStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        viewModel = Scopes.getViewModel(this, DetailViewModel.class);

        isStep = viewModel.getSelectedStepPosition().blockingFirst() != -1;
        isLandscapeMode = getResources().getBoolean(R.bool.landscape);


        if (isStep) {
            setupPlayerView();
            if (!isLandscapeMode) setupSteps();
        } else {
            setupIngredients();
        }

        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStep) playerView.setPlayer(viewModel.getPlayer());
    }

    @Override
    protected void onPause() {
        if (isStep) {
            playerView.setPlayer(null);
            viewModel.releasePlayer();
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void setupToolbar() {
        bindData(viewModel.getSelectedRecipe(), recipe -> toolbar.setTitle(recipe.getName()));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupSteps() {
        ViewPager pager = (ViewPager) getLayoutInflater()
                .inflate(R.layout.view_detail_steps, detailContainer, false);
        detailContainer.addView(pager);

        StepPagerAdapter adapter = new StepPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        bindData(viewModel.getSteps(), adapter::setData);
        tabs.setupWithViewPager(pager);

        bindData(viewModel.getSelectedStepPosition(), pager::setCurrentItem);

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                viewModel.selectStepPosition(position);
            }
        });
    }

    private int lastSystemUIVisibility;

    private void setupPlayerView() {
        bindData(viewModel.getPlayerStatus(), status -> {
            playerThumb.setVisibility(status == PlayerHolder.PlayerStatus.NORMAL ? View.GONE : View.VISIBLE);
            switch (status) {
                case SOURCE_ERROR:
                    showSnack(R.string.video_not_available);
                    break;
                case UNEXPECTED_ERROR:
                    showSnack(R.string.unexpected_error);
                    break;
            }
        });

        bindData(viewModel.getPlaybackStatus(), status -> {
            switch (status) {
                case PLAYED:
                    keepScreenOn(true);
                    break;
                case STOPPED:
                    keepScreenOn(false);
                    break;
            }
        });

        bindData(viewModel.getSelectedStep(), step -> Utils.setImage(playerThumb, step.getThumbnailURL()));

        Utils.setAspectRatio(playerView);

        playerView.setControllerAutoShow(false);
        playerView.setControllerShowTimeoutMs(CONTROLLER_SHOW_TIMEOUT_MS);

        if (isLandscapeMode) {
            appbar.setVisibility(View.GONE);
            tabs.setVisibility(View.GONE);
            detailContainer.setVisibility(View.GONE);
            enterFullScreen();
        }
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((lastSystemUIVisibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0
                    && (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                resetHideTimer();
                playerView.showController();
            }
            lastSystemUIVisibility = visibility;
        });
    }

    private void setupIngredients() {
        tabs.setVisibility(View.GONE);
        playerView.setVisibility(View.GONE);
        playerThumb.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.detail_container, new IngredientsFragment())
                .commit();
    }

    private final Handler leanBackHandler = new Handler();
    private final Runnable enterLeanback = this::enterFullScreen;

    private void resetHideTimer() {
        leanBackHandler.removeCallbacks(enterLeanback);
        leanBackHandler.postDelayed(enterLeanback, CONTROLLER_SHOW_TIMEOUT_MS);
    }
}
