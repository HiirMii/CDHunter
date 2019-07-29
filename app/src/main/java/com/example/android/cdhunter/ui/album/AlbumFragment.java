package com.example.android.cdhunter.ui.album;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.android.cdhunter.utils.Constants;
import com.example.android.cdhunter.utils.NetworkConnection;
import com.example.android.cdhunter.viewmodel.AlbumViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private String currentArtistName;
    private String currentAlbumName;
    private String currentOwnershipStatus;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.album_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.fab_wishlist_view)
    LinearLayout fabWishlistView;
    @BindView(R.id.tv_fab_wishlist)
    TextView fabWishlistTextView;
    @BindView(R.id.fab_wishlist)
    FloatingActionButton fabWishlist;
    @BindView(R.id.fab_collection_view)
    LinearLayout fabCollectionView;
    @BindView(R.id.tv_fab_collection)
    TextView fabCollectionTextView;
    @BindView(R.id.fab_collection)
    FloatingActionButton fabCollection;
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

    private AlbumViewModel albumViewModel;

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

        albumViewModel = ViewModelProviders
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

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        fabAdd.setOnClickListener(v -> animateFAB());

        fabWishlist.setOnClickListener(v -> {
            switch (currentOwnershipStatus) {
                case "":
                    updateOwnershipStatusToWishlist();
                    break;
                case "collection":
                    updateOwnershipStatusToWishlist();
                    break;
                case "wishlist":
                    updateOwnershipStatusToEmptyString();
                    break;
            }
            Objects.requireNonNull(getActivity()).finish();
        });

        fabCollection.setOnClickListener(v -> {
            switch (currentOwnershipStatus) {
                case "":
                    updateOwnershipStatusToCollection();
                    break;
                case "collection":
                    updateOwnershipStatusToEmptyString();
                    break;
                case "wishlist":
                    updateOwnershipStatusToCollection();
                    break;
            }
            Objects.requireNonNull(getActivity()).finish();
        });
    }

    public void animateFAB() {

        setFabTextViews(currentOwnershipStatus);

        if (isFabOpen) {
            fabAdd.startAnimation(rotate_backward);
            fabWishlistView.startAnimation(fab_close);
            fabCollectionView.startAnimation(fab_close);
            fabWishlist.setClickable(false);
            fabCollection.setClickable(false);
            isFabOpen = false;
        } else {
            fabAdd.startAnimation(rotate_forward);
            fabWishlistView.startAnimation(fab_open);
            fabCollectionView.startAnimation(fab_open);
            fabWishlist.setClickable(true);
            fabCollection.setClickable(true);
            isFabOpen = true;
        }
    }

    public void updateOwnershipStatusToCollection() {
        albumViewModel.updateAlbumsOwnershipStatus(firebaseUser.getUid(), currentArtistName,
                currentAlbumName, Constants.COLLECTION);
    }

    public void updateOwnershipStatusToWishlist() {
        albumViewModel.updateAlbumsOwnershipStatus(firebaseUser.getUid(), currentArtistName,
                currentAlbumName, Constants.WISHLIST);
    }

    public void updateOwnershipStatusToEmptyString() {
        albumViewModel.updateAlbumsOwnershipStatus(firebaseUser.getUid(), currentArtistName,
                currentAlbumName, "");
    }

    public void setTrackListData(Album album) {

        currentArtistName = album.getArtistName();
        currentAlbumName = album.getAlbumName();
        currentOwnershipStatus = album.getOwnershipStatus();

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

    public void setFabTextViews(String ownershipStatus) {
        switch (ownershipStatus) {
            case "":
                fabCollectionTextView.setText(R.string.fab_collection_add);
                fabWishlistTextView.setText(R.string.fab_wishlist_add);
                break;
            case "collection":
                fabCollectionTextView.setText(R.string.fab_collection_remove);
                fabWishlistTextView.setText(R.string.fab_wishlist_add);
                break;
            case "wishlist":
                fabCollectionTextView.setText(R.string.fab_collection_add);
                fabWishlistTextView.setText(R.string.fab_wishlist_remove);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
