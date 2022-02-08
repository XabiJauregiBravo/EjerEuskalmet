package com.example.ejereuskalmet.Lecturas;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.Mapa.MapaFragment;
import com.example.ejereuskalmet.Balizas.BalizasRVAdapter;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;
import org.json.JSONArray;
import java.util.List;

public class LecturasFragment extends Fragment {

    public static LecturasRVAdapter lecturasRVAdapter = new LecturasRVAdapter();
    public static BalizasRVAdapter balizasRVadapter = new BalizasRVAdapter();
    private ViewModelLecturas viewModelLecturas;
    private MainActivity mainActivity;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public LecturasFragment() {
        // Required empty public constructor
    }
    public LecturasFragment(MainActivity mainActivity, SectionsPagerAdapter sectionsPagerAdapter) {
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
        lecturasRVAdapter = new LecturasRVAdapter(mainActivity,sectionsPagerAdapter);

        rvmisbalizas.setAdapter(lecturasRVAdapter);

        /** OBSERVER PARA LAS BALIZAS AÑADIDAS **/

        viewModelLecturas = new ViewModelProvider(mainActivity).get(ViewModelLecturas.class);

        final Observer<List<Balizas>> nameObserver = new Observer<List<Balizas>>() {
            @Override
            public void onChanged(List<Balizas> dbData) {
                if (dbData != null){
                    lecturasRVAdapter.setBalizas(dbData);
                } else {
                    System.out.println("La lista está vacía");
                }
            }
        };
        viewModelLecturas.getBalizasActivadas().observe(mainActivity, nameObserver);


        /** OBSERVER PARA AÑADIR LAS LECTURAS **/

        viewModelLecturas = new ViewModelProvider(mainActivity).get(ViewModelLecturas.class);

        final Observer<List<Datos>> nameObserver2 = new Observer<List<Datos>>() {
            @Override
            public void onChanged(List<Datos> dbData) {
                if (dbData != null){
                    lecturasRVAdapter.setMislecturas(dbData);
                } else {
                    System.out.println("La lista está vacía");
                }
            }
        };
        viewModelLecturas.getAllDatos().observe(mainActivity, nameObserver2);

        return inf;
    }

    public static void setTrue(Balizas baliza){
        balizasRVadapter.setTrue(baliza);
    }
    public static void setFalse(Balizas baliza){
        balizasRVadapter.setFalse(baliza);
    }
}