package io.github.vladimirmi.bakingapp.presentation.detail.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.entity.Step;
import io.github.vladimirmi.bakingapp.presentation.detail.DetailActivity;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;

/**
 * A fragment representing a Step detail screen.
 * This fragment is either contained in a {@link MasterActivity}
 * in two-pane mode (on tablets) or a {@link DetailActivity}
 * on handsets.
 */
@SuppressWarnings("WeakerAccess")
public class StepFragment extends Fragment {

    private static final String STEP = "STEP";

    @BindView(R.id.step_title) TextView stepTitle;
    @BindView(R.id.step_description) TextView stepDescription;
    Unbinder unbinder;

    private Step step;

    public StepFragment() {
    }

    public static StepFragment newInstance(Step step) {
        Bundle args = new Bundle();
        args.putParcelable(STEP, step);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        step = getArguments().getParcelable(STEP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        stepTitle.setText(step.getShortDescription());
        stepDescription.setText(step.getDescription());
    }
}
