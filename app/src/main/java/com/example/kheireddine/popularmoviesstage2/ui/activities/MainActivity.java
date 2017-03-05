package com.example.kheireddine.popularmoviesstage2.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.kheireddine.popularmoviesstage2.R;
import com.example.kheireddine.popularmoviesstage2.ui.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        } else {
            addGridFragment();
        }

    }


    public void addGridFragment() {
        if (!isFinishing()) {
            MainFragment mainFragment = MainFragment.create();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fl_main_grid, mainFragment)
                    .commit();
        }
    }



}

