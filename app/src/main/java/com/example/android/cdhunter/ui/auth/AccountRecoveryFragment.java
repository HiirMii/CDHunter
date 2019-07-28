package com.example.android.cdhunter.ui.auth;


import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.android.cdhunter.ui.common.BaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class AccountRecoveryFragment extends Fragment implements Injectable {

    private static final String TAG = "AccountRecoveryFragment";

    private FirebaseAuth firebaseAuth;

    @BindView(R.id.auth_account_recovery_toolbar)
    Toolbar toolbar;
    @BindView(R.id.auth_account_recovery_email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.auth_account_recovery_email_input)
    TextInputEditText emailInput;
    @BindView(R.id.auth_account_recovery_submit_btn)
    MaterialButton recoveryBtn;

    private Unbinder unbinder;

    public AccountRecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account_recovery, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).onBackPressed());

        firebaseAuth = FirebaseAuth.getInstance();

        recoveryBtn.setOnClickListener(v -> accountRecovery(
                Objects.requireNonNull(emailInput.getText()).toString()));
    }

    private void accountRecovery(String email) {
        if (!validateForm()) {
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), getString(R.string.auth_account_recovery_user_msg),
                        Toast.LENGTH_SHORT).show();
                ((BaseActivity) Objects.requireNonNull(getActivity())).loadFragmentWithBackStackClear
                        (getFragmentManager(), new AuthMethodPickerFragment(), R.id.auth_fragment_container);
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                }
                catch (FirebaseAuthInvalidUserException invalidUser) {
                    Timber.tag(TAG).d("Invalid user.");
                    emailLayout.setError(getString(R.string.auth_account_recovery_error_msg));
                }
                catch (Exception e) {
                    Timber.tag(TAG).d(task.getException(), "accountRecovery:failure");
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

        return valid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
