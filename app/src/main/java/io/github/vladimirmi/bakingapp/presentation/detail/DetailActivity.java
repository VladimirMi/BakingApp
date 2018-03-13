package io.github.vladimirmi.bakingapp.presentation.detail;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.Utils;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.detail.ingredients.IngredientsFragment;
import io.github.vladimirmi.bakingapp.presentation.detail.step.StepPagerAdapter;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;
import io.github.vladimirmi.bakingapp.presentation.recipelist.RecipeListActivity;

/**
 * An activity representing a single Recipe entity detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MasterActivity}.
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.detail_container) FrameLayout detailContainer;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.playerView) PlayerView playerView;

    private DetailViewModel viewModel;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        viewModel = Scopes.getViewModel(this, DetailViewModel.class);

        // init state if come from widget
        if (getIntent() != null && getIntent().hasExtra(RecipeListActivity.EXTRA_RECIPE_ID)) {
            int recipeId = getIntent().getIntExtra(RecipeListActivity.EXTRA_RECIPE_ID, 0);
            viewModel.selectRecipe(recipeId);
            setIntent(null);
        }

        if (viewModel.getSelectedStepPosition().getValue() == -1) {
            setupIngredients();
        } else {
            setupSteps();
        }

        setupToolbar();

    }

    private void setupToolbar() {
        toolbar.setTitle(viewModel.getSelectedRecipe().getName());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupSteps() {
        ViewPager pager = (ViewPager) getLayoutInflater()
                .inflate(R.layout.view_detail_steps, detailContainer, false);
        detailContainer.addView(pager);

        StepPagerAdapter adapter = new StepPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        adapter.setData(viewModel.getSteps());
        tabs.setupWithViewPager(pager);

        viewModel.getSelectedStepPosition().observe(this, pager::setCurrentItem);

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                viewModel.selectStepPosition(position);
            }
        });

        setupPlayer();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupPlayer() {
        playerView.setPlayer(viewModel.getPlayer());

        viewModel.isCanShowMultimedia().observe(this, can -> {
            playerView.getOverlayFrameLayout().setVisibility(can ? View.GONE : View.VISIBLE);
        });

        ImageView artView = playerView.findViewById(R.id.exo_artwork);
        viewModel.getSelectedStep().observe(this, step -> Utils.setImage(artView, step.getThumbnailURL()));

        Utils.setAspectRatio(playerView);
    }

    private void setupIngredients() {
        tabs.setVisibility(View.GONE);
        playerView.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_container, new IngredientsFragment())
                .commit();
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
}
