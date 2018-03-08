package io.github.vladimirmi.bakingapp.data.net;

import java.util.List;

import io.github.vladimirmi.bakingapp.data.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Rest service interface
 */

public interface RestService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
