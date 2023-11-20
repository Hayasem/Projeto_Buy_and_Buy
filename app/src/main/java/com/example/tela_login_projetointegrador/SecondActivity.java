package com.example.tela_login_projetointegrador;

import android.content.Intent;
import android.os.Bundle;
//import androidx.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;

public class SecondActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        getMenuInflater().inflate(R.menu.navigation,menu);

    }

    @Override
    public boolean onNavigationItemSelected( @NotNull MenuItem menuItem) {

        if(menuItem.getItemId()==R.id.homeId){
            Log.d("clicounotificacao","clicou notificacao");
            startActivity(new Intent(this, MenuScreen.class));
            finish();
            return true;
        }else if(menuItem.getItemId() == R.id.notificacaoId){
            Log.d("clicounotificacao","clicou notificacao");
            startActivity(new Intent(this, ActivityNotificacao.class));
            finish();
            return true;
        }else if (menuItem.getItemId() ==R.id.carrinhoId) {
            Log.d("clicounotificacao","clicou carrinho");
            startActivity(new Intent(this, ActivityNotificacao.class));
            finish();
            return true;
        }else if(menuItem.getItemId() ==R.id.profileId){
            Log.d("clicounotificacao","clicou profile");
            startActivity(new Intent(this, TelaPerfilUsuario.class));
            finish();
            return true;
        }
        return false;
    }
}
