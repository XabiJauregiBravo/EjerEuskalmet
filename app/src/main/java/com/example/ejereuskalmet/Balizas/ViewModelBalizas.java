package com.example.ejereuskalmet.Balizas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.MainActivity;
import java.util.List;

public class ViewModelBalizas extends ViewModel {
    public LiveData<List<Balizas>> balizas;
    public MainActivity mainActivity;

    public LiveData<List<Balizas>> getBalizas() {
        return mainActivity.db.balizasDao().getAll();
    }
}