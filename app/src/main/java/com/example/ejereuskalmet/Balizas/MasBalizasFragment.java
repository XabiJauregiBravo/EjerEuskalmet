package com.example.ejereuskalmet.Balizas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.Lecturas.LecturasRVAdapter;
import com.example.ejereuskalmet.Lecturas.ViewModelLecturas;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;

import java.util.List;

public class MasBalizasFragment extends Fragment {

    private ViewModelBalizas viewModelBalizas;
    private ViewModelLecturas viewModelLecturas;
    private MainActivity mainActivity;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private EditText editTextBusqueda;

    private static String TextoBusqueda = "";

    public MasBalizasFragment() {
        // Required empty public constructor
    }

    public MasBalizasFragment(MainActivity mainActivity, SectionsPagerAdapter sectionsPagerAdapter) {
        this.mainActivity = mainActivity;
        this.sectionsPagerAdapter = sectionsPagerAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inf = inflater.inflate(R.layout.fragment_mas_balizas, container, false);

        editTextBusqueda = inf.findViewById(R.id.editTextBusqueda);
        TextoBusqueda = editTextBusqueda.getText().toString();

        MasBalizasRVAdapter masBalizasRVAdapter = new MasBalizasRVAdapter(mainActivity, sectionsPagerAdapter);

        RecyclerView rvmasbalizas = inf.findViewById(R.id.rvmasbalizas);
        rvmasbalizas.setLayoutManager(new LinearLayoutManager(mainActivity));

        rvmasbalizas.setAdapter(masBalizasRVAdapter);

        /** OBSERVER PARA LAS BALIZAS **/

        viewModelBalizas = new ViewModelProvider(mainActivity).get(ViewModelBalizas.class);

        final Observer<List<Balizas>> nameObserver = new Observer<List<Balizas>>() {
            @Override
            public void onChanged(List<Balizas> dbData) {
                if (dbData != null) {
                    masBalizasRVAdapter.setBalizas(dbData);
                } else {
                    System.out.println("La lista está vacía");
                }
            }
        };
        viewModelBalizas.getBalizas().observe(mainActivity, nameObserver);

        LecturasRVAdapter lecturasRVAdapter = new LecturasRVAdapter(mainActivity, sectionsPagerAdapter);

        /** OBSERVER PARA LOS DATOS DE LAS BALIZAS **/

        viewModelLecturas = new ViewModelProvider(mainActivity).get(ViewModelLecturas.class);

        final Observer<List<Datos>> nameObserver2 = new Observer<List<Datos>>() {
            @Override
            public void onChanged(List<Datos> dbData) {
                if (dbData != null) {
                    lecturasRVAdapter.setMislecturas(dbData);
                } else {
                    System.out.println("La lista está vacía");
                }
            }
        };
        viewModelLecturas.getAllDatos().observe(mainActivity, nameObserver2);


        /** BUSCADOR DE BALIZAS **/


        editTextBusqueda.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    TextoBusqueda = editTextBusqueda.getText().toString();
                    Toast toast1 = Toast.makeText(mainActivity.getApplicationContext(), TextoBusqueda, Toast.LENGTH_SHORT);
                    toast1.show();

                    TextoBusqueda = TextoBusqueda + "%";
                    System.out.println("Texto busqueda : " + TextoBusqueda);

                    HandlerThread ht = new HandlerThread("HandleThread");
                    ht.start();

                    Handler handlerLeer = new Handler(ht.getLooper());

                    handlerLeer.post(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println("" + mainActivity.db.balizasDao().Busqueda(TextoBusqueda));

                            int PosicionPrimerBuscada = masBalizasRVAdapter.getItemCount() - 1;

                            for (int i = 0; i < masBalizasRVAdapter.balizas.size(); i++) {
                                if (masBalizasRVAdapter.balizas.get(i).name.equals(mainActivity.db.balizasDao().Busqueda(TextoBusqueda))) {
                                    PosicionPrimerBuscada = i;
                                }
                            }

                            int finalPosicionPrimerBuscada = PosicionPrimerBuscada;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rvmasbalizas.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            rvmasbalizas.scrollToPosition(finalPosicionPrimerBuscada);
                                        }
                                    });
                                }
                            });
                        }

                    });
                    return true;
                }
                return false;
            }
        });
        return inf;
    }
}

