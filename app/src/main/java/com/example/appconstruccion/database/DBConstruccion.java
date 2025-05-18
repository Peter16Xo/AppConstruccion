package com.example.appconstruccion.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;



public class DBConstruccion extends SQLiteOpenHelper {

    public static final String DB_NAME = "Construccion.db";
    public static final int DB_VERSION = 12;

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

        String queryMaterialUsado = "CREATE TABLE MaterialUsado (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreMaterial TEXT," +
                "cantidad INTEGER," +
                "costoUnidad REAL," +
                "fechaUso TEXT," +
                "observaciones TEXT," +
                "idObra INTEGER," +
                "FOREIGN KEY(idObra) REFERENCES obra(id))";

        db.execSQL(queryMaterialUsado);

        String queryPersonal = "CREATE TABLE personal (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreobra TEXT," +
                "cedula TEXT," +
                "nombres TEXT," +
                "apellidos TEXT," +
                "cargo TEXT," +
                "telefono TEXT," +
                "tarea TEXT," +
                "asistencia INTEGER)";
        db.execSQL(queryPersonal);

        String queryAvanceObra = "CREATE TABLE avance_obra (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "obra_id INTEGER NOT NULL," +
                "fecha TEXT NOT NULL," +
                "porcentaje_avance REAL NOT NULL," +
                "descripcion TEXT," +
                "inspeccion_calidad TEXT," +
                "fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (obra_id) REFERENCES obra(id))"; // Referencia a la tabla "obra"
        db.execSQL(queryAvanceObra);



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS obra");
        db.execSQL("DROP TABLE IF EXISTS materialUsado");
        db.execSQL("DROP TABLE IF EXISTS personal");
        db.execSQL("DROP TABLE IF EXISTS avance_obra");

        onCreate(db);
    }
    public List<String> obtenerNombresObras() {
        List<String> listaObras = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombreProyecto FROM obra", null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                listaObras.add(nombre);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaObras;
    }


}