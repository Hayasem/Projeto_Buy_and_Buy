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
import com.example.tela_login_projetointegrador.database.DatabaseConnection;
import com.example.tela_login_projetointegrador.database.UserManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
            }else{
                if(userManager.estaBloqueado(email)){
                    Snackbar snackbar = Snackbar.make(view, "Conta bloqueada. Tente novamente mais tarde.", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    boolean loginValido = userManager.compararSenha(email, senha);
                    if (loginValido) {
                        userManager.setUserLogado(email);
                        String codigoVerificacao = gerarCodigoVerificacao();
                        enviarCodigoporEmail(email, codigoVerificacao);
                        criarJanelaCodigo(codigoVerificacao);
                        userManager.resetarTentativas(email);
                    }else{
                        userManager.incrementarTentativas(email);
                        int tentativas = userManager.obterTentativas(email);
                        Snackbar snackbar = Snackbar.make(view, "Email ou senha incorretos.", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();

                        if (tentativas >= 3) {
                            userManager.bloquearConta(email);
                            snackbar = Snackbar.make(view, "Conta bloqueada após 3 tentativas falhas. Tente novamente em 15 minutos.", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.RED);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    }
                }
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
    private void enviarCodigoporEmail(String emailDestino, String codigoVerificacao) {
        final String username = "buybuy@gmail.com";
        final String password = "ewwg yyfl aemm hitu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailDestino));
            message.setSubject("Código de Verificação");
            message.setText("Seu código de verificação é: " + codigoVerificacao);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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