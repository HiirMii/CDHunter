package com.example.android.cdhunter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.cdhunter.model.common.ArtistSummary;
import com.example.android.cdhunter.model.topalbums.AlbumSummary;
import com.example.android.cdhunter.repository.AlbumRepository;
import com.example.android.cdhunter.repository.AlbumSummaryRepository;
import com.example.android.cdhunter.repository.ArtistSummaryRepository;

import java.util.List;

import javax.inject.Inject;

/**
 *  This ViewModel handles both Artist and Album data for HomeFragment and SearchFragment
 */

public class SuggestionsViewModel extends ViewModel {

    private AlbumSummaryRepository albumSummaryRepository;
    private ArtistSummaryRepository artistSummaryRepository;
    private AlbumRepository albumRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public SuggestionsViewModel(AlbumSummaryRepository albumSummaryRepository,
                                ArtistSummaryRepository artistSummaryRepository,
                                AlbumRepository albumRepository) {
        this.albumSummaryRepository = albumSummaryRepository;
        this.artistSummaryRepository = artistSummaryRepository;
        this.albumRepository = albumRepository;
    }

    public LiveData<List<AlbumSummary>> getUserInterestArtistTopAlbums(String userId) {
        return albumSummaryRepository.getUserInterestArtistTopAlbums(userId);
    }

    public LiveData<List<ArtistSummary>> getSimilarArtists(String userId) {
        return artistSummaryRepository.getSimilarArtists(userId);
    }

    public LiveData<List<ArtistSummary>> getArtistsForSearchQuery(String artistName) {
        return artistSummaryRepository.getArtistsForSearchQuery(artistName);
    }

    public LiveData<List<ArtistSummary>> getChartTopArtists() {
        return artistSummaryRepository.getChartTopArtists();
    }

    public LiveData<List<ArtistSummary>> getTagTopArtists(String tag) {
        return artistSummaryRepository.getTagTopArtists(tag);
    }

    public LiveData<List<ArtistSummary>> getUserInterestTagTopArtists(String userId) {
        return artistSummaryRepository.getUserInterestTagTopArtists(userId);
    }

    public void insertAllAlbums (String userId) {
        albumRepository.insertAllAlbums(userId);
    }
}
