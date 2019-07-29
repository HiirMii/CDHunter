package com.example.android.cdhunter.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.common.Tag;
import com.example.android.cdhunter.ui.searchresults.SearchResultsActivity;
import com.example.android.cdhunter.utils.NetworkConnection;
import com.example.android.cdhunter.viewmodel.SearchViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment implements Injectable,
        TagAdapter.TagAdapterOnClickHandler {

    private static final String ARTIST_NAME = "artistName";
    private static final String TAG = "tag";
    private static final String SEARCH_INPUT = "searchInput";
    private static final String SEARCH_TYPE = "searchType";


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.et_search)
    EditText searchEditText;
    @BindView(R.id.iv_search_icon)
    ImageView searchIcon;
    @BindView(R.id.tv_search_label)
    TextView searchLabel;
    @BindView(R.id.search_main_view)
    NestedScrollView searchMainView;
    @BindView(R.id.search_error_view)
    View errorView;
    @BindView(R.id.iv_error_view_icon)
    ImageView errorViewIcon;
    @BindView(R.id.tv_error_view_title)
    TextView errorViewTitle;
    @BindView(R.id.tv_error_view_subtitle)
    TextView errorViewSubtitle;
    @BindView(R.id.rv_tag_list)
    RecyclerView tagListRecyclerView;

    private Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!NetworkConnection.isConnected(Objects.requireNonNull(getContext()))) {
            searchEditText.setVisibility(View.GONE);
            searchIcon.setVisibility(View.GONE);
            searchLabel.setVisibility(View.GONE);
            searchMainView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            errorViewIcon.setImageResource(R.drawable.ic_no_wifi);
            errorViewTitle.setText(R.string.error_message_no_internet_title);
            errorViewSubtitle.setText(R.string.error_message_no_internet_subtitle);
        }

        SearchViewModel searchViewModel = ViewModelProviders
                .of(this, viewModelFactory).get(SearchViewModel.class);

        searchViewModel.getTopTags().observe(this, tags -> {
            if (tags != null) {
                setTagListData(tags);
            }
        });

        searchIcon.setOnClickListener(v -> {
            String inputText = searchEditText.getText().toString().trim();
            performSearch(inputText);
        });

        searchEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        String inputText = searchEditText.getText().toString().trim();
                        performSearch(inputText);
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
    }

    @Override
    public void onTagClick(String tag) {
        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
        intent.putExtra(SEARCH_INPUT, tag);
        intent.putExtra(SEARCH_TYPE, TAG);
        startActivity(intent);
    }

    public void setTagListData(List<Tag> tagListData) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        tagListRecyclerView.setLayoutManager(gridLayoutManager);
        tagListRecyclerView.setHasFixedSize(true);
        TagAdapter tagAdapter = new TagAdapter(this);
        tagListRecyclerView.setAdapter(tagAdapter);
        tagAdapter.setTagList(tagListData);
    }

    public void performSearch(String inputText) {
        if (TextUtils.isEmpty(inputText)) {
            Toast.makeText(getContext(), getString(R.string.empty_search_toast_message), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
            intent.putExtra(SEARCH_INPUT, inputText);
            intent.putExtra(SEARCH_TYPE, ARTIST_NAME);
            startActivity(intent);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
