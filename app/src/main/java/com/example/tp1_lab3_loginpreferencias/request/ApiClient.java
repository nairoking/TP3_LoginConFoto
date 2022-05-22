package com.example.tp1_lab3_loginpreferencias.request;

import android.content.Context;
import android.widget.Toast;

import com.example.tp1_lab3_loginpreferencias.model.Usuario;
import com.example.tp1_lab3_loginpreferencias.ui.registro.RegistroActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ApiClient {

    private static File archivo;
    private static ApiClient api=null;

    private static File getarchivo(Context context){
        if(archivo == null){
            archivo =new File(context.getFilesDir(),"objetos.dat");
        }
        return archivo;
    }

    public static void guardarUsuario(Context context, Usuario u){
        Usuario user=new Usuario( u.getDni(),u.getApellido(), u.getNombre(), u.getMail(), u.getPassword(), u.getFoto());

        File archivo= getarchivo(context);
        try {
            if (!archivo.exists()) {

                archivo.createNewFile();

            }
            //Nodo
            FileOutputStream fo=new FileOutputStream(archivo);
            //Buffer
            BufferedOutputStream bo=new BufferedOutputStream(fo);
            //Convertir de primitivo a byte
            ObjectOutputStream ous=new ObjectOutputStream(bo);

            ous.writeObject(user);
            bo.flush();
            fo.close();



        }catch (IOException ex){
            //Mostrar el error
            Toast.makeText(context,ex.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public static Usuario login (Context context, String mail, String password){
        Usuario usuario = null;
        getarchivo(context);
        usuario = leer(context);
        if(mail.equals(usuario.getMail()) && password.equals(usuario.getPassword())){
            return usuario;
        }


        return null;
    }

    public static Usuario leer(Context context){
         Usuario user = null;
        File archivo= getarchivo(context);

        try {
            if (!archivo.exists()) {

            archivo.createNewFile();

        }
            FileInputStream fi=new FileInputStream(archivo);
            BufferedInputStream bi = new BufferedInputStream(fi);
            ObjectInputStream ois=new ObjectInputStream(bi);

             user=(Usuario) ois.readObject();



            fi.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException ex) {

        }catch(ClassNotFoundException cnf){


        }
        return user;
    }




}
