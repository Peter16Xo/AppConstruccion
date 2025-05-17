package com.example.appconstruccion.views.materialUsado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.MaterialUsadoController;
import com.example.appconstruccion.model.MaterialUsado;

public class EditarMaterialUsadoActivity extends AppCompatActivity {

    private EditText etNombre, etCantidad, etCostoUnidad, etFechaUso, etObservaciones;
    private MaterialUsadoController controller;
    private MaterialUsado material;
    private int idObra = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_material_usado);

        int idMaterial = getIntent().getIntExtra("idMaterial", -1);
        idObra = getIntent().getIntExtra("idObra", -1);

        if (idMaterial == -1 || idObra == -1) {
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

        // Asignar el idObra al objeto, por seguridad
        material.setIdObra(idObra);

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

        Button btnGuardar = findViewById(R.id.btn_actualizar_material);
        btnGuardar.setOnClickListener(v -> actualizarMaterial());
    }

    private void actualizarMaterial() {
        String nombre = etNombre.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String costoStr = etCostoUnidad.getText().toString().trim();
        String fechaUso = etFechaUso.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || costoStr.isEmpty() || fechaUso.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double costoUnidad = Double.parseDouble(costoStr);

            material.setNombreMaterial(nombre);
            material.setCantidad(cantidad);
            material.setCostoUnidad(costoUnidad);
            material.setFechaUso(fechaUso);
            material.setObservaciones(observaciones);
            material.setIdObra(idObra); // Asegurar coherencia

            boolean actualizado = controller.actualizarMaterial(material);
            if (actualizado) {
                Toast.makeText(this, "Material actualizado", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ListaMaterialUsadoActivity.class);
                intent.putExtra("idObra", idObra);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // ✅ Evita pila duplicada
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad o costo inválido", Toast.LENGTH_SHORT).show();
        }
    }
}
