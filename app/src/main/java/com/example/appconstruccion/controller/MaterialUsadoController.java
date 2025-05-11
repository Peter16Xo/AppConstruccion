package com.example.appconstruccion.controller;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appconstruccion.database.DBConstruccion;
import com.example.appconstruccion.model.MaterialUsado;

import java.util.ArrayList;
import java.util.List;
public class MaterialUsadoController {
    private final DBConstruccion dbHelper;

    public MaterialUsadoController(Context context) {
        dbHelper = new DBConstruccion(context);
    }

    // CREATE
    public boolean agregarMaterial(MaterialUsado material) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombreMaterial", material.getNombreMaterial());
        valores.put("cantidad", material.getCantidad());
        valores.put("costoUnidad", material.getCostoUnidad());
        valores.put("fechaUso", material.getFechaUso());
        valores.put("observaciones", material.getObservaciones());
        valores.put("idObra", material.getIdObra());

        long resultado = db.insert("MaterialUsado", null, valores);
        return resultado != -1;
    }

    // READ (obtener todos)
    public List<MaterialUsado> obtenerTodos() {
        List<MaterialUsado> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MaterialUsado", null);
        if (cursor.moveToFirst()) {
            do {
                MaterialUsado material = new MaterialUsado(
                        cursor.getInt(0), // id
                        cursor.getString(1), // nombreMaterial
                        cursor.getInt(2), // cantidad
                        cursor.getDouble(3), // costoUnidad
                        cursor.getString(4), // fechaUso
                        cursor.getString(5), // observaciones
                        cursor.getInt(6) // idObra
                );
                lista.add(material);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // UPDATE
    public boolean actualizarMaterial(MaterialUsado material) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombreMaterial", material.getNombreMaterial());
        valores.put("cantidad", material.getCantidad());
        valores.put("costoUnidad", material.getCostoUnidad());
        valores.put("fechaUso", material.getFechaUso());
        valores.put("observaciones", material.getObservaciones());
        valores.put("idObra", material.getIdObra());

        int filas = db.update("MaterialUsado", valores, "id=?", new String[]{String.valueOf(material.getId())});
        return filas > 0;
    }

    // DELETE
    public boolean eliminarMaterial(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filas = db.delete("MaterialUsado", "id=?", new String[]{String.valueOf(id)});
        return filas > 0;
    }
}


