package io.github.vladimirmi.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.idlingresource.IdlingResources;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;
import io.github.vladimirmi.bakingapp.presentation.recipelist.RecipeListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Vladimir Mikhalev 16.03.2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public IntentsTestRule<RecipeListActivity> rule = new IntentsTestRule<>(RecipeListActivity.class);
    private IdlingResources.SimpleIdleResource idlingResource;

    @Before
    public void registerIdlingResource() {
        RecipeRepository repository = Scopes.appScope().getInstance(RecipeRepository.class);
        repository.getRecipes().blockingGet();
//        idlingResource = repository.getIdlingResource();
//        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void clickRecipe_opensMasterActivity() {
        onView(withId(R.id.recipe_list)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("Brownies")), click())
        );

        intended(hasComponent(MasterActivity.class.getName()));
        onView(withId(R.id.toolbar)).check(matches(Matchers.withToolbarTitle("Brownies")));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
