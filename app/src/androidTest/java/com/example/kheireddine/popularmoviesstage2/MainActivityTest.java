package com.example.kheireddine.popularmoviesstage2;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.kheireddine.popularmoviesstage2.ui.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by kheirus on 03/03/2017.
 */


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mMovieListActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before


    @Test
    public void mainActivityLaunches(){
        onView(withId(R.id.rv_movies_list)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void canScroll(){

    }
}
