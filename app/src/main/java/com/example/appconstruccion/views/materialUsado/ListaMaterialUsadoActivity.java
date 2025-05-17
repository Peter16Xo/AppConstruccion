package com.example.appconstruccion.views.materialUsado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.MaterialUsadoController;
import com.example.appconstruccion.model.MaterialUsado;

import java.util.*;

public class ListaMaterialUsadoActivity extends AppCompatActivity {

    private ListView listaView;
    private MaterialUsadoController controller;
    private List<MaterialUsado> listaMateriales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_material_usado);

        listaView = findViewById(R.id.listaMaterialUsado);
        controller = new MaterialUsadoController(this);

        cargarLista();
    }

    private void cargarLista() {
        int idObra = getIntent().getIntExtra("idObra", -1);
        if (idObra == -1) {
            Toast.makeText(this, "Error: ID de obra no recibido", Toast.LENGTH_SHORT).show();
            finish(); // Cerramos la activity porque no hay ID válido
            return;
        }

        listaMateriales = controller.obtenerTodos(idObra);

        if (listaMateriales == null || listaMateriales.isEmpty()) {
            Toast.makeText(this, "No hay materiales usados para esta obra", Toast.LENGTH_SHORT).show();
            // Limpiar listaView por si acaso
            listaView.setAdapter(null);
            return;
        }

        List<String> datosFormateados = new ArrayList<>();
        int contador = 1;

        for (MaterialUsado m : listaMateriales) {
            String info = "Material #" + contador + "\n"
                    + "Nombre del Material: " + m.getNombreMaterial() + "\n"
                    + "Cantidad: " + m.getCantidad() + "\n"
                    + "Costo Unidad: $" + m.getCostoUnidad() + "\n"
                    + "Fecha Uso: " + m.getFechaUso() + "\n"
                    + "Observaciones: " + m.getObservaciones() + "\n"
                    + "------------------------------";
            datosFormateados.add(info);
            contador++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosFormateados);
        listaView.setAdapter(adapter);

        listaView.setOnItemClickListener((parent, view, position, id) -> mostrarOpciones(position));
    }

    private void mostrarOpciones(int pos) {
        if (listaMateriales == null || pos < 0 || pos >= listaMateriales.size()) {
            Toast.makeText(this, "Material no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialUsado materialSeleccionado = listaMateriales.get(pos);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Opciones")
                .setMessage("¿Qué deseas hacer con este material?")
                .setPositiveButton("Editar", (dialog, which) -> {
                    Intent intent = new Intent(this, EditarMaterialUsadoActivity.class);
                    intent.putExtra("idMaterial", materialSeleccionado.getId());
                    intent.putExtra("idObra", materialSeleccionado.getIdObra());
                    startActivity(intent);
                })
                .setNegativeButton("Eliminar", (dialog, which) -> {
                    boolean eliminado = controller.eliminarMaterial(materialSeleccionado.getId());
                    if (eliminado) {
                        Toast.makeText(this, "Material eliminado", Toast.LENGTH_SHORT).show();
                        cargarLista();
                    } else {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }
}

