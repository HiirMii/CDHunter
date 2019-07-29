package com.example.android.cdhunter.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.ui.album.AlbumActivity;
import com.example.android.cdhunter.utils.Constants;
import com.example.android.cdhunter.viewmodel.AlbumViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WishlistFragment extends Fragment implements Injectable,
        AlbumAdapter.AlbumAdapterOnClickHandler {

    private static final String ARTIST_NAME = "artistName";
    private static final String ALBUM_NAME = "albumName";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.wishlist_error_view)
    View errorView;
    @BindView(R.id.iv_error_view_icon)
    ImageView errorViewIcon;
    @BindView(R.id.tv_error_view_title)
    TextView errorViewTitle;
    @BindView(R.id.tv_error_view_subtitle)
    TextView errorViewSubtitle;
    @BindView(R.id.tv_wishlist_label)
    TextView wishlistLabel;
    @BindView(R.id.rv_wishlist)
    RecyclerView wishlistListRecyclerView;

    private Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
         unbinder = ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AlbumViewModel albumViewModel = ViewModelProviders.of(this,
                viewModelFactory).get(AlbumViewModel.class);
        albumViewModel.getAlbumList(firebaseUser.getUid(), Constants.WISHLIST).observe(
                this, albumList -> {
                    if(albumList != null && !albumList.isEmpty()) {
                        wishlistLabel.setVisibility(View.VISIBLE);
                        wishlistListRecyclerView.setVisibility(View.VISIBLE);
                        setAlbumData(albumList);
                    } else {
                        wishlistLabel.setVisibility(View.INVISIBLE);
                        wishlistListRecyclerView.setVisibility(View.INVISIBLE);
                        errorView.setVisibility(View.VISIBLE);
                        errorViewIcon.setImageResource(R.drawable.ic_list_empty);
                        errorViewTitle.setText(R.string.error_message_empty_wishlist_title);
                        errorViewSubtitle.setText(R.string.error_message_empty_list_subtitle);
                    }
                }
        );
    }

    @Override
    public void onAlbumClick(String artistName, String albumName) {
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra(ARTIST_NAME, artistName);
        intent.putExtra(ALBUM_NAME, albumName);
        startActivity(intent);
    }

    public void setAlbumData(List<Album> albumData) {
        LinearLayoutManager albumListLayoutManager = new LinearLayoutManager(getActivity());
        wishlistListRecyclerView.setLayoutManager(albumListLayoutManager);
        wishlistListRecyclerView.setHasFixedSize(true);
        AlbumAdapter albumAdapter = new AlbumAdapter(this);
        wishlistListRecyclerView.setAdapter(albumAdapter);
        albumAdapter.setAlbumList(albumData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
