package com.example.android.cdhunter.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.ui.auth.AuthActivity;
import com.example.android.cdhunter.utils.Constants;
import com.example.android.cdhunter.viewmodel.AlbumViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment implements Injectable {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient googleSignInClient;

    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_profile_user_name)
    TextView userName;
    @BindView(R.id.tv_profile_collection_album_amount)
    TextView collectionAlbumAmount;
    @BindView(R.id.tv_profile_wishlist_album_amount)
    TextView wishlistAlbumAmount;
    @BindView(R.id.profile_log_out_btn)
    MaterialButton logOutButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getContext()), gso);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        userName.setText(firebaseUser.getDisplayName());

        AlbumViewModel albumViewModel = ViewModelProviders.of(this,
                viewModelFactory).get(AlbumViewModel.class);

        albumViewModel.getAlbumList(firebaseUser.getUid(), Constants.COLLECTION).observe(
                this, albumList -> {
                    if (albumList != null) {
                        collectionAlbumAmount.setText(String.valueOf(albumList.size()));
                    } else {
                        collectionAlbumAmount.setText("0");
                    }
                }
        );

        albumViewModel.getAlbumList(firebaseUser.getUid(), Constants.WISHLIST).observe(
                this, albumList -> {
                    if (albumList != null) {
                        wishlistAlbumAmount.setText(String.valueOf(albumList.size()));
                    } else {
                        wishlistAlbumAmount.setText("0");
                    }
                }
        );

        logOutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            googleSignInClient.signOut().addOnCompleteListener(Objects.requireNonNull(getActivity()),
                    task -> {
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        startActivity(intent);
            });
        });
    }
}
