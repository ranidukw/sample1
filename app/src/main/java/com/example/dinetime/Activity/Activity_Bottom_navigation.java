package com.example.dinetime.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.dinetime.Fragments.CartFragment;
import com.example.dinetime.Fragments.HistoryFragment;
import com.example.dinetime.Fragments.HomeFragment;
import com.example.dinetime.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_Bottom_navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomMainNAV;
    public Fragment selectedfragment = null;
    FragmentManager fragmentManager;

    String TAG_HOME = "HOME";
    String TAG_CART = "CART";
    String TAG_HISTORY = "HISOTRY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        setupUI();
        setupListners();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();
    }

    private void setupUI(){

        fragmentContainer = findViewById(R.id.fragmentContainer);
        bottomMainNAV = findViewById(R.id.bottomMainNAV);
    }

    private void setupListners(){
        bottomMainNAV.setOnNavigationItemSelectedListener(this);
    }

    public void openFragment(Fragment fragment,final String tag){
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment,tag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                openFragment(new HomeFragment(),TAG_HOME);
                return true;

            case R.id.menu_cart:
                openFragment(new CartFragment(),TAG_CART);
                return true;

            case R.id.menu_history:
                openFragment(new HistoryFragment(),TAG_HISTORY);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}