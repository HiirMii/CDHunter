package com.example.android.cdhunter.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.cdhunter.viewmodel.SuggestionsViewModel;
import com.example.android.cdhunter.viewmodel.AlbumViewModel;
import com.example.android.cdhunter.viewmodel.ArtistViewModel;
import com.example.android.cdhunter.viewmodel.CdHunterViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SuggestionsViewModel.class)
    abstract ViewModel bindsSuggestionsViewModel(
            SuggestionsViewModel suggestionsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel.class)
    abstract ViewModel bindsAlbumViewModel(AlbumViewModel albumViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ArtistViewModel.class)
    abstract ViewModel bindsArtistViewModel(ArtistViewModel artistViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(CdHunterViewModelFactory factory);
}
