package com.example.appconstruccion.model;

public class CostoTotal {
    private int id;
    private int idObra;
    private double presupuestoTotal;
    private double gastoMateriales;
    private double gastoManoObra;
    private double gastoHerramientas;

    // Constructor completo
    public CostoTotal(int id, int idObra, double presupuestoTotal, double gastoMateriales, double gastoManoObra, double gastoHerramientas) {
        this.id = id;
        this.idObra = idObra;
        this.presupuestoTotal = presupuestoTotal;
        this.gastoMateriales = gastoMateriales;
        this.gastoManoObra = gastoManoObra;
        this.gastoHerramientas = gastoHerramientas;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdObra() {
        return idObra;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }

    public double getPresupuestoTotal() {
        return presupuestoTotal;
    }

    public void setPresupuestoTotal(double presupuestoTotal) {
        this.presupuestoTotal = presupuestoTotal;
    }

    public double getGastoMateriales() {
        return gastoMateriales;
    }

    public void setGastoMateriales(double gastoMateriales) {
        this.gastoMateriales = gastoMateriales;
    }

    public double getGastoManoObra() {
        return gastoManoObra;
    }

    public void setGastoManoObra(double gastoManoObra) {
        this.gastoManoObra = gastoManoObra;
    }

    public double getGastoHerramientas() {
        return gastoHerramientas;
    }

    public void setGastoHerramientas(double gastoHerramientas) {
        this.gastoHerramientas = gastoHerramientas;
    }

    public double getTotalGastado() {
        return gastoMateriales + gastoManoObra + gastoHerramientas;
    }

    public double getDiferencia() {
        return presupuestoTotal - getTotalGastado();
    }
}