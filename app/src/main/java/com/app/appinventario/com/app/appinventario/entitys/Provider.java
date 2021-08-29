package com.app.appinventario.com.app.appinventario.entitys;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Provider implements Serializable {
    private int id_proveedor;
    private String nombre, telefono, correo, direccion;
    private int  estado;

    public Provider() {

    }

    public Provider(int id_proveedor, String nombre, String telefono, String correo, String direccion) {
        this.id_proveedor = id_proveedor;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Provider(String nombre, String telefono, String correo, String direccion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public Provider(int id_proveedor, String nombre, String telefono, String correo, String direccion, int estado) {
        this.id_proveedor = id_proveedor;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre;
    }
}
