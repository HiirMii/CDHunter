package com.example.android.cdhunter.ui.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseActivity extends AppCompatActivity {

    public void loadFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
            fragmentManager.beginTransaction()
                    .replace(containerId, fragment)
                    .commitAllowingStateLoss();
    }

    public void loadFragmentWithBackStack(
            FragmentManager fragmentManager, Fragment fragment, int containerId) {
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void loadFragmentWithBackStackClear(FragmentManager fragmentManager,
                                               Fragment fragment, int containerId) {

        fragmentManager.popBackStackImmediate();

        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss();
    }
}
