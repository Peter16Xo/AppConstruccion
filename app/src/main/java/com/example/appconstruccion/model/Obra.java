package com.example.appconstruccion.model;

public class Obra {
    private int id;
    private String nombreProyecto;
    private String cliente;
    private String ubicacion;
    private String fechaInicio;
    private String fechaFin;

    public Obra() {}

    public Obra(int id, String nombreProyecto, String cliente, String ubicacion, String fechaInicio, String fechaFin) {
        this.id = id;
        this.nombreProyecto = nombreProyecto;
        this.cliente = cliente;
        this.ubicacion = ubicacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreProyecto() { return nombreProyecto; }
    public void setNombreProyecto(String nombreProyecto) { this.nombreProyecto = nombreProyecto; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
}


