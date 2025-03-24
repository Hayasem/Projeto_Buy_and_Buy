package com.example.tela_login_projetointegrador.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPassActivity extends AppCompatActivity {

    private Button btn_enviar_senha, btn_voltar;
    private EditText edit_email;
    private ProgressBar progressBar2;
    private FirebaseAuth mAuth;
    private String strEmail;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_senha);


    btn_enviar_senha = findViewById(R.id.enviar_senha_nova);
    btn_voltar = findViewById(R.id.botao_voltar);
    edit_email = findViewById(R.id.input_edit_email);
    progressBar2 = findViewById(R.id.progressBar2);

    mAuth = FirebaseAuth.getInstance();

    btn_enviar_senha.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            strEmail = edit_email.getText().toString().trim();
            if (strEmail.isEmpty()){
                edit_email.setError("Insira algum email no campo!");
                return;
            }
            if (!Utils.isEmailvalido(strEmail)) {
                edit_email.setError("Formato de email inválido!");
                return;
            }
            resetPassword();
        }
    });
    btn_voltar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getOnBackPressedDispatcher().onBackPressed();
        }
    });

        }
    private void resetPassword(){
        progressBar2.setVisibility(View.VISIBLE);
        btn_enviar_senha.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
            public void onSuccess(Void unused) {
                Toast.makeText(RecoverPassActivity.this, "O link para recriação de sua senha lhe foi enviado em seu email registrado!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecoverPassActivity.this, MainActivity.class);
                startActivity(intent);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RecoverPassActivity.this, "Erro: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.INVISIBLE);
                        btn_enviar_senha.setVisibility(View.VISIBLE);
                    }
                });
    }
}