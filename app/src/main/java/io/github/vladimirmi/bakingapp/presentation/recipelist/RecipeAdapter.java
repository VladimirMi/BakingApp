package io.github.vladimirmi.bakingapp.presentation.recipelist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeVH> {

    @Override
    public RecipeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecipeVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class RecipeVH extends RecyclerView.ViewHolder {

        public RecipeVH(View itemView) {
            super(itemView);
        }
    }
}
