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

        String[] opciones = {"Editar", "Eliminar", "Ver materiales usados"};

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Opciones")
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0: // Editar
                            Intent intentEditar = new Intent(this, EditarObraActivity.class);
                            intentEditar.putExtra("id", obraSeleccionada.getId());
                            startActivity(intentEditar);
                            break;
                        case 1: // Eliminar
                            boolean eliminado = controller.eliminarObra(obraSeleccionada.getId());
                            if (eliminado) {
                                Toast.makeText(this, "Obra eliminada", Toast.LENGTH_SHORT).show();
                                cargarLista();
                            } else {
                                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2: // Ver materiales usados
                            Intent intentMateriales = new Intent(this, com.example.appconstruccion.views.materialUsado.ListaMaterialUsadoActivity.class);
                            intentMateriales.putExtra("idObra", obraSeleccionada.getId());
                            startActivity(intentMateriales);
                            break;
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }
}
