
package com.example.appconstruccion.views.costosTotales;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.CostoTotalController;
import com.example.appconstruccion.model.CostoTotal;

import java.util.ArrayList;
import java.util.List;

public class ListaCostosTotalesActivity extends AppCompatActivity {

    private ListView listaCostos;
    private Button btnAgregar;
    private CostoTotalController controller;
    private List<CostoTotal> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_costos_totales);

        listaCostos = findViewById(R.id.listaCostos);
        btnAgregar = findViewById(R.id.btn_agregar);
        controller = new CostoTotalController(this);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormularioCostoTotalActivity.class);
            intent.putExtra("modo", "nuevo");
            startActivity(intent);
        });

        listaCostos.setOnItemClickListener((parent, view, position, id) -> mostrarOpciones(position));
    }

    private void cargarLista() {
        listaDatos = controller.obtenerTodos();

        if (listaDatos == null || listaDatos.isEmpty()) {
            listaCostos.setAdapter(null);
            Toast.makeText(this, "No hay registros", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> texto = new ArrayList<>();
        for (CostoTotal c : listaDatos) {
            texto.add("Obra ID: " + c.getIdObra() +
                    "\nPresupuesto: $" + c.getPresupuestoTotal() +
                    "\nGastado: $" + c.getTotalGastado() +
                    "\nRestante: $" + c.getDiferencia());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, texto);
        listaCostos.setAdapter(adapter);
    }

    private void mostrarOpciones(int pos) {
        if (listaDatos == null || pos < 0 || pos >= listaDatos.size()) return;

        CostoTotal seleccionado = listaDatos.get(pos);

        new AlertDialog.Builder(this)
                .setTitle("Opciones")
                .setMessage("¿Qué deseas hacer con este registro?")
                .setPositiveButton("Editar", (dialog, which) -> {
                    Intent intent = new Intent(this, FormularioCostoTotalActivity.class);
                    intent.putExtra("modo", "editar");
                    intent.putExtra("idCosto", seleccionado.getId());
                    startActivity(intent);
                })
                .setNegativeButton("Eliminar", (dialog, which) -> {
                    boolean eliminado = controller.eliminarPorId(seleccionado.getId());
                    if (eliminado) {
                        Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                        cargarLista();
                    } else {
                        Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
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
