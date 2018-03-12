package io.github.vladimirmi.bakingapp.presentation.detail.ingredients;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Ingredient;

/**
 * Created by Vladimir Mikhalev 11.03.2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientVH> {

    private final List<Ingredient> ingredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientVH(view);
    }

    @Override
    public void onBindViewHolder(IngredientVH holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientVH extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name) TextView ingredientName;
        @BindView(R.id.ingredient_quantity) TextView ingredientMeasure;

        public IngredientVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Ingredient ingredient) {
            ingredientName.setText(ingredient.getIngredient());
            ingredientMeasure.setText(ingredient.getQuantity());
        }
    }
}
