package com.example.ejereuskalmet.MisBalizas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MisBalizasRVAdapter extends RecyclerView.Adapter<MisBalizasRVAdapter.ViewHolder> {

    public List<Balizas> misbalizas;
    private LayoutInflater mInflater;
    private MainActivity main;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public MisBalizasRVAdapter() {
    }

    public MisBalizasRVAdapter(Context context,SectionsPagerAdapter sectionsPagerAdapter) {
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
        private final TextView irrandiance;
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
            irrandiance = view.findViewById(R.id.tvIrrandiance);
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
        public TextView getIrrandiance() {
            return irrandiance;
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.misbalizas, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        sectionsPagerAdapter.FuncionAdapter2(misbalizas,main);

        viewHolder.getNombre().setText(misbalizas.get(position).name);
        viewHolder.getHora().setText(misbalizas.get(position).hora);
        viewHolder.getMax_speed().setText(String.valueOf(misbalizas.get(position).max_speed));
        viewHolder.getHumidity().setText(String.valueOf(misbalizas.get(position).humidity));
        viewHolder.getMean_speed().setText(String.valueOf(misbalizas.get(position).mean_speed));
        viewHolder.getMean_direction().setText(String.valueOf(misbalizas.get(position).mean_direction));
        viewHolder.getTemperature().setText(String.valueOf(misbalizas.get(position).temperature));
        viewHolder.getIrrandiance().setText(String.valueOf(misbalizas.get(position).irradiance));
        viewHolder.getPrecipitation().setText(String.valueOf(misbalizas.get(position).precipitation));

    }

    @Override
    public int getItemCount() {
        if (misbalizas == null){
            return 0;
        }else{
            return misbalizas.size();
        }
    }

    public void setUpdatedData(List<Balizas> balizas, MainActivity main) {

        /*MANEJAR LOS DATOS DEL JSON RESPONSE1 PARA UPDATEAR LOS DATOS DE LAS BALIZAS*/

       /*
        int year = Calendar.getInstance().get(Calendar.YEAR);

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        Balizas b = new Balizas();

        System.out.println("Size de balizas : " + balizas.size());

        RequestQueue queue = Volley.newRequestQueue(main);

        HandlerThread ht = new HandlerThread("HandleThread");
        ht.start();

        Handler handlerLeer = new Handler(ht.getLooper());

        for (int i = 0; i < balizas.size(); i++) {


            String url = "https://www.euskalmet.euskadi.eus/vamet/stations/readings/" + balizas.get(i).id + "/" + year + "/" + month + "/" + day + "/readingsData.json";

            int finalI = i;
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

                            switch (a[i]) {
                                case "11":
                                    System.out.println("mean_speed de " + balizas.get(finalI).id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    misbalizas.get(finalI).mean_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                    misbalizas.get(finalI).hora = horas.get(horas.size() - 1);
                                    break;
                                case "12":
                                    System.out.println("mean_direction de " + balizas.get(finalI).id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    misbalizas.get(finalI).mean_direction = (double) objfinal.get(horas.get(horas.size() - 1));
                                    misbalizas.get(finalI).hora = horas.get(horas.size() - 1);
                                    break;
                                case "14":
                                    System.out.println("max_speed de " + balizas.get(finalI).id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    misbalizas.get(finalI).max_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                    misbalizas.get(finalI).hora = horas.get(horas.size() - 1);
                                    break;
                                case "21":
                                    System.out.println("temperature de " + balizas.get(finalI).id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    misbalizas.get(finalI).temperature = (double) objfinal.get(horas.get(horas.size() - 1));
                                    misbalizas.get(finalI).hora = horas.get(horas.size() - 1);
                                    break;
                                case "31":
                                    System.out.println("humidity de " + balizas.get(finalI).id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    misbalizas.get(finalI).humidity = (double) objfinal.get(horas.get(horas.size() - 1));
                                    misbalizas.get(finalI).hora = horas.get(horas.size() - 1);
                                    break;
                                case "40":
                                    System.out.println("precipitation de " + balizas.get(finalI).id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    misbalizas.get(finalI).precipitation = (double) objfinal.get(horas.get(horas.size() - 1));
                                    misbalizas.get(finalI).hora = horas.get(horas.size() - 1);
                                    break;
                                case "70":
                                    System.out.println("irradiance de " + balizas.get(finalI).id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    misbalizas.get(finalI).irradiance = (double) objfinal.get(horas.get(horas.size() - 1));
                                    misbalizas.get(finalI).hora = horas.get(horas.size() - 1);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet");
                }
            });

            queue.add(jsonObjectRequest);

            this.misbalizas = balizas;
            //notifyDataSetChanged();



        }*/
    }
    public void setBalizas(List<Balizas> balizas) {
        this.misbalizas = balizas;
        notifyDataSetChanged();
    }
}
