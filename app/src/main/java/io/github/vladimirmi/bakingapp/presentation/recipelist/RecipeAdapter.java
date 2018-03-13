package io.github.vladimirmi.bakingapp.presentation.recipelist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Recipe;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeVH> {

    private List<Recipe> recipes = new ArrayList<>();
    private final OnRecipeClickListener listener;

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }

    @Override
    public RecipeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe, parent, false);
        return new RecipeVH(view);
    }

    @Override
    public void onBindViewHolder(RecipeVH holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
        holder.itemView.setOnClickListener(view -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeVH extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_image) ImageView recipeImage;
        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.recipe_servings) TextView recipeServings;

        RecipeVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Recipe recipe) {
            Context context = recipeImage.getContext();
            Glide.with(context).load(recipe.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.cook_hat)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .crossFade()
                    .into(recipeImage);

            recipeName.setText(recipe.getName());
            recipeServings.setText(context.getResources().getQuantityString(
                    R.plurals.recipe_serving,
                    recipe.getServings(),
                    recipe.getServings()
            ));
        }
    }

    interface OnRecipeClickListener {

        void onRecipeClick(Recipe recipe);
    }
}
