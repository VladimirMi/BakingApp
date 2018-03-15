package io.github.vladimirmi.bakingapp.presentation.master;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Step;

/**
 * Created by Vladimir Mikhalev 08.03.2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepVH> {

    private final OnItemClickListener listener;
    private List<Step> steps = new ArrayList<>();
    private int selectedPosition = -1;

    public StepAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Step> steps) {
        this.steps = new ArrayList<>(steps);
        notifyDataSetChanged();
    }

    @Override
    public StepVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);

        return new StepVH(view);
    }

    @Override
    public void onBindViewHolder(StepVH holder, int position, List<Object> payloads) {
        holder.select(!payloads.isEmpty());
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(StepVH holder, int position) {
        holder.select(position == selectedPosition);
        holder.bind(steps.get(position), position);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void selectItem(int position) {
        if (selectedPosition == position) return;
        selectedPosition = position;
        notifyDataSetChanged();
    }

    static class StepVH extends RecyclerView.ViewHolder {

        StepVH(View itemView) {
            super(itemView);
        }

        @SuppressLint("DefaultLocale")
        void bind(Step step, int position) {
            String text;
            if (position == 0) {
                text = step.getShortDescription();
            } else {
                text = String.format("%d. %s", position, step.getShortDescription());
            }
            ((Button) itemView).setText(text);
        }

        void select(boolean selected) {
            itemView.setBackgroundColor(selected ? Color.LTGRAY : Color.TRANSPARENT);
        }
    }

    interface OnItemClickListener {

        void onItemClick(int position);
    }
}
