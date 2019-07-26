package com.example.android.cdhunter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.repository.AlbumRepository;

import java.util.List;

import javax.inject.Inject;

/**
 *  This ViewModel handles Album data for AlbumFragment, CollectionFragment and WishlistFragment
 */

public class AlbumViewModel extends ViewModel {

    private AlbumRepository albumRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public AlbumViewModel(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public LiveData<Album> getAlbum(String artistName, String albumName, String userId, String albumId) {
        return albumRepository.getAlbum(artistName, albumName, userId, albumId);
    }

    public LiveData<List<Album>> getAlbumList(String userId, String ownershipStatus) {
        return albumRepository.getAllAlbums(userId, ownershipStatus);
    }
}
