package com.example.android.cdhunter.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.db.CdHunterDb;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.album.Album;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements Injectable {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private CdHunterDb db;

    private Album album;

    @BindView(R.id.tv_artist_name)
    TextView artistName;
    @BindView(R.id.tv_album_name)
    TextView albumName;


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

        db = CdHunterDb.getInstance(getContext());
        album = db.albumDao().getSingleAlbum(firebaseUser.getUid(), "e5544c68-43e9-4754-9239-b618454557f4");

        if (album != null) {
            artistName.setText(album.getArtistName());
            albumName.setText(album.getAlbumName());
        }
    }
}
