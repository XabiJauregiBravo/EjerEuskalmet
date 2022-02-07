package com.example.ejereuskalmet;

import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.Api.Api;
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
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(MainActivity.this, MainActivity.this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(2);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        db = Room.databaseBuilder(this.getApplicationContext(), AppDatabase.class, "db-euskalmet").fallbackToDestructiveMigration().build();

        RequestQueue queue = Volley.newRequestQueue(this);
        Api api = new Api(this,queue);
        api.getBalizasApi();
    }
}