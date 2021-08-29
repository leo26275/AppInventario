package com.app.appinventario.com.app.appinventario.entitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class Product implements Serializable {
    private int id_producto;
    private String codigo;
    private String nombre;
    private int stock;
    private String imagen;
    private String dato;
    private Bitmap imagenn;
    private int id_categoria;
    private int id_proveedor;
    private  int estado;
    private Category category;
    private Provider provider;
    private String newcategoria;
    private String newproveedor;

    public String getNewcategoria() {
        return newcategoria;
    }

    public void setNewcategoria(String newcategoria) {
        this.newcategoria = newcategoria;
    }

    public String getNewproveedor() {
        return newproveedor;
    }

    public void setNewproveedor(String newproveedor) {
        this.newproveedor = newproveedor;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Product() {
    }

    public Product(String codigo) {
        this.codigo = codigo;
    }

    public Product(int id_producto, int estado) {
        this.id_producto = id_producto;
        this.estado = estado;
    }

    public Product(int id_producto, String codigo, String nombre, int stock, String imagen, int id_categoria, int id_proveedor, int estado) {
        this.id_producto = id_producto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.imagen = imagen;
        this.id_categoria = id_categoria;
        this.id_proveedor = id_proveedor;
        this.estado = estado;
    }

    public Product(String codigo, String nombre, int stock, String imagen, int id_categoria, int id_proveedor, int estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.imagen = imagen;
        this.id_categoria = id_categoria;
        this.id_proveedor = id_proveedor;
        this.estado = estado;
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

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
