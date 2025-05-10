package com.example.appconstruccion.views.usuarios;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.UsuarioController;
import com.example.appconstruccion.model.Usuario;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private EditText etCedula, etNombre, etApellido, etTelefono;
    private UsuarioController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        controller = new UsuarioController(this);

        etCedula = findViewById(R.id.et_cedula);
        etNombre = findViewById(R.id.et_nombre);
        etApellido = findViewById(R.id.et_apellido);
        etTelefono = findViewById(R.id.et_telefono);
    }

    public void guardarUsuario(View view) {
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(0, cedula, nombre, apellido, telefono);
        boolean guardado = controller.agregarUsuario(usuario);

        if (guardado) {
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            etCedula.setText("");
            etNombre.setText("");
            etApellido.setText("");
            etTelefono.setText("");
        } else {
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
        }
    }
}
