package com.example.recipeapp;


import android.view.View;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.anything;
@RunWith(AndroidJUnit4.class)
public class RecipeChooseTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule= new ActivityTestRule<>(MainActivity.class) ;

    @Test
    public void clickRecyclerViewButton_StartRecipeActivity () {
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.recipe_details_holder)).check(matches(ViewMatchers.isDisplayed()));
    }
}
