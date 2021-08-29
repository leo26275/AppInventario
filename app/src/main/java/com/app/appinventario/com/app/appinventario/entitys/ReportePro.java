package com.app.appinventario.com.app.appinventario.entitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ReportePro implements Serializable {
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private String codigo;
    private String nombre;
    private float precio_compra;
    private float coste_pro;
    private float ganancia;
    private String imagen;
    private String dato;
    private Bitmap imagenn;

    public ReportePro(String codigo, String nombre, float precio_compra, float coste_pro, float ganancia, String imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio_compra = precio_compra;
        this.coste_pro = coste_pro;
        this.ganancia = ganancia;
        this.imagen = imagen;
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

    public float getPrecio_compra() {
        return Float.parseFloat(decimalFormat.format(precio_compra));
    }

    public void setPrecio_compra(float precio_compra) {
        this.precio_compra = precio_compra;
    }

    public float getCoste_pro() {
        return Float.parseFloat(decimalFormat.format(coste_pro));
    }

    public void setCoste_pro(float coste_pro) {
        this.coste_pro = coste_pro;
    }

    public float getGanancia() {
        return Float.parseFloat(decimalFormat.format(ganancia));
    }

    public void setGanancia(float ganancia) {
        this.ganancia = ganancia;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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
