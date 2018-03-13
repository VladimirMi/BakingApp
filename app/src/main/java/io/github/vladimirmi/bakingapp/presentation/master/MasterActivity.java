package io.github.vladimirmi.bakingapp.presentation.master;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.Utils;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.detail.DetailActivity;
import io.github.vladimirmi.bakingapp.presentation.detail.ingredients.IngredientsFragment;
import io.github.vladimirmi.bakingapp.presentation.detail.step.StepFragment;

/**
 * An activity representing a list of Recipe entities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MasterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.steps_list) RecyclerView stepsList;
    @BindView(R.id.ingredients) TextView ingredients;
    PlayerView playerView;

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
            setupPlayer();
        }

        setupToolbar();
        setupIngredients();
        setupSteps();
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
    private void setupPlayer() {
        playerView = findViewById(R.id.playerView);

        playerView.setPlayer(viewModel.getPlayer());

        viewModel.isCanShowMultimedia().observe(this, can -> {
            playerView.getOverlayFrameLayout().setVisibility(can ? View.GONE : View.VISIBLE);
        });

        ImageView artView = playerView.findViewById(R.id.exo_artwork);
        viewModel.getSelectedStep().observe(this, step -> Utils.setImage(artView, step.getThumbnailURL()));

        Utils.setAspectRatio(playerView);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupIngredients() {
        ingredients.setOnClickListener(v -> showIngredients());

        viewModel.getSelectedStepPosition().observe(this, position -> {
            if (twoPane && position == -1) {
                ingredients.setBackgroundColor(Color.LTGRAY);
                showIngredients();
            } else {
                ingredients.setBackgroundColor(Color.WHITE);
            }
        });
    }

    private void setupSteps() {
        stepsList.setFocusable(false);
        stepsList.setLayoutManager(new LinearLayoutManager(this));
        StepAdapter adapter = new StepAdapter(this::showStep);
        stepsList.setAdapter(adapter);

        adapter.setData(viewModel.getSteps());

        if (twoPane) {
            viewModel.getSelectedStepPosition().observe(this, adapter::selectItem);
        }
    }

    private void showIngredients() {
        viewModel.selectStepPosition(-1);
        if (twoPane) {
            playerView.setVisibility(View.GONE);
            Fragment fragment = new IngredientsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        }
    }

    private void showStep(int position) {
        viewModel.selectStepPosition(position);
        if (twoPane) {
            playerView.setVisibility(View.VISIBLE);
            Fragment fragment = StepFragment.newInstance(viewModel.getSteps().get(position));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        }
    }

}
