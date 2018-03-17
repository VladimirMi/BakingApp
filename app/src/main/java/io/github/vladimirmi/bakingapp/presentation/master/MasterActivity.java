package io.github.vladimirmi.bakingapp.presentation.master;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.PlayerHolder;
import io.github.vladimirmi.bakingapp.data.entity.Step;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.BaseActivity;
import io.github.vladimirmi.bakingapp.presentation.detail.DetailActivity;
import io.github.vladimirmi.bakingapp.presentation.detail.ingredients.IngredientsFragment;
import io.github.vladimirmi.bakingapp.presentation.detail.step.StepFragment;
import io.github.vladimirmi.bakingapp.presentation.recipelist.RecipeListActivity;
import io.github.vladimirmi.bakingapp.utils.LineDividerItemDecoration;
import io.github.vladimirmi.bakingapp.utils.Utils;

/**
 * An activity representing a list of Recipe entities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings("WeakerAccess")
public class MasterActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.steps_list) RecyclerView stepsList;
    @BindView(R.id.ingredients) TextView ingredients;
    @Nullable @BindView(R.id.player_view) PlayerView playerView;
    @Nullable @BindView(R.id.player_thumb) ImageView playerThumb;

    private boolean twoPane;
    private MasterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ButterKnife.bind(this);

        viewModel = Scopes.getViewModel(this, MasterViewModel.class);

        if (findViewById(R.id.detail_container) != null) {
            twoPane = true;
            setupPlayerView();
        }

        // init state if come from widget
        if (getIntent() != null && getIntent().hasExtra(RecipeListActivity.EXTRA_RECIPE_ID)) {
            int recipeId = getIntent().getIntExtra(RecipeListActivity.EXTRA_RECIPE_ID, 0);
            viewModel.selectRecipe(recipeId);
            showIngredients();
            setIntent(null);
        }

        setupToolbar();
        setupIngredients();
        setupSteps();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (twoPane) playerView.setPlayer(viewModel.getPlayer());
    }

    @Override
    protected void onPause() {
        if (twoPane) playerView.setPlayer(null);
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

        bindData(viewModel.getSelectedStep(), step -> Utils.setImage(playerThumb, step.getThumbnailURL()));
    }

    private void setupIngredients() {
        ingredients.setOnClickListener(v -> showIngredients());

        bindData(viewModel.getSelectedStepPosition(), position -> {
            if (twoPane && position == -1) {
                ingredients.setBackgroundColor(Color.LTGRAY);
                showIngredients();
            } else {
                ingredients.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    private void setupSteps() {
        stepsList.setFocusable(false);
        stepsList.setLayoutManager(new LinearLayoutManager(this));
        StepAdapter adapter = new StepAdapter(viewModel::selectStepPosition);
        stepsList.setAdapter(adapter);
        stepsList.addItemDecoration(new LineDividerItemDecoration(this));

        bindData(viewModel.getSteps(), adapter::setData);

        bindData(viewModel.getSelectedStep(), this::showStep);

        if (twoPane) {
            bindData(viewModel.getSelectedStepPosition(), adapter::selectItem);
        }
    }

    private void showIngredients() {
        if (twoPane) {
            playerView.setVisibility(View.GONE);
            Fragment fragment = new IngredientsFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            startDetailActivity();
        }
    }

    private void showStep(Step step) {
        if (twoPane) {
            playerView.setVisibility(View.VISIBLE);
            Utils.setAspectRatio(playerView);
            Fragment fragment = StepFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            startDetailActivity();
        }
    }

    private void startDetailActivity() {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @VisibleForTesting
    public boolean isTwoPane() {
        return twoPane;
    }

    private void showSnack(int stringRes) {
        Snackbar.make(toolbar, stringRes, Snackbar.LENGTH_SHORT).show();
    }
}
