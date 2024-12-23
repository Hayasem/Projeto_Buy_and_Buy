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

        public void cadastrarUsuarioAuth    (Usuario usuario, OnCompleteListener<Void> listener){
            firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String idUsuario = firebaseDatabase.child("usuarios").push().getKey();
                                usuario.setIdUsuario(idUsuario);

                                assert idUsuario != null;
                                firebaseDatabase.child("usuarios").child(idUsuario).setValue(usuario).addOnCompleteListener(listener);
                            }else{
                                Exception exception = task.getException();
                                mostrarErroDialog(exception != null ? exception.getMessage() : "Não foi possível efetuar o cadastro, tente novamente.");
                            }
                        }
                    });
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
        public String gerarSalt(){
            SecureRandom random;
            try {
                random = SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                random = new SecureRandom(); // fallback para o padrão
            }
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        }
        public String gerarHashSenha(String senha, String salt){
            try{
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest((senha + salt).getBytes(StandardCharsets.UTF_8));
                StringBuilder hexString = new StringBuilder(2*hash.length);
                for(byte b : hash){
                    hexString.append(String.format("%02x", b & 0xff));
                }
                return hexString.toString();
            }catch (NoSuchAlgorithmException e){
                e.printStackTrace();
                return null;
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
