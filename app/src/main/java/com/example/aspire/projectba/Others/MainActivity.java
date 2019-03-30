package com.example.aspire.projectba.Others;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.aspire.projectba.Fragments.FragmentDefinicoes;
import com.example.aspire.projectba.Fragments.FragmentPrincipal;
import com.example.aspire.projectba.Fragments.FragmentZonas;
import com.example.aspire.projectba.R;

public class MainActivity extends AppCompatActivity {


    int MY_PERMISSIONS_REQUEST_REQUEST_SMS = 1;
    private Button armar, desarmar;
    private TextView textView;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_saloes:
                            selectedFragment = new FragmentPrincipal();
                            break;
                        case R.id.nav_marcacoes:
                            selectedFragment = new FragmentZonas();
                            break;
                        case R.id.nav_definicoes:
                            selectedFragment = new FragmentDefinicoes();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentPrincipal()).commit();
        }
    }

}
