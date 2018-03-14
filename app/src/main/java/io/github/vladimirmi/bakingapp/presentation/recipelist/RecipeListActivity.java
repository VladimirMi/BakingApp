package io.github.vladimirmi.bakingapp.presentation.recipelist;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Recipe;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;
import io.github.vladimirmi.bakingapp.utils.Utils;

/**
 * An activity representing a list of Recipes.
 */

public class RecipeListActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.recipe_list) RecyclerView recipeList;

    private RecipeListViewModel viewModel;
    private boolean chooseRecipeForWidget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        viewModel = Scopes.getViewModel(this, RecipeListViewModel.class);

        setupToolbar();
        setupRecycler();

        chooseRecipeForWidget = getIntent() != null && getIntent().hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID);
        if (chooseRecipeForWidget) {
            showToast(R.string.choose_recipe);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecycler() {
        GridLayoutManager lm = new GridLayoutManager(this, calculateSpanCount());
        recipeList.setLayoutManager(lm);
        RecipeAdapter adapter = new RecipeAdapter(this::showRecipe);
        recipeList.setAdapter(adapter);

        viewModel.getRecipes().observe(this, adapter::setData);
    }

    private void showRecipe(Recipe recipe) {
        if (chooseRecipeForWidget) {
            int widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            viewModel.saveRecipeForWidget(widgetId, recipe);
            showToast(R.string.widget_updated);
            finishAffinity();
        } else {
            viewModel.selectRecipe(recipe);
            Intent intent = new Intent(this, MasterActivity.class);
            startActivity(intent);
        }
    }

    private void showToast(int stringResId) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
    }

    private int calculateSpanCount() {
        final int maxPosterWidth = (int) getResources().getDimension(R.dimen.card_max_width);

        int spanCount = 0;
        DisplayMetrics displayMetrics = Utils.getDisplayMetrics(this);
        int posterWidthDp;
        do {
            spanCount++;
            posterWidthDp = displayMetrics.widthPixels / spanCount;
        } while (posterWidthDp >= maxPosterWidth);

        return spanCount;
    }
}
