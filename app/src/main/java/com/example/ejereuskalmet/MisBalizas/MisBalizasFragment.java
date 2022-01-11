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
import com.example.ejereuskalmet.MainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisBalizasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisBalizasFragment extends Fragment {

    public static List<Balizas> misbalizas = new ArrayList<>();
    public static MisBalizasRVAdapter misBalizasRVAdapter = new MisBalizasRVAdapter();
    public static MasBalizasRVAdapter masBalizasRVadapter = new MasBalizasRVAdapter();
    private ViewModelMisBalizas viewModelMisBalizas;
    private static JSONArray response;
    private MainActivity mainActivity;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public MisBalizasFragment() {
        // Required empty public constructor
    }
    public MisBalizasFragment(JSONArray response, MainActivity mainActivity, SectionsPagerAdapter sectionsPagerAdapter) {
        this.response = response;
        this.mainActivity = mainActivity;
        this.sectionsPagerAdapter = sectionsPagerAdapter;
    }

    public static MisBalizasFragment newInstance(String param1, String param2) {
        MisBalizasFragment fragment = new MisBalizasFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_mis_balizas, container, false);

        RecyclerView rvmisbalizas = inf.findViewById(R.id.rvmisbalizas);
        rvmisbalizas.setLayoutManager(new LinearLayoutManager(mainActivity));
        misBalizasRVAdapter = new MisBalizasRVAdapter(mainActivity,sectionsPagerAdapter);

        rvmisbalizas.setAdapter(misBalizasRVAdapter);
        HandlerThread ht = new HandlerThread("HandleThread");
        ht.start();

        Handler handlerLeer = new Handler(ht.getLooper());

        handlerLeer.post(new Runnable() {
            @Override
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewModelMisBalizas = new ViewModelProvider(mainActivity).get(ViewModelMisBalizas.class);

                        final Observer<List<Balizas>> nameObserver = new Observer<List<Balizas>>() {
                            @Override
                            public void onChanged(List<Balizas> dbData) {
                                if (dbData != null){
                                    System.out.println("Size de dbData en el fragmento: "+dbData.size());
                                   // misbalizas = dbData;
                                    misBalizasRVAdapter.setBalizas(dbData);
                                } else {
                                    System.out.println("La lista está vacía");
                                }
                            }
                        };
                        viewModelMisBalizas.getBalizasActivadas().observe(mainActivity, nameObserver);
                        //misBalizasRVAdapter.setBalizas(misbalizas);

                        System.out.println("Size de misbalizas en el fragmento: " + misbalizas.size());

                    }
                });
            }
        });
        return inf;
    }

    public static void setTrue(Balizas baliza){
        masBalizasRVadapter.setTrue(baliza);
    }

    public static void setUpdatedData(List<Balizas> balizas, MainActivity main){
        misBalizasRVAdapter.setUpdatedData(balizas,main);
    }

    public static void setFalse(Balizas baliza){
        masBalizasRVadapter.setFalse(baliza);
    }
}