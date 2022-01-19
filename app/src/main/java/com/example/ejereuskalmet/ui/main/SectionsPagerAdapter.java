package com.example.ejereuskalmet.ui.main;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.Mapa.MapaFragment;
import com.example.ejereuskalmet.MasBalizas.MasBalizasFragment;
import com.example.ejereuskalmet.MasBalizas.MasBalizasRVAdapter;
import com.example.ejereuskalmet.MisBalizas.MisBalizasFragment;
import com.example.ejereuskalmet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab1, R.string.tab2, R.string.tab3};
    private final Context mContext;
    private MainActivity ma;
    private JSONArray response1;

    public SectionsPagerAdapter(Context context, MainActivity ma, FragmentManager fm, JSONArray response1) {
        super(fm);
        mContext = context;
        this.ma = ma;
        this.response1 = response1;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragmento = null;
        switch (position){
            case 0:
                fragmento = new MisBalizasFragment(response1,ma,this);
                break;
            case 1:
                fragmento = new MasBalizasFragment(response1,ma,this);
                break;
            case 2:
                fragmento = new MapaFragment();
                break;
        }
        return fragmento;
    }

    public void FuncionAdapter(boolean isChecked,Balizas baliza, MainActivity ma){
        if(isChecked){
            Toast.makeText(ma, "On en " + baliza.id, Toast.LENGTH_SHORT).show();
            MisBalizasFragment.setTrue(baliza);
        }
        else {
            Toast.makeText(ma, "Off en " + baliza.id, Toast.LENGTH_SHORT).show();
            MisBalizasFragment.setFalse(baliza);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}