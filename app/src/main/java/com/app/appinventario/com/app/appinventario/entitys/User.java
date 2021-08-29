package com.app.appinventario.com.app.appinventario.entitys;

import java.io.Serializable;

public class User implements Serializable {
    private int id_usuario;
    private String usuario, password, correo;
    private int  estado;

    public User() {
    }

    public User(int id_usuario, String usuario, String password, String correo, int estado) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.password = password;
        this.correo = correo;
        this.estado = estado;
    }

    public User(int id_usuario, String usuario, String password, String correo) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.password = password;
        this.correo = correo;
    }

    public User(String usuario, String password, String correo) {
        this.usuario = usuario;
        this.password = password;
        this.correo = correo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
