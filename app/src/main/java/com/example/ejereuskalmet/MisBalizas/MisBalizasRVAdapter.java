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

        System.out.println("size mislecturas :" +mislecturas.size());

        System.out.println("hora :" + mislecturas.get(position).hora);

        System.out.println("max_speed :" +mislecturas.get(position).max_speed);

        System.out.println("humidity :" +mislecturas.get(position).humidity);

        System.out.println("mean_direction :" +mislecturas.get(position).mean_direction);

        System.out.println("temperature :" +mislecturas.get(position).temperature);

        System.out.println("irradiance :" +mislecturas.get(position).irradiance);

        System.out.println("precipitation :" +mislecturas.get(position).precipitation);

        viewHolder.getNombre().setText(misbalizas.get(position).name);

        viewHolder.getHora().setText(mislecturas.get(position).hora);
        viewHolder.getMax_speed().setText(String.valueOf(mislecturas.get(position).max_speed));
        viewHolder.getHumidity().setText(String.valueOf(mislecturas.get(position).humidity));
        viewHolder.getMean_speed().setText(String.valueOf(mislecturas.get(position).mean_speed));
        viewHolder.getMean_direction().setText(String.valueOf(mislecturas.get(position).mean_direction));
        viewHolder.getTemperature().setText(String.valueOf(mislecturas.get(position).temperature));
        viewHolder.getIrrandiance().setText(String.valueOf(mislecturas.get(position).irradiance));
        viewHolder.getPrecipitation().setText(String.valueOf(mislecturas.get(position).precipitation));
    }

    @Override
    public int getItemCount() {
        if (misbalizas == null){
            return 0;
        }else{
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
