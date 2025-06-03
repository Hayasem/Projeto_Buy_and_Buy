package com.example.tela_login_projetointegrador.activities;
import android.os.Bundle;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.fragments.CartFragment;
import com.example.tela_login_projetointegrador.fragments.FragmentNotificacao;
import com.example.tela_login_projetointegrador.fragments.UserFragment;
import com.example.tela_login_projetointegrador.listeners.OnCartCountChangeListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

public class SecondActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnCartCountChangeListener {

    private BottomNavigationView navigationView;
    private OnCartCountChangeListener cartCountChangeListener;
    public void setCartCountChangeListener(OnCartCountChangeListener listener) {
        this.cartCountChangeListener = listener;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        navigationView = findViewById(R.id.bottom_navigation_view);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.homeId);

        setCartCountChangeListener(this);

        if (savedInstanceState == null) { // Carregar a primeira tela (Fragment)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentProdutos, new HomeActivity())
                    .commit();
        }
    }
    private void updateCartBadge(int count) {
        BadgeDrawable badgeDrawable = navigationView.getOrCreateBadge(R.id.carrinhoId);

        if (count > 0) {
            badgeDrawable.setNumber(count);
            badgeDrawable.setVisible(true);
            // Opcional: Ajuste fino de posicionamento se necessário (mas geralmente não é preciso com BadgeDrawable)
            // badgeDrawable.setHorizontalOffset(10);
            // badgeDrawable.setVerticalOffset(-10);
        } else {
            badgeDrawable.setVisible(false); // Esconde a badge se o carrinho estiver vazio
            // Ou, para remover completamente e liberar recursos:
            // navigationView.removeBadge(R.id.carrinhoId);
        }
    }
    @Override
    public void onCartCountChanged(int count) { // <--- ADICIONE ESTE MÉTODO (IMPLEMENTAÇÃO DA INTERFACE)
        updateCartBadge(count); // Chama o método para atualizar a badge com a contagem real
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onNavigationItemSelected( @NotNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        int itemId = menuItem.getItemId();
        // metodo responsavel por fazer a navegacao entre as telas

        if(menuItem.getItemId()==R.id.homeId){
            Fragment menuScreen = HomeActivity.newInstance();
            openFragment(menuScreen);
            return true;
        }else if(menuItem.getItemId() == R.id.notification_icon){
            Fragment fragmentNotificacao = FragmentNotificacao.newInstance();
            openFragment(fragmentNotificacao);
            return true;
        }else if (menuItem.getItemId() ==R.id.carrinhoId) {
            Fragment cartScreen = CartFragment.newInstance();
            openFragment(cartScreen);
            return true;
        }else if (menuItem.getItemId() ==R.id.profileId) {
            Fragment telaPerfilUsuario = UserFragment.newInstance();
        }
        if (selectedFragment != null){
            openFragment(selectedFragment);
            return true;
        }
        return false;
    }
    private void openFragment(Fragment fragment) { //metodo generico para efetuar a troca de fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentProdutos, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
    