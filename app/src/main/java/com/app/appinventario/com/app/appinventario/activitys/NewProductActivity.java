package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.MainActivity;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.adapters.AdapterProductImg;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Product;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NewProductActivity extends AppCompatActivity {
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";
    private static final String CARPETA_IMAGEN = "imagenes";
    public Product product;
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;
    File fileImagen;
    Bitmap bitmap;


    TextInputLayout code, name;
    Spinner caterory, provider;
    ImageView imgFoto;
    public static ArrayList<Provider> listaProvider;
    public static ArrayList<Category> listaCategory;
    public static ArrayList<Product> listaProduct;
    public Provider providers;
    public Category categorys;
    public static String url = "http://192.168.0.6:80/AppInventory/InsertarProducto.php";
    public static String url2 = "http://192.168.0.6:80/AppInventory/Product.php";

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private final int MIS_PERMISOS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        name = findViewById(R.id.name);
        caterory = findViewById(R.id.category);
        provider = findViewById(R.id.provider);
        code = findViewById(R.id.code);
        imgFoto = findViewById(R.id.imgFoto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaProduct = new ArrayList<>();
        listaProvider = new ArrayList<>();
        listaCategory = new ArrayList<>();
        fillSpinner();
        showDataProduct();

    }

    public void escanear() {
        IntentIntegrator  intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("ESCANEAR CODIGO");
        intent.setCameraId(0);
        intent.setOrientationLocked(false);
        intent.setBeepEnabled(false);
        intent.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(NewProductActivity.this, "Cancelaste el escaneo", Toast.LENGTH_SHORT).show();
            } else {
                code.getEditText().setText(result.getContents().toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        switch (requestCode) {
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(NewProductActivity.this.getContentResolver(), miPath);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = redimensionarImagen(bitmap,600,800);
                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(NewProductActivity.this, new String[]{path}, null,
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

    private boolean solicitaPermisosVersionesSuperiores() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((NewProductActivity.this.checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&NewProductActivity.this.checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NewProductActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[] {WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(NewProductActivity.this);
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

    public void onSaveProduct(View view) {
        if(code.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Escanea codigo de barra o genera uno", Toast.LENGTH_SHORT).show();
        }else if(exist()){
            Toast.makeText(this, "El codigo ingresado ya existe, por favor ingresar uno nuevo", Toast.LENGTH_SHORT).show();
        }else if(name.getEditText().getText().toString().isEmpty()){
            name.setError("Complete campo nombre");
        }else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            final Category cate = (Category) caterory.getSelectedItem();
            final Provider pro = (Provider) provider.getSelectedItem();
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("registra")) {
                        Toast.makeText(NewProductActivity.this, "Producto registrado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        limpiar();
                    } else {
                        Toast.makeText(NewProductActivity.this, "Producto no se registro" + response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(NewProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String imagen = convertirImgString(bitmap);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", "create");
                    params.put("code", code.getEditText().getText().toString());
                    params.put("name", name.getEditText().getText().toString());
                    params.put("id_categoria", String.valueOf(cate.getId_categoria()));
                    params.put("id_proveedor", String.valueOf(pro.getId_proveedor()));
                    params.put("image", imagen);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(NewProductActivity.this);
            requestQueue.add(request);
        }
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }

    private void limpiar(){
        name.getEditText().setText("");
        code.getEditText().setText("");
        imgFoto.setImageResource(0);
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
                params.put("action", "consult2");
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
                params.put("action", "consult2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void fillSpinner(){
        showDataProvider();
        showDataCategory();
        provider.setAdapter(new ArrayAdapter<>(NewProductActivity.this,android.R.layout.simple_spinner_dropdown_item,listaProvider));
        caterory.setAdapter(new ArrayAdapter<>(NewProductActivity.this,android.R.layout.simple_spinner_dropdown_item,listaCategory));
    }

    public void onCodigo(View view) {
        escanear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.lista){
            Intent intent = new Intent(this, ConsultarProductoActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }else if(id == R.id.salir){
            SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            boolean estado = preferences.getBoolean("estado", false);
            if(!estado) {
                preferences.edit().clear().commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }

        return true;
    }

    public void onImagen(View view) {
        mostrarDialogOpciones();
    }

    public void onCodigoGenerado(View view) {
        if(name.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Para generar el código, es necesario ingresar el nombre", Toast.LENGTH_SHORT).show();
        }else {

            String codeGenerate="";
            char codeG =  name.getEditText().getText().toString().charAt(0);
            String letra1 = String.valueOf(codeG);
            for (Product dato : listaProduct){
                if (dato.getCodigo().charAt(0)==codeG){
                    String cod = dato.getCodigo().substring(1);
                    int nuevo = Integer.parseInt(cod)+1;
                    String nuevocod = letra1.toUpperCase()+ "000"+String.valueOf(nuevo);
                    code.getEditText().setText(nuevocod);
                    break;
                }else {
                    code.getEditText().setText(codeG+"0001");
                }
            }

        }
    }

    public boolean exist(){
        boolean bandera = true;
        for (Product dato : listaProduct){
            if (dato.getCodigo().equals(code.getEditText().getText().toString())){
                bandera =  true;
                break;
            }else{
                bandera = false;
            }
        }
        return bandera;
    }

    public  void showDataProduct(){
        StringRequest request = new StringRequest(Request.Method.POST,url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaProduct.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            String codigo = object.getString("codigo");
                            product = new Product(codigo);
                            listaProduct.add(product);
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
                params.put("action", "consult2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void onCancelar(View view) {
        limpiar();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
