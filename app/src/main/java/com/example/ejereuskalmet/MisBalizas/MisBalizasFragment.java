package com.example.ejereuskalmet.MisBalizas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.Mapa.MapaFragment;
import com.example.ejereuskalmet.MasBalizas.MasBalizasRVAdapter;
import com.example.ejereuskalmet.MasBalizas.ViewModelMasBalizas;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MisBalizasFragment extends Fragment {

    public static List<Balizas> misbalizas = new ArrayList<>();
    public static List<Datos> mislecturas = new ArrayList<>();
    public static MisBalizasRVAdapter misBalizasRVAdapter = new MisBalizasRVAdapter();
    public static MasBalizasRVAdapter masBalizasRVadapter = new MasBalizasRVAdapter();
    public static MapaFragment mapaFragment = new MapaFragment();
    private ViewModelMisBalizas viewModelMisBalizas;
    private static JSONArray response;
    private MainActivity mainActivity;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public MisBalizasFragment() {
        // Required empty public constructor
    }
    public MisBalizasFragment(MainActivity mainActivity, SectionsPagerAdapter sectionsPagerAdapter) {
        this.mainActivity = mainActivity;
        this.sectionsPagerAdapter = sectionsPagerAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inf = inflater.inflate(R.layout.fragment_mis_balizas, container, false);

        RecyclerView rvmisbalizas = inf.findViewById(R.id.rvmisbalizas);
        rvmisbalizas.setLayoutManager(new LinearLayoutManager(mainActivity));
        misBalizasRVAdapter = new MisBalizasRVAdapter(mainActivity,sectionsPagerAdapter);

        rvmisbalizas.setAdapter(misBalizasRVAdapter);

        /** OBSERVER PARA LAS BALIZAS AÑADIDAS **/

        viewModelMisBalizas = new ViewModelProvider(mainActivity).get(ViewModelMisBalizas.class);

        final Observer<List<Balizas>> nameObserver = new Observer<List<Balizas>>() {
            @Override
            public void onChanged(List<Balizas> dbData) {
                if (dbData != null){
                    misBalizasRVAdapter.setBalizas(dbData);
                } else {
                    System.out.println("La lista está vacía");
                }
            }
        };
        viewModelMisBalizas.getBalizasActivadas().observe(mainActivity, nameObserver);


        /** OBSERVER PARA AÑADIR LAS LECTURAS **/

        viewModelMisBalizas = new ViewModelProvider(mainActivity).get(ViewModelMisBalizas.class);

        final Observer<List<Datos>> nameObserver2 = new Observer<List<Datos>>() {
            @Override
            public void onChanged(List<Datos> dbData) {
                if (dbData != null){
                    misBalizasRVAdapter.setMislecturas(dbData);
                } else {
                    System.out.println("La lista está vacía");
                }
            }
        };
        viewModelMisBalizas.getAllDatos().observe(mainActivity, nameObserver2);

        return inf;
    }

    public static void setTrue(Balizas baliza){
        masBalizasRVadapter.setTrue(baliza);
    }
    public static void setFalse(Balizas baliza){
        masBalizasRVadapter.setFalse(baliza);
    }
}