package com.app.appinventario.com.app.appinventario.entitys;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Category implements Serializable {
    private int id_categoria;
    private String nombre;
    private int  estado;

    public Category() {
    }

    public Category(int id_categoria, String nombre, int estado) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Category(String nombre) {
        this.nombre = nombre;
    }


    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
