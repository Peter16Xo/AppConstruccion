package com.example.appconstruccion.views.avanceObra;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.os.Bundle;
import com.example.appconstruccion.R;
import com.example.appconstruccion.database.DBConstruccion;
import com.example.appconstruccion.views.avanceObra.ListaAvancesActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import android.app.DatePickerDialog;
import android.widget.Toast;
import android.content.Intent;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.util.Log; // Importa la clase Log

public class AvanceObraActivity extends AppCompatActivity {

    EditText editTextFecha;
    EditText editTextPorcentaje;
    EditText editTextComentario;
    Spinner spinnerObras;
    CheckBox checkBoxAprobada;
    CheckBox checkBoxNoAprobada;
    Button buttonGuardar;
    Button botonVerListaAvances;
    DBConstruccion dbHelper;
    SQLiteDatabase db;
    Calendar calendar = Calendar.getInstance();

    public AvanceObraActivity() {
        // Constructor vacío
    }

    public AvanceObraActivity(int id, int obraId, String fecha, double porcentajeAvance, String descripcion, String inspeccionCalidad) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avance_obra);

        // Inicializa las vistas
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextPorcentaje = findViewById(R.id.editTextPorcentaje);
        editTextComentario = findViewById(R.id.editTextComentario);
        spinnerObras = findViewById(R.id.spinnerObras);
        checkBoxAprobada = findViewById(R.id.checkBoxAprobada);
        checkBoxNoAprobada = findViewById(R.id.checkBoxNoAprobada);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        botonVerListaAvances = findViewById(R.id.botonVerListaAvances); // Inicializa el botón

        // Inicializa la conexión a la base de datos
        dbHelper = new DBConstruccion(this);
        db = dbHelper.getReadableDatabase();
        Log.d("AvanceObraActivity", "onCreate - DB is open: " + db.isOpen()); // LOG

        // Carga las obras en el Spinner
        cargarObrasEnSpinner();

        // Establece el OnClickListener para el EditText de la fecha
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        // Establece el OnClickListener para el botón Guardar
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAvanceObra();
            }
        });

        // *** OnClickListener arreglado para el botón "Ver Lista de Avances" ***
        botonVerListaAvances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre de la obra seleccionada del Spinner
                String nombreObraSeleccionada = spinnerObras.getSelectedItem().toString();
                int obraIdSeleccionada = -1;
                Cursor cursorObra = null;
                SQLiteDatabase dbReadable = dbHelper.getReadableDatabase(); // Usa una instancia de lectura

                try {
                    String[] projection = {"id"};
                    String selection = "nombreProyecto = ?";
                    String[] selectionArgs = {nombreObraSeleccionada};
                    Log.d("AvanceObraActivity", "Ver Lista - Nombre Obra Seleccionada: " + nombreObraSeleccionada); // LOG
                    Log.d("AvanceObraActivity", "Ver Lista - DB is open: " + dbReadable.isOpen()); // LOG
                    cursorObra = dbReadable.query("obra", projection, selection, selectionArgs, null, null, null);

                    Log.d("AvanceObraActivity", "Ver Lista - Cursor Obra is null: " + (cursorObra == null)); // LOG
                    if (cursorObra != null && cursorObra.moveToFirst()) {
                        obraIdSeleccionada = cursorObra.getInt(cursorObra.getColumnIndexOrThrow("id"));
                        Log.d("AvanceObraActivity", "Ver Lista - Obra ID encontrado: " + obraIdSeleccionada); // LOG
                    } else {
                        Log.d("AvanceObraActivity", "Ver Lista - No se encontró la obra con el nombre: " + nombreObraSeleccionada); // LOG
                        Toast.makeText(AvanceObraActivity.this, "No se encontró la obra seleccionada", Toast.LENGTH_SHORT).show();
                        return; // Salir si no se encuentra la obra
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AvanceObraActivity.this, "Error al obtener el ID de la obra", Toast.LENGTH_SHORT).show();
                    return;
                } finally {
                    if (cursorObra != null) {
                        cursorObra.close();
                    }
                    dbReadable.close(); // Cierra la instancia de lectura
                }

                if (obraIdSeleccionada != -1) {
                    Intent intent = new Intent(AvanceObraActivity.this, ListaAvancesActivity.class);
                    intent.putExtra("obra_id", obraIdSeleccionada);
                    startActivity(intent);
                } else {
                    Toast.makeText(AvanceObraActivity.this, "Seleccione una obra para ver sus avances", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarObrasEnSpinner() {
        List<String> listaNombresObras = new ArrayList<>();
        Cursor cursor = null;

        try {
            String[] projection = {"nombreProyecto"};
            cursor = db.query(
                    "obra",
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            Log.d("AvanceObraActivity", "cargarObras - DB is open: " + db.isOpen()); // LOG
            Log.d("AvanceObraActivity", "cargarObras - Cursor is null: " + (cursor == null)); // LOG
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nombreObra = cursor.getString(cursor.getColumnIndexOrThrow("nombreProyecto"));
                    listaNombresObras.add(nombreObra);
                    Log.d("AvanceObraActivity", "cargarObras - Nombre Obra en Spinner: " + nombreObra); // LOG
                } while (cursor.moveToNext());
            } else {
                Log.d("AvanceObraActivity", "cargarObras - No se encontraron obras en la base de datos"); // LOG
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaNombresObras
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObras.setAdapter(adapter);
        Log.d("AvanceObraActivity", "cargarObras - Spinner configurado con " + listaNombresObras.size() + " obras"); // LOG
    }

    private void mostrarDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarEditTextFecha();
        };

        new DatePickerDialog(AvanceObraActivity.this,
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

    private void guardarAvanceObra() {

        String fecha = editTextFecha.getText().toString();
        String porcentajeStr = editTextPorcentaje.getText().toString().replace("%", "");
        String descripcion = editTextComentario.getText().toString();
        String nombreObraSeleccionada = spinnerObras.getSelectedItem().toString();
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

        // Obtener el ID de la obra seleccionada desde la base de datos
        int obraId = -1;
        Cursor cursorObra = null;
        try {
            String[] projection = {"id"};
            String selection = "nombreProyecto = ?";
            String[] selectionArgs = {nombreObraSeleccionada};
            Log.d("AvanceObraActivity", "guardarAvanceObra - Nombre Obra Seleccionada: " + nombreObraSeleccionada); // LOG
            Log.d("AvanceObraActivity", "guardarAvanceObra - DB is open: " + db.isOpen()); // LOG
            cursorObra = db.query("obra", projection, selection, selectionArgs, null, null, null);

            Log.d("AvanceObraActivity", "guardarAvanceObra - Cursor Obra is null: " + (cursorObra == null)); // LOG
            if (cursorObra != null) {
                Log.d("AvanceObraActivity", "guardarAvanceObra - Cursor Obra count: " + cursorObra.getCount()); // LOG
                if (cursorObra.moveToFirst()) {
                    obraId = cursorObra.getInt(cursorObra.getColumnIndexOrThrow("id"));
                    Log.d("AvanceObraActivity", "guardarAvanceObra - Obra ID encontrado: " + obraId); // LOG
                } else {
                    Log.d("AvanceObraActivity", "guardarAvanceObra - No se encontró la obra con el nombre: " + nombreObraSeleccionada); // LOG
                }
                cursorObra.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al obtener el ID de la obra", Toast.LENGTH_SHORT).show();
            return;
        } finally {
            if (cursorObra != null) {
                cursorObra.close();
            }
        }

        if (obraId != -1) {
            ContentValues values = new ContentValues();
            values.put("obra_id", obraId);
            values.put("fecha", fecha);
            values.put("porcentaje_avance", porcentajeAvance);
            values.put("descripcion", descripcion);
            values.put("inspeccion_calidad", inspeccionCalidad);

            SQLiteDatabase dbWritable = dbHelper.getWritableDatabase();
            long newRowId = dbWritable.insert("avance_obra", null, values);
            dbWritable.close();

            if (newRowId != -1) {
                Toast.makeText(this, "Registro guardado con éxito", Toast.LENGTH_SHORT).show();
                // Limpiar los campos después de guardar (opcional)
                editTextFecha.setText("");
                editTextPorcentaje.setText("");
                editTextComentario.setText("");
                checkBoxAprobada.setChecked(false);
                checkBoxNoAprobada.setChecked(false);
            } else {
                Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se encontró el ID de la obra seleccionada", Toast.LENGTH_SHORT).show();
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