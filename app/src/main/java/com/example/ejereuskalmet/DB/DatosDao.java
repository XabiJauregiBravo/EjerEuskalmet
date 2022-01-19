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

    @Query("SELECT COUNT(1) FROM Datos WHERE id = :id;")
    boolean Existe(String id);

    @Query("UPDATE Datos SET mean_direction = :mean_direction, mean_speed = :mean_speed, max_speed = :max_speed, temperature = :temperature, humidity = :humidity, precipitation = :precipitation, irradiance = :irradiance  WHERE id = :id")
    void update(String id,double mean_direction,double mean_speed,double max_speed,double temperature,double humidity,double precipitation,double irradiance);

    @Insert
    void insert(Datos datos);

    @Delete
    void delete(Datos datos);

    @Query("DELETE FROM Datos")
    void deleteAll();

    @Query("DELETE FROM Datos WHERE id = :id")
    void deleteBalizaDatos(String id);

}