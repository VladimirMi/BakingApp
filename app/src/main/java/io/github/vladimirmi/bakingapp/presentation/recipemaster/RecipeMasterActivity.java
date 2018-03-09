package io.github.vladimirmi.bakingapp.presentation.recipemaster;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Step;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.recipedetail.RecipeDetailActivity;

/**
 * An activity representing a list of Recipe entities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeMasterActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "RECIPE_ID";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.master_list) RecyclerView entitiesList;

    private boolean mTwoPane;
    private RecipeMasterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_master);
        ButterKnife.bind(this);

        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        }
        viewModel = ViewModelProviders.of(this, Scopes.VmFactory()).get(RecipeMasterViewModel.class);

        setupToolbar();
        setupRecycler();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupRecycler() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        entitiesList.setLayoutManager(lm);
        RecipeStepAdapter adapter = new RecipeStepAdapter(this::showStep);
        entitiesList.setAdapter(adapter);

        viewModel.getRecipe(getIntent().getIntExtra(RECIPE_ID, 0))
                .observe(this, recipe -> {
                    List<Step> steps = recipe.getSteps();
                    adapter.setData(steps);
                });
    }

    private void showStep(Step step) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.STEP, step);
        startActivity(intent);
    }

}
