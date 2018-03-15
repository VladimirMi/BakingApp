package io.github.vladimirmi.bakingapp.presentation.detail.ingredients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.BaseFragment;

/**
 * Created by Vladimir Mikhalev 11.03.2018.
 */

@SuppressWarnings("WeakerAccess")
public class IngredientsFragment extends BaseFragment {

    @BindView(R.id.ingredients_list) RecyclerView ingredientsList;
    Unbinder unbinder;
    IngredientsViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = Scopes.getViewModel(this, IngredientsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
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
        RecyclerView ingredientList = view.findViewById(R.id.ingredients_list);
        ingredientList.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientList.setAdapter(new IngredientsAdapter(Collections.emptyList()));

        bindData(viewModel.getIngredients(),
                ingredients -> ingredientList.swapAdapter(new IngredientsAdapter(ingredients), false));
    }
}
