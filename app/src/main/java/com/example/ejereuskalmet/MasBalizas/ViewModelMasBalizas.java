package com.example.ejereuskalmet.MasBalizas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.MainActivity;
import java.util.List;

public class ViewModelMasBalizas extends ViewModel {
    public LiveData<List<Balizas>> balizas;
    public MainActivity mainActivity;

    public LiveData<List<Balizas>> getBalizas() {
        return mainActivity.db.balizasDao().getAll();
    }
}