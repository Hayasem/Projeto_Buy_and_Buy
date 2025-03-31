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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification() != null){
            String titulo = message.getNotification().getTitle();
            String textoMensagem = message.getNotification().getBody();
            Log.d("FCM", "Título: " + titulo + " - Mensagem: " + textoMensagem);
            enviarNotificacao(titulo, textoMensagem);
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
}
