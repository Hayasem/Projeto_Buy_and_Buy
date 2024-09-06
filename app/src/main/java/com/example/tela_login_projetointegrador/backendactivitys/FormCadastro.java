package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tela_login_projetointegrador.Format.CepTextWatcher;
import com.example.tela_login_projetointegrador.Format.CpfTextWatcher;
import com.example.tela_login_projetointegrador.Format.EmailTextWatcher;
import com.example.tela_login_projetointegrador.Format.SenhaTextWatcher;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.TelefoneManager;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.example.tela_login_projetointegrador.model.Telefone;
import com.example.tela_login_projetointegrador.model.Usuario;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class FormCadastro extends AppCompatActivity {
    private AppCompatActivity binding;
    //Objetos que serão utilizados para realização do cadastro:
    private EditText edit_nome, editemail, edit_senha, edit_cep, edit_cpf, edit_numero;
    private TextInputLayout helper_text, layout_nome, layout_contato, layout_email, layout_cep, layout_cpf;
    private Button bt_cadastrar;
    private DatabaseConnection db;
    private UserManager userManager;
    private TelefoneManager telefoneManager;
    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso!"};

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
        telefoneManager = new TelefoneManager(db);
        acoesClick();

    }

    private void acoesClick() {
        //Com esse comando,o botão agora consegue "escutar" eventos de click:
        //Ou seja, ele pode realizar o cadastro caso todas as informações estejam corretas;
        bt_cadastrar.setOnClickListener(view -> {
            String nome = edit_nome.getText().toString();
            String email = editemail.getText().toString();
            String senha = edit_senha.getText().toString();
            String cpf = edit_cpf.getText().toString();
            String cep = edit_cep.getText().toString();
            String numero = edit_numero.getText().toString();


            if (Utils.isCampoVazio(nome) ||
                    Utils.isCampoVazio(email) ||
                    Utils.isCampoVazio(senha) ||
                    Utils.isCampoVazio(cpf) ||
                    Utils.isCampoVazio(cep) ||
                    Utils.isCampoVazio(numero)){
                exibirSnackbar(mensagens[0], view);
                return;
            }

            if (!Utils.isCPFValido(cpf)) {
                exibirSnackbar("CPF inválido! Formato esperado: XXX.XXX.XXX-XX", view);
                return;
            }

            if(!Utils.isCepValido(cep)) {
                exibirSnackbar("CEP inválido! Formato esperado: XXXXX-XXX", view);
                return;
            }

            if(!Utils.isValidaCelular(numero)) {
                exibirSnackbar("Número de celular inválido! Formato esperado: (XX) XXXXX-XXXX", view);
                return;
            }
            if (!Utils.isEmailvalido(email)){
                exibirSnackbar("Endereço de email inválido", view);
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setCpf(cpf);
            usuario.setCep(cep);

            long usuarioId = userManager.cadastrarUsuario(usuario);
            if (usuarioId != -1){
                Log.i("Cadastro do usuario realizado com sucesso!", "O usuário foi cadastrado!");
                Telefone telefone = new Telefone(Utils.limparTelefone(numero),usuarioId);
                long result = telefoneManager.cadastrarTelefone(telefone);
                if (result  != -1){
                    Log.i("Cadastro do telefone realizado com sucesso!", "O telefone foi cadastrado!");
                }else{
                    Log.i("Erro ao salvar telefone", "Erro ao salvar telefone");
                }
            }
            boolean autenticado = userManager.compararSenha(email, senha);
            if (autenticado){
                Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.GREEN);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Intent intent = new Intent(FormCadastro.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }, 1000);

            }else{
                Snackbar snackbar = Snackbar.make(view, "Autenticação falhou. Verifique suas credenciais.", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
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
        transaction.replace(R.id.fragmentProdutos, fragment);
        transaction.addToBackStack(null); // Adiciona à pilha de fragmentos para permitir voltar
        transaction.commit(); // Confirma a transação
    }

    //Recuperando pelo id:
    private void iniciarComponentes(){
        layout_nome = findViewById(R.id.input_layout_nome);
        layout_contato = findViewById(R.id.input_layout_telefone);
        layout_email = findViewById(R.id.input_layout_email);
        edit_nome = findViewById(R.id.input_edit_nome);
        editemail = findViewById(R.id.input_edit_email);
        edit_senha = findViewById(R.id.input_edit_senha);
        edit_cpf = findViewById(R.id.input_edit_cpf);
        edit_cep = findViewById(R.id.input_edit_cep);
        edit_numero = findViewById(R.id.input_edit_telefone);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        helper_text = findViewById(R.id.input_layout_senha);
        db = new DatabaseConnection(this);
        edit_cpf.addTextChangedListener(new CpfTextWatcher());
        edit_cep.addTextChangedListener(new CepTextWatcher());
        edit_senha.addTextChangedListener(new SenhaTextWatcher(helper_text));
        editemail.addTextChangedListener(new EmailTextWatcher(layout_email));
        edit_numero.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }
}