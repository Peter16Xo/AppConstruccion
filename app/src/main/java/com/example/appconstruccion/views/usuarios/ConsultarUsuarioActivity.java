package com.example.appconstruccion.views.usuarios;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.controller.UsuarioController;
import com.example.appconstruccion.model.Usuario;

import java.util.List;

public class ConsultarUsuarioActivity extends AppCompatActivity {

    private EditText etBuscarCedula, etNombre, etApellido, etTelefono;
    private UsuarioController controller;
    private Usuario usuarioEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_usuario);

        controller = new UsuarioController(this);

        etBuscarCedula = findViewById(R.id.et_buscar_cedula);
        etNombre = findViewById(R.id.et_nombre_consulta);
        etApellido = findViewById(R.id.et_apellido_consulta);
        etTelefono = findViewById(R.id.et_telefono_consulta);
    }

    public void buscarUsuario(View view) {
        String cedula = etBuscarCedula.getText().toString().trim();
        if (cedula.isEmpty()) {
            Toast.makeText(this, "Ingresa una cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Usuario> lista = controller.obtenerTodos();
        usuarioEncontrado = null;

        for (Usuario u : lista) {
            if (u.getCedula().equals(cedula)) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado != null) {
            etNombre.setText(usuarioEncontrado.getNombre());
            etApellido.setText(usuarioEncontrado.getApellido());
            etTelefono.setText(usuarioEncontrado.getTelefono());
            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
        } else {
            etNombre.setText("");
            etApellido.setText("");
            etTelefono.setText("");
            Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    public void actualizarUsuario(View view) {
        if (usuarioEncontrado == null) {
            Toast.makeText(this, "Busca un usuario primero", Toast.LENGTH_SHORT).show();
            return;
        }

        usuarioEncontrado.setNombre(etNombre.getText().toString());
        usuarioEncontrado.setApellido(etApellido.getText().toString());
        usuarioEncontrado.setTelefono(etTelefono.getText().toString());

        boolean actualizado = controller.agregarUsuario(usuarioEncontrado); // Usa tu método update si lo tienes
        if (actualizado) {
            Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarUsuario(View view) {
        if (usuarioEncontrado == null) {
            Toast.makeText(this, "Busca un usuario primero", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean eliminado = controller.eliminarUsuario(usuarioEncontrado.getId());
        if (eliminado) {
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
            etNombre.setText("");
            etApellido.setText("");
            etTelefono.setText("");
            etBuscarCedula.setText("");
            usuarioEncontrado = null;
        } else {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
        }
    }
}
