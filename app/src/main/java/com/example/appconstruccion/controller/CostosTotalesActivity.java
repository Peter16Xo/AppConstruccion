package com.example.appconstruccion.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appconstruccion.R;
import com.example.appconstruccion.database.DBConstruccion;

public class CostosTotalesActivity extends AppCompatActivity {

    private EditText etPresupuestoTotal, etGastosMateriales, etManoDeObra, etHerramientas;
    private Button btnRegistrarGasto;
    private TextView tvRestante;
    private DBConstruccion dbHelper;
    private long proyectoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costos_totales);  // Asegúrate de que este archivo XML esté correctamente configurado

        // Enlazar las vistas
        etPresupuestoTotal = findViewById(R.id.etPresupuestoTotal);
        etGastosMateriales = findViewById(R.id.etGastosMateriales);
        etManoDeObra = findViewById(R.id.etManoDeObra);
        etHerramientas = findViewById(R.id.etHerramientas);
        btnRegistrarGasto = findViewById(R.id.btnRegistrarGasto);
        tvRestante = findViewById(R.id.tvRestante);

        dbHelper = new DBConstruccion(this);

        // Registrar el presupuesto total y los gastos cuando el usuario haga clic en el botón
        btnRegistrarGasto.setOnClickListener(v -> {
            try {
                // Obtener el presupuesto total ingresado por el usuario
                double presupuesto = Double.parseDouble(etPresupuestoTotal.getText().toString());

                // Crear el proyecto con el presupuesto
                proyectoId = dbHelper.addProyecto(presupuesto);  // Esta función guarda el presupuesto y devuelve el ID del proyecto

                // Obtener los gastos ingresados
                double gastoMateriales = Double.parseDouble(etGastosMateriales.getText().toString());
                double gastoManoDeObra = Double.parseDouble(etManoDeObra.getText().toString());
                double gastoHerramientas = Double.parseDouble(etHerramientas.getText().toString());

                // Insertar los gastos en la base de datos
                dbHelper.addGasto(proyectoId, "Materiales", gastoMateriales);
                dbHelper.addGasto(proyectoId, "Mano de Obra", gastoManoDeObra);
                dbHelper.addGasto(proyectoId, "Herramientas", gastoHerramientas);

                // Calcular la diferencia entre el presupuesto y los gastos
                double totalGastos = dbHelper.getTotalGastos(proyectoId);  // Obtener el total de los gastos registrados
                double totalPresupuesto = dbHelper.getPresupuesto(proyectoId);  // Obtener el presupuesto del proyecto
                double restante = totalPresupuesto - totalGastos;

                // Mostrar el presupuesto restante
                tvRestante.setText("Restante: $" + restante);

            } catch (NumberFormatException e) {
                // Manejo de errores si el usuario no ingresa números válidos
                Toast.makeText(CostosTotalesActivity.this, "Por favor ingrese valores válidos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

