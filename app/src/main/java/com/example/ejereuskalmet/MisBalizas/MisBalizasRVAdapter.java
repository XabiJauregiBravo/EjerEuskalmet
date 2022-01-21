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
}
