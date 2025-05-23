package com.example.appconstruccion.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appconstruccion.R;
import com.example.appconstruccion.views.avanceObra.AvanceObraActivity;
import com.example.appconstruccion.views.costosTotales.ListaCostosTotalesActivity;
import com.example.appconstruccion.views.materialUsado.AgregarMaterialUsadoActivity;
import com.example.appconstruccion.views.materialUsado.ListaMaterialUsadoActivity;
import com.example.appconstruccion.views.obra.ListaObrasActivity;
import com.example.appconstruccion.views.obra.RegistroObraActivity;
import com.example.appconstruccion.views.Personal.RegistroPersonalActivity;



public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Activar Toolbar personalizado
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void irARegistrarObra(View view) {
        Intent intent = new Intent(this, RegistroObraActivity.class);
        startActivity(intent);
    }

    public void irAAvanceObra(View view) {
        Intent intent = new Intent( this, AvanceObraActivity.class);
        startActivity(intent);
    }

    public void irAMateriales(View view) {
        Intent intent = new Intent(this, AgregarMaterialUsadoActivity.class);
        startActivity(intent);
    }

    public void irAPersonal(View view) {
        Intent ventanaPersonal = new Intent(this, RegistroPersonalActivity.class);
        startActivity(ventanaPersonal);
        Toast.makeText(this, "Abrir módulo Personal Asignado", Toast.LENGTH_SHORT).show();
    }

    public void irACostos(View view) {
        startActivity(new Intent(this, ListaCostosTotalesActivity.class));
    }

    public void irAListaObras(View view) {
        startActivity(new Intent(this, ListaObrasActivity.class));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnOption_acercaDe) {
            Toast.makeText(this, "Acerca de BUILD GEST", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.mnOption_ayuda) {
            Toast.makeText(this, "Ayuda no disponible aún", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.mnOption_configuracion) {
            Toast.makeText(this, "Configuración en desarrollo", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
