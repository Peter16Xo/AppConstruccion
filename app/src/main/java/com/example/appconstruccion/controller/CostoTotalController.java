package com.example.appconstruccion.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appconstruccion.database.DBConstruccion;
import com.example.appconstruccion.model.CostoTotal;
import com.example.appconstruccion.model.Obra;

import java.util.ArrayList;
import java.util.List;

public class CostoTotalController {
    private final DBConstruccion dbHelper;

    public CostoTotalController(Context context) {
        dbHelper = new DBConstruccion(context);
    }

    // Verificar si ya existe un costo para una obra
    public boolean existeCostoParaObra(int idObra) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM CostoTotal WHERE idObra = ?", new String[]{String.valueOf(idObra)});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;
    }

    // INSERTAR nuevo costo (solo si no existe ya)
    public boolean guardarCostoTotal(CostoTotal costo) {
        if (existeCostoParaObra(costo.getIdObra())) {
            return false; // Ya existe
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("idObra", costo.getIdObra());
        valores.put("presupuestoTotal", costo.getPresupuestoTotal());
        valores.put("gastoMateriales", costo.getGastoMateriales());
        valores.put("gastoManoObra", costo.getGastoManoObra());
        valores.put("gastoHerramientas", costo.getGastoHerramientas());

        long resultado = db.insert("CostoTotal", null, valores);
        db.close();
        return resultado != -1;
    }

    // ACTUALIZAR un costo existente
    public boolean actualizarCostoTotal(CostoTotal costo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("idObra", costo.getIdObra());
        valores.put("presupuestoTotal", costo.getPresupuestoTotal());
        valores.put("gastoMateriales", costo.getGastoMateriales());
        valores.put("gastoManoObra", costo.getGastoManoObra());
        valores.put("gastoHerramientas", costo.getGastoHerramientas());

        int filas = db.update("CostoTotal", valores, "id = ?", new String[]{String.valueOf(costo.getId())});
        db.close();
        return filas > 0;
    }

    // OBTENER todos los costos
    public List<CostoTotal> obtenerTodos() {
        List<CostoTotal> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CostoTotal", null);

        if (cursor.moveToFirst()) {
            do {
                CostoTotal c = new CostoTotal(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getDouble(5)
                );
                lista.add(c);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    // OBTENER un costo por ID
    public CostoTotal obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("CostoTotal", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            CostoTotal c = new CostoTotal(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getDouble(2),
                    cursor.getDouble(3),
                    cursor.getDouble(4),
                    cursor.getDouble(5)
            );
            cursor.close();
            db.close();
            return c;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    // ELIMINAR por ID
    public boolean eliminarPorId(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filas = db.delete("CostoTotal", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return filas > 0;
    }

    // Obtener todas las obras
    public List<Obra> obtenerTodasObras() {
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
        db.close();
        return lista;
    }

    // Obtener obra por ID
    public Obra obtenerObraPorId(int idObra) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Obra obra = null;

        Cursor cursor = db.rawQuery("SELECT * FROM obra WHERE id = ?", new String[]{String.valueOf(idObra)});
        if (cursor.moveToFirst()) {
            obra = new Obra();
            obra.setId(cursor.getInt(0));
            obra.setNombreProyecto(cursor.getString(1));
            obra.setCliente(cursor.getString(2));
            obra.setUbicacion(cursor.getString(3));
            obra.setFechaInicio(cursor.getString(4));
            obra.setFechaFin(cursor.getString(5));
        }

        cursor.close();
        db.close();
        return obra;
    }
}
