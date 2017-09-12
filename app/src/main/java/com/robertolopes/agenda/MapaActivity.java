package com.robertolopes.agenda;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MapaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();
        tx.replace(R.id.frame_mapa, new MapaFragment());
        tx.commit();
    }
}
