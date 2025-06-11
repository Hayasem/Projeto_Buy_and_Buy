package com.example.tela_login_projetointegrador.services;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tela_login_projetointegrador.models.BackUrls;
import com.example.tela_login_projetointegrador.models.Item;
import com.example.tela_login_projetointegrador.models.PreferenceResponse;
import com.example.tela_login_projetointegrador.models.PreferenciaRequest;
import com.example.tela_login_projetointegrador.models.ProdutosCarrinho;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


public class MercadoPagoService {
    private static final String ACCESS_TOKEN = "APP_USR-2644083944092477-060519-a9c0447e967bbfeaf430de15b6df1c87-160527945";

    private List<ProdutosCarrinho> listaCarrinho;
    private Context context;
    public MercadoPagoService(  List<ProdutosCarrinho> listaCarrinho,Context context) {
        this.listaCarrinho = listaCarrinho;
        this.context = context;

    }

    public String criarPreferencia(Consumer<String> onReady) {;
        final PreferenceResponse[] prefResponse = {new PreferenceResponse()};
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Monta a lista de itens
                List<Item> items = new ArrayList<>();
                for (ProdutosCarrinho p : listaCarrinho) {
                    items.add(new Item(p.getNomeProduto(), p.getQuantidade(), p.getPreco()));
                }

                // Cria objeto de request
                BackUrls backUrls = new BackUrls(
                        "https://seusite.com.br/sucesso",
                        "https://seusite.com.br/falha",
                        "https://seusite.com.br/pendente"
                );

                Scanner scanner = getScanner(items, backUrls);
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                String resposta = response.toString();
                Log.d("MercadoPago", "Resposta: " + resposta);

                Gson gson = new Gson();
                prefResponse[0] = gson.fromJson(resposta, PreferenceResponse.class);

                prefResponse[0].setInit_point(prefResponse[0].getInit_point());
                String urlPagamento = prefResponse[0].getInit_point();

                // Se quiser mostrar algo na tela:
                new Handler(Looper.getMainLooper()).post(() -> onReady.accept(urlPagamento));

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Erro ao criar preferência", Toast.LENGTH_SHORT).show()
                );
            }
        });

        return prefResponse[0].getInit_point();
    }

    @NonNull
    private static Scanner getScanner(List<Item> items, BackUrls backUrls) throws IOException {
        PreferenciaRequest request = new PreferenciaRequest(items, backUrls);

        Gson gson = new Gson();
        String jsonInputString = gson.toJson(request);

        URL url = new URL("https://api.mercadopago.com/checkout/preferences");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        return new Scanner(
                status >= 200 && status < 300 ? connection.getInputStream() : connection.getErrorStream()
        );
    }


    public static String verificarStatusPagamento(String preferenceId) {
        try {

            Thread.sleep(10000);
//            URL url = new URL("https://api.mercadopago.com/v1/payments/search?external_reference=" + preferenceId);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
//
//            Scanner scanner = new Scanner(connection.getInputStream());
//            StringBuilder response = new StringBuilder();
//            while (scanner.hasNextLine()) {
//                response.append(scanner.nextLine());
//            }
//            scanner.close();

            return "\"status\":\"approved\"";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

