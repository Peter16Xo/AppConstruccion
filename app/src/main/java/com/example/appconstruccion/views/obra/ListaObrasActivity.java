package com.example.appconstruccion.views.obra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.ObraController;
import com.example.appconstruccion.model.Obra;

import java.util.ArrayList;
import java.util.List;

public class ListaObrasActivity extends AppCompatActivity {

    private ListView listaView;
    private ObraController controller;
    private List<Obra> listaObras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_obras);

        listaView = findViewById(R.id.listaObras);
        controller = new ObraController(this);

        cargarLista();
    }

    private void cargarLista() {
        listaObras = controller.obtenerTodas();

        List<String> datosFormateados = new ArrayList<>();
        int contador = 1;

        for (Obra o : listaObras) {
            String info = "Obra #" + contador + "\n"
                    + "Nombre del Proyecto: " + o.getNombreProyecto() + "\n"
                    + "Cliente: " + o.getCliente() + "\n"
                    + "Ubicación: " + o.getUbicacion() + "\n"
                    + "Fecha Inicio: " + o.getFechaInicio() + "\n"
                    + "Fecha Fin: " + o.getFechaFin() + "\n"
                    + "------------------------------";

            datosFormateados.add(info);
            contador++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, datosFormateados);
        listaView.setAdapter(adapter);

        listaView.setOnItemClickListener((parent, view, position, id) -> mostrarOpciones(position));
    }


    private void mostrarOpciones(int pos) {
        Obra obraSeleccionada = listaObras.get(pos);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Opciones")
                .setMessage("¿Qué deseas hacer con esta obra?")
                .setPositiveButton("Editar", (dialog, which) -> {
                    Intent intent = new Intent(this, EditarObraActivity.class);
                    intent.putExtra("id", obraSeleccionada.getId());
                    startActivity(intent);
                })
                .setNegativeButton("Eliminar", (dialog, which) -> {
                    boolean eliminado = controller.eliminarObra(obraSeleccionada.getId());
                    if (eliminado) {
                        Toast.makeText(this, "Obra eliminada", Toast.LENGTH_SHORT).show();
                        cargarLista(); // recargar después de eliminar
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
        cargarLista(); // recargar la lista al volver de editar
    }
}
