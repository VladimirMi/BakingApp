package io.github.vladimirmi.bakingapp.presentation.master;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.RecipeStepVH> {

    private final OnItemClickListener listener;
    private List<Step> steps = new ArrayList<>();
    private int selectedPosition;

    public StepAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Step> steps) {
        this.steps = new ArrayList<>(steps);
        notifyDataSetChanged();
    }

    @Override
    public RecipeStepVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);

        return new RecipeStepVH(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepVH holder, int position, List<Object> payloads) {
        holder.select(!payloads.isEmpty());
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(RecipeStepVH holder, int position) {
        holder.bind(steps.get(position), position);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void selectItem(int position) {
        if (selectedPosition == position) return;
        notifyItemChanged(position, true); // have payload -> select
        notifyItemChanged(selectedPosition); // unselect
        selectedPosition = position;
    }

    static class RecipeStepVH extends RecyclerView.ViewHolder {

        @BindView(R.id.text) TextView textView;

        RecipeStepVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Step step, int position) {
            @SuppressLint("DefaultLocale")
            String text = String.format("%d. %s", position, step.getShortDescription());
            textView.setText(text);
        }

        void select(boolean selected) {
            itemView.setBackgroundColor(selected ? Color.GRAY : Color.WHITE);
        }
    }

    interface OnItemClickListener {

        void onItemClick(int position);
    }
}
