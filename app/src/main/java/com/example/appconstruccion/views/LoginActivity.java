package com.example.appconstruccion.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.appconstruccion.views.usuarios.ConsultarUsuarioActivity;
import com.example.appconstruccion.R;
import com.example.appconstruccion.views.usuarios.RegistroUsuarioActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsuario, txtContrasena;
    private CheckBox checkRecordar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.usuario);
        txtContrasena = findViewById(R.id.contrasena);
        checkRecordar = findViewById(R.id.cbox_recordarContrasena);

        leerCredenciales();
    }

    public void validarLogin(View view) {
        String usuario = txtUsuario.getText().toString();
        String contrasena = txtContrasena.getText().toString();

        if (usuario.equals("admin") && contrasena.equals("1234")) {
            if (checkRecordar.isChecked()) {
                guardarCredenciales(usuario, contrasena);
            }
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarCredenciales(String usuario, String clave) {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usuario", usuario);
        editor.putString("clave", clave);
        editor.apply();
    }

    private void leerCredenciales() {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String usuario = prefs.getString("usuario", "");
        String clave = prefs.getString("clave", "");

        txtUsuario.setText(usuario);
        txtContrasena.setText(clave);
    }

    public void registrarUsuario(View view) {
        startActivity(new Intent(this, RegistroUsuarioActivity.class));
    }

    public void consultarUsuario(View view) {
        startActivity(new Intent(this, ConsultarUsuarioActivity.class));
    }


}
