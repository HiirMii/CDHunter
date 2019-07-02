package com.example.android.cdhunter.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;

import org.jetbrains.annotations.NotNull;

public class WishlistFragment extends Fragment implements Injectable {


    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }
}
