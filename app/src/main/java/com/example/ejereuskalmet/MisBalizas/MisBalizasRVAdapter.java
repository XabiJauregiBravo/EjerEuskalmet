package com.example.ejereuskalmet.MisBalizas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.MasBalizas.MasBalizasRVAdapter;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MisBalizasRVAdapter extends RecyclerView.Adapter<MisBalizasRVAdapter.ViewHolder> {

    public List<Balizas> misbalizas = new ArrayList<>();
    public List<Datos> mislecturas = new ArrayList<>();
    private LayoutInflater mInflater;
    private MainActivity main;
    private SectionsPagerAdapter sectionsPagerAdapter;

    /**
     * RUNNABLE PARA REALIZAR UN UPDATEO DE LOS ULTIMOS DATOS CADA 5 MIN
     **/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(300000);
                    actualizarBalizas((ArrayList<Balizas>) MasBalizasRVAdapter.balizas);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    };

    Thread hilo = new Thread(runnable);

    public MisBalizasRVAdapter() {
    }

    public MisBalizasRVAdapter(Context context, SectionsPagerAdapter sectionsPagerAdapter) {
        this.mInflater = LayoutInflater.from(context);
        this.main = (MainActivity) context;
        this.sectionsPagerAdapter = sectionsPagerAdapter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nombre;
        private final TextView hora;
        private final TextView mean_speed;
        private final TextView mean_direction;
        private final TextView max_speed;
        private final TextView temperature;
        private final TextView precipitation;
        private final TextView humidity;

        public ViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.tvNombre);
            hora = view.findViewById(R.id.tvHora);
            mean_speed = view.findViewById(R.id.tvMeanSpeed);
            mean_direction = view.findViewById(R.id.tvMeanDirection);
            max_speed = view.findViewById(R.id.tvMaxSpeed);
            precipitation = view.findViewById(R.id.tvPrecipitation);
            temperature = view.findViewById(R.id.tvTemperature);
            humidity = view.findViewById(R.id.tvHumidity);
        }

        public TextView getNombre() {
            return nombre;
        }

        public TextView getHora() {
            return hora;
        }

        public TextView getMean_speed() {
            return mean_speed;
        }

        public TextView getMax_speed() {
            return max_speed;
        }

        public TextView getTemperature() {
            return temperature;
        }

        public TextView getHumidity() {
            return humidity;
        }

        public TextView getMean_direction() {
            return mean_direction;
        }

        public TextView getPrecipitation() {
            return precipitation;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.misbalizas, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        int posicion = position;
        System.out.println(posicion);
        Balizas b = misbalizas.get(posicion);

        ArrayList<Datos> lecturaBaliza = new ArrayList<>();

        if (mislecturas != null) {
            for (Datos lectura : mislecturas) {
                if (lectura != null && lectura.id != null) {
                    if (lectura.id.equals(b.id)) {
                        lecturaBaliza.add(lectura);
                    }
                }
            }

            if (lecturaBaliza != null && lecturaBaliza.size() > 0) {
                System.out.println("size: " + lecturaBaliza.size());
                viewHolder.getNombre().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).name);
                viewHolder.getHora().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).hora);
                viewHolder.getMax_speed().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).max_speed + " km/h");
                viewHolder.getHumidity().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).humidity + " %");
                viewHolder.getMean_speed().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).mean_speed + " m/s");
                viewHolder.getMean_direction().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).mean_direction + " º");
                viewHolder.getTemperature().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).temperature + " ºC");
                viewHolder.getPrecipitation().setText(lecturaBaliza.get(lecturaBaliza.size() - 1).precipitation + " l/m²");
            }

            if (hilo.isAlive()) {
            } else {
                hilo.start();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (misbalizas == null) {
            return 0;
        } else {
            return misbalizas.size();
        }
    }

    public void setBalizas(List<Balizas> balizas) {
        this.misbalizas = balizas;
        notifyDataSetChanged();
    }

    public void setMislecturas(List<Datos> mislecturas) {
        this.mislecturas = mislecturas;
        notifyDataSetChanged();
    }

    public void actualizarBalizas(ArrayList<Balizas> misbalizas) {

        for (Balizas baliza : misbalizas) {

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

            //System.out.println("Size de balizas response : " + response.length());

            RequestQueue queue = Volley.newRequestQueue(main);

            String url = "https://www.euskalmet.euskadi.eus/vamet/stations/readings/" + baliza.id + "/" + year + "/" + month1 + "/" + day + "/readingsData.json";
            System.out.println(baliza.name + " y su url " + url);
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

                        HandlerThread ht = new HandlerThread("HandleThread");
                        ht.start();

                        Handler handlerLeer = new Handler(ht.getLooper());

                        handlerLeer.post(new Runnable() {
                            @Override
                            public void run() {
                                if (main.db.datosDao().Existe(lectura.id)) {

                                    System.out.println("Nombre lectura: " + lectura.name);
                                    main.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                } else {
                                    System.out.println("Nombre lectura: " + lectura.name);
                                    main.db.datosDao().insert(lectura);
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
                                if (main.db.datosDao().Existe(lectura.id)) {

                                    System.out.println("Nombre lectura: " + lectura.name);
                                    main.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                } else {
                                    System.out.println("Nombre lectura: " + lectura.name);
                                    main.db.datosDao().insert(lectura);
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
                    System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet enla funcion actualizar balizas");
                }
            });
            queue.add(jsonObjectRequest);
        }
    }
}
