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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.MisBalizas.MisBalizasRVAdapter;
import com.example.ejereuskalmet.MisBalizas.ViewModelMisBalizas;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private ViewModelMisBalizas viewModelMisBalizas;
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
                                                    rvmasbalizas.scrollToPosition(finalPosicionPrimerBuscada + 3);
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

                //mainActivity.db.balizasDao().deleteAll();
                //mainActivity.db.datosDao().deleteAll();

                MisBalizasRVAdapter misBalizasRVAdapter = new MisBalizasRVAdapter(mainActivity, sectionsPagerAdapter);

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewModelMisBalizas = new ViewModelProvider(mainActivity).get(ViewModelMisBalizas.class);

                        final Observer<List<Datos>> nameObserver = new Observer<List<Datos>>() {
                            @Override
                            public void onChanged(List<Datos> dbData) {
                                if (dbData != null) {
                                    misBalizasRVAdapter.setMislecturas(dbData);
                                } else {
                                    System.out.println("La lista está vacía");
                                }
                            }
                        };
                        viewModelMisBalizas.getAllDatos().observe(mainActivity, nameObserver);
                    }
                });


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
                    if (baliza.stationType.equals("METEOROLOGICAL")) {

                        HandlerThread ht = new HandlerThread("HandleThread");
                        ht.start();

                        Handler handlerLeer = new Handler(ht.getLooper());

                        handlerLeer.post(new Runnable() {
                            @Override
                            public void run() {

                                if (mainActivity.db.balizasDao().Existe(baliza.id)) {
                                    System.out.println("Nombre baliza:" + baliza.name);
                                    System.out.println(baliza.name + " ya existe");
                                } else {
                                    System.out.println("Nombre baliza:" + baliza.name);
                                    mainActivity.db.balizasDao().insert(baliza);
                                }

                            }
                        });


                        /* Crear todas las lecturas */

                        int year = Calendar.getInstance().get(Calendar.YEAR);

                        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

                        String month1 = "" + month;

                        if (month < 10) {
                            month1 = "0" + month;
                            ;
                        }

                        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                        Datos lectura = new Datos();

                        lectura.id = baliza.id;

                        System.out.println("Size de balizas response : " + response.length());

                        RequestQueue queue = Volley.newRequestQueue(mainActivity);

                        String url = "https://www.euskalmet.euskadi.eus/vamet/stations/readings/" + baliza.id + "/" + year + "/" + month1 + "/" + day + "/readingsData.json";
                        System.out.println(baliza.name+ " y su url "+url);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String a[] = {"11", "12", "14", "21", "31", "40", "70"};

                                    for (int i = 0; i < a.length; i++) {

                                        JSONObject obj1 = (JSONObject) response.get(a[i]);
                                        JSONObject obj2 = (JSONObject) obj1.get("data");
                                        obj2.get(obj2.names().get(0).toString());
                                        JSONObject objfinal = (JSONObject) obj2.get(obj2.names().get(0).toString());
                                        ArrayList<String> horas = new ArrayList<>();

                                        for (int j = 0; j < objfinal.names().length(); j++) {
                                            horas.add(objfinal.names().get(j).toString());
                                        }
                                        Collections.sort(horas);

                                        lectura.hora = horas.get(horas.size() - 1);

                                        lectura.name = baliza.name;

                                        switch (a[i]) {
                                            case "11":
                                                // System.out.println("mean_speed de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.mean_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "12":
                                                //  System.out.println("mean_direction de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.mean_direction = (double) objfinal.get(horas.get(horas.size() - 1));

                                                break;
                                            case "14":
                                                // System.out.println("max_speed de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.max_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "21":
                                                //System.out.println("temperature de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.temperature = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "31":
                                                //System.out.println("humidity de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.humidity = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "40":
                                                //  System.out.println("precipitation de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.precipitation = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                        }
                                    }
                                    rvmasbalizas.setAdapter(masBalizasRVAdapter);

                                    HandlerThread ht = new HandlerThread("HandleThread");
                                    ht.start();

                                    Handler handlerLeer = new Handler(ht.getLooper());

                                    handlerLeer.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mainActivity.db.datosDao().Existe(lectura.id)) {

                                                System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                            } else {
                                                System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().insert(lectura);
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    HandlerThread ht = new HandlerThread("HandleThread");
                                    ht.start();

                                    Handler handlerLeer = new Handler(ht.getLooper());

                                    handlerLeer.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mainActivity.db.datosDao().Existe(lectura.id)) {

                                                System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                            } else {
                                                System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().insert(lectura);
                                            }
                                        }
                                    });
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error);
                                System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet");
                                HandlerThread ht = new HandlerThread("HandleThread");
                                ht.start();

                                Handler handlerLeer = new Handler(ht.getLooper());

                                handlerLeer.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainActivity.db.balizasDao().delete(baliza);
                                    }

                                });
                            }
                        });

                        queue.add(jsonObjectRequest);

                    }
                }
            }
        });
        return inf;
    }
}

