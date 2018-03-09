package io.github.vladimirmi.bakingapp.presentation.recipelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;

/**
 * An activity representing a list of Recipes.
 */

public class RecipeListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.recipe_list) RecyclerView recipeList;

    private RecipeListViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        viewModel = Scopes.getViewModel(this, RecipeListViewModel.class);

        setupToolbar();
        setupRecycler();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecycler() {
        GridLayoutManager lm = new GridLayoutManager(this, 1);
        recipeList.setLayoutManager(lm);
        RecipeAdapter adapter = new RecipeAdapter(this::showRecipe);
        recipeList.setAdapter(adapter);

        viewModel.getRecipes().observe(this, adapter::setData);
    }

    private void showRecipe(Recipe recipe) {
        Intent intent = new Intent(this, MasterActivity.class);
        intent.putExtra(MasterActivity.RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
