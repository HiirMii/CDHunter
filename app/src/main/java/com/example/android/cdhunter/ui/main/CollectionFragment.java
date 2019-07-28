package com.example.android.cdhunter.ui.main;


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

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.utils.Constants;
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

    @BindView(R.id.collection_error_view)
    View errorView;
    @BindView(R.id.iv_error_view_icon)
    ImageView errorViewIcon;
    @BindView(R.id.tv_error_view_title)
    TextView errorViewTitle;
    @BindView(R.id.tv_error_view_subtitle)
    TextView errorViewSubtitle;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AlbumViewModel albumViewModel = ViewModelProviders.of(this,
                viewModelFactory).get(AlbumViewModel.class);
        albumViewModel.getAlbumList(firebaseUser.getUid(), Constants.COLLECTION).observe(
                this, albumList -> {
                    if(albumList != null && !albumList.isEmpty()) {

                    } else {
                        errorView.setVisibility(View.VISIBLE);
                        errorViewIcon.setImageResource(R.drawable.ic_list_empty);
                        errorViewTitle.setText(R.string.error_message_empty_collection_title);
                        errorViewSubtitle.setText(R.string.error_message_empty_list_subtitle);
                    }
                }
        );

    }
}
