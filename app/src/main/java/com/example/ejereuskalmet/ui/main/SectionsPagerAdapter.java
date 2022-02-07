package com.example.ejereuskalmet.ui.main;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.Grafico.GraficoFragment;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.Mapa.MapaFragment;
import com.example.ejereuskalmet.Balizas.BalizasFragment;
import com.example.ejereuskalmet.Lecturas.LecturasFragment;
import com.example.ejereuskalmet.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab0,R.string.tab1, R.string.tab2, R.string.tab3};
    private final Context mContext;
    private MainActivity ma;

    public SectionsPagerAdapter(Context context, MainActivity ma, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.ma = ma;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragmento = null;
        switch (position){
            case 0:
                fragmento = new GraficoFragment(ma,this);
                break;
            case 1:
                fragmento = new LecturasFragment(ma,this);
                break;
            case 2:
                fragmento = new BalizasFragment(ma,this);
                break;
            case 3:
                fragmento = new MapaFragment(ma,this);
                break;
        }
        return fragmento;
    }

    public void FuncionAdapter(boolean isChecked,Balizas baliza, MainActivity ma){
        if(isChecked){
            Toast.makeText(ma, "Activada " + baliza.name, Toast.LENGTH_SHORT).show();
            LecturasFragment.setTrue(baliza);
        }
        else {
            Toast.makeText(ma, "Desactivada " + baliza.name, Toast.LENGTH_SHORT).show();
            LecturasFragment.setFalse(baliza);
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