package com.example.tela_login_projetointegrador.backendactivitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tela_login_projetointegrador.Format.EmailTextWatcher;
import com.example.tela_login_projetointegrador.Format.SenhaTextWatcher;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

//Classe Principal, e objetos da classe:
//--------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {
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

        DatabaseConnection databaseConnection = new DatabaseConnection(this);
        SQLiteDatabase db =  databaseConnection.getWritableDatabase();
        userManager = new UserManager(db);

        setContentView(com.example.tela_login_projetointegrador.R.layout.activity_main);

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
            }else{
                boolean loginValido = userManager.compararSenha(email, senha);
                if (loginValido){
                    userManager.setUserLogado(email);
                    Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GREEN);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                    }, 1000);
                }else{
                    Snackbar snackbar = Snackbar.make(view, "Email ou senha incorretos.", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
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