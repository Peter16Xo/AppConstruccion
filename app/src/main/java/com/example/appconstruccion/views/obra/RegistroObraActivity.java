package com.example.appconstruccion.views.obra;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.ObraController;
import com.example.appconstruccion.model.Obra;

import java.util.Calendar;

public class RegistroObraActivity extends AppCompatActivity {

    private EditText etNombre, etCliente, etUbicacion, etInicio, etFin;
    private ObraController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_obra);

        controller = new ObraController(this);

        etNombre = findViewById(R.id.et_nombre_proyecto);
        etCliente = findViewById(R.id.et_cliente);
        etUbicacion = findViewById(R.id.et_ubicacion);
        etInicio = findViewById(R.id.et_fecha_inicio);
        etFin = findViewById(R.id.et_fecha_fin);

        // Mostrar calendario al hacer clic en fechas
        etInicio.setOnClickListener(v -> mostrarDatePicker(etInicio));
        etFin.setOnClickListener(v -> mostrarDatePicker(etFin));
    }

    // Método para mostrar el calendario y establecer la fecha en el campo
    private void mostrarDatePicker(final EditText campoFecha) {
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    campoFecha.setText(fecha);
                }, año, mes, dia);
        datePickerDialog.show();
    }

    public void guardarObra(View view) {
        String nombre = etNombre.getText().toString().trim();
        String cliente = etCliente.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String inicio = etInicio.getText().toString().trim();
        String fin = etFin.getText().toString().trim();

        if (nombre.isEmpty() || cliente.isEmpty() || ubicacion.isEmpty() || inicio.isEmpty() || fin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Obra obra = new Obra(0, nombre, cliente, ubicacion, inicio, fin);
        boolean resultado = controller.agregarObra(obra);

        if (resultado) {
            Toast.makeText(this, "Obra guardada correctamente", Toast.LENGTH_SHORT).show();
            etNombre.setText("");
            etCliente.setText("");
            etUbicacion.setText("");
            etInicio.setText("");
            etFin.setText("");
        } else {
            Toast.makeText(this, "Error al guardar la obra. Verifica la tabla en DB o los campos vacíos.", Toast.LENGTH_LONG).show();
            Log.e("DB_ERROR", "Falló el insert. Datos: " + obra.getNombreProyecto() + ", " + obra.getFechaInicio());
        }
    }
}
