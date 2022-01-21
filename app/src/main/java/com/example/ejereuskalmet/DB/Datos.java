package com.example.ejereuskalmet.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Datos {

        @PrimaryKey @NonNull
        public String id;

        @ColumnInfo(name = "name")
        public String name;

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

        @ColumnInfo(name = "hora")
        public String hora;
}