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
import com.example.android.cdhunter.db.CdHunterDb;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.model.album.AlbumResponse;
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

    private CdHunterDb db;

    private Album album;

    @BindView(R.id.tv_home)
    TextView homeTextView;
    @BindView(R.id.tv_artist_name)
    TextView artistName;
    @BindView(R.id.save_to_db_btn)
    Button saveToDbButton;
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

        Call<AlbumResponse> call = lastFmService.getAlbumInfo("Dream Theater",
                "Awake", Constants.API_KEY);

        call.enqueue(new Callback<AlbumResponse>() {
            @Override
            public void onResponse(@NotNull Call<AlbumResponse> call, @NotNull Response<AlbumResponse> response) {
                AlbumResponse albumResponse = response.body();
                assert albumResponse != null;

                album = new Album(firebaseUser.getUid(),
                        albumResponse.getAlbum().getAlbumName(),
                        albumResponse.getAlbum().getArtistName(),
                        albumResponse.getAlbum().getAlbumId(),
                        albumResponse.getAlbum().getListOfImages(),
                        albumResponse.getAlbum().getTrackList().getTrackList(),
                        albumResponse.getAlbum().getWiki().getSummary(),
                        getString(R.string.album_ownership_status_collection));

                artistName.setText(albumResponse.getAlbum().getAlbumName());

            }

            @Override
            public void onFailure(@NotNull Call<AlbumResponse> call, @NotNull Throwable t) {
                Toast.makeText(getActivity(), "error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        db = CdHunterDb.getInstance(getContext());

        saveToDbButton.setOnClickListener(v -> db.albumDao().insertSingleAlbum(album));

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
