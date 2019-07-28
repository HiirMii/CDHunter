package com.example.android.cdhunter.ui.searchresults;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.common.ArtistSummary;
import com.example.android.cdhunter.ui.artist.ArtistActivity;
import com.example.android.cdhunter.utils.NetworkConnection;
import com.example.android.cdhunter.viewmodel.SuggestionsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchResultsFragment extends Fragment implements Injectable,
        ArtistSummaryVerticalAdapter.ArtistSummaryAdapterOnClickHandler {

    private static final String ARTIST_NAME = "artistName";
    private static final String TAG = "tag";
    private static final String SEARCH_INPUT = "searchInput";
    private static final String SEARCH_TYPE = "searchType";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.search_results_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_results_main_view)
    NestedScrollView searchResultsMainView;
    @BindView(R.id.search_results_error_view)
    View errorView;
    @BindView(R.id.iv_error_view_icon)
    ImageView errorViewIcon;
    @BindView(R.id.tv_error_view_title)
    TextView errorViewTitle;
    @BindView(R.id.tv_error_view_subtitle)
    TextView errorViewSubtitle;
    @BindView(R.id.rv_search_results_list)
    RecyclerView searchResultsRecyclerView;

    private Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String searchInput = Objects.requireNonNull(Objects.requireNonNull(getActivity())
                .getIntent().getExtras()).getString(SEARCH_INPUT);

        String inputType = Objects.requireNonNull(getActivity().getIntent()
                .getExtras()).getString(SEARCH_TYPE);

        toolbar.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        if (!NetworkConnection.isConnected(Objects.requireNonNull(getContext()))) {
            searchResultsMainView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            errorViewIcon.setImageResource(R.drawable.ic_no_wifi);
            errorViewTitle.setText(R.string.error_message_no_internet_search_results_title);
            errorViewSubtitle.setText(R.string.error_message_no_internet_subtitle);
        }

        SuggestionsViewModel suggestionsViewModel = ViewModelProviders
                .of(this, viewModelFactory).get(SuggestionsViewModel.class);

        assert inputType != null;
        if (inputType.equals(TAG)) {
            suggestionsViewModel.getTagTopArtists(searchInput).observe(this,
                    artistSummaries -> {
                        if (artistSummaries != null) {
                            setSearchResultsData(artistSummaries);
                        }
                    });
        }
        if (inputType.equals(ARTIST_NAME)) {
            suggestionsViewModel.getArtistsForSearchQuery(searchInput).observe(this,
                    artistSummaries -> {
                        if (artistSummaries != null) {
                            setSearchResultsData(artistSummaries);
                        }
                    });
        }
    }

    @Override
    public void onArtistSummaryClick(String artistName) {
        Intent intent = new Intent(getActivity(), ArtistActivity.class);
        intent.putExtra(ARTIST_NAME, artistName);
        startActivity(intent);
    }

    public void setSearchResultsData(List<ArtistSummary> searchResultsData) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        searchResultsRecyclerView.setLayoutManager(linearLayoutManager);
        searchResultsRecyclerView.setHasFixedSize(true);
        ArtistSummaryVerticalAdapter artistSummaryVerticalAdapter =
                new ArtistSummaryVerticalAdapter(this);
        searchResultsRecyclerView.setAdapter(artistSummaryVerticalAdapter);
        artistSummaryVerticalAdapter.setArtistSummaryList(searchResultsData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
