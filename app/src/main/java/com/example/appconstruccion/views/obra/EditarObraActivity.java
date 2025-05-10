package com.example.appconstruccion.views.obra;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.ObraController;
import com.example.appconstruccion.model.Obra;

import java.util.Calendar;
import java.util.List;

public class EditarObraActivity extends AppCompatActivity {

    private EditText etNombre, etCliente, etUbicacion, etInicio, etFin;
    private ObraController controller;
    private Obra obra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_obra);

        controller = new ObraController(this);

        etNombre = findViewById(R.id.et_edit_nombre_proyecto);
        etCliente = findViewById(R.id.et_edit_cliente);
        etUbicacion = findViewById(R.id.et_edit_ubicacion);
        etInicio = findViewById(R.id.et_edit_fecha_inicio);
        etFin = findViewById(R.id.et_edit_fecha_fin);

        etInicio.setOnClickListener(v -> mostrarDatePicker(etInicio));
        etFin.setOnClickListener(v -> mostrarDatePicker(etFin));


        int idObra = getIntent().getIntExtra("id", -1);
        if (idObra != -1) {
            obra = buscarObraPorId(idObra);
            if (obra != null) {
                etNombre.setText(obra.getNombreProyecto());
                etCliente.setText(obra.getCliente());
                etUbicacion.setText(obra.getUbicacion());
                etInicio.setText(obra.getFechaInicio());
                etFin.setText(obra.getFechaFin());
            } else {
                Toast.makeText(this, "Obra no encontrada", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private Obra buscarObraPorId(int id) {
        List<Obra> obras = controller.obtenerTodas();
        for (Obra o : obras) {
            if (o.getId() == id) {
                return o;
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

    public void actualizarObra(View view) {
        if (obra == null) return;

        obra.setNombreProyecto(etNombre.getText().toString().trim());
        obra.setCliente(etCliente.getText().toString().trim());
        obra.setUbicacion(etUbicacion.getText().toString().trim());
        obra.setFechaInicio(etInicio.getText().toString().trim());
        obra.setFechaFin(etFin.getText().toString().trim());

        boolean actualizado = controller.actualizarObra(obra);

        if (actualizado) {
            Toast.makeText(this, "Obra actualizada correctamente", Toast.LENGTH_SHORT).show();
            finish(); // volver a la lista
        } else {
            Toast.makeText(this, "Error al actualizar la obra", Toast.LENGTH_SHORT).show();
        }
    }
}
