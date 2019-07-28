package com.example.android.cdhunter.di;

import com.example.android.cdhunter.ui.album.AlbumActivity;
import com.example.android.cdhunter.ui.artist.ArtistActivity;
import com.example.android.cdhunter.ui.auth.AuthActivity;
import com.example.android.cdhunter.ui.main.MainActivity;
import com.example.android.cdhunter.ui.profile.ProfileActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract AuthActivity contributeAuthActivity();
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ProfileActivity contributeProfileActivity();
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ArtistActivity contributeArtistActivity();
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract AlbumActivity contributeAlbumActivity();
}
