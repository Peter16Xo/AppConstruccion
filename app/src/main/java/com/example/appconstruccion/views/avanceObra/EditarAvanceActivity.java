package com.example.appconstruccion.views.avanceObra;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent; // Importa Intent
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.appconstruccion.R;
import com.example.appconstruccion.database.DBConstruccion;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditarAvanceActivity extends AppCompatActivity {

    private EditText editTextFecha;
    private EditText editTextPorcentaje;
    private EditText editTextComentario;
    private CheckBox checkBoxAprobada;
    private CheckBox checkBoxNoAprobada;
    private Button buttonGuardar;
    private DBConstruccion dbHelper;
    private SQLiteDatabase db;
    private int avanceId;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_avance); // Crea este layout

        editTextFecha = findViewById(R.id.editTextFechaEditar); // Asegúrate de tener estos IDs en el layout
        editTextPorcentaje = findViewById(R.id.editTextPorcentajeEditar);
        editTextComentario = findViewById(R.id.editTextComentarioEditar);
        checkBoxAprobada = findViewById(R.id.checkBoxAprobadaEditar);
        checkBoxNoAprobada = findViewById(R.id.checkBoxNoAprobadaEditar);
        buttonGuardar = findViewById(R.id.buttonGuardarEditar);

        dbHelper = new DBConstruccion(this);
        db = dbHelper.getWritableDatabase();

        avanceId = getIntent().getIntExtra("avance_id", -1);
        if (avanceId == -1) {
            Toast.makeText(this, "Error: No se recibió el ID del avance", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosAvance();

        editTextFecha.setOnClickListener(v -> mostrarDatePicker());

        buttonGuardar.setOnClickListener(v -> guardarAvanceEditado());
    }

    private void cargarDatosAvance() {
        Cursor cursor = null;
        try {
            String[] projection = {"fecha", "porcentaje_avance", "descripcion", "inspeccion_calidad"};
            String selection = "id = ?";
            String[] selectionArgs = {String.valueOf(avanceId)};
            cursor = db.query("avance_obra", projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                editTextFecha.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha")));
                editTextPorcentaje.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("porcentaje_avance"))));
                editTextComentario.setText(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                String inspeccion = cursor.getString(cursor.getColumnIndexOrThrow("inspeccion_calidad"));
                checkBoxAprobada.setChecked(inspeccion.contains("Aprobada"));
                checkBoxNoAprobada.setChecked(inspeccion.contains("No aprobada"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar los datos del avance", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void mostrarDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarEditTextFecha();
        };

        new DatePickerDialog(EditarAvanceActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void actualizarEditTextFecha() {
        String formatoFecha = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatoFecha, Locale.getDefault());
        editTextFecha.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void guardarAvanceEditado() {
        String fecha = editTextFecha.getText().toString();
        String porcentajeStr = editTextPorcentaje.getText().toString().replace("%", "");
        String descripcion = editTextComentario.getText().toString();
        String inspeccionCalidad = "";

        double porcentajeAvance = 0.0;
        try {
            porcentajeAvance = Double.parseDouble(porcentajeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Porcentaje de avance no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkBoxAprobada.isChecked() && checkBoxNoAprobada.isChecked()) {
            inspeccionCalidad = "Aprobada y No aprobada";
        } else if (checkBoxAprobada.isChecked()) {
            inspeccionCalidad = "Aprobada";
        } else if (checkBoxNoAprobada.isChecked()) {
            inspeccionCalidad = "No aprobada";
        } else {
            Toast.makeText(this, "Seleccione la inspección de calidad", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("fecha", fecha);
        values.put("porcentaje_avance", porcentajeAvance);
        values.put("descripcion", descripcion);
        values.put("inspeccion_calidad", inspeccionCalidad);

        int rowsAffected = db.update("avance_obra", values, "id = ?", new String[]{String.valueOf(avanceId)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Avance actualizado con éxito", Toast.LENGTH_SHORT).show();

            // Aqui se creo un intent para el resultado y finalizacion
            Intent resultIntent = new Intent();
            resultIntent.putExtra("avance_id", avanceId);
            resultIntent.putExtra("fecha", fecha);
            resultIntent.putExtra("porcentaje_avance", porcentajeAvance);
            resultIntent.putExtra("descripcion", descripcion);
            resultIntent.putExtra("inspeccion_calidad", inspeccionCalidad);
            resultIntent.putExtra("position", getIntent().getIntExtra("position", -1)); // **DEVUELVE LA POSICIÓN**

            setResult(RESULT_OK, resultIntent); // Establece el resultado como OK y adjunta el Intent
            finish(); // Volver a la lista de avances
        } else {
            Toast.makeText(this, "Error al actualizar el avance", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}