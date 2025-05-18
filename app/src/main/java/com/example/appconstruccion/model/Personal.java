package com.example.appconstruccion.model;

public class Personal {
    private int id;
    private String nombreobra;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String telefono;
    private String tarea;
    private boolean asistencia;

    public Personal(){}
    public Personal(int id, String nombreobra, String cedula, String nombres, String apellidos, String cargo, String telefono, String tarea, boolean asistencia){
        this.id = id;
        this.nombreobra = nombreobra;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.telefono = telefono;
        this.tarea = tarea;
        this.asistencia = asistencia;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNombreobra() {return nombreobra;}
    public void setNombreobra(String nombreobra) {this.nombreobra = nombreobra;}

    public String getCedula() {return cedula;}
    public void setCedula(String cedula) {this.cedula = cedula;}

    public String getNombres() {return nombres;}
    public void setNombres(String nombres) {this.nombres = nombres;}

    public String getApellidos() {return apellidos;}
    public void setApellidos(String apellidos) {this.apellidos = apellidos;}

    public String getCargo() {return cargo;}
    public void setCargo(String cargo) {this.cargo = cargo;}

    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}

    public String getTarea() {return tarea;}
    public void setTarea(String tarea) {this.tarea = tarea;}

    public boolean getAsistencia() {return asistencia;}
    public void setAsistencia(boolean asistencia) {this.asistencia = asistencia;}
}
