package com.maratodev.civeforum;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView$OnNavigationItemSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String myuid;
    BottomNavigationView navigationView;
    private BottomNavigationView$OnNavigationItemSelectedListener selectedListener;
    
    public MainActivity() {
        this.selectedListener = (BottomNavigationView$OnNavigationItemSelectedListener)new BottomNavigationView$OnNavigationItemSelectedListener() {
            final MainActivity this$0;
            
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_home) {
                    this.this$0.ReplaceFragments(new HomeFragment());
                }
                if (menuItem.getItemId() == R.id.nav_profile) {
                    this.this$0.ReplaceFragments(new ProfileFragment());
                }
                if (menuItem.getItemId() == R.id.nav_ask) {
                    this.this$0.ReplaceFragments(new AddTopicFragment());
                }
                return false;
            }
        };
    }
    
    private void ReplaceFragments(final Fragment fragment) {
        final FragmentTransaction beginTransaction = this.getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.content, fragment);
        beginTransaction.commit();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_main);
        this.firebaseAuth = FirebaseAuth.getInstance();
        (this.navigationView = (BottomNavigationView)this.findViewById(R.id.navigation)).setOnNavigationItemSelectedListener(this.selectedListener);
        final HomeFragment homeFragment = new HomeFragment();
        final FragmentTransaction beginTransaction = this.getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.content, (Fragment)homeFragment);
        beginTransaction.commit();
    }
}
