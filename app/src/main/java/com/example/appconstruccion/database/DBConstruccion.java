package com.example.appconstruccion.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConstruccion extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    public static final String DB_NAME = "Construccion.db";
    public static final int DB_VERSION = 3;  // Asegúrate de que la versión sea la correcta (incrementada si agregas tablas)

    // Constructor de la base de datos
    public DBConstruccion(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla usuario
        String queryUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cedula TEXT NOT NULL," +
                "nombre TEXT," +
                "apellido TEXT," +
                "telefono TEXT)";
        db.execSQL(queryUsuario);

        // Crear tabla obra
        String queryObra = "CREATE TABLE IF NOT EXISTS obra (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreProyecto TEXT," +
                "cliente TEXT," +
                "ubicacion TEXT," +
                "fechaInicio TEXT," +
                "fechaFin TEXT)";
        db.execSQL(queryObra);

        // Crear tabla proyecto (presupuesto total)
        String queryProyecto = "CREATE TABLE IF NOT EXISTS proyecto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "presupuesto_estimado REAL)";
        db.execSQL(queryProyecto);

        // Crear tabla gastos (para registrar los gastos de cada proyecto)
        String queryGastos = "CREATE TABLE IF NOT EXISTS gastos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "proyecto_id INTEGER," +
                "categoria TEXT," +
                "monto REAL," +
                "FOREIGN KEY(proyecto_id) REFERENCES proyecto(id))";
        db.execSQL(queryGastos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si la versión de la base de datos ha cambiado, eliminar las tablas anteriores
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS proyecto");
            db.execSQL("DROP TABLE IF EXISTS gastos");
        }

        // Llamar a onCreate para crear las nuevas tablas
        onCreate(db);
    }

    // Método para insertar un proyecto con el presupuesto total
    public long addProyecto(double presupuesto) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insertar el presupuesto en la tabla 'proyecto'
        String query = "INSERT INTO proyecto (presupuesto_estimado) VALUES (" + presupuesto + ")";
        db.execSQL(query);

        // Obtener el ID del proyecto insertado
        long proyectoId = -1;
        String selectQuery = "SELECT last_insert_rowid()";  // Esta función obtiene el último ID insertado
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            proyectoId = cursor.getLong(0);  // Obtener el ID del último proyecto insertado
        }
        cursor.close();
        return proyectoId;
    }

    // Método para insertar un gasto relacionado con un proyecto
    public void addGasto(long proyectoId, String categoria, double monto) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO gastos (proyecto_id, categoria, monto) VALUES (" +
                proyectoId + ", '" + categoria + "', " + monto + ")";
        db.execSQL(query);
    }

    // Método para obtener el total de los gastos de un proyecto
    public double getTotalGastos(long proyectoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(monto) FROM gastos WHERE proyecto_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(proyectoId)});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        cursor.close();
        return 0;
    }

    // Método para obtener el presupuesto total de un proyecto
    public double getPresupuesto(long proyectoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT presupuesto_estimado FROM proyecto WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(proyectoId)});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        cursor.close();
        return 0;
    }
}

