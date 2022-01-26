package com.example.ejereuskalmet.Mapa;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.MasBalizas.MasBalizasRVAdapter;
import com.example.ejereuskalmet.MasBalizas.ViewModelMasBalizas;
import com.example.ejereuskalmet.MisBalizas.MisBalizasRVAdapter;
import com.example.ejereuskalmet.MisBalizas.ViewModelMisBalizas;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapaFragment extends SupportMapFragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    public MainActivity ma;
    public SectionsPagerAdapter sectionsPagerAdapter;
    public MisBalizasRVAdapter misBalizasRVAdapter = new MisBalizasRVAdapter();
    public ViewModelMisBalizas viewModelMisBalizas;
    public List<Balizas> balizas = new ArrayList<>();

    public MapaFragment() {
        // Required empty public constructor
    }

    public MapaFragment(MainActivity ma, SectionsPagerAdapter sectionsPagerAdapter) {
        this.ma = ma;
        this.sectionsPagerAdapter = sectionsPagerAdapter;
    }

    public static MapaFragment newInstance(String param1, String param2) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = super.onCreateView(inflater, container, savedInstanceState);
        getMapAsync(this);

        misBalizasRVAdapter = new MisBalizasRVAdapter(ma, sectionsPagerAdapter);

        /** OBSERVER PARA LAS BALIZAS AÑADIDAS **/

        viewModelMisBalizas = new ViewModelProvider(ma).get(ViewModelMisBalizas.class);

        final Observer<List<Balizas>> nameObserver = new Observer<List<Balizas>>() {
            @Override
            public void onChanged(List<Balizas> dbData) {
                if (dbData != null) {
                    misBalizasRVAdapter.setBalizas(dbData);
                } else {
                    System.out.println("La lista está vacía");
                }
            }
        };
        viewModelMisBalizas.getBalizasActivadas().observe(ma, nameObserver);


        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng CamaraInicial = new LatLng(42.8992, -3.1828);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(CamaraInicial));

        googleMap.setMinZoomPreference(6.0f);
        googleMap.setMaxZoomPreference(14.0f);

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(42.9995, -2.73515)).zoom(8).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        for (Balizas b : MasBalizasRVAdapter.balizas) {

            if (b.activated.equals("true")) {
                LatLng position2 = new LatLng(b.x, b.y);
                googleMap.addMarker(new MarkerOptions().position(position2).title(b.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                LatLng position2 = new LatLng(b.x, b.y);
                googleMap.addMarker(new MarkerOptions().position(position2).title(b.name));
            }
        }

        googleMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        for (Balizas b : MasBalizasRVAdapter.balizas) {
            if (b.name.equals(marker.getTitle())) {
                if (b.activated.equals("true")) {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    sectionsPagerAdapter.FuncionAdapter(false, b, ma);
                } else {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    sectionsPagerAdapter.FuncionAdapter(true, b, ma);
                }
            }
        }
    }
}