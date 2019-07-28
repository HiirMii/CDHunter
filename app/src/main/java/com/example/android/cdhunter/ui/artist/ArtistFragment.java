package com.example.android.cdhunter.ui.artist;


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

import com.bumptech.glide.Glide;
import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.topalbums.AlbumSummary;
import com.example.android.cdhunter.ui.album.AlbumActivity;
import com.example.android.cdhunter.utils.NetworkConnection;
import com.example.android.cdhunter.viewmodel.ArtistViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ArtistFragment extends Fragment implements Injectable,
        AlbumSummaryVerticalAdapter.AlbumSummaryOnClickHandler {

    private static final String ARTIST_NAME = "artistName";
    private static final String ALBUM_NAME = "albumName";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.artist_toolbar)
    Toolbar toolbar;
    @BindView(R.id.artist_main_view)
    NestedScrollView artistMainView;
    @BindView(R.id.artist_error_view)
    View errorView;
    @BindView(R.id.iv_error_view_icon)
    ImageView errorViewIcon;
    @BindView(R.id.tv_error_view_title)
    TextView errorViewTitle;
    @BindView(R.id.tv_error_view_subtitle)
    TextView errorViewSubtitle;
    @BindView(R.id.iv_artist_image)
    ImageView artistImage;
    @BindView(R.id.tv_artist_name)
    TextView artistName;
    @BindView(R.id.tv_artist_summary)
    TextView artistSummary;
    @BindView(R.id.rv_artist_top_album_list)
    RecyclerView artistTopAlbumRecyclerView;

    private Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String artistNameFromIntent = Objects.requireNonNull(
                Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString(ARTIST_NAME);

        toolbar.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        if (!NetworkConnection.isConnected(Objects.requireNonNull(getContext()))) {
            artistMainView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            errorViewIcon.setImageResource(R.drawable.ic_no_wifi);
            errorViewTitle.setText(R.string.error_message_no_internet_artist_title);
            errorViewSubtitle.setText(R.string.error_message_no_internet_subtitle);
        }

        ArtistViewModel artistViewModel = ViewModelProviders
                .of(this, viewModelFactory).get(ArtistViewModel.class);

        artistViewModel.getArtist(artistNameFromIntent, firebaseUser.getUid()).observe(this,
                artist -> {
                    if (artist != null) {
                        Glide.with(getContext())
                                .load(artist.getImageList().get(4).getImageUrl())
                                .error(R.drawable.ic_no_wifi)
                                .placeholder(R.drawable.ic_logo)
                                .into(artistImage);
                        artistName.setText(artist.getName());
                        artistSummary.setText(artist.getBio().getSummary());
                    }
                });

        artistViewModel.getArtistTopAlbums(artistNameFromIntent).observe(this,
                albumSummaries -> {
                    if (albumSummaries != null) {
                        setTopAlbumData(albumSummaries);
                    }
                });
    }

    @Override
    public void onAlbumSummaryClick(String artistName, String albumName) {
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra(ARTIST_NAME, artistName);
        intent.putExtra(ALBUM_NAME, albumName);
        startActivity(intent);
    }

    public void setTopAlbumData(List<AlbumSummary> albumData) {
        LinearLayoutManager topAlbumsLayoutManager = new LinearLayoutManager(getActivity());
        artistTopAlbumRecyclerView.setLayoutManager(topAlbumsLayoutManager);
        artistTopAlbumRecyclerView.setHasFixedSize(true);
        AlbumSummaryVerticalAdapter albumSummaryVerticalAdapter = new AlbumSummaryVerticalAdapter(this);
        artistTopAlbumRecyclerView.setAdapter(albumSummaryVerticalAdapter);
        albumSummaryVerticalAdapter.setAlbumSummaryList(albumData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
