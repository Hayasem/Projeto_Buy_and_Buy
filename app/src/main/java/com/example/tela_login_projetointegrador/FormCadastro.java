package com.example.tela_login_projetointegrador;

import android.graphics.Color;
import android.hardware.biometrics.BiometricPrompt;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.FirebaseKt;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {


    //Objetos que serão utilizados para realização do cadastro:
    private EditText edit_nome,editemail,edit_senha;
    private Button bt_cadastrar;

    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso!"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        iniciarComponentes();

        //Com esse comando,o botão agora consegue "escutar" eventos de click:
        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = edit_nome.getText().toString();
                String email = editemail.getText().toString();
                String senha = edit_senha.getText().toString();

                //Verificação se todos os campos estão preenchidos:
                if(nome.isEmpty()  || email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{

                    cadastrarusuario(view);

                }

            }
        });
    }

    private void cadastrarusuario(View view){
        String email = editemail.getText().toString();
        String senha = edit_senha.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                //O objeto Task é o responsável pelo cadrasto de usuários, ele basicamente vai portar o resultado do cadastro
                if(task.isSuccessful()){

                    SalvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GREEN);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{


                    //Bloco de codigo para apresentar mensagens de erro devido a exceções durante Cadastro:
                    String erro;
                    try {
                        throw task.getException();

                    // Verifica se a senha digitada contém o número de caracteres mínimos:
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "É necessário que a senha possua no mínimo 6 caracteres!";

                    // Verifica se o email digitado já se encontra cadastrado:
                    }catch (FirebaseAuthUserCollisionException e) {
                        erro = "Essa conta já foi cadastrada!";

                    // Verifica se o email digitado está correto:
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email inválido!";

                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário, tente novamente";

                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    //Bloco de código voltado para salvar informações do usuário como nome por exemplo:
    private void SalvarDadosUsuario(){
        String nome = edit_nome.getText().toString();

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);

        //Obter o usuário atual,e conseguirá salvar o id de cada usuario
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = database.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("database", "Sucesso ao salvar os dados");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("database_error", "Erro ao salvar os dados" + e.toString());

                    }
                });
    }
    //Recuperando pelo id:
    private void iniciarComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        editemail = findViewById(R.id.editemail);
        edit_senha = findViewById(R.id.edit_senha);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);

    }
}