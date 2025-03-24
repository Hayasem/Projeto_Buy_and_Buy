package com.example.tela_login_projetointegrador.activities;

import android.content.Intent;
import android.graphics.Color;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tela_login_projetointegrador.Formats.CepTextWatcher;
import com.example.tela_login_projetointegrador.Formats.CpfTextWatcher;
import com.example.tela_login_projetointegrador.Formats.EmailTextWatcher;
import com.example.tela_login_projetointegrador.Formats.NomeTextWatcher;
import com.example.tela_login_projetointegrador.Formats.SenhaTextWatcher;
import com.example.tela_login_projetointegrador.Formats.TelefoneTextWatcher;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.Telefone;
import com.example.tela_login_projetointegrador.models.Usuario;
import com.example.tela_login_projetointegrador.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//Classe e objetos que serão utilizados para realização do cadastro:
//--------------------------------------------------------------------------------------------------
public class SingUpActivity extends AppCompatActivity {
    private EditText edit_nome, editemail, edit_senha, edit_cep, edit_cpf, edit_numero;
    private TextInputLayout helper_text, layout_nome, layout_contato, layout_email, layout_cep, layout_cpf;
    private Button bt_cadastrar;
    private FirebaseAuth auth;
    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso!"};

//--------------------------------------------------------------------------------------------------
//Métodos importantes para iniciar o ciclo de vida da activity:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_form_cadastro);
        iniciarComponentes();
        auth = FirebaseAuth.getInstance();
        acoesClick();

    }
    private void acoesClick() {
        bt_cadastrar.setOnClickListener(this::criarUsuario);

    }

    private void exibirSnackbar(String erro, View view) {
        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void criarUsuario(View view){
        iniciarComponentes();
        String nome = edit_nome.getText().toString();
        String email = editemail.getText().toString();
        String senha = edit_senha.getText().toString();
        String cpf = edit_cpf.getText().toString();
        String cep = edit_cep.getText().toString();
        String contato = edit_numero.getText().toString();

        if (Utils.isCampoVazio(nome) || Utils.isCampoVazio(email) ||
            Utils.isCampoVazio(senha) || Utils.isCampoVazio(cpf) ||
            Utils.isCampoVazio(cep) || Utils.isCampoVazio(contato)) {
            exibirSnackbar(mensagens[0], view);
            return;
        }

        if (!Utils.isCPFValido(cpf)) {
            exibirSnackbar("CPF inválido", view);
            return;
        }

        if (!Utils.isCepValido(cep)) {
            exibirSnackbar("CEP inválido! Formato esperado: XXXXX-XXX", view);
            return;
        }

        if (!Utils.isValidaCelular(contato)) {
            exibirSnackbar("Número de celular inválido! Formato esperado: (XX) XXXXX-XXXX", view);
            return;
        }
        if (!Utils.isEmailvalido(email)) {
            exibirSnackbar("Endereço de email inválido", view);
            return;
        }
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                                if (emailTask.isSuccessful()) {
                                    Toast.makeText(SingUpActivity.this, "Verifique seu e-mail para validar a conta.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                    String userId = user.getUid();
                                    DatabaseReference telefoneRef = FirebaseDatabase.getInstance().getReference("telefones");
                                    String telefoneId = telefoneRef.push().getKey();
                                    String hashSenha = Utils.gerarHashSenha(senha, Utils.gerarSalt());
                                    DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);

                                    Usuario usuario = new Usuario(
                                            userId, telefoneId, nome, cpf, email, senha, cep, hashSenha, Utils.gerarSalt(), Utils.obterDataHoraAtual());
                                    usuarioRef.setValue(usuario);

                                    DatabaseReference carrinhoRef = usuarioRef.child("carrinho");
                                    carrinhoRef.setValue("");

                                    Telefone telefone = new Telefone(telefoneId, userId, contato);
                                    assert telefoneId != null;
                                    telefoneRef.child(telefoneId).setValue(telefone);

                                } else {
                                    Log.e("AuthError", "Erro ao enviar email de verificação", emailTask.getException());
                                    Toast.makeText(SingUpActivity.this, "Erro ao enviar email de verificação.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {
                        // Tratando erros de cadastro
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthUserCollisionException) {
                            Log.e("AuthError", "O e-mail já está sendo usado", exception);
                            Toast.makeText(SingUpActivity.this, "Este e-mail já está sendo utilizado.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("AuthError", "Erro ao criar usuário", exception);
                            Toast.makeText(SingUpActivity.this, "Erro ao criar conta. Tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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