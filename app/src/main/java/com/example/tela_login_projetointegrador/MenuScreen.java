package com.example.tela_login_projetointegrador;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MenuScreen extends AppCompatActivity {

    private ImageButton bt_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        IniciarComponentes();

        bt_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MenuScreen.this, TelaPerfilUsuario.class);
                startActivity(intent);

            }
        });

    }
    private void IniciarComponentes(){
        bt_perfil = findViewById(R.id.bt_perfil);
    }

}
