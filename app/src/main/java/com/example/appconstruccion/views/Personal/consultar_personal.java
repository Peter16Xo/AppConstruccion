package com.example.appconstruccion.views.Personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.ObraController;
import com.example.appconstruccion.controller.PersonalController;
import com.example.appconstruccion.model.Personal;

import java.util.List;

public class consultar_personal extends AppCompatActivity {

    private EditText txtCedula, txtNombres, txtApellidos, txtCargo, txtTelefono, txtTarea_especifica;
    private Personal personalEncontrado;
    private Spinner spinnerObras;
    private CheckBox chkAsistencia;
    private PersonalController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_personal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = new PersonalController(this);

        txtCedula = findViewById(R.id.consultarP_txtCedula);
        txtNombres = findViewById(R.id.consultarP_txtNombres);
        txtApellidos = findViewById(R.id.consultarP_txtApellidos);
        txtCargo = findViewById(R.id.consultarP_txtCargo);
        txtTelefono = findViewById(R.id.consultarP_txtTelefono);
        txtTarea_especifica = findViewById(R.id.consultarP_txtTarea);

        // Se usa el ObraController para llenar el spinner
        ObraController obraController = new ObraController(this);
        List<String> listaObras = obraController.getListaObras();
        if (listaObras.isEmpty()) {
            listaObras.add("No hay obras registradas");
        }
        spinnerObras = findViewById(R.id.consultarP_spnObras);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaObras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObras.setAdapter(adapter);

        chkAsistencia = findViewById(R.id.consultarP_chkAsistencia);
    }

    private boolean soloLetras(String texto) {
        return texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public void buscarPersonal(View view){
        String cedula = txtCedula.getText().toString().trim();
        if (cedula.isEmpty()){
            Toast.makeText(this, "Ingresa una cédula", Toast.LENGTH_LONG).show();
            return;
        }

        List<Personal> lista = controller.obtenerTodas();
        personalEncontrado = null;

        for (Personal u : lista){
            if(u.getCedula().equals(cedula)){
                personalEncontrado = u;
                break;
            }
        }

        if (personalEncontrado != null) {
            // Seleccionar la obra en el spinner
            String obraNombre = personalEncontrado.getNombreobra();
            ArrayAdapter adapter = (ArrayAdapter) spinnerObras.getAdapter();
            int position = adapter.getPosition(obraNombre);
            if (position >= 0) {
                spinnerObras.setSelection(position);
            }

            txtNombres.setText(personalEncontrado.getNombres());
            txtApellidos.setText(personalEncontrado.getApellidos());
            txtCargo.setText(personalEncontrado.getCargo());
            txtTelefono.setText(personalEncontrado.getTelefono());
            txtTarea_especifica.setText(personalEncontrado.getTarea());

            // Marcar checkbox si corresponde
            chkAsistencia.setChecked(personalEncontrado.getAsistencia());
            Toast.makeText(this, "Personal encontrado", Toast.LENGTH_SHORT).show();
        } else {
            txtNombres.setText("");
            txtApellidos.setText("");
            txtCargo.setText("");
            txtTelefono.setText("");
            txtTarea_especifica.setText("");
            Toast.makeText(this, "No se encontró al personal", Toast.LENGTH_SHORT).show();
        }
    }

    public void actualizarPersonal(View view){
        if (personalEncontrado == null) {
            Toast.makeText(this, "Busca al personal primero", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombres = txtNombres.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String cargo = txtCargo.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String tarea = txtTarea_especifica.getText().toString().trim();

        if (nombres.isEmpty() || apellidos.isEmpty() || cargo.isEmpty() || telefono.isEmpty() || tarea.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!soloLetras(nombres) || !soloLetras(apellidos) || !soloLetras(cargo) || !soloLetras(tarea)) {
            Toast.makeText(this, "Nombres, apellidos, cargo y tarea solo deben contener letras", Toast.LENGTH_SHORT).show();
            return;
        }

        personalEncontrado.setNombreobra(spinnerObras.getSelectedItem().toString());
        personalEncontrado.setNombres(txtNombres.getText().toString());
        personalEncontrado.setApellidos(txtApellidos.getText().toString());
        personalEncontrado.setCargo(txtCargo.getText().toString());
        personalEncontrado.setTelefono(txtTelefono.getText().toString());
        personalEncontrado.setTarea(txtTarea_especifica.getText().toString());
        personalEncontrado.setAsistencia(chkAsistencia.isChecked());


        boolean actualizar = controller.actualizarPersonal(personalEncontrado);
        if (actualizar){
            Toast.makeText(this, "Personal actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarPersonal(View view) {
        if (personalEncontrado == null) {
            Toast.makeText(this, "Busca al personal primero", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    boolean eliminar = controller.eliminarPersonal(personalEncontrado.getId());
                    if (eliminar) {
                        Toast.makeText(this, "Personal eliminado", Toast.LENGTH_SHORT).show();
                        txtCedula.setText("");
                        txtNombres.setText("");
                        txtApellidos.setText("");
                        txtCargo.setText("");
                        txtTelefono.setText("");
                        txtTarea_especifica.setText("");
                        chkAsistencia.setChecked(false);
                        spinnerObras.setSelection(0); // Opcional: reinicia el spinner
                    } else {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null) // No hace nada si el usuario cancela
                .show();
    }
}