package com.example.ejereuskalmet.MisBalizas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.MainActivity;
import java.util.List;

public class ViewModelMisBalizas extends ViewModel {
    public LiveData<List<Balizas>> balizas;
    public MainActivity mainActivity;

    public LiveData<List<Balizas>> getBalizasActivadas() {
        return mainActivity.db.balizasDao().getAllBalizasActivadas();
    }
}