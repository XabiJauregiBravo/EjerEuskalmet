package com.example.ejereuskalmet.Lecturas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;
import java.util.List;

public class ViewModelLecturas extends ViewModel {
    public MainActivity mainActivity;

    public LiveData<List<Datos>> getAllDatos() {
        return mainActivity.db.datosDao().getAll();
    }

    public LiveData<List<Balizas>> getBalizasActivadas() {
        return mainActivity.db.balizasDao().getAllBalizasActivadas();
    }
}