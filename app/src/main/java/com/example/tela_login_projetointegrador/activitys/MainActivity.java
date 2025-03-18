package com.example.tela_login_projetointegrador.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tela_login_projetointegrador.Format.EmailTextWatcher;
import com.example.tela_login_projetointegrador.Format.SenhaTextWatcher;
import com.example.tela_login_projetointegrador.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

//Classe Principal, e objetos da classe:
//--------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView text_tela_cadastro, text_button_tela_cadastro, text_esqueci_senha;
    private TextInputLayout layout_email, layout_senha;
    private EditText edit_email;
    private EditText edit_senha;
    private Button bt_entrar;
    private UserManager userManager;
    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso!"};

    protected static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_ATTEMPTS = "attempts";
    protected static final String KEY_LAST_LOGIN = "last_login";
    protected static final long LOGIN_EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    protected static final int MAXIMO_TENTATIVAS = 3; // Máximo de tentativas
    private SharedPreferences sharedPreferences;
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
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        long lastLogin = sharedPreferences.getLong(KEY_LAST_LOGIN, 0);
        long currentTime = System.currentTimeMillis();
        if (lastLogin != 0 && (currentTime - lastLogin < LOGIN_EXPIRATION_TIME)) {
            // Usuário logado recentemente, vá direto para a tela principal
            navegaHome();
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        IniciarComponentes();
        acoesClick();
    }
//---------------------------------------------------------------------------------------------

// Métodos da tela de login, para iniciá-la e para realizar o login:
//---------------------------------------------------------------------------------------------
    private void acoesClick() {
        text_button_tela_cadastro.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,FormCadastro.class);
            startActivity(intent);
        });

        text_esqueci_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,activity_recuperar_senha.class);
                startActivity(intent);
            }
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
                try{
                    auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()){
                                    resetAttempts();
                                    navegaHome();
                                }else{
                                    enviarEmailVerificacao(user);
                                }
                            }
                        }else{

                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.e("LoginError", "Credenciais inválidas", exception);
                                Snackbar.make(view, "Email ou senha incorretos.", Snackbar.LENGTH_SHORT).show();
                                validaTentativas();


                            } else if (exception instanceof FirebaseAuthInvalidUserException) {
                                Log.e("LoginError", "Usuário não encontrado ou desativado", exception);
                                Snackbar.make(view, "Usuário não encontrado ou desativado.", Snackbar.LENGTH_SHORT).show();
                            } else if (exception instanceof FirebaseAuthEmailException) {
                                Log.e("LoginError", "Erro relacionado ao e-mail", exception);
                                Snackbar.make(view, "Erro ao enviar email de verificação. Tente novamente.", Snackbar.LENGTH_SHORT).show();
                            }else if (exception instanceof FirebaseTooManyRequestsException){
                                showToast("Você excedeu o número máximo de tentativas.");
                            } else {
                                Log.e("LoginError", "Erro no login", exception);
                                Snackbar.make(view, "Erro inesperado. Tente novamente.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("LoginError", "Erro ao tentar logar", e);
                    Snackbar.make(view, "Erro inesperado. Tente novamente.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void navegaHome() {
        sharedPreferences.edit().putLong(KEY_LAST_LOGIN, System.currentTimeMillis()).apply();
        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
        startActivity(intent);
    }

    private void validaTentativas() {

        incrementAttempts();

        int attemptsLeft = MAXIMO_TENTATIVAS - getAttempts();
        if (attemptsLeft <= 0) {
            // Usuário bloqueado
            showToast("Você excedeu o número máximo de tentativas.");
        }
    }

    private void enviarEmailVerificacao(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Snackbar.make(bt_entrar, "E-mail de verificação enviado. Verifique sua caixa de entrada.", Snackbar.LENGTH_LONG).show();
            }else{
                Exception exception = task.getException();
                assert exception != null;

                Log.i("Error Send Email", Objects.requireNonNull(exception.getLocalizedMessage()));
                Snackbar.make(bt_entrar, "Falha ao enviar o e-mail de verificação "+ exception.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
//---------------------------------------------------------------------------------------------
    private void IniciarComponentes(){
        layout_email = findViewById(R.id.input_layout_email);
        layout_senha = findViewById(R.id.input_layout_senha);
        text_button_tela_cadastro = findViewById(R.id.text_botao_cadastro);
        text_tela_cadastro = findViewById(com.example.tela_login_projetointegrador.R.id.text_tela_cadastro);
        edit_email = findViewById(R.id.input_edit_email);
        edit_senha = findViewById(R.id.input_edit_senha);
        bt_entrar = findViewById(com.example.tela_login_projetointegrador.R.id.bt_entrar);
        edit_email.addTextChangedListener(new EmailTextWatcher(layout_email));
        edit_senha.addTextChangedListener(new SenhaTextWatcher(layout_senha));
        text_esqueci_senha = findViewById(R.id.text_esqueci_senha);
    }


    private void incrementAttempts() {
        int attempts = getAttempts();
        attempts++;
        sharedPreferences.edit().putInt(KEY_ATTEMPTS, attempts).apply();
    }
    private void resetAttempts() {
        sharedPreferences.edit().putInt(KEY_ATTEMPTS, 0).apply();
    }

    private int getAttempts() {
        return sharedPreferences.getInt(KEY_ATTEMPTS, 0);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}