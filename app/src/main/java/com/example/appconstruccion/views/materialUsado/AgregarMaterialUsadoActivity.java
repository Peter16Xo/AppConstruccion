package com.example.appconstruccion.views.materialUsado;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.MaterialUsadoController;
import com.example.appconstruccion.controller.ObraController;
import com.example.appconstruccion.model.MaterialUsado;
import com.example.appconstruccion.model.Obra;

import java.util.*;

public class AgregarMaterialUsadoActivity extends AppCompatActivity {

    private Spinner spinnerObra;
    private EditText etNombre, etCantidad, etCostoUnidad, etFechaUso, etObservaciones;
    private MaterialUsadoController materialController;
    private ObraController obraController;
    private List<Obra> listaObras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_material_usado);

        materialController = new MaterialUsadoController(this);
        obraController = new ObraController(this);

        spinnerObra = findViewById(R.id.spinner_obra);
        etNombre = findViewById(R.id.et_nombre_material);
        etCantidad = findViewById(R.id.et_cantidad_material);
        etCostoUnidad = findViewById(R.id.et_costo_unidad_material);
        etFechaUso = findViewById(R.id.et_fecha_uso_material);
        etObservaciones = findViewById(R.id.et_observaciones_material);

        etFechaUso.setOnClickListener(v -> mostrarDatePicker());

        cargarObrasEnSpinner();
    }

    private void cargarObrasEnSpinner() {
        listaObras = obraController.obtenerTodas(); // Implementar este método
        List<String> nombresObras = new ArrayList<>();
        for (Obra o : listaObras) {
            nombresObras.add(o.getNombreProyecto() + " - " + o.getCliente());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresObras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObra.setAdapter(adapter);
    }

    private void mostrarDatePicker() {
        final Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            etFechaUso.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
        }, año, mes, dia);
        datePicker.show();
    }

    public void guardarMaterialUsado(View view) {
        String nombre = etNombre.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String costoStr = etCostoUnidad.getText().toString().trim();
        String fechaUso = etFechaUso.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || costoStr.isEmpty() || fechaUso.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        double costoUnidad = Double.parseDouble(costoStr);
        int idObra = listaObras.get(spinnerObra.getSelectedItemPosition()).getId();

        MaterialUsado material = new MaterialUsado(0, nombre, cantidad, costoUnidad, fechaUso, observaciones, idObra);
        boolean agregado = materialController.agregarMaterial(material);

        if (agregado) {
            Toast.makeText(this, "Material agregado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar el material", Toast.LENGTH_SHORT).show();
        }
    }
}