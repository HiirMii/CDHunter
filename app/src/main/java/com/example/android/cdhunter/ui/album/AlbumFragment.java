package com.example.android.cdhunter.ui.album;


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
import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.utils.NetworkConnection;
import com.example.android.cdhunter.viewmodel.AlbumViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AlbumFragment extends Fragment implements Injectable {

    private static final String ARTIST_NAME = "artistName";
    private static final String ALBUM_NAME = "albumName";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.album_toolbar)
    Toolbar toolbar;
    @BindView(R.id.album_main_view)
    NestedScrollView artistMainView;
    @BindView(R.id.album_error_view)
    View errorView;
    @BindView(R.id.iv_error_view_icon)
    ImageView errorViewIcon;
    @BindView(R.id.tv_error_view_title)
    TextView errorViewTitle;
    @BindView(R.id.tv_error_view_subtitle)
    TextView errorViewSubtitle;
    @BindView(R.id.iv_album_image)
    ImageView albumImage;
    @BindView(R.id.tv_album_name)
    TextView albumName;
    @BindView(R.id.tv_artist_name)
    TextView artistName;
    @BindView(R.id.tv_album_summary)
    TextView albumSummary;
    @BindView(R.id.tv_track_list_label)
    TextView trackListLabel;
    @BindView(R.id.rv_track_list)
    RecyclerView trackListRecyclerView;

    private Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
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

        String albumNameFromIntent = Objects.requireNonNull(
                Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString(ALBUM_NAME);

        toolbar.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        if (!NetworkConnection.isConnected(Objects.requireNonNull(getContext()))) {
            artistMainView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            errorViewIcon.setImageResource(R.drawable.ic_no_wifi);
            errorViewTitle.setText(R.string.error_message_no_internet_album_title);
            errorViewSubtitle.setText(R.string.error_message_no_internet_subtitle);
        }

        AlbumViewModel albumViewModel = ViewModelProviders
                .of(this, viewModelFactory).get(AlbumViewModel.class);

        albumViewModel.getAlbum(firebaseUser.getUid(), artistNameFromIntent, albumNameFromIntent)
                .observe(this, album -> {
                    if (album != null) {
                        Glide.with(getContext())
                                .load(album.getListOfImages().get(4).getImageUrl())
                                .error(R.drawable.ic_no_wifi)
                                .placeholder(R.drawable.ic_logo)
                                .into(albumImage);
                        albumName.setText(album.getAlbumName());
                        artistName.setText(album.getArtistName());
                        albumSummary.setText(album.getAlbumSummary());
                        setTrackListData(album);
                    }
        });
    }

    public void setTrackListData(Album album) {
        if (!album.getListOfTracks().isEmpty()) {
            trackListLabel.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager trackListLayoutManager = new LinearLayoutManager(getActivity());
        trackListRecyclerView.setLayoutManager(trackListLayoutManager);
        trackListRecyclerView.setHasFixedSize(true);
        TrackAdapter trackAdapter = new TrackAdapter();
        trackListRecyclerView.setAdapter(trackAdapter);
        trackAdapter.setTrackList(album.getListOfTracks());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
