package com.example.ejereuskalmet;

import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.DB.AppDatabase;
import com.google.android.material.tabs.TabLayout;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;
import com.example.ejereuskalmet.databinding.ActivityMainBinding;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static AppDatabase db;
    public static JSONArray response1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Room.databaseBuilder(this.getApplicationContext(), AppDatabase.class, "db-euskalmet").fallbackToDestructiveMigration().build();

        // LLAMADA A LA API PARA CONSEGUIR LOS DATOS DE LAS BALIZAS Y PODER MOSTRARLO EN LA LISTA RECICLABLE
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.euskalmet.euskadi.eus/vamet/stations/stationList/stationList.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                MainActivity.this.response1 = response;
                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(MainActivity.this, MainActivity.this, getSupportFragmentManager(), response);
                ViewPager viewPager = binding.viewPager;
                viewPager.setAdapter(sectionsPagerAdapter);
                viewPager.setCurrentItem(1);
                TabLayout tabs = binding.tabs;
                tabs.setupWithViewPager(viewPager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet");
            }
        });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonArrayRequest);

    }
}