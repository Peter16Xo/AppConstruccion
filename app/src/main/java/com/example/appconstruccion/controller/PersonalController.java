package com.example.appconstruccion.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.example.appconstruccion.database.DBConstruccion;
import com.example.appconstruccion.model.Obra;
import com.example.appconstruccion.model.Personal;

import java.util.ArrayList;
import java.util.List;

public class PersonalController {
    private final DBConstruccion dbHelper;

    public PersonalController(Context context) {
        dbHelper = new DBConstruccion(context);
    }

    // CREATE
    public boolean agregarPersonal(Personal personal) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombreobra", personal.getNombreobra());
        valores.put("cedula", personal.getCedula());
        valores.put("nombres", personal.getNombres());
        valores.put("apellidos", personal.getApellidos());
        valores.put("cargo", personal.getCargo());
        valores.put("telefono", personal.getTelefono());
        valores.put("tarea", personal.getTarea());
        valores.put("asistencia", personal.getAsistencia());

        long resultado = db.insert("personal", null, valores);
        return resultado != -1;
    }

    // READ (obtener todas)
    public List<Personal> obtenerTodas() {
        List<Personal> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM personal", null);
        if (cursor.moveToFirst()) {
            do {
                Personal personal = new Personal();
                personal.setId(cursor.getInt(0));
                personal.setNombreobra(cursor.getString(1));
                personal.setCedula(cursor.getString(2));
                personal.setNombres(cursor.getString(3));
                personal.setApellidos(cursor.getString(4));
                personal.setCargo(cursor.getString(5));
                personal.setTelefono(cursor.getString(6));
                personal.setTarea(cursor.getString(7));

                int asistenciaInt = cursor.getInt(8);
                personal.setAsistencia(asistenciaInt == 1);
                lista.add(personal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    //UPDATE
    public boolean actualizarPersonal(Personal personal){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombreobra", personal.getNombreobra());
        valores.put("nombres", personal.getNombres());
        valores.put("apellidos", personal.getApellidos());
        valores.put("cargo", personal.getCargo());
        valores.put("telefono", personal.getTelefono());
        valores.put("tarea", personal.getTarea());
        valores.put("asistencia", personal.getAsistencia());

        int filas = db.update("personal", valores, "id=?", new String[]{String.valueOf(personal.getId())});
        return filas > 0;
    }

    // DELETE
    public boolean eliminarPersonal(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filas = db.delete("personal", "id=?", new String[]{String.valueOf(id)});
        return filas > 0;
    }
}
