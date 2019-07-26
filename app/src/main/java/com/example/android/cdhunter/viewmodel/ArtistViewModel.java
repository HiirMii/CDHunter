package com.example.android.cdhunter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.cdhunter.model.artist.Artist;
import com.example.android.cdhunter.model.topalbums.AlbumSummary;
import com.example.android.cdhunter.repository.AlbumSummaryRepository;
import com.example.android.cdhunter.repository.ArtistRepository;

import java.util.List;

import javax.inject.Inject;

/**
 *  This ViewModel handles  both Artist and Album data for ArtistFragment
 */

public class ArtistViewModel extends ViewModel {

    private ArtistRepository artistRepository;
    private AlbumSummaryRepository albumSummaryRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public ArtistViewModel(ArtistRepository artistRepository, AlbumSummaryRepository albumSummaryRepository) {
        this.artistRepository = artistRepository;
        this.albumSummaryRepository = albumSummaryRepository;
    }

    public LiveData<Artist> getArtist(String artistName, String userId) {
        return artistRepository.getArtist(artistName, userId);
    }

    public LiveData<List<AlbumSummary>> getArtistTopAlbums(String artistName) {
        return albumSummaryRepository.getArtistTopAlbums(artistName);
    }
}
