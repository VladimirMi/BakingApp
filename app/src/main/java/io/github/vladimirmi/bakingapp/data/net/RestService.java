package io.github.vladimirmi.bakingapp.data.net;

import java.util.List;

import io.github.vladimirmi.bakingapp.data.Recipe;
import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Rest service interface
 */

public interface RestService {

    @GET("baking.json")
    Single<List<Recipe>> getRecipes();
}
