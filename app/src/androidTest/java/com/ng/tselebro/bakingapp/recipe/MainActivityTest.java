package com.ng.tselebro.bakingapp.recipe;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipe.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;


    @Before
    public void registerIdlingResource(){
    mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void showRecyclerView(){
        ViewInteraction recyclerView = onView(ViewMatchers.withId(
                R.id.recipeList));
        recyclerView.check(matches(isDisplayed()));
    }


    @Test
    public void mainActivityTest() {
        ViewInteraction textView = onView(
                allOf(withText("Available Recipes"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Available Recipes")));

    }

    @Test
    public void recipeThumbnailCheck() {

        ViewInteraction imageView = onView(allOf(withId(R.id.recipeImage), hasSibling(withText("Nutella Pie")), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0), 0), isDisplayed()));
        imageView.check(matches(isDisplayed()));
    }


    @Test
    public void clickRecyclerView_LaunchesRecipeDetails(){
        ViewInteraction recyclerView = onView(withId(R.id.recipeList));
        recyclerView.perform(actionOnItemAtPosition(1, click()));
    }


    @After
    public void unregisterIdlingResource(){
        if (mIdlingResource != null){
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
