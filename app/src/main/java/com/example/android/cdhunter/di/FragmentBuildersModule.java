package com.example.android.cdhunter.di;

import com.example.android.cdhunter.ui.main.CollectionFragment;
import com.example.android.cdhunter.ui.main.HomeFragment;
import com.example.android.cdhunter.ui.main.SearchFragment;
import com.example.android.cdhunter.ui.main.WishlistFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();

    @ContributesAndroidInjector
    abstract WishlistFragment contributeWishlistFragment();
}
