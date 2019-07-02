package com.example.android.cdhunter.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.ui.common.BaseActivity;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class AuthActivity extends BaseActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        loadFragment(getSupportFragmentManager(), new AuthMethodPickerFragment(),
                R.id.auth_fragment_container);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
