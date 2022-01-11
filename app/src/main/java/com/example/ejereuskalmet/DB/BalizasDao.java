package com.example.ejereuskalmet.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BalizasDao {
    @Query("SELECT * FROM Balizas ORDER BY name")
    LiveData<List<Balizas>> getAll();

//    @Query("SELECT * FROM Balizas ORDER BY :busqueda")
//    List<Balizas> Busqueda(String busqueda);

    @Insert
    void insert(Balizas balizas);

    @Delete
    void delete(Balizas balizas);

    @Query("DELETE FROM Balizas")
    void deleteAll();

    @Query("UPDATE Balizas SET activated = 'true' WHERE id = :id")
    void setTrue(String id);

    @Query("UPDATE Balizas SET activated = 'false' WHERE id = :id")
    void setFalse(String id);

    @Query("SELECT * FROM Balizas WHERE activated = 'true' ORDER BY name")
    LiveData<List<Balizas>> getAllBalizasActivadas();

    @Query("UPDATE Balizas SET mean_direction = :mean_direction, mean_speed = :mean_speed, max_speed = :max_speed, temperature = :temperature, humidity = :humidity, precipitation = :precipitation, irradiance = :irradiance  WHERE id = :id")
    void updateDatosMeteorologicos(String id,double mean_direction,double mean_speed,double max_speed,double temperature,double humidity,double precipitation,double irradiance);
}