package com.example.android.cdhunter.di;

import com.example.android.cdhunter.ui.auth.AccountRecoveryFragment;
import com.example.android.cdhunter.ui.auth.AuthMethodPickerFragment;
import com.example.android.cdhunter.ui.auth.LogInFragment;
import com.example.android.cdhunter.ui.auth.SignUpFragment;
import com.example.android.cdhunter.ui.main.CollectionFragment;
import com.example.android.cdhunter.ui.main.HomeFragment;
import com.example.android.cdhunter.ui.main.SearchFragment;
import com.example.android.cdhunter.ui.main.WishlistFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    // main activity fragments
    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();

    @ContributesAndroidInjector
    abstract WishlistFragment contributeWishlistFragment();

    // auth activity fragments
    @ContributesAndroidInjector
    abstract AuthMethodPickerFragment contributeAuthPickerFragment();

    @ContributesAndroidInjector
    abstract SignUpFragment contributeSignUpFragment();

    @ContributesAndroidInjector
    abstract LogInFragment contributeLogInFragment();

    @ContributesAndroidInjector
    abstract AccountRecoveryFragment contributeAccountRecoveryFragment();
}
