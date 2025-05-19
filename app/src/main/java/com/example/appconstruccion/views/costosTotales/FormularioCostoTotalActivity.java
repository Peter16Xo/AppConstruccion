package com.example.appconstruccion.views.costosTotales;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.CostoTotalController;
import com.example.appconstruccion.controller.MaterialUsadoController;
import com.example.appconstruccion.model.CostoTotal;
import com.example.appconstruccion.model.MaterialUsado;
import com.example.appconstruccion.model.Obra;

import java.util.ArrayList;
import java.util.List;

public class FormularioCostoTotalActivity extends AppCompatActivity {

    private Spinner spinnerObras;
    private TextView tvPresupuestoRestante;

    private EditText etPresupuesto, etMateriales, etManoObra, etHerramientas;
    private Button btnGuardar;

    private CostoTotalController controller;
    private MaterialUsadoController materialController;

    private String modo;
    private int idCostoEditar = -1;

    private List<Obra> listaObras;
    private int idObraSeleccionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costo_total);

        controller = new CostoTotalController(this);
        materialController = new MaterialUsadoController(this);

        spinnerObras = findViewById(R.id.spinner_obra);
        etPresupuesto = findViewById(R.id.et_presupuesto_total);
        etMateriales = findViewById(R.id.et_gasto_materiales);
        etManoObra = findViewById(R.id.et_gasto_mano_obra);
        etHerramientas = findViewById(R.id.et_gasto_herramientas);
        btnGuardar = findViewById(R.id.btn_guardar_costos);

        cargarObrasEnSpinner();

        modo = getIntent().getStringExtra("modo");
        if ("editar".equals(modo)) {
            idCostoEditar = getIntent().getIntExtra("idCosto", -1);
        }

        tvPresupuestoRestante = findViewById(R.id.tv_presupuesto_restante);

// Detectar cambios
        etPresupuesto.addTextChangedListener(new SimpleTextWatcher());
        etManoObra.addTextChangedListener(new SimpleTextWatcher());
        etHerramientas.addTextChangedListener(new SimpleTextWatcher());


        btnGuardar.setOnClickListener(v -> guardarDatos());
    }

    private class SimpleTextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            actualizarPresupuestoRestante();
        }

        @Override
        public void afterTextChanged(android.text.Editable s) {}
    }

    private void actualizarPresupuestoRestante() {
        try {
            double presupuesto = Double.parseDouble(etPresupuesto.getText().toString());
            double materiales = Double.parseDouble(etMateriales.getText().toString());
            double manoObra = Double.parseDouble(etManoObra.getText().toString());
            double herramientas = Double.parseDouble(etHerramientas.getText().toString());

            double restante = presupuesto - (materiales + manoObra + herramientas);
            tvPresupuestoRestante.setText("Presupuesto restante: $" + String.format("%.2f", restante));
        } catch (Exception e) {
            tvPresupuestoRestante.setText("Presupuesto restante: $0.00");
        }
    }


    private void cargarObrasEnSpinner() {
        listaObras = controller.obtenerTodasObras();

        List<String> nombresObras = new ArrayList<>();
        for (Obra obra : listaObras) {
            nombresObras.add(obra.getNombreProyecto() + " - " + obra.getCliente());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresObras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObras.setAdapter(adapter);

        spinnerObras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idObraSeleccionada = listaObras.get(position).getId();

                // Actualizar gasto de materiales automáticamente
                actualizarGastoMaterialesPorObra(idObraSeleccionada);

                if ("editar".equals(modo)) {
                    cargarDatos(idObraSeleccionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idObraSeleccionada = -1;
            }
        });
    }

    private void actualizarGastoMaterialesPorObra(int idObra) {
        List<MaterialUsado> listaMateriales = materialController.obtenerTodos(idObra);
        double total = 0;

        for (MaterialUsado m : listaMateriales) {
            total += m.getCantidad() * m.getCostoUnidad();
        }

        etMateriales.setText(String.valueOf(total));
        actualizarPresupuestoRestante();
    }

    private void cargarDatos(int idObra) {
        CostoTotal costo = controller.obtenerPorId(idCostoEditar);
        if (costo != null) {
            etPresupuesto.setText(String.valueOf(costo.getPresupuestoTotal()));
            etMateriales.setText(String.valueOf(costo.getGastoMateriales()));
            etManoObra.setText(String.valueOf(costo.getGastoManoObra()));
            etHerramientas.setText(String.valueOf(costo.getGastoHerramientas()));

            for (int i = 0; i < listaObras.size(); i++) {
                if (listaObras.get(i).getId() == costo.getIdObra()) {
                    spinnerObras.setSelection(i);
                    break;
                }
            }
        }
    }

    private void guardarDatos() {
        try {
            int idObra = idObraSeleccionada;
            double presupuesto = Double.parseDouble(etPresupuesto.getText().toString());
            double mat = Double.parseDouble(etMateriales.getText().toString());
            double mano = Double.parseDouble(etManoObra.getText().toString());
            double herramientas = Double.parseDouble(etHerramientas.getText().toString());

            CostoTotal costo = new CostoTotal(idCostoEditar, idObra, presupuesto, mat, mano, herramientas);

            boolean resultado;
            if ("editar".equals(modo)) {
                resultado = controller.actualizarCostoTotal(costo);
            } else {
                resultado = controller.guardarCostoTotal(costo);
            }

            if (resultado) {
                Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Verifica los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
