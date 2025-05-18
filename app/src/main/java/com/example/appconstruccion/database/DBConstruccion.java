package com.example.appconstruccion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConstruccion extends SQLiteOpenHelper {

    public static final String DB_NAME = "Construccion.db";
    public static final int DB_VERSION = 5;

    public DBConstruccion(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryUsuario = "CREATE TABLE usuario (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cedula TEXT NOT NULL," +
                "nombre TEXT," +
                "apellido TEXT," +
                "telefono TEXT)";
        db.execSQL(queryUsuario);

        String queryObra = "CREATE TABLE obra (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreProyecto TEXT," +
                "cliente TEXT," +
                "ubicacion TEXT," +
                "fechaInicio TEXT," +
                "fechaFin TEXT)";
        db.execSQL(queryObra);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS obra");

        onCreate(db);
    }



}
