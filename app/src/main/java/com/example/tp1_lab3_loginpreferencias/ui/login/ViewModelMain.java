package com.example.tp1_lab3_loginpreferencias.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tp1_lab3_loginpreferencias.model.Usuario;
import com.example.tp1_lab3_loginpreferencias.request.ApiClient;
import com.example.tp1_lab3_loginpreferencias.ui.registro.RegistroActivity;

public class ViewModelMain extends AndroidViewModel {
    private Context context;
    private Usuario u;
    private MutableLiveData<Usuario> usuario;

    public ViewModelMain(@NonNull Application application) {

        super(application);
        context = application.getApplicationContext();
    }

    public void login(String user, String pass){
       u = ApiClient.login(context,user,pass);
       if(u!=null){
           Intent i = new Intent(context, RegistroActivity.class);
           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(i);
       }else{
           Toast.makeText(context, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
       }


    }


}
