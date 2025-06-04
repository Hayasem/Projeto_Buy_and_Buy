package com.example.tela_login_projetointegrador.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Adicione este import para depuração
import android.view.MenuItem;

import androidx.annotation.NonNull; // Use o NonNull do AndroidX
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate; // Importe para o tema
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.fragments.CartFragment;
import com.example.tela_login_projetointegrador.fragments.FragmentCadastrarProdutos;
import com.example.tela_login_projetointegrador.fragments.FragmentNotificacao;
import com.example.tela_login_projetointegrador.fragments.HomeFragment;
import com.example.tela_login_projetointegrador.fragments.UserFragment;
import com.example.tela_login_projetointegrador.listeners.OnCartCountChangeListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecondActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnCartCountChangeListener {

    private BottomNavigationView navigationView;
    private OnCartCountChangeListener cartCountChangeListener; // Mantenha isso se você o usa em outro lugar
    private static final String CURRENT_FRAGMENT_TAG_KEY = "current_fragment_tag"; // Chave para salvar o fragmento ativo
    private String activeFragmentTag; // Para armazenar a tag do fragmento ativo

    // Chaves para as preferências de acessibilidade (consistentes com BaseFragment)
    private static final String PREFS_NAME = "AccessibilityPrefs";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";


    public void setCartCountChangeListener(OnCartCountChangeListener listener) {
        this.cartCountChangeListener = listener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // PASSO 1: Carregar a preferência de tema ANTES de super.onCreate()
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isDarkModeEnabled = prefs.getBoolean(KEY_DARK_MODE, false);
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity); // Verifique se este é o nome correto do seu layout da Activity

        navigationView = findViewById(R.id.bottom_navigation_view); // Verifique o ID
        navigationView.setOnNavigationItemSelectedListener(this);

        setCartCountChangeListener(this); // Mantenha esta linha se precisar da interface

        // PASSO 2: Lógica unificada para carregamento/restauração do Fragmento
        if (savedInstanceState != null) {
            // A Activity foi recriada (ex: por mudança de tema ou rotação de tela)
            activeFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG_KEY);
            Log.d("SecondActivity", "onCreate: Restoring fragment with tag: " + activeFragmentTag);

            if (activeFragmentTag == null) {
                // Caso a tag salva seja nula por algum motivo, volta para o padrão
                activeFragmentTag = "home_fragment";
            }
            // Não precisamos encontrar o fragmento existente e reanexar aqui.
            // Basta chamar loadFragment que cuidará da substituição.
            loadFragment(activeFragmentTag);
            setSelectedNavigationItem(activeFragmentTag); // Seleciona o item correto na BottomNavigationView
        } else {
            // Primeira inicialização da Activity (não houve recriação)
            Log.d("SecondActivity", "onCreate: Initializing HomeFragment.");
            loadFragment("home_fragment"); // Carrega o Fragmento inicial (HomeFragment)
            setSelectedNavigationItem("home_fragment"); // Define o item inicial na BottomNavigationView
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // PASSO 3: Salva a tag do fragmento atualmente ativo
        outState.putString(CURRENT_FRAGMENT_TAG_KEY, activeFragmentTag);
        Log.d("SecondActivity", "onSaveInstanceState: Saving fragment tag: " + activeFragmentTag);
    }

    // PASSO 4: Consolidar a lógica de carregamento de fragmentos
    private void loadFragment(String fragmentTag) {
        Fragment fragment = null;
        int menuItemId = -1; // Para manter o item do menu selecionado corretamente

        switch (fragmentTag) {
            case "home_fragment":
                fragment = HomeFragment.newInstance(false); // Assumindo false como padrão para Home
                menuItemId = R.id.homeId; // ID do seu item "Home" no menu
                break;
            case "search_fragment": // Se você tiver um fragmento de busca separado, ou se HomeFragment lida com isso
                fragment = HomeFragment.newInstance(true); // Assumindo true para busca
                menuItemId = R.id.searchId; // ID do seu item "Search" no menu
                break;
            case "cart_fragment":
                fragment = CartFragment.newInstance();
                menuItemId = R.id.carrinhoId; // ID do seu item "Carrinho" no menu
                break;
            case "user_fragment":
                fragment = UserFragment.newInstance();
                menuItemId = R.id.profileId; // ID do seu item "Profile" no menu
                break;
            case "fragment_cadastrar_produtos":
                fragment = FragmentCadastrarProdutos.newInstance();
                // Se não tiver um item de menu para isso na BottomNavigationView, menuItemId pode ser -1
                break;
            case "fragment_notificacao":
                fragment = FragmentNotificacao.newInstance();
                // Se não tiver um item de menu para isso na BottomNavigationView, menuItemId pode ser -1
                break;
            default:
                Log.e("SecondActivity", "loadFragment: Unknown fragment tag: " + fragmentTag);
                fragment = HomeFragment.newInstance(false); // Fallback para HomeFragment
                fragmentTag = "home_fragment"; // Atualiza a tag para o fallback
                menuItemId = R.id.homeId;
                break;
        }

        if (fragment != null) {
            activeFragmentTag = fragmentTag; // Atualiza a tag do fragmento ativo
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Use o ID correto do container dos seus fragments
            // Pelo seu código, parece ser R.id.fragmentProdutos
            transaction.replace(R.id.fragmentProdutos, fragment, fragmentTag);
            transaction.addToBackStack(null); // Permite navegar para trás pelos fragments
            transaction.commit();
            Log.d("SecondActivity", "loadFragment: Loaded " + fragmentTag);

            // Atualiza a seleção da BottomNavigationView se um ID de menu válido foi encontrado
            if (menuItemId != -1) {
                setSelectedNavigationItem(fragmentTag);
            }
        }
    }

    // PASSO 5: Lógica para a BottomNavigationView
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        String tagToLoad = null;

        if (itemId == R.id.homeId) {
            tagToLoad = "home_fragment";
        } else if (itemId == R.id.searchId) {
            tagToLoad = "search_fragment"; // Ou "home_fragment" se HomeFragment gerencia a busca
        } else if (itemId == R.id.carrinhoId) {
            tagToLoad = "cart_fragment";
        } else if (itemId == R.id.profileId) {
            tagToLoad = "user_fragment";
        }
        // Adicione outros itens de menu aqui, se houver

        if (tagToLoad != null) {
            // Evita recarregar o mesmo fragmento se já estiver ativo
            if (!tagToLoad.equals(activeFragmentTag)) {
                loadFragment(tagToLoad);
            } else {
                Log.d("SecondActivity", "onNavigationItemSelected: Fragment " + tagToLoad + " is already active.");
            }
            return true;
        }
        return false;
    }

    // Método auxiliar para selecionar o item correto na BottomNavigationView
    private void setSelectedNavigationItem(String fragmentTag) {
        int itemIdToSelect = -1;
        switch (fragmentTag) {
            case "home_fragment":
                itemIdToSelect = R.id.homeId;
                break;
            case "search_fragment":
                itemIdToSelect = R.id.searchId;
                break;
            case "cart_fragment":
                itemIdToSelect = R.id.carrinhoId;
                break;
            case "user_fragment":
                itemIdToSelect = R.id.profileId;
                break;
            // Adicione cases para outros fragmentos que têm um item correspondente na BottomNavigationView
        }
        if (itemIdToSelect != -1 && navigationView.getSelectedItemId() != itemIdToSelect) {
            navigationView.setSelectedItemId(itemIdToSelect);
        }
    }


    // Métodos para o badge do carrinho (mantidos como estão, mas verificando IDs)
    private void updateCartBadge(int count) {
        BadgeDrawable badgeDrawable = navigationView.getOrCreateBadge(R.id.carrinhoId); // Verifique se R.id.carrinhoId é o ID correto do item do menu do carrinho

        if (count > 0) {
            badgeDrawable.setNumber(count);
            badgeDrawable.setVisible(true);
        } else {
            badgeDrawable.setVisible(false);
            // navigationView.removeBadge(R.id.carrinhoId); // Descomente para remover completamente a badge
        }
    }

    @Override
    public void onCartCountChanged(int count) {
        updateCartBadge(count);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}