package com.example.appconstruccion.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appconstruccion.database.DBConstruccion;
import com.example.appconstruccion.model.Obra;

import java.util.ArrayList;
import java.util.List;

public class ObraController {

    private final DBConstruccion dbHelper;

    public ObraController(Context context) {
        dbHelper = new DBConstruccion(context);
    }

    // CREATE
    public boolean agregarObra(Obra obra) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombreProyecto", obra.getNombreProyecto());
        valores.put("cliente", obra.getCliente());
        valores.put("ubicacion", obra.getUbicacion());
        valores.put("fechaInicio", obra.getFechaInicio());
        valores.put("fechaFin", obra.getFechaFin());

        long resultado = db.insert("obra", null, valores);
        return resultado != -1;
    }

    // READ (obtener todas)
    public List<Obra> obtenerTodas() {
        List<Obra> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM obra", null);
        if (cursor.moveToFirst()) {
            do {
                Obra obra = new Obra();
                obra.setId(cursor.getInt(0));
                obra.setNombreProyecto(cursor.getString(1));
                obra.setCliente(cursor.getString(2));
                obra.setUbicacion(cursor.getString(3));
                obra.setFechaInicio(cursor.getString(4));
                obra.setFechaFin(cursor.getString(5));
                lista.add(obra);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // UPDATE
    public boolean actualizarObra(Obra obra) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombreProyecto", obra.getNombreProyecto());
        valores.put("cliente", obra.getCliente());
        valores.put("ubicacion", obra.getUbicacion());
        valores.put("fechaInicio", obra.getFechaInicio());
        valores.put("fechaFin", obra.getFechaFin());

        int filas = db.update("obra", valores, "id=?", new String[]{String.valueOf(obra.getId())});
        return filas > 0;
    }

    // DELETE
    public boolean eliminarObra(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filas = db.delete("obra", "id=?", new String[]{String.valueOf(id)});
        return filas > 0;
    }

    // NUEVO MÉTODO: Obtener el último ID de obra
    public int obtenerUltimoIdObra() {
        int idObra = -1;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM obra ORDER BY id DESC LIMIT 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            idObra = cursor.getInt(0);
            cursor.close();
        }

        db.close();
        return idObra;
    }
}

