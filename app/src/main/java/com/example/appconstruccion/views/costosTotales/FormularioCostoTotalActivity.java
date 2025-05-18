package com.example.appconstruccion.views.costosTotales;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.CostoTotalController;
import com.example.appconstruccion.model.CostoTotal;

public class FormularioCostoTotalActivity extends AppCompatActivity {

    private EditText etIdObra, etPresupuesto, etMateriales, etManoObra, etHerramientas;
    private Button btnGuardar;
    private CostoTotalController controller;
    private String modo;
    private int idCostoEditar = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costo_total);

        controller = new CostoTotalController(this);
        etIdObra = findViewById(R.id.et_id_obra);
        etPresupuesto = findViewById(R.id.et_presupuesto_total);
        etMateriales = findViewById(R.id.et_gasto_materiales);
        etManoObra = findViewById(R.id.et_gasto_mano_obra);
        etHerramientas = findViewById(R.id.et_gasto_herramientas);
        btnGuardar = findViewById(R.id.btn_guardar_costos);

        modo = getIntent().getStringExtra("modo");
        if ("editar".equals(modo)) {
            idCostoEditar = getIntent().getIntExtra("idCosto", -1);
            cargarDatos();
        }

        btnGuardar.setOnClickListener(v -> guardarDatos());
    }

    private void cargarDatos() {
        CostoTotal costo = controller.obtenerPorId(idCostoEditar);
        if (costo != null) {
            etIdObra.setText(String.valueOf(costo.getIdObra()));
            etPresupuesto.setText(String.valueOf(costo.getPresupuestoTotal()));
            etMateriales.setText(String.valueOf(costo.getGastoMateriales()));
            etManoObra.setText(String.valueOf(costo.getGastoManoObra()));
            etHerramientas.setText(String.valueOf(costo.getGastoHerramientas()));
        }
    }

    private void guardarDatos() {
        try {
            int idObra = Integer.parseInt(etIdObra.getText().toString());
            double presupuesto = Double.parseDouble(etPresupuesto.getText().toString());
            double mat = Double.parseDouble(etMateriales.getText().toString());
            double mano = Double.parseDouble(etManoObra.getText().toString());
            double herramientas = Double.parseDouble(etHerramientas.getText().toString());

            CostoTotal costo = new CostoTotal(idCostoEditar, idObra, presupuesto, mat, mano, herramientas);

            boolean resultado = false;
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
