package com.example.appconstruccion.views.materialUsado;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.MaterialUsadoController;
import com.example.appconstruccion.model.MaterialUsado;

import java.util.Calendar;
import java.util.List;

public class EditarMaterialUsadoActivity extends AppCompatActivity {

    private EditText etNombreMaterial, etCantidad, etCostoUnidad, etFechaUso, etObservaciones, etIdObra;
    private MaterialUsadoController controller;
    private MaterialUsado material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_material_usado);

        controller = new MaterialUsadoController(this);

        etNombreMaterial = findViewById(R.id.et_edit_nombre_material);
        etCantidad = findViewById(R.id.et_edit_cantidad);
        etCostoUnidad = findViewById(R.id.et_edit_costo_unidad);
        etFechaUso = findViewById(R.id.et_edit_fecha_uso);
        etObservaciones = findViewById(R.id.et_edit_observaciones);
        etIdObra = findViewById(R.id.et_edit_id_obra);


        etFechaUso.setOnClickListener(v -> mostrarDatePicker(etFechaUso));

        int idMaterial = getIntent().getIntExtra("id", -1);
        if (idMaterial != -1) {
            material = buscarMaterialPorId(idMaterial);
            if (material != null) {
                etNombreMaterial.setText(material.getNombreMaterial());
                etCantidad.setText(String.valueOf(material.getCantidad()));
                etCostoUnidad.setText(String.valueOf(material.getCostoUnidad()));
                etFechaUso.setText(material.getFechaUso());
                etObservaciones.setText(material.getObservaciones());
            } else {
                Toast.makeText(this, "Material no encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private MaterialUsado buscarMaterialPorId(int id) {
        List<MaterialUsado> materiales = controller.obtenerTodos();
        for (MaterialUsado m : materiales) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    private void mostrarDatePicker(final EditText campoFecha) {
        final Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            campoFecha.setText(fecha);
        }, año, mes, dia);

        picker.show();
    }

    public void actualizarMaterial(View view) {
        if (material == null) return;

        material.setNombreMaterial(etNombreMaterial.getText().toString().trim());
        material.setCantidad(Integer.parseInt(etCantidad.getText().toString().trim()));
        material.setCostoUnidad(Double.parseDouble(etCostoUnidad.getText().toString().trim()));
        material.setFechaUso(etFechaUso.getText().toString().trim());
        material.setObservaciones(etObservaciones.getText().toString().trim());

        boolean actualizado = controller.actualizarMaterial(material);

        if (actualizado) {
            Toast.makeText(this, "Material actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar el material", Toast.LENGTH_SHORT).show();
        }
    }
}