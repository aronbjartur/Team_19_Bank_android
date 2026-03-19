package com.example.team19bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // P2 LAUSN: Keyrum þetta BARA ef appið er að opnast í fyrsta skipti
        if (savedInstanceState == null) {
            setupNavigation();
        }
    }

    private void setupNavigation() {
        SharedPreferences sharedPref = getSharedPreferences("BankAppPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            replaceFragment(new HomeFragment(), false);
        } else {
            replaceFragment(new LoginFragment(), false);
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}