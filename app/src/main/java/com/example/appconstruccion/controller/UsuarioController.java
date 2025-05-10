package com.example.appconstruccion.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appconstruccion.database.DBConstruccion;
import com.example.appconstruccion.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioController {

    private final DBConstruccion dbHelper;

    public UsuarioController(Context context) {
        dbHelper = new DBConstruccion(context);
    }

    public boolean agregarUsuario(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("cedula", usuario.getCedula());
        valores.put("nombre", usuario.getNombre());
        valores.put("apellido", usuario.getApellido());
        valores.put("telefono", usuario.getTelefono());

        long resultado = db.insert("usuario", null, valores);
        return resultado != -1;
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario", null);
        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getInt(0));
                usuario.setCedula(cursor.getString(1));
                usuario.setNombre(cursor.getString(2));
                usuario.setApellido(cursor.getString(3));
                usuario.setTelefono(cursor.getString(4));
                lista.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public boolean eliminarUsuario(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("usuario", "id=?", new String[]{String.valueOf(id)}) > 0;
    }
}
