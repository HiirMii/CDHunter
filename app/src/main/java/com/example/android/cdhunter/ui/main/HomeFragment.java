package com.example.android.cdhunter.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.tag.TagResponse;
import com.example.android.cdhunter.ui.auth.AuthActivity;
import com.example.android.cdhunter.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.cdhunter.ui.main.MainActivity.RC_SIGN_IN;

public class HomeFragment extends Fragment implements Injectable {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient googleSignInClient;

    @BindView(R.id.tv_home)
    TextView homeTextView;
    @BindView(R.id.tv_artist_name)
    TextView artistName;
    @BindView(R.id.auth_sign_out_btn)
    Button signOutButton;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getContext()), gso);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        homeTextView.setText(String.format("Logged in as %s", firebaseUser.getDisplayName()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        LastFmService lastFmService = retrofit.create(LastFmService.class);

        Call<TagResponse> call = lastFmService.getTagTopArtists("Nu Metal", Constants.API_KEY);

        call.enqueue(new Callback<TagResponse>() {
            @Override
            public void onResponse(@NotNull Call<TagResponse> call, @NotNull Response<TagResponse> response) {
                TagResponse tagResponse = response.body();
                assert tagResponse != null;
                artistName.setText(tagResponse.getArtistListObject().getArtistList().get(0).getArtistName());

            }

            @Override
            public void onFailure(@NotNull Call<TagResponse> call, @NotNull Throwable t) {
                Toast.makeText(getActivity(), "error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        signOutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            googleSignInClient.signOut().addOnCompleteListener(Objects.requireNonNull(getActivity()),
                    task -> {
                        Intent authIntent = new Intent(getActivity(), AuthActivity.class);
                        startActivityForResult(authIntent, RC_SIGN_IN);
                    });
        });
    }
}
