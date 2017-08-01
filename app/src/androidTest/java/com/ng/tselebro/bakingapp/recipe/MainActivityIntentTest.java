package com.ng.tselebro.bakingapp.recipe;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ng.tselebro.bakingapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ng.tselebro.bakingapp.recipe.MainActivity.RECIPE_ID;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityTestIntentsRule = new IntentsTestRule<MainActivity>(MainActivity.class);

    @Before
    public void stubAllExternalIntents(){
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void  recipeCardIsSelected_passIntent(){
        ViewInteraction recyclerView = onView(withId(R.id.recipeList)).perform();
        recyclerView.perform(actionOnItemAtPosition(0 , click()));
        intended(allOf(hasExtra(RECIPE_ID,(long) 1)));


    }

    @Test
    public void recipeCardSelected(){
        ViewInteraction recyclerView = onView(withId(R.id.recipeList));
        recyclerView.perform(actionOnItemAtPosition(1, click()));
        Intent intent = new Intent();
        intent.putExtra(RECIPE_ID, 3);
        Instrumentation.ActivityResult  activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(allOf(hasExtra(RECIPE_ID, 3))).respondWith(activityResult);

        onView(allOf(withText("Ingredients"))).check(matches(isDisplayed()));
    }
}
