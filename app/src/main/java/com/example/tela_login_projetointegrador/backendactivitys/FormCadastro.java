package com.example.tela_login_projetointegrador.backendactivitys;

import android.graphics.Color;
import android.hardware.biometrics.BiometricPrompt;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.tela_login_projetointegrador.R;
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
    private EditText edit_nome,editemail,edit_senha, edit_cep, edit_cpf, edit_contato;
    private Button bt_cadastrar;

    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso!"};
    String usuarioID;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_form_cadastro);
        iniciarComponentes();

        //Com esse comando,o botão agora consegue "escutar" eventos de click:
        //Ou seja, ele pode realizar o cadastro caso todas as informações estejam corretas;
        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = edit_nome.getText().toString();
                String email = editemail.getText().toString();
                String senha = edit_senha.getText().toString();
                String cpf = edit_cpf.getText().toString();
                String cep = edit_cep.getText().toString();
                String contato = edit_contato.getText().toString();

                //Verificação se todos os campos estão preenchidos:
                if(nome.isEmpty()  || email.isEmpty() || senha.isEmpty() || cpf.isEmpty() ||
                cep.isEmpty() || contato.isEmpty()) {
                    exibirSnackbar(mensagens[0], view);
                }else if  (!cpf.matches("\\d{3}.\\d{3}.\\d{3}-\\d{2}")) {
                    exibirSnackbar("CPF inválido! Formato esperado: XXX.XXX.XXX-XX", view);
                } else if (!cep.matches("\\d{5}-\\d{3}")) {
                    exibirSnackbar("CEP inválido! Formato esperado: XXXXX-XXX", view);
                } else if (!contato.matches("(\\d{2}) \\d{9}")) {
                    exibirSnackbar("Número de celular inválido! Formato esperado: (XX) XXXXXXXXX", view);
                }else{
                    cadastrarusuario(view);
                }
            }
        });
    }
    private void exibirSnackbar(String erro, View view) {
        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Adiciona à pilha de fragmentos para permitir voltar
        transaction.commit(); // Confirma a transação
    }

    //Método responsável por cadastrar o usuário;
    private void cadastrarusuario(View view){
        String email = editemail.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                //O objeto Task é o responsável pelo cadastro de usuários, ele basicamente vai portar o resultado do cadastro
                if(task.isSuccessful()){
                    SalvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GREEN);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    openFragment(MenuScreen.newInstance());

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
        String email = editemail.getText().toString();
        String cpf = edit_cpf.getText().toString();
        String cep = edit_cep.getText().toString();
        String contato = edit_contato.getText().toString();

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarios.put("email", email);
        usuarios.put("cpf", cpf);
        usuarios.put("cep", cep);
        usuarios.put("contato", contato);

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
        edit_nome = findViewById(com.example.tela_login_projetointegrador.R.id.edit_nome);
        editemail = findViewById(com.example.tela_login_projetointegrador.R.id.editemail);
        edit_senha = findViewById(com.example.tela_login_projetointegrador.R.id.edit_senha);
        edit_cpf = findViewById(R.id.edit_cpf);
        edit_cep = findViewById(R.id.edit_cep);
        edit_contato = findViewById(R.id.edit_contato);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }
}