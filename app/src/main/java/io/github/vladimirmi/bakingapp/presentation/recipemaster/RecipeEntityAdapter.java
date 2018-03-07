package io.github.vladimirmi.bakingapp.presentation.recipemaster;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class RecipeEntityAdapter extends RecyclerView.Adapter<RecipeEntityAdapter.RecipeEntityVH> {

    @Override
    public RecipeEntityVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecipeEntityVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class RecipeEntityVH extends RecyclerView.ViewHolder {

        public RecipeEntityVH(View itemView) {
            super(itemView);
        }
    }
}
