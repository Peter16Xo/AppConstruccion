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
        valores.put("idObra", material.getIdObra());
        valores.put("nombreMaterial", material.getNombreMaterial());
        valores.put("cantidad", material.getCantidad());
        valores.put("costoUnidad", material.getCostoUnidad());
        valores.put("fechaUso", material.getFechaUso());
        valores.put("observaciones", material.getObservaciones());

        long resultado = db.insert("MaterialUsado", null, valores);
        db.close();
        return resultado != -1;
    }

    // READ (obtener todos)
    public List<MaterialUsado> obtenerTodos(int idObra) {
        List<MaterialUsado> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // ✅ CORREGIDO: idObra, no id_obra
        Cursor cursor = db.rawQuery("SELECT * FROM MaterialUsado WHERE idObra = ?",
                new String[]{String.valueOf(idObra)});

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
        db.close();
        return lista;
    }

    // READ (por ID)
    public MaterialUsado obtenerMaterialPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("MaterialUsado", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            MaterialUsado material = new MaterialUsado(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6)
            );
            cursor.close();
            db.close();
            return material;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
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
        db.close();
        return filas > 0;
    }

    // DELETE
    public boolean eliminarMaterial(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filas = db.delete("MaterialUsado", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return filas > 0;
    }
}
