package io.github.vladimirmi.bakingapp;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.github.vladimirmi.bakingapp.data.RecipeRepository;
import io.github.vladimirmi.bakingapp.data.entity.Recipe;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.detail.DetailActivity;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by Vladimir Mikhalev 16.03.2018.
 */

@RunWith(AndroidJUnit4.class)
public class MasterActivityTest {

    @Rule
    public IntentsTestRule<MasterActivity> rule = new IntentsTestRule<>(MasterActivity.class);

    private boolean twoPane;

    @Before
    public void setupTests() {
        RecipeRepository repository = Scopes.appScope().getInstance(RecipeRepository.class);

        List<Recipe> recipes = repository.getRecipes().blockingGet();
        repository.selectRecipe(recipes.get(0).getId()).blockingAwait();

        twoPane = rule.getActivity().isTwoPane();
    }

    @Test
    public void masterDetailFlow_twoPane_showIngredients() {
        if (!twoPane) return;
        //Ingredients should be displayed by default
        onView(withId(R.id.ingredients_list)).check(matches(isDisplayed()));
        onView(withId(R.id.player_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.step_title)).check(doesNotExist());
        onView(withId(R.id.step_description)).check(doesNotExist());

        masterDetailFlow_twoPane_showStep();
    }

    public void masterDetailFlow_twoPane_showStep() {
        onView(withId(R.id.steps_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        onView(withId(R.id.player_view)).check(matches(isDisplayed()));
        onView(withId(R.id.step_title)).check(matches(isDisplayed()));
        onView(withId(R.id.step_description)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_list)).check(doesNotExist());
    }

    @Test
    public void masterDetailFlow_onePane_showIngredients() {
        if (twoPane) return;
        onView(withId(R.id.ingredients)).perform(click());

        intended(hasComponent(DetailActivity.class.getName()));
        onView(withId(R.id.ingredients_list)).check(matches(isDisplayed()));

        Espresso.pressBack();

        masterDetailFlow_onePane_showStep();
    }

    public void masterDetailFlow_onePane_showStep() {
        onView(withId(R.id.steps_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        intended(hasComponent(DetailActivity.class.getName()), times(2));
        onView(withId(R.id.player_view)).check(matches(isDisplayed()));
        onView(withId(R.id.steps_pager)).check(matches(isDisplayed()));

        Espresso.pressBack();

        masterDetailFlow_onePane_showStep_landscape();
    }

    public void masterDetailFlow_onePane_showStep_landscape() {
        onView(withId(R.id.steps_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        Utils.getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        intended(hasComponent(DetailActivity.class.getName()), times(3));
        onView(withId(R.id.player_view)).check(matches(isDisplayed()));
        onView(withId(R.id.steps_pager)).check(doesNotExist());
    }
}
