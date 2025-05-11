package com.example.appconstruccion.views.materialUsado;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.MaterialUsadoController;
import com.example.appconstruccion.model.MaterialUsado;

import java.util.Calendar;

public class AgregarMaterialUsadoActivity extends AppCompatActivity {

    private EditText etNombre, etCantidad, etCostoUnidad, etFechaUso, etObservaciones, etIdObra;
    private MaterialUsadoController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_material_usado);

        controller = new MaterialUsadoController(this);

        // Inicialización de los campos EditText
        etNombre = findViewById(R.id.et_nombre_material);
        etCantidad = findViewById(R.id.et_cantidad_material);
        etCostoUnidad = findViewById(R.id.et_costo_unidad_material);
        etFechaUso = findViewById(R.id.et_fecha_uso_material);
        etObservaciones = findViewById(R.id.et_observaciones_material);
        etIdObra = findViewById(R.id.et_id_obra_material);

        // Mostrar el calendario al hacer clic en la fecha de uso
        etFechaUso.setOnClickListener(v -> mostrarDatePicker());
    }


    private void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    etFechaUso.setText(fecha);
                }, año, mes, dia);
        datePickerDialog.show();
    }

    public void guardarMaterialUsado(View view) {
        String nombre = etNombre.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String costoStr = etCostoUnidad.getText().toString().trim();
        String fechaUso = etFechaUso.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();
        String idObraStr = etIdObra.getText().toString().trim();

        // Verificación de campos vacíos
        if (nombre.isEmpty() || cantidadStr.isEmpty() || costoStr.isEmpty() || fechaUso.isEmpty() || idObraStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversión de datos de texto a los tipos correctos
        int cantidad = Integer.parseInt(cantidadStr);
        double costoUnidad = Double.parseDouble(costoStr);
        int idObra = Integer.parseInt(idObraStr);

        // Asignación del valor para el ID (esto depende de cómo estés manejando los IDs en tu sistema)
        int id = 0;  // Este valor debe ser generado correctamente, aquí estamos asignando 0 como valor temporal

        // Crear el objeto MaterialUsado con los parámetros requeridos
        MaterialUsado material = new MaterialUsado(id, nombre, cantidad, costoUnidad, fechaUso, observaciones, idObra);

        // Llamada al controlador para agregar el material usado
        boolean resultado = controller.agregarMaterial(material);

        if (resultado) {
            Toast.makeText(this, "Material agregado correctamente", Toast.LENGTH_SHORT).show();
            // Limpiar los campos después de agregar el material
            etNombre.setText("");
            etCantidad.setText("");
            etCostoUnidad.setText("");
            etFechaUso.setText("");
            etObservaciones.setText("");
            etIdObra.setText("");
        } else {
            Toast.makeText(this, "Error al guardar el material. Verifica la tabla en DB o los campos vacíos.", Toast.LENGTH_LONG).show();
            Log.e("DB_ERROR", "Falló el insert. Datos: " + material.getNombreMaterial() + ", " + material.getFechaUso());
        }
    }
}
