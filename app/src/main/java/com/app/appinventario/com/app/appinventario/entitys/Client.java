package com.app.appinventario.com.app.appinventario.entitys;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Client implements Serializable {
    private int id_cliente;
    private String nombre,direccion,telefono;
    private int estado;

    public Client() {
    }


    public Client(int id_cliente, String nombre, String direccion, String telefono, int estado) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
    }

    public Client(String nombre, String direccion, String telefono, int estado) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
    }

    public Client(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
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
