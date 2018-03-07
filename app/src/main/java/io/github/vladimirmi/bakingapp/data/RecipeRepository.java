package io.github.vladimirmi.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.github.vladimirmi.bakingapp.data.net.RestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 07.03.2018.
 */

public class RecipeRepository {

    private RestService rest;

    @Inject
    public RecipeRepository(RestService restService) {
        this.rest = restService;
    }

    public LiveData<List<Recipe>> getRecipes() {
        final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

        rest.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.e(t);
            }
        });

        return data;
    }

}
