package com.example.ejereuskalmet.MasBalizas;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MasBalizasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasBalizasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static JSONArray response;
    private ViewModelMasBalizas viewModelMasBalizas;
    private MainActivity mainActivity;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private EditText editTextBusqueda;

    private static String TextoBusqueda = "";

    public MasBalizasFragment() {
        // Required empty public constructor
    }

    public MasBalizasFragment(JSONArray response, MainActivity mainActivity, SectionsPagerAdapter sectionsPagerAdapter) {
        this.response = response;
        this.mainActivity = mainActivity;
        this.sectionsPagerAdapter = sectionsPagerAdapter;
    }

    public static MasBalizasFragment newInstance(String param1, String param2) {
        MasBalizasFragment fragment = new MasBalizasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inf = inflater.inflate(R.layout.fragment_mas_balizas, container, false);

        MasBalizasRVAdapter masBalizasRVAdapter = new MasBalizasRVAdapter(mainActivity, sectionsPagerAdapter);

        editTextBusqueda = inf.findViewById(R.id.editTextBusqueda);
        TextoBusqueda = editTextBusqueda.getText().toString();


        RecyclerView rvmasbalizas = inf.findViewById(R.id.rvmasbalizas);
        rvmasbalizas.setLayoutManager(new LinearLayoutManager(mainActivity));


        rvmasbalizas.setAdapter(masBalizasRVAdapter);

        HandlerThread ht = new HandlerThread("HandleThread");
        ht.start();

        Handler handlerLeer = new Handler(ht.getLooper());

        handlerLeer.post(new Runnable() {
            @Override
            public void run() {

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewModelMasBalizas = new ViewModelProvider(mainActivity).get(ViewModelMasBalizas.class);

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
                        viewModelMasBalizas.getBalizas().observe(mainActivity, nameObserver);
                    }
                });


                editTextBusqueda.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            TextoBusqueda = editTextBusqueda.getText().toString();
                            Toast toast1 = Toast.makeText(mainActivity.getApplicationContext(), TextoBusqueda, Toast.LENGTH_SHORT);
                            toast1.show();

                            HandlerThread ht2 = new HandlerThread("HandleThread");
                            ht2.start();

                            Handler handlerLeer2 = new Handler(ht2.getLooper());

                            handlerLeer2.post(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      TextoBusqueda = TextoBusqueda + "%";

                                                      System.out.println("Texto busqueda : " + TextoBusqueda);

                                                      //Funcion para saber en que posicion esta el primero de esa letra
                                                      // mainActivity.db.balizasDao().Busqueda(TextoBusqueda);
                                                      System.out.println("" + mainActivity.db.balizasDao().Busqueda(TextoBusqueda));

                                                      int PosicionPrimerBuscada = masBalizasRVAdapter.getItemCount() - 1;

                                                      for (int i = 0; i < masBalizasRVAdapter.balizas.size(); i++) {
                                                            if (masBalizasRVAdapter.balizas.get(i).name.equals( mainActivity.db.balizasDao().Busqueda(TextoBusqueda))) {
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
                                                                      rvmasbalizas.scrollToPosition(finalPosicionPrimerBuscada+3);
                                                                  }
                                                              });
                                                          }
                                                      });

                                                  }
                                              }
                            );

                            return true;
                        }
                        return false;
                    }
                });

/*
        mainActivity.db.balizasDao().deleteAll();

        //Manejar JSON
        for (int i = 0; i < response.length(); i++) {

            JSONObject object = null;
            Balizas baliza = new Balizas();

            try {
                object = (JSONObject) response.get(i);

                baliza.id = object.get("id").toString();

                baliza.name = object.get("name").toString();

                baliza.nameEus = object.get("nameEus").toString();

                baliza.municipality = object.get("municipality").toString();

                baliza.province = object.get("province").toString();

                baliza.altitude = object.get("altitude").toString();

                baliza.x = Double.parseDouble(object.get("y").toString());

                baliza.y = Double.parseDouble(object.get("x").toString());

                baliza.stationType = object.get("stationType").toString();

                baliza.activated = "false";

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (baliza.stationType.equals("METEOROLOGICAL")){
                mainActivity.db.balizasDao().insert(baliza);
            }
        }
*/
            }
        });
        return inf;
    }
}

