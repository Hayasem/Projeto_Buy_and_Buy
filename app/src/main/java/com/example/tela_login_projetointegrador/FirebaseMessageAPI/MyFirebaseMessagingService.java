package com.example.tela_login_projetointegrador.FirebaseMessageAPI;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.ModelNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getNotification() != null) {
            String titulo = message.getNotification().getTitle();
            String mensagem = message.getNotification().getBody();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String notiId = UUID.randomUUID().toString();

            ModelNotification model = new ModelNotification(notiId, titulo, mensagem, System.currentTimeMillis());

            FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(userId)
                    .child("notificacoes")
                    .child(notiId)
                    .setValue(model);
        }
    }
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(userID)
                .child("fmctoken")
                .setValue(token);
    }
}
