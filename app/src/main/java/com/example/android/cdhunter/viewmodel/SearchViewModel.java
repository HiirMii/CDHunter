package com.example.android.cdhunter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.cdhunter.model.common.Tag;
import com.example.android.cdhunter.repository.TagRepository;

import java.util.List;

import javax.inject.Inject;

/**
 *  This ViewModel handles Album data for SearchFragment
 */

public class SearchViewModel extends ViewModel {

    private TagRepository tagRepository;

    @Inject
    public SearchViewModel(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public LiveData<List<Tag>> getTopTags() {
        return tagRepository.getTopTags();
    }
}
