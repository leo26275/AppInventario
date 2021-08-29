package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Product;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.app.appinventario.com.app.appinventario.entitys.VolleySingleton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditProductActivity extends AppCompatActivity {
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";
    private static final String CARPETA_IMAGEN = "imagenes";
    public Product product;
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;
    File fileImagen;
    Bitmap bitmap;
    ProgressDialog pDialog;

    TextInputLayout code, name;
    Spinner caterory, provider;
    ImageView imgFoto;

    public Provider providers;
    public Category categorys;

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private final int MIS_PERMISOS = 100;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    public static String id;

    public static ArrayList<Provider> listaProvider;
    public static ArrayList<Category> listaCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            id = extras.getString("id");

        }
        Toast.makeText(EditProductActivity.this, "id " + id, Toast.LENGTH_LONG).show();

        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        caterory = findViewById(R.id.category);
        provider = findViewById(R.id.provider);
        imgFoto = findViewById(R.id.imgFoto);

        listaProvider = new ArrayList<>();
        listaCategory = new ArrayList<>();
        fillSpinner();
        cargarWebService();
    }

    private void cargarWebService() {
        pDialog=new ProgressDialog(EditProductActivity.this);
        pDialog.setMessage("Cargando...");
        pDialog.show();



        String urls="http://192.168.0.6:80/AppInventory/ConsultarProduc.php?id_producto=" + id;

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, urls, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();

                Product miProduct=new Product();

                JSONArray json=response.optJSONArray("producto");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    miProduct.setCodigo(jsonObject.optString("codigo"));
                    miProduct.setNombre(jsonObject.optString("nombre"));
                    miProduct.setStock(jsonObject.optInt("stock"));
                    miProduct.setImagen(jsonObject.optString("imagen"));
                    miProduct.setId_categoria(jsonObject.optInt("id_categoria"));
                    miProduct.setId_proveedor(jsonObject.optInt("id_proveedor"));
                    miProduct.setEstado(jsonObject.optInt("estado"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                code.getEditText().setText(miProduct.getCodigo());//SE MODIFICA
                name.getEditText().setText(miProduct.getNombre());//SE MODIFICA
                //caterory.setSelection(1);
                // provider.setSelection(miProduct.getId_proveedor());
                System.out.println("Categoria " + miProduct.getId_categoria() +" " + "Proveedor " + miProduct.getId_proveedor());

                String urlImagen="http://192.168.0.6:80/AppInventory/" + miProduct.getImagen();
                cargarWebServiceImagen(urlImagen);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProductActivity.this, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                pDialog.hide();
                Log.d("ERROR: ", error.toString());
            }
        });

        //request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(EditProductActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarWebServiceImagen(String urlImagen) {
        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap=response;//SE MODIFICA
                imgFoto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProductActivity.this,"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
                Log.i("ERROR IMAGEN","Response -> "+error);
            }
        });
        //  request.add(imageRequest);
        VolleySingleton.getIntanciaVolley(EditProductActivity.this).addToRequestQueue(imageRequest);
    }

    public  void showDataProvider(){
        StringRequest request = new StringRequest(Request.Method.POST, NewProviderActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaProvider.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id_proveedor");
                            String nombre = object.getString("nombre");
                            String telefono = object.getString("telefono");
                            String correo = object.getString("correo");
                            String direccion = object.getString("direccion");
                            int estado = object.getInt("estado");

                            providers = new Provider(id, nombre, telefono, correo, direccion, estado);
                            listaProvider.add(providers);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "consult");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public  void showDataCategory(){
        StringRequest request = new StringRequest(Request.Method.POST, NewCategoryActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaCategory.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id_categoria");
                            String nombre = object.getString("nombre");
                            int estado = object.getInt("estado");

                            categorys = new Category(id, nombre, estado);
                            listaCategory.add(categorys);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "consult");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void fillSpinner(){
        showDataProvider();
        showDataCategory();
        provider.setAdapter(new ArrayAdapter<>(EditProductActivity.this,android.R.layout.simple_spinner_dropdown_item,listaProvider));
        caterory.setAdapter(new ArrayAdapter<>(EditProductActivity.this,android.R.layout.simple_spinner_dropdown_item,listaCategory));
    }

    //todo para actualizar

    private void webServiceActualizar() {
        pDialog=new ProgressDialog(EditProductActivity.this);
        pDialog.setMessage("Cargando...");
        pDialog.show();

        String url="http://192.168.0.6:80/AppInventory/UpdateProduct.php";

        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                if (response.trim().equalsIgnoreCase("actualiza")){
                    Toast.makeText(EditProductActivity.this,"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProductActivity.this, ConsultarProductoActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditProductActivity.this,"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProductActivity.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String codigo=code.getEditText().getText().toString();
                String nombre=name.getEditText().getText().toString();
                final Category cate = (Category) caterory.getSelectedItem();
                final Provider pro = (Provider) provider.getSelectedItem();

                String imagen=convertirImgString(bitmap);

                Map<String,String> parametros=new HashMap<>();
                parametros.put("id_producto", id);
                parametros.put("codigo",codigo);
                parametros.put("nombre",nombre);
                parametros.put("id_categoria", String.valueOf(cate.getId_categoria()));
                parametros.put("id_proveedor",String.valueOf(pro.getId_proveedor()));
                parametros.put("imagen", imagen);

                return parametros;
            }
        };
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(EditProductActivity.this).addToRequestQueue(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProductActivity.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    abrirCamara();
                }else{
                    if(opciones[i].equals("Elegir de Galeria")){
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"seleccione"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada=miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }

        if(isCreada==true){
            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString() + ".jpg";

            path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                    + File.separator + nombre;

            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));
            startActivityForResult(intent,COD_FOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(EditProductActivity.this.getContentResolver(), miPath);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = redimensionarImagen(bitmap,600,800);
                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(EditProductActivity.this, new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);
                bitmap = redimensionarImagen(bitmap,600,800);
                break;

        }


    }


    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto=altoNuevo/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);
        }else{
            return bitmap;
        }
    }

    //PERMISOS
    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((EditProductActivity.this.checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&EditProductActivity.this.checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }


        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(EditProductActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT);
                imgFoto.setEnabled(true);//se vincula el evento a la imagen
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(EditProductActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",EditProductActivity.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditProductActivity.this,"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(EditProductActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    public void onImagen(View view) {
        mostrarDialogOpciones();
    }

    public void onSaveProduct(View view) {
        webServiceActualizar();
    }

    public void onCancelar(View view) {
        Intent intent = new Intent(EditProductActivity.this, ConsultarProductoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ConsultarProductoActivity.class);
        startActivity(intent);
    }
}