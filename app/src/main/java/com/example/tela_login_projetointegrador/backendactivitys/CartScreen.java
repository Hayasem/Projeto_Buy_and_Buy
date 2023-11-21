package com.example.tela_login_projetointegrador.backendactivitys;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tela_login_projetointegrador.Adapter.MyAdapter;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartScreen extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_screen, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_products);
        List<Product> productList = carregaDados();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter(getContext(), productList));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    public static CartScreen newInstance() {
        return new CartScreen();
    }
    private List<Product> carregaDados(){
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product(R.drawable.img_backpack,
                "Mochila militar",
                "Uma mochila perfeita para aqueles que gostam de se aventurar. Consta com 6 diferentes bolsos a fim de armazenar tudo de essêncial, a fim de atender até aqueles com mais preparo. Anti-impermeável, e fácil de se ajustar, uma dos top modelos da loja.",
                "R$ 149,99");
        productList.add(product1);

        Product product2 = new Product(R.drawable.img_racket,
                "Raquete elétrica de alta tensão",
                "Não aguenta mais aqueles mosquitos atrapalhando seu sono durante a madrugada? Acabe com todos eles utilizando nosso mais novo modelo de raquete elétrica de alta tensão. Você merece ter uma boa noite de sono depois de um longo dia de trabalho, não deixe mais esses pequenos encreiqueiros te incomodarem.",
                "R$ 79,99");
        productList.add(product2);

        Product product3 = new Product(R.drawable.img_sneakers,
                "Tênis Yonex",
                "Tênis perfeitamente equilibrado entre adrenalina e conforto, seus pés vão se sentir no paraíso ao calçar. Fácil de limpar, e adaptável a circunferência de seus pés.",
                "R$ 200,00");
        productList.add(product3);

        Product product4 = new Product(R.drawable.oculos_de_sol,
                "Óculos de sol",
                "Cansado de, mesmo em um dia tão lindo não poder olhar para o sol? Pois seus problemas acabaram, venha confirir nosso mais novo modelo de óculos de sol testado e aprovado por 327 de 328 cientistas.",
                "R$ 20,00");
        productList.add(product4);
        return productList;
    }
}