package com.example.android.cdhunter.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.common.ArtistSummary;
import com.example.android.cdhunter.model.topalbums.AlbumSummary;
import com.example.android.cdhunter.ui.album.AlbumActivity;
import com.example.android.cdhunter.ui.artist.ArtistActivity;
import com.example.android.cdhunter.ui.profile.ProfileActivity;
import com.example.android.cdhunter.utils.NetworkConnection;
import com.example.android.cdhunter.viewmodel.SuggestionsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements Injectable,
        ArtistSummaryHorizontalAdapter.ArtistSummaryAdapterOnClickHandler,
        AlbumSummaryHorizontalAdapter.AlbumSummaryAdapterOnClickHandler {

    private static final String ARTIST_NAME = "artistName";
    private static final String ALBUM_NAME = "albumName";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.iv_profile_icon)
    ImageView profileIcon;
    @BindView(R.id.home_main_view)
    NestedScrollView homeMainView;
    @BindView(R.id.home_error_view)
    View errorView;
    @BindView(R.id.iv_error_view_icon)
    ImageView errorViewIcon;
    @BindView(R.id.tv_error_view_title)
    TextView errorViewTitle;
    @BindView(R.id.tv_error_view_subtitle)
    TextView errorViewSubtitle;
    @BindView(R.id.rv_chart_list)
    RecyclerView chartRecyclerView;
    @BindView(R.id.similar_artist_list_layout)
    LinearLayout similarArtistListLayout;
    @BindView(R.id.rv_similar_artist_list)
    RecyclerView similarArtistRecyclerView;
    @BindView(R.id.tag_top_artist_list_layout)
    LinearLayout tagTopArtistLinearLayout;
    @BindView(R.id.rv_tag_top_artist_list)
    RecyclerView tagTopArtistRecyclerView;
    @BindView(R.id.similar_artist_top_albums_list_layout)
    LinearLayout similarArtistTopAlbumsLinearLayout;
    @BindView(R.id.rv_similar_artist_top_albums_list)
    RecyclerView similarArtistTopAlbumsRecyclerView;


    private Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!NetworkConnection.isConnected(Objects.requireNonNull(getContext()))) {
            homeMainView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            errorViewIcon.setImageResource(R.drawable.ic_no_wifi);
            errorViewTitle.setText(R.string.error_message_no_internet_title);
            errorViewSubtitle.setText(R.string.error_message_no_internet_subtitle);
        }

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        SuggestionsViewModel suggestionsViewModel = ViewModelProviders
                .of(this, viewModelFactory).get(SuggestionsViewModel.class);

        suggestionsViewModel.insertAllAlbums();

        suggestionsViewModel.getChartTopArtists().observe(this,
                artistSummaries -> {
                    if (artistSummaries != null) {
                        setChartListData(artistSummaries);
                    }
                });

        suggestionsViewModel.getSimilarArtists(firebaseUser.getUid()).observe(this,
                artistSummaries -> {
                    if (artistSummaries != null) {
                        similarArtistListLayout.setVisibility(View.VISIBLE);
                        setSimilarArtistListData(artistSummaries);
                    }
                });

        suggestionsViewModel.getUserInterestTagTopArtists(firebaseUser.getUid()).observe(this,
                artistSummaries -> {
                    if (artistSummaries != null) {
                        tagTopArtistLinearLayout.setVisibility(View.VISIBLE);
                        setTagTopArtistListData(artistSummaries);
                    }
                });

        suggestionsViewModel.getUserInterestArtistTopAlbums(firebaseUser.getUid()).observe(this,
                albumSummaries -> {
                    if (albumSummaries != null) {
                        similarArtistTopAlbumsLinearLayout.setVisibility(View.VISIBLE);
                        setSimilarArtistTopAlbumsData(albumSummaries);
                    }
                });
    }

    @Override
    public void onArtistSummaryClick(String artistName) {
        Intent intent = new Intent(getActivity(), ArtistActivity.class);
        intent.putExtra(ARTIST_NAME, artistName);
        startActivity(intent);
    }

    @Override
    public void onAlbumSummaryClick(String artistName, String albumName) {
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra(ARTIST_NAME, artistName);
        intent.putExtra(ALBUM_NAME, albumName);
        startActivity(intent);
    }

    public void setChartListData(List<ArtistSummary> chartListData) {
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        chartRecyclerView.setLayoutManager(horizontalLayoutManager);
        chartRecyclerView.setHasFixedSize(true);
        ArtistSummaryHorizontalAdapter artistSummaryHorizontalAdapter =
                new ArtistSummaryHorizontalAdapter(this);
        chartRecyclerView.setAdapter(artistSummaryHorizontalAdapter);
        artistSummaryHorizontalAdapter.setArtistSummaryList(chartListData);
    }

    public void setSimilarArtistListData(List<ArtistSummary> similarArtistListData) {
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        similarArtistRecyclerView.setLayoutManager(horizontalLayoutManager);
        similarArtistRecyclerView.setHasFixedSize(true);
        ArtistSummaryHorizontalAdapter artistSummaryHorizontalAdapter =
                new ArtistSummaryHorizontalAdapter(this);
        similarArtistRecyclerView.setAdapter(artistSummaryHorizontalAdapter);
        artistSummaryHorizontalAdapter.setArtistSummaryList(similarArtistListData);
    }

    public void setTagTopArtistListData(List<ArtistSummary> tagTopArtistListData) {
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tagTopArtistRecyclerView.setLayoutManager(horizontalLayoutManager);
        tagTopArtistRecyclerView.setHasFixedSize(true);
        ArtistSummaryHorizontalAdapter artistSummaryHorizontalAdapter =
                new ArtistSummaryHorizontalAdapter(this);
        tagTopArtistRecyclerView.setAdapter(artistSummaryHorizontalAdapter);
        artistSummaryHorizontalAdapter.setArtistSummaryList(tagTopArtistListData);

    }

    public void setSimilarArtistTopAlbumsData(List<AlbumSummary> similarArtistTopAlbumsData) {
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        similarArtistTopAlbumsRecyclerView.setLayoutManager(horizontalLayoutManager);
        similarArtistTopAlbumsRecyclerView.setHasFixedSize(true);
        AlbumSummaryHorizontalAdapter albumSummaryHorizontalAdapter =
                new AlbumSummaryHorizontalAdapter(this);
        similarArtistTopAlbumsRecyclerView.setAdapter(albumSummaryHorizontalAdapter);
        albumSummaryHorizontalAdapter.setAlbumSummaryList(similarArtistTopAlbumsData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
