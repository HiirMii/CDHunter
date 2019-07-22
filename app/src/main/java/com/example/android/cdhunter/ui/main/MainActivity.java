package com.example.android.cdhunter.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.ui.auth.AuthActivity;
import com.example.android.cdhunter.ui.common.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector,
        BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    // sign in request code
    public static final int RC_SIGN_IN = 1;

    // firebase instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    Fragment currentFragment;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentFragment = new HomeFragment();

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // user is signed in
                ButterKnife.bind(this);
                loadFragment(getSupportFragmentManager(), currentFragment, R.id.main_fragment_container);
                navigationView.setOnNavigationItemSelectedListener(this);
                Timber.tag(TAG).d("User successfully logged in.");
            } else {
                // user is signed out
                Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                startActivityForResult(authIntent, RC_SIGN_IN);
            }
        };
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_search:
                fragment = new SearchFragment();
                break;
            case R.id.nav_collection:
                fragment = new CollectionFragment();
                break;
            case R.id.nav_wishlist:
                fragment = new WishlistFragment();
                break;
        }

        currentFragment = fragment;
        loadFragment(getSupportFragmentManager(), fragment, R.id.main_fragment_container);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED){
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
