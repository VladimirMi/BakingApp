package io.github.vladimirmi.bakingapp.presentation.recipemaster;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Step;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepVH> {

    private final OnEntityClickListener listener;
    private List<Step> steps = new ArrayList<>();

    public RecipeStepAdapter(OnEntityClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Step> steps) {
        this.steps = new ArrayList<>(steps);
        notifyDataSetChanged();
    }


    @Override
    public RecipeStepVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_step, parent, false);

        return new RecipeStepVH(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepVH holder, int position) {
        Step step = steps.get(position);
        holder.bind(step, position);
        holder.itemView.setOnClickListener(v -> listener.onStepClick(step));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static class RecipeStepVH extends RecyclerView.ViewHolder {

        @BindView(R.id.text) TextView textView;

        RecipeStepVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Step step, int position) {
            @SuppressLint("DefaultLocale")
            String text = String.format("%d. %s", position + 1, step.getShortDescription());
            textView.setText(text);
        }
    }

    interface OnEntityClickListener {

        void onStepClick(Step step);
    }
}
