package com.example.appconstruccion.model;

public class MaterialUsado {
    private int id;
    private String nombreMaterial;
    private int cantidad;
    private double costoUnidad;
    private String fechaUso;
    private String observaciones;
    private int idObra;

    public MaterialUsado(int id, String nombreMaterial, int cantidad, double costoUnidad, String fechaUso, String observaciones, int idObra) {
        this.id = id;
        this.nombreMaterial = nombreMaterial;
        this.cantidad = cantidad;
        this.costoUnidad = costoUnidad;
        this.fechaUso = fechaUso;
        this.observaciones = observaciones;
        this.idObra = idObra;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreMaterial() { return nombreMaterial; }
    public void setNombreMaterial(String nombreMaterial) { this.nombreMaterial = nombreMaterial; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getCostoUnidad() { return costoUnidad; }
    public void setCostoUnidad(double costoUnidad) { this.costoUnidad = costoUnidad; }

    public String getFechaUso() { return fechaUso; }
    public void setFechaUso(String fechaUso) { this.fechaUso = fechaUso; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public int getIdObra() { return idObra; }
    public void setIdObra(int idObra) { this.idObra = idObra; }
}
