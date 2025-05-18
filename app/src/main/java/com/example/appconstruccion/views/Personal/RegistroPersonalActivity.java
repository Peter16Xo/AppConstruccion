package com.example.appconstruccion.views.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.ObraController;
import com.example.appconstruccion.controller.PersonalController;
import com.example.appconstruccion.model.Personal;

import java.util.List;

public class RegistroPersonalActivity extends AppCompatActivity {

    private EditText txtCedula, txtNombres, txtApellidos, txtCargo, txtTelefono, txtTarea_especifica;
    private Spinner spinnerObras;
    private CheckBox chkAsistencia;
    private PersonalController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_personal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = new PersonalController(this);

        txtCedula = findViewById(R.id.personal_txtCedula);
        txtNombres = findViewById(R.id.personal_txtNombres);
        txtApellidos = findViewById(R.id.personal_txtApellidos);
        txtCargo = findViewById(R.id.personal_txtCargo);
        txtTelefono = findViewById(R.id.personal_txtTelefono);
        txtTarea_especifica = findViewById(R.id.personal_txtTarea);

        // Se usa el ObraController para llenar el spinner
        ObraController obraController = new ObraController(this);
        List<String> listaObras = obraController.getListaObras();
        if (listaObras.isEmpty()) {
            listaObras.add("No hay obras registradas");
        }
        spinnerObras = findViewById(R.id.personal_spnObras);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaObras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObras.setAdapter(adapter);

        chkAsistencia = findViewById(R.id.personalA_chkAsistencia);
    }

    private boolean soloLetras(String texto) {
        return texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public void guardarPersonal(View view){
        String nombreobra = spinnerObras.getSelectedItem().toString();
        String cedula = txtCedula.getText().toString().trim();
        String nombres = txtNombres.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String cargo = txtCargo.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String tarea = txtTarea_especifica.getText().toString().trim();
        boolean asistencia = chkAsistencia.isChecked();

        if (nombreobra.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || cargo.isEmpty() || telefono.isEmpty() || tarea.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!soloLetras(nombres) || !soloLetras(apellidos) || !soloLetras(cargo) || !soloLetras(tarea)) {
            Toast.makeText(this, "Nombres, apellidos, cargo y tarea solo deben contener letras", Toast.LENGTH_SHORT).show();
            return;
        }

        Personal personal = new Personal(0, nombreobra, cedula, nombres, apellidos, cargo, telefono, tarea, asistencia);
        boolean resultado = controller.agregarPersonal(personal);

        if (resultado) {
            Toast.makeText(this, "Personal guardado correctamente", Toast.LENGTH_SHORT).show();
            txtCedula.setText("");
            txtNombres.setText("");
            txtApellidos.setText("");
            txtCargo.setText("");
            txtTelefono.setText("");
            txtTarea_especifica.setText("");
        } else {
            Toast.makeText(this, "Error al guardar el personal. Verifica la tabla en DB o los campos vacíos.", Toast.LENGTH_LONG).show();
            Log.e("DB_ERROR", "Falló el insert. Datos: " + personal.getNombres() + ", " + personal.getCedula());
        }
    }

    public void buscarPersonal(View view){
        Intent consultarPersonal = new Intent(this, consultar_personal.class);
        startActivity(consultarPersonal);
    }
}