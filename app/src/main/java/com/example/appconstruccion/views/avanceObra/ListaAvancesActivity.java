package com.example.appconstruccion.views.avanceObra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import com.example.appconstruccion.R;
import com.example.appconstruccion.database.DBConstruccion;
import java.util.ArrayList;
import java.util.List;

public class ListaAvancesActivity extends AppCompatActivity implements com.example.appconstruccion.views.avanceObra.AvanceListAdapter.OnItemClickListener {

    private RecyclerView recyclerViewAvances;
    private com.example.appconstruccion.views.avanceObra.AvanceListAdapter adapter;
    private List<com.example.appconstruccion.views.avanceObra.AvanceObraData> listaAvances;
    private DBConstruccion dbHelper;
    private SQLiteDatabase db;
    private int obraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_avances);

        obraId = getIntent().getIntExtra("obra_id", -1);
        if (obraId == -1) {
            Toast.makeText(this, "Error: No se recibió el ID de la obra", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewAvances = findViewById(R.id.recyclerViewAvances);
        recyclerViewAvances.setLayoutManager(new LinearLayoutManager(this));

        listaAvances = new ArrayList<>();
        dbHelper = new DBConstruccion(this);
        db = dbHelper.getReadableDatabase();

        cargarAvancesObra();

        adapter = new com.example.appconstruccion.views.avanceObra.AvanceListAdapter(listaAvances);
        adapter.setOnItemClickListener(this);
        recyclerViewAvances.setAdapter(adapter);
    }

    private void cargarAvancesObra() {
        listaAvances.clear();
        Cursor cursor = null;
        try {
            String[] projection = {"id", "fecha", "porcentaje_avance", "descripcion", "inspeccion_calidad"};
            String selection = "obra_id = ?";
            String[] selectionArgs = {String.valueOf(obraId)};
            cursor = db.query("avance_obra", projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                    double porcentajeAvance = cursor.getDouble(cursor.getColumnIndexOrThrow("porcentaje_avance"));
                    String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                    String inspeccionCalidad = cursor.getString(cursor.getColumnIndexOrThrow("inspeccion_calidad"));
                    com.example.appconstruccion.views.avanceObra.AvanceObraData avance = new com.example.appconstruccion.views.avanceObra.AvanceObraData(id, obraId, fecha, porcentajeAvance, descripcion, inspeccionCalidad);
                    listaAvances.add(avance);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar los avances", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onEditarClick(int position) {
        com.example.appconstruccion.views.avanceObra.AvanceObraData avance = listaAvances.get(position);
        Intent intent = new Intent(this, com.example.appconstruccion.views.avanceObra.EditarAvanceActivity.class);
        intent.putExtra("avance_id", avance.getId());
        intent.putExtra("position", position); // **PASAMOS LA POSICIÓN**
        startActivityForResult(intent, 1); // El '1' es un código de solicitud (request code)
    }

    @Override
    public void onEliminarClick(final int avanceId) { // Recibimos el ID ahora
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este avance?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarAvance(avanceId); // Usamos el ID para eliminar
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void eliminarAvance(int avanceId) {
        SQLiteDatabase dbWritable = dbHelper.getWritableDatabase();
        int rowsDeleted = dbWritable.delete("avance_obra", "id = ?", new String[]{String.valueOf(avanceId)});
        dbWritable.close();
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Avance eliminado", Toast.LENGTH_SHORT).show();
            // **Actualiza la lista local y notifica al adapter buscando por ID**
            for (int i = 0; i < listaAvances.size(); i++) {
                if (listaAvances.get(i).getId() == avanceId) {
                    listaAvances.remove(i);
                    adapter.notifyItemRemoved(i);
                    break;
                }
            }
        } else {
            Toast.makeText(this, "Error al eliminar el avance", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int avanceIdActualizado = data.getIntExtra("avance_id", -1);
            String fechaActualizada = data.getStringExtra("fecha");
            double porcentajeActualizado = data.getDoubleExtra("porcentaje_avance", 0.0);
            String descripcionActualizada = data.getStringExtra("descripcion");
            String inspeccionActualizada = data.getStringExtra("inspeccion_calidad");
            int positionActualizada = data.getIntExtra("position", -1);

            if (avanceIdActualizado != -1 && positionActualizada != -1) {
                // Actualiza el objeto en tu lista local
                com.example.appconstruccion.views.avanceObra.AvanceObraData avanceActualizado = new com.example.appconstruccion.views.avanceObra.AvanceObraData(
                        avanceIdActualizado,
                        obraId,
                        fechaActualizada,
                        porcentajeActualizado,
                        descripcionActualizada,
                        inspeccionActualizada
                );
                listaAvances.set(positionActualizada, avanceActualizado);
                adapter.notifyItemChanged(positionActualizada); // Notifica al adapter que un ítem ha cambiado
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Ya no es necesario recargar completamente aquí, la eliminación actualiza la lista
        // cargarAvancesObra();
        // adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}