package com.example.ejereuskalmet.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DatosDao {
    @Query("SELECT * FROM Datos ORDER BY name")
    LiveData<List<Datos>> getAll();

    @Insert
    void insert(Datos datos);

    @Delete
    void delete(Datos datos);

    @Query("DELETE FROM Datos")
    void deleteAll();

    @Query("DELETE FROM Datos WHERE id = :id")
    void deleteBalizaDatos(String id);

}