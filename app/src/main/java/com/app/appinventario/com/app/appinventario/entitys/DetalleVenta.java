package com.app.appinventario.com.app.appinventario.entitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class DetalleVenta implements Serializable {
    private int id_numero_venta;
    private String codigo;
    private String nombre;
    private String imagen;
    private String dato;
    private Bitmap imagenn;
    private int cantdad;
    private float precio_venta;
    private float total;

    public DetalleVenta() {
    }

    public DetalleVenta(String codigo, String nombre, String imagen, int cantdad, float precio_venta, float total) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.imagen = imagen;
        this.cantdad = cantdad;
        this.precio_venta = precio_venta;
        this.total = total;
    }

    public DetalleVenta(String codigo, String nombre, int cantdad, float precio_venta, float total) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantdad = cantdad;
        this.precio_venta = precio_venta;
        this.total = total;
    }

    public int getId_numero_venta() {
        return id_numero_venta;
    }

    public void setId_numero_venta(int id_numero_venta) {
        this.id_numero_venta = id_numero_venta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCantdad() {
        return cantdad;
    }

    public void setCantdad(int cantdad) {
        this.cantdad = cantdad;
    }

    public float getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(float precio_venta) {
        this.precio_venta = precio_venta;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;

        try {
            byte[] byteCode= Base64.decode(dato,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles

            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagenn=Bitmap.createScaledBitmap(foto,alto,ancho,true);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getImagenn() {
        return imagenn;
    }

    public void setImagenn(Bitmap imagenn) {
        this.imagenn = imagenn;
    }
}
