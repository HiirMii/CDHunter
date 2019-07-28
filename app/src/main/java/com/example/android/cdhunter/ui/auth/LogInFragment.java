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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.di.Injectable;
import com.example.android.cdhunter.ui.common.BaseActivity;
import com.example.android.cdhunter.ui.main.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class LogInFragment extends Fragment implements Injectable {


    private static final int PASSWORD_LENGTH = 8;
    private static final String TAG = "LogInFragment";

    private FirebaseAuth firebaseAuth;

    @BindView(R.id.auth_log_in_toolbar)
    Toolbar toolbar;
    @BindView(R.id.auth_log_in_submit_btn)
    MaterialButton loginSubmitBtn;
    @BindView(R.id.auth_log_in_account_recovery_btn)
    MaterialButton accountRecoveryBtn;
    @BindView(R.id.auth_log_in_email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.auth_log_in_email_input)
    TextInputEditText emailInput;
    @BindView(R.id.auth_log_in_password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.auth_log_in_password_input)
    TextInputEditText passwordInput;

    private Unbinder unbinder;


    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
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

        loginSubmitBtn.setOnClickListener(v -> logIn(Objects.requireNonNull(emailInput.getText()).toString(),
                Objects.requireNonNull(passwordInput.getText()).toString()));

        accountRecoveryBtn.setOnClickListener(v -> ((BaseActivity) Objects.requireNonNull(
                getActivity())).loadFragmentWithBackStack(getFragmentManager(),
                new AccountRecoveryFragment(), R.id.auth_fragment_container));
    }

    private void logIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Timber.tag(TAG).d("logInWithEmail:success");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).finish();
                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        }
                        catch (FirebaseAuthInvalidUserException invalidUser) {
                            Timber.tag(TAG).d("Invalid user");
                            emailLayout.setError(getString(R.string.auth_log_in_email_invalid_error_msg));
                        }
                        catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                            Timber.tag(TAG).d("Wrong password");
                            passwordLayout.setError(getString(R.string.auth_log_in_wrong_password_error_msg));
                        }
                        catch (Exception e) {
                            Timber.tag(TAG).d(task.getException(), "logInWithEmail:failure");
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

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
