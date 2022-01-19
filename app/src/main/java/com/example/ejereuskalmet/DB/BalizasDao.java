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

    @Query("SELECT name FROM Balizas WHERE name LIKE :busqueda")
    String Busqueda(String busqueda);

    @Query("SELECT COUNT(1) FROM Balizas WHERE id = :id;")
    boolean Existe(String id);

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
}