package com.example.tp1_lab3_loginpreferencias.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tp1_lab3_loginpreferencias.R;
import com.example.tp1_lab3_loginpreferencias.ui.registro.RegistroActivity;
import com.example.tp1_lab3_loginpreferencias.ui.registro.ViewModelRegistro;

public class MainActivity extends AppCompatActivity {

    private EditText etUser, etPass;
    private Button btIngrear,btRegistrar;
    private ViewModelMain vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ViewModelMain.class);
        inicializarVistas();
    }

    public void inicializarVistas(){
        etUser= findViewById(R.id.etUsuarioLogin);
        etPass = findViewById(R.id.etContraseñaLogin);
        btIngrear = findViewById(R.id.btIngresar);
        btRegistrar = findViewById(R.id.btRegistrar);

        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(i);

            }
        });

        btIngrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                vm.login(etUser.getText().toString(),etPass.getText().toString());
            }
        });

    }
}