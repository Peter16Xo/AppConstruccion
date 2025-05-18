package com.example.appconstruccion.views.avanceObra;

public class AvanceObraData {
    private int id;
    private int obraId;
    private String fecha;
    private double porcentajeAvance;
    private String descripcion;
    private String inspeccionCalidad;

    public AvanceObraData(int id, int obraId, String fecha, double porcentajeAvance, String descripcion, String inspeccionCalidad) {
        this.id = id;
        this.obraId = obraId;
        this.fecha = fecha;
        this.porcentajeAvance = porcentajeAvance;
        this.descripcion = descripcion;
        this.inspeccionCalidad = inspeccionCalidad;
    }

    public int getId() {
        return id;
    }

    public int getObraId() {
        return obraId;
    }

    public String getFecha() {
        return fecha;
    }

    public double getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getInspeccionCalidad() {
        return inspeccionCalidad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setPorcentajeAvance(double porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setInspeccionCalidad(String inspeccionCalidad) {
        this.inspeccionCalidad = inspeccionCalidad;
    }
}
