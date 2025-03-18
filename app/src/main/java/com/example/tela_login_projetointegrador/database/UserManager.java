    package com.example.tela_login_projetointegrador.database;

    import android.content.Context;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;

    import com.example.tela_login_projetointegrador.model.Usuario;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.ValueEventListener;

    import java.nio.charset.StandardCharsets;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.text.SimpleDateFormat;
    import java.util.Base64;
    import java.util.Calendar;
    import java.util.Locale;

    public class UserManager {
        private DatabaseReference firebaseDatabase;
        private FirebaseAuth firebaseAuth;
        private Context context;

        public UserManager(DatabaseReference firebaseDatabase) {
            this.firebaseDatabase = firebaseDatabase;
            this.firebaseAuth = FirebaseAuth.getInstance();
        }
        public void consultarUsuario(String idUsuario, ValueEventListener listener) {
            firebaseDatabase.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(listener);
        }

    // Método para salvar o email e senha no Banco de Dados:
    //---------------------------------------------------------------------------------------------
            public void compararSenha(String email, String senha, ValueEventListener listener){
                firebaseDatabase.child("usuarios").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(listener);
            }
    //Método para verificar se o email inserido pelo usuário já está cadastrado:
    //----------------------------------------------------------------------------------------------
        public void isEmailCadastrado(String email, ValueEventListener listener) {
            firebaseDatabase.child("usuarios").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(listener);
        }

    //---------------------------------------------------------------------------------------------
    //Método para deslogar o usuário
        public void deslogarUsuario() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }
    }

        private void mostrarErroDialog(String mensagem) {
            new AlertDialog.Builder(context)
                    .setTitle("Erro")
                    .setMessage(mensagem)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
