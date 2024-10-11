package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tela_login_projetointegrador.Format.EmailTextWatcher;
import com.example.tela_login_projetointegrador.Format.SenhaTextWatcher;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

//Classe Principal, e objetos da classe:
//--------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView text_tela_cadastro;
    private TextInputLayout layout_email, layout_senha;
    private EditText edit_email;
    private EditText edit_senha;
    private Button bt_entrar;
    private UserManager userManager;
    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso!"};

//---------------------------------------------------------------------------------------------

// Métodos essenciais para o ciclo de vida da Activity:
//---------------------------------------------------------------------------------------------
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        IniciarComponentes();
        acoesClick();
    }
//---------------------------------------------------------------------------------------------

// Métodos da tela de login, para iniciá-la e para realizar o login:
//---------------------------------------------------------------------------------------------
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
            }else {
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()){
                                String codigoVerificacao = gerarCodigoVerificacao();
                                criarJanelaCodigo(codigoVerificacao);
                            }else{
                                enviarEmailVerificacao(user);
                            }
                        }
                    }else{
                        Snackbar snackbar = Snackbar.make(view, "Email ou senha incorretos.", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                });
            }
        });
    }
    private String gerarCodigoVerificacao(){
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }
    private void criarJanelaCodigo(String codigoCorreto){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Verificação de código");

        final EditText campoCodigo = new EditText(MainActivity.this);
        campoCodigo.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(campoCodigo);

        builder.setPositiveButton("Verificar", (dialog, which) -> {
            String codigoInserido = campoCodigo.getText().toString();
            if (codigoInserido.equals(codigoCorreto)) {
                Snackbar.make(campoCodigo, "Verificação bem-sucedida!", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MenuScreen.class);
                startActivity(intent);
            } else {
                Snackbar.make(campoCodigo, "Código incorreto. Tente novamente.", Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void enviarEmailVerificacao(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Snackbar.make(bt_entrar, "E-mail de verificação enviado. Verifique sua caixa de entrada.", Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(bt_entrar, "Falha ao enviar o e-mail de verificação.", Snackbar.LENGTH_LONG).show();
            }
        });
    }
//---------------------------------------------------------------------------------------------
    private void IniciarComponentes(){
        layout_email = findViewById(R.id.input_layout_email);
        layout_senha = findViewById(R.id.input_layout_senha);
        text_tela_cadastro = findViewById(com.example.tela_login_projetointegrador.R.id.text_tela_cadastro);
        edit_email = findViewById(R.id.input_edit_email);
        edit_senha = findViewById(R.id.input_edit_senha);
        bt_entrar = findViewById(com.example.tela_login_projetointegrador.R.id.bt_entrar);
        edit_email.addTextChangedListener(new EmailTextWatcher(layout_email));
        edit_senha.addTextChangedListener(new SenhaTextWatcher(layout_senha));
    }
}