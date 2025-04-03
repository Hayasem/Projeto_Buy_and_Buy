package com.example.tela_login_projetointegrador.Alerts;

import android.app.AlertDialog;
import android.content.Context;

public class DialogUtils {

    public static void showMessage(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("OK", null).show();
    }
}
