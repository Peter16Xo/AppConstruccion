package com.example.appconstruccion.views.materialUsado;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.MaterialUsadoController;
import com.example.appconstruccion.model.MaterialUsado;

public class EditarMaterialUsadoActivity extends AppCompatActivity {

    private EditText etNombre, etCantidad, etCostoUnidad, etFechaUso, etObservaciones;
    private MaterialUsadoController controller;
    private MaterialUsado material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_material_usado);

        int idMaterial = getIntent().getIntExtra("idMaterial", -1);

        if (idMaterial == -1) {
            Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        controller = new MaterialUsadoController(this);
        material = controller.obtenerMaterialPorId(idMaterial);

        if (material == null) {
            Toast.makeText(this, "Material no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombre = findViewById(R.id.et_edit_nombre_material);
        etCantidad = findViewById(R.id.et_edit_cantidad);
        etCostoUnidad = findViewById(R.id.et_edit_costo_unidad);
        etFechaUso = findViewById(R.id.et_edit_fecha_uso);
        etObservaciones = findViewById(R.id.et_edit_observaciones);

        etNombre.setText(material.getNombreMaterial());
        etCantidad.setText(String.valueOf(material.getCantidad()));
        etCostoUnidad.setText(String.valueOf(material.getCostoUnidad()));
        etFechaUso.setText(material.getFechaUso());
        etObservaciones.setText(material.getObservaciones());

        Button btnGuardar = findViewById(R.id.btn_guardar_edicion);
        btnGuardar.setOnClickListener(v -> {
            material.setNombreMaterial(etNombre.getText().toString().trim());
            material.setCantidad(Integer.parseInt(etCantidad.getText().toString().trim()));
            material.setCostoUnidad(Double.parseDouble(etCostoUnidad.getText().toString().trim()));
            material.setFechaUso(etFechaUso.getText().toString().trim());
            material.setObservaciones(etObservaciones.getText().toString().trim());

            boolean actualizado = controller.actualizarMaterial(material);
            if (actualizado) {
                Toast.makeText(EditarMaterialUsadoActivity.this, "Material actualizado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditarMaterialUsadoActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


