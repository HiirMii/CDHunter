package com.example.android.cdhunter.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.ui.common.BaseActivity;
import com.example.android.cdhunter.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AuthMethodPickerFragment extends Fragment implements Injectable {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "AuthMethodPicker";
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @BindView(R.id.sign_up_btn)
    MaterialButton signUpButton;

    @BindView(R.id.google_btn)
    MaterialButton googleLoginButton;

    @BindView(R.id.login_btn)
    MaterialButton loginButton;

    public AuthMethodPickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth_method_picker, container, false);
        ButterKnife.bind(this, view);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getContext()), gso);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        signUpButton.setOnClickListener(v -> ((BaseActivity) Objects.requireNonNull(
                getActivity())).loadFragmentWithBackStack(getFragmentManager(),
                new SignUpFragment(), R.id.auth_fragment_container));

        googleLoginButton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

        });

        loginButton.setOnClickListener(v -> ((BaseActivity) Objects.requireNonNull(
                getActivity())).loadFragmentWithBackStack(getFragmentManager(),
                new LogInFragment(), R.id.auth_fragment_container));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Timber.tag(TAG).w(e, "Google sign in failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Timber.d("firebaseAuthWithGoogle:%s", acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCredential:success");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.tag(TAG).w(task.getException(), "signInWithCredential:failure");
                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
