package com.example.tela_login_projetointegrador.backendactivitys;

import android.database.sqlite.SQLiteDatabase;
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
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.example.tela_login_projetointegrador.model.Entrega;
import com.example.tela_login_projetointegrador.model.Telefone;
import com.example.tela_login_projetointegrador.model.Usuario;
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
    private EditText edit_nome, editemail, edit_senha, edit_cep, edit_cpf, edit_numero;
    private Button bt_cadastrar;
    private UserManager userManager;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso!"};

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_form_cadastro);
        iniciarComponentes();

        DatabaseConnection databaseConnection = new DatabaseConnection(this);
        SQLiteDatabase db = databaseConnection.getWritableDatabase();
        userManager = new UserManager(db);

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
                String numero = edit_numero.getText().toString();

                //Verificação se todos os campos estão preenchidos:
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cpf.isEmpty() ||
                        cep.isEmpty() || numero.isEmpty()) {
                    exibirSnackbar(mensagens[0], view);
                } else if (!cpf.matches("\\d{11}")) {
                    exibirSnackbar("CPF inválido! Formato esperado: XXX.XXX.XXX-XX", view);
                } else if (!cep.matches("\\d{8}")) {
                    exibirSnackbar("CEP inválido! Formato esperado: XXXXX-XXX", view);
                } else if (!numero.matches("\\d{9}")) {
                    exibirSnackbar("Número de celular inválido! Formato esperado: (XX) XXXXXXXXX", view);
                } else {
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    usuario.setCpf(cpf);
                    usuario.setCep(cep);

                    Telefone telefone = new Telefone();
                    telefone.setNumero(numero);

                    boolean autenticado = userManager.autenticarUsuario(email, senha);
                    if (autenticado){
                        Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.GREEN);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                        openFragment(MenuScreen.newInstance());
                    }else{
                        Snackbar snackbar = Snackbar.make(view, "Autenticação falhou. Verifique suas credenciais.", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }

                    userManager.cadastrarUsuario(usuario, telefone);
                }
            }
        });
        userManager = new UserManager(new DatabaseConnection(this).getWritableDatabase());
        Usuario usuarioBuscado = userManager.getUsuario("irineu@gmail.com");
        if (usuarioBuscado != null){
            Log.i("Usuário encontrado", "Nome: " + usuarioBuscado.getNome() +
                    "CPF: " + usuarioBuscado.getCpf());
        }else {
            Log.i("UsuarioNaoEncontrado", "Usuário não encontrado no banco de dados.");
        }
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

    //Recuperando pelo id:
    private void iniciarComponentes(){
        edit_nome = findViewById(com.example.tela_login_projetointegrador.R.id.edit_nome);
        editemail = findViewById(com.example.tela_login_projetointegrador.R.id.editemail);
        edit_senha = findViewById(com.example.tela_login_projetointegrador.R.id.edit_senha);
        edit_cpf = findViewById(R.id.edit_cpf);
        edit_cep = findViewById(R.id.edit_cep);
        edit_numero = findViewById(R.id.edit_contato);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }
}