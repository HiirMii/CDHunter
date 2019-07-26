package com.example.android.cdhunter.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.viewmodel.ArtistViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements Injectable {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.tv_artist_name)
    TextView artistName;
    @BindView(R.id.tv_album_name)
    TextView albumName;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        ArtistViewModel artistViewModel = ViewModelProviders.of(
                this,viewModelFactory).get(ArtistViewModel.class);
        artistViewModel.getArtist("Dream Theater", firebaseUser.getUid()).observe(
                this, artist -> {
                    if (artist != null) {
                        artistName.setText(artist.getName());
                    }
                }
        );
        artistViewModel.getArtistTopAlbums("Dream Theater").observe(
                this, albumSummaries -> {
                    if (albumSummaries != null) {
                        albumName.setText(albumSummaries.get(3).getName());
                    }
                });
    }
}
