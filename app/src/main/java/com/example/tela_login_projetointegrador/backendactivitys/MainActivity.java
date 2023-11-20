package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tela_login_projetointegrador.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.*;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    //Código para mudar de telas:
    private TextView text_tela_cadastro;
    private EditText edit_email;
    private EditText edit_senha;
    private Button bt_entrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_main);
        IniciarComponentes();
        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,FormCadastro.class);
                startActivity(intent);

            }
        });

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if(email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    AutenticarUsuario(view);
                }
            }
        });

    }
    private void AutenticarUsuario(View view){

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuScreen();
                        }
                    },2000);
                }else{

                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao realizar o Login, tente novamente";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    //Ciclo de vida:
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            MenuScreen();
        }
    }

    private void MenuScreen(){
        Intent intent = new Intent(MainActivity.this, MenuScreen.class);
        startActivity(intent);
        finish();
    }
    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(com.example.tela_login_projetointegrador.R.id.text_tela_cadastro);
        edit_email = findViewById(com.example.tela_login_projetointegrador.R.id.edit_email);
        edit_senha = findViewById(com.example.tela_login_projetointegrador.R.id.edit_senha);
        bt_entrar = findViewById(com.example.tela_login_projetointegrador.R.id.bt_entrar);
        progressBar = findViewById(R.id.progressBar);
    }
}