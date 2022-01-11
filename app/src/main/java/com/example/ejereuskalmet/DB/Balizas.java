package com.example.ejereuskalmet.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Balizas {
    @PrimaryKey @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "nameEus")
    public String nameEus;

    @ColumnInfo(name = "municipality")
    public String municipality;

    @ColumnInfo(name = "province")
    public String province;

    @ColumnInfo(name = "altitude")
    public String altitude;

    @ColumnInfo(name = "x")
    public double x;

    @ColumnInfo(name = "y")
    public double y;

    @ColumnInfo(name = "stationType")
    public String stationType;

    @ColumnInfo(name = "activated")
    public String activated;

    @ColumnInfo(name = "mean_speed")
    public double mean_speed;

    @ColumnInfo(name = "mean_direction")
    public double mean_direction;

    @ColumnInfo(name = "max_speed")
    public double max_speed;

    @ColumnInfo(name = "temperature")
    public double temperature;

    @ColumnInfo(name = "humidity")
    public double humidity;

    @ColumnInfo(name = "precipitation")
    public double precipitation;

    @ColumnInfo(name = "irradiance")
    public double irradiance;

    @ColumnInfo(name = "hora")
    public String hora;
}