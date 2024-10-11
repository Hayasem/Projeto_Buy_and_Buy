package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tela_login_projetointegrador.Format.CepTextWatcher;
import com.example.tela_login_projetointegrador.Format.CpfTextWatcher;
import com.example.tela_login_projetointegrador.Format.EmailTextWatcher;
import com.example.tela_login_projetointegrador.Format.NomeTextWatcher;
import com.example.tela_login_projetointegrador.Format.SenhaTextWatcher;
import com.example.tela_login_projetointegrador.Format.TelefoneTextWatcher;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.TelefoneManager;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.example.tela_login_projetointegrador.model.Telefone;
import com.example.tela_login_projetointegrador.model.Usuario;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


//Classe e objetos que serão utilizados para realização do cadastro:
//--------------------------------------------------------------------------------------------------
public class FormCadastro extends AppCompatActivity {
    private EditText edit_nome, editemail, edit_senha, edit_cep, edit_cpf, edit_numero;
    private TextInputLayout helper_text, layout_nome, layout_contato, layout_email, layout_cep, layout_cpf;
    private Button bt_cadastrar;
    private UserManager userManager;
    private TelefoneManager telefoneManager;
    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso!"};

//--------------------------------------------------------------------------------------------------
//Métodos importantes para iniciar o ciclo de vida da activity:
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_form_cadastro);
        iniciarComponentes();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        acoesClick();

    }
//--------------------------------------------------------------------------------------------------
//Com esse comando,o botão agora consegue "escutar" eventos de click:
//Ou seja, ele pode realizar o cadastro caso todas as informações estejam corretas;
    private void acoesClick() {
        bt_cadastrar.setOnClickListener(view -> {
            String nome = edit_nome.getText().toString();
            String email = editemail.getText().toString();
            String senha = edit_senha.getText().toString();
            String cpf = edit_cpf.getText().toString();
            String cep = edit_cep.getText().toString();
            String numero = edit_numero.getText().toString();

//--------------------------------------------------------------------------------------------------
// Condicional que trabalhará realizando determinadas validações das informações inseridas:
            if (Utils.isCampoVazio(nome) ||
                    Utils.isCampoVazio(email) ||
                    Utils.isCampoVazio(senha) ||
                    Utils.isCampoVazio(cpf) ||
                    Utils.isCampoVazio(cep) ||
                    Utils.isCampoVazio(numero)) {
                exibirSnackbar(mensagens[0], view);
                return;
            }

            if (!Utils.isCPFValido(cpf)) {
                exibirSnackbar("CPF inválido! Formato esperado: XXX.XXX.XXX-XX", view);
                return;
            }

            if (!Utils.isCepValido(cep)) {
                exibirSnackbar("CEP inválido! Formato esperado: XXXXX-XXX", view);
                return;
            }

            if (!Utils.isValidaCelular(numero)) {
                exibirSnackbar("Número de celular inválido! Formato esperado: (XX) XXXXX-XXXX", view);
                return;
            }
            if (!Utils.isEmailvalido(email)) {
                exibirSnackbar("Endereço de email inválido", view);
                return;
            }
//--------------------------------------------------------------------------------------------------
//Condicional que verifica se o email inserido pelo usuário já existe no Banco de Dados:
            userManager.isEmailCadastrado(email, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        exibirSnackbar("Esse email já existe!", view);
                    } else {
                        //Instancia de um novo usuário:
                        Usuario usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        usuario.setCpf(cpf);
                        usuario.setCep(cep);

                        userManager.cadastrarUsuario(usuario, task -> {
                            if (task.isSuccessful()) {
                                Log.i("Cadastro", "Usuário cadastrado com sucesso!");
                                Telefone telefone = new Telefone(Utils.limparTelefone(numero), usuario.getIdUsuario());
                                long result = telefoneManager.cadastrarTelefone(telefone);
                                if (result != -1) {
                                    Log.i("Cadastro", "Telefone cadastrado com sucesso!");
                                } else {
                                    Log.e("Erro", "Erro ao salvar telefone");
                                }
                                exibirSnackbar(mensagens[1], view);
                                // Redirecionar para MainActivity
                                new Handler().postDelayed(() -> {
                                    Intent intent = new Intent(FormCadastro.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }, 1000);
                            } else {
                                // Cadastro falhou
                                Log.e("Erro", "Erro ao cadastrar usuário: " + Objects.requireNonNull(task.getException()).getMessage());
                                exibirSnackbar("Erro ao cadastrar usuário: " + task.getException().getMessage(), view);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Erro", "Erro ao consultar email: " + error.getMessage());
                    exibirSnackbar("Erro ao verificar email: " + error.getMessage(), view);
                }
            });
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
        transaction.replace(R.id.fragmentProdutos, fragment);
        transaction.addToBackStack(null); // Adiciona à pilha de fragmentos para permitir voltar
        transaction.commit(); // Confirma a transação
    }

    //Recuperando pelo id:
    private void iniciarComponentes(){
        layout_nome = findViewById(R.id.input_layout_nome);
        layout_contato = findViewById(R.id.input_layout_telefone);
        layout_email = findViewById(R.id.input_layout_email);
        layout_cep = findViewById(R.id.input_layout_cep);
        layout_cpf = findViewById(R.id.input_layout_cpf);
        layout_contato = findViewById(R.id.input_layout_telefone);
        edit_nome = findViewById(R.id.input_edit_nome);
        editemail = findViewById(R.id.input_edit_email);
        edit_senha = findViewById(R.id.input_edit_senha);
        edit_cpf = findViewById(R.id.input_edit_cpf);
        edit_cep = findViewById(R.id.input_edit_cep);
        edit_numero = findViewById(R.id.input_edit_telefone);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        helper_text = findViewById(R.id.input_layout_senha);
        edit_nome.addTextChangedListener(new NomeTextWatcher(layout_nome));
        edit_numero.addTextChangedListener(new TelefoneTextWatcher(layout_contato));
        edit_cpf.addTextChangedListener(new CpfTextWatcher(layout_cpf));
        edit_cep.addTextChangedListener(new CepTextWatcher(layout_cep));
        edit_senha.addTextChangedListener(new SenhaTextWatcher(helper_text));
        editemail.addTextChangedListener(new EmailTextWatcher(layout_email));
        edit_numero.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }
}