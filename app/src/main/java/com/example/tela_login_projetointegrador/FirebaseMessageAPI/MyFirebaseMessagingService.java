package com.example.tela_login_projetointegrador.FirebaseMessageAPI;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.HolderNotificacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification() != null){
            String titulo = message.getNotification().getTitle();
            String textoMensagem = message.getNotification().getBody();
            Log.d("FCM", "Título: " + titulo + " - Mensagem: " + textoMensagem);

            enviarNotificacao(titulo, textoMensagem);
            salvarNotificacaoFirebase(titulo, textoMensagem, message);
        }

        if (message.getData().size() > 0) {
            Log.d("FCM", "Dados da mensagem: " + message.getData());
        }
    }
    public void enviarNotificacao(String titulo, String textoMensagem){
        String channelID = "canal_notificacoes";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelID, "Notificações", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.add_comment_icon)
                .setContentTitle(titulo)
                .setContentText(textoMensagem)
                .setAutoCancel(true);

        manager.notify(1, builder.build());
    }

    private void salvarNotificacaoFirebase(String titulo, String textoMensagem, RemoteMessage message){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            String idUsuario = auth.getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notificacoes").child(idUsuario);

            String idNotificacao = databaseReference.push().getKey();

            if (idNotificacao != null) {
                HolderNotificacao notificacao = new HolderNotificacao(
                        idNotificacao,
                        idUsuario,
                        message.getData().get("image_notif"),
                        titulo,
                        textoMensagem,
                        new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()),
                        new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date())
                );
                databaseReference.child(idNotificacao).setValue(notificacao)
                        .addOnSuccessListener(aVoid -> Log.e("Firebase", "Notificação salva com sucesso"))
                        .addOnFailureListener(e -> Log.e("Firebase", "Erro ao salvar notificação", e));
            }
             }else{
                Log.e("Firebase", "Usuário não autenticado. Notificação não salva.");
            }
        }
}
