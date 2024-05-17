package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private EditText edit_email;
    private EditText edit_senha;
    private Button bt_entrar;
    private UserManager userManager;
    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso!"};


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseConnection databaseConnection = new DatabaseConnection(this);
        SQLiteDatabase db =  databaseConnection.getWritableDatabase();
        userManager = new UserManager(db);

        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_main);

        IniciarComponentes();
        acoesClick();
    }

    private void acoesClick() {
        text_tela_cadastro.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,FormCadastro.class);
            startActivity(intent);
        });

        bt_entrar.setOnClickListener(view -> {

            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                boolean loginValido = userManager.compararSenha(email, senha);
                if (loginValido){

                    userManager.setUserLogado(email);
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GREEN);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                    }, 1000);
                }else{
                    Snackbar snackbar = Snackbar.make(view, "Email ou senha incorretos.", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }


    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(com.example.tela_login_projetointegrador.R.id.text_tela_cadastro);
        edit_email = findViewById(com.example.tela_login_projetointegrador.R.id.edit_email);
        edit_senha = findViewById(com.example.tela_login_projetointegrador.R.id.edit_senha);
        bt_entrar = findViewById(com.example.tela_login_projetointegrador.R.id.bt_entrar);
    }
}