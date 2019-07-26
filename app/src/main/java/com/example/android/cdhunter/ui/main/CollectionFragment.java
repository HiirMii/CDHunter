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
import com.example.android.cdhunter.viewmodel.AlbumViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionFragment extends Fragment implements Injectable {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.tv_album_summary)
    TextView albumSummary;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public CollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        AlbumViewModel albumViewModel = ViewModelProviders.of(
                this, viewModelFactory).get(AlbumViewModel.class);
        albumViewModel.getAlbum("Dream Theater", "Images And Words",
                firebaseUser.getUid(), "4b5a4d0e-1268-4ed5-8b48-6d0740053813")
                .observe(this, album -> {
                    if (album != null) {
                        albumSummary.setText(album.getAlbumSummary());
                    }
                });
    }
}
