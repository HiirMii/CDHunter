package com.example.android.cdhunter.ui.auth;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.ui.main.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class SignUpFragment extends Fragment implements Injectable {

    private static final int PASSWORD_LENGTH = 8;
    private static final String TAG = "SignUpFragment";

    private FirebaseAuth firebaseAuth;

    @BindView(R.id.auth_sign_up_toolbar)
    Toolbar toolbar;
    @BindView(R.id.auth_sign_up_submit_btn)
    MaterialButton signUpBtn;
    @BindView(R.id.auth_sign_up_username_layout)
    TextInputLayout usernameLayout;
    @BindView(R.id.auth_sign_up_username_input)
    TextInputEditText usernameInput;
    @BindView(R.id.auth_sign_up_email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.auth_sign_up_email_input)
    TextInputEditText emailInput;
    @BindView(R.id.auth_sign_up_password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.auth_sign_up_password_input)
    TextInputEditText passwordInput;

    private Unbinder unbinder;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        passwordInput.setTypeface(Typeface.DEFAULT);
        passwordInput.setTransformationMethod( new PasswordTransformationMethod());

        firebaseAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(v -> signUp(Objects.requireNonNull(usernameInput.getText()).toString(),
                Objects.requireNonNull(emailInput.getText()).toString(),
                Objects.requireNonNull(passwordInput.getText()).toString()));
    }

    private void signUp(String username, String email, String password) {
        Timber.d("Create account for: " + username + " with " + email);
        if (!validateForm()) {
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                    if (task.isSuccessful()) {
                        Timber.tag(TAG).d("createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        assert user != null;
                        addUsernameToUser(user, username);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        }
                        catch (FirebaseAuthUserCollisionException userCollision) {
                            Timber.tag(TAG).d("User already exists.");
                            emailLayout.setError(getString(R.string.auth_sign_up_email_exists_error_msg));
                        }
                        catch (Exception e) {
                            Timber.tag(TAG).d(task.getException(), "createUserWithEmail:failure");
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUsernameToUser(FirebaseUser firebaseUser, String username) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Timber.tag(TAG).d("User profile updated.");
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        // username

        String username = Objects.requireNonNull(usernameInput.getText()).toString();
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError(getString(R.string.auth_sign_up_username_empty_error_msg));
            valid = false;
        } else {
            usernameLayout.setError(null);
        }

        // email

        String email = Objects.requireNonNull(emailInput.getText()).toString();
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.auth_sign_up_email_empty_error_msg));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.auth_sign_up_email_valid_error_msg));
            valid = false;
        } else {
            emailLayout.setError(null);
        }

        // password

        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.auth_sign_up_password_empty_error_msg));
            valid = false;
        } else if (password.length() < PASSWORD_LENGTH) {
            passwordLayout.setError(getString(R.string.auth_sign_up_password_length_error_msg));
            valid = false;
        } else {
            passwordLayout.setError(null);
        }

        return valid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
