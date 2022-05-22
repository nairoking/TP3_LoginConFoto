package com.example.tp1_lab3_loginpreferencias.ui.registro;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp1_lab3_loginpreferencias.R;
import com.example.tp1_lab3_loginpreferencias.model.Usuario;

import java.io.File;
import java.io.IOException;



public class RegistroActivity extends AppCompatActivity {

    private Context context;
    private TextView etNombre;
    private TextView etApellido, etDni,etEmail,etPass, tvRuta;
    private Button btGuardar, btTomarFoto;
    private ViewModelRegistro vm;
    private ImageView ivFoto;
    private static int REQUEST_IMAGE_CAPTURE=1;
    private String rutaFoto = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ViewModelRegistro.class);


        vm.getUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if(usuario == null){
                    etNombre.setText("");
                    etApellido.setText("");
                    etDni.setText("");
                    etEmail.setText("");
                    etPass.setText("");
                }else{
                    etNombre.setText(usuario.getNombre()+"");
                    etApellido.setText(usuario.getApellido()+"");
                    etDni.setText(usuario.getDni()+"");
                    etEmail.setText(usuario.getMail()+"");
                    etPass.setText(usuario.getPassword()+"");
                    tvRuta.setText(usuario.getFoto()+"");

                    //Cargar la foto
                    Bitmap imageBitmap = BitmapFactory.decodeFile(usuario.getFoto());
                    ivFoto.setImageBitmap(imageBitmap);

                }

            }
        });
        vm.leerDatos();
        inicializarVista();

    }

    public void inicializarVista(){
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etDni = findViewById(R.id.etDni);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btGuardar = findViewById(R.id.button);
        btTomarFoto = findViewById(R.id.btTomarFoto);
        ivFoto = findViewById(R.id.ivFoto);
        tvRuta = findViewById(R.id.tvRuta);


        btTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                    // guardar la foto y guarda la ruta en la variable
                    File imagenArchivo = null;
                    imagenArchivo = crearImagen("foto1");
                    if (imagenArchivo != null){
                        Uri fotoUri = FileProvider.getUriForFile(RegistroActivity.this,"com.example.tp1_lab3_loginpreferencias", imagenArchivo );
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                        startActivityForResult(takePictureIntent,1);
                    }


                }
            }
        });
        validaPermisos();



        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // agregar el link q esta en la variable

                Usuario u= new Usuario(etDni.getText().toString(),etApellido.getText().toString(),etNombre.getText().toString(),etEmail.getText().toString(),etPass.getText().toString(), rutaFoto);
                vm.guardarDatos(u);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK ){
            Bitmap imageBitmap = BitmapFactory.decodeFile(rutaFoto);
            ivFoto.setImageBitmap(imageBitmap);
        }


        // vm.respuetaDeCamara(requestCode,resultCode,data,REQUEST_IMAGE_CAPTURE);
    }



    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){

            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RegistroActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistroActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{CAMERA},100);
            }
        });
        dialogo.show();
    }

    public File crearImagen(String nombre){

        try {
            String nombreImagen = nombre;
            File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imagen = File.createTempFile(nombreImagen,".jpg",directorio);

            rutaFoto = imagen.getAbsolutePath();
            return imagen;

        }catch(IOException e){
            Toast.makeText(this,"Error metodo crear imagen",Toast.LENGTH_LONG).show();
        }

        return null;
    }
}



