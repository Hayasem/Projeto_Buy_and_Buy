package com.example.tela_login_projetointegrador.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.tela_login_projetointegrador.R;
import com.example.tela_login_projetointegrador.models.CategoriaProduto;
import com.example.tela_login_projetointegrador.models.Produto;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentCadastrarProdutos extends Fragment {


    private TextInputEditText edit_nome_produto, edit_preco_produto;
    private TextView hint_image;
    private EditText editDescricaoProduto, editQuantidadeProduto;
    private Spinner spinner_categoria_produto;
    private Button btn_confirmar_cadastro_produto;
    private ImageView imageView;
    private String imageString;
    private DatabaseReference productsDataRef;
    private static final int PERMISSION_REQUEST_CODE = 100;
    String[] mensagens = {"Preencha todos os campos",
            "Produto Cadastrado com sucesso"};

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 950, 900, true);
                        imageView.setImageBitmap(resizedBitmap);
                        imageString = encodeImage(resizedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_de_produtos, container, false);
        inicializaComponentes(view);
        solicitaPermissao();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return view;
        }

        String userId = auth.getCurrentUser().getUid();
        productsDataRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId).child("produtos");

        configurarSpinnerCategorias();
        configurarBotaoCadastrarProduto(userId);
        configurarImageView();

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle("Cadastro de Produto");

        return view;
    }
    private void inicializaComponentes(View view){
        edit_nome_produto = view.findViewById(R.id.edit_nome_produto);
        edit_preco_produto = view.findViewById(R.id.edit_preco_produto);
        spinner_categoria_produto = view.findViewById(R.id.spinner_categoria_produto);
        editDescricaoProduto = view.findViewById(R.id.edit_descricao_produto);
        editQuantidadeProduto = view.findViewById(R.id.edit_quantidade_produto);
        btn_confirmar_cadastro_produto = view.findViewById(R.id.btn_confirmar_cadastro_produto);
        imageView = view.findViewById(R.id.img_produto); // adicione isso
        hint_image = view.findViewById(R.id.tv_hint_imagem);
    }
    private void configurarSpinnerCategorias() {
            List<CategoriaProduto> categorias = new ArrayList<>();
            categorias.add(new CategoriaProduto(0, "Selecione a categoria"));
            categorias.add(new CategoriaProduto(1, "Eletronicos"));
            categorias.add(new CategoriaProduto(2, "Brinquedos"));
            categorias.add(new CategoriaProduto(3, "Vestuários"));
            categorias.add(new CategoriaProduto(4, "Cama/Mesa/Banho"));

            ArrayAdapter<CategoriaProduto> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, categorias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_categoria_produto.setAdapter(adapter);
    }
    private void configurarBotaoCadastrarProduto(String userID) {
        btn_confirmar_cadastro_produto.setOnClickListener(v -> {
            String titulo = edit_nome_produto.getText().toString().trim();
            String descricao = editDescricaoProduto.getText().toString().trim();
            String preco = edit_preco_produto.getText().toString().trim();
            String quantidadeTexto = editQuantidadeProduto.getText().toString().trim();
            CategoriaProduto categoriaProduto = (CategoriaProduto) spinner_categoria_produto.getSelectedItem();

            if (titulo.isEmpty() || descricao.isEmpty() || preco.isEmpty()
                    || quantidadeTexto.isEmpty() || categoriaProduto == null
                    || imageString == null || categoriaProduto.getId() == 0) {
                exibirSnackbar(mensagens[0], v);
                return;
            }

            try {
                float precoFloat = Float.parseFloat(preco);
                int quantidadeProd = Integer.parseInt(quantidadeTexto);

                Produto produto = new Produto();
                produto.setIdUsuario(userID);
                produto.setNomeProduto(titulo);
                produto.setDescricao(descricao);
                produto.setPreco(precoFloat);
                produto.setIdCategoria(categoriaProduto.getId());
                produto.setImagem(imageString);
                produto.setQuantidade(quantidadeProd);

                salvarProdutosFirebase(produto, v);
                limparCampos();
            } catch (NumberFormatException e) {
                exibirSnackbar("Preço ou quantidade inválidos", v);
            }
        });
    }
    private void configurarImageView() {
        imageView.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    imagePickerLauncher.launch(intent);
        });
    }
    private void salvarProdutosFirebase(Produto produto, View view){
        String productId = productsDataRef.push().getKey();
        produto.setIdProduto(productId);
        if (productId != null){
            productsDataRef.child(productId).setValue(produto.toMap())
                    .addOnSuccessListener(aVoid -> exibirSnackbar(mensagens[1],view))
                    .addOnFailureListener(e -> exibirSnackbar("Ocorreu um erro ao salvar o produto: " + e.getMessage(), view));
        }
    }
    private void limparCampos(){
        edit_nome_produto.setText("");
        editDescricaoProduto.setText("");
        edit_preco_produto.setText("");
        editQuantidadeProduto.setText("");
        spinner_categoria_produto.setSelection(0);
        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        imageString= null;
    }
    private void solicitaPermissao(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }
    private void exibirSnackbar(String erro, View view) {
        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.GREEN);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap redimensionarBitmap(Bitmap bitmap, int width, int height){
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
            } else {
                // Permissão negada
                Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(requireContext());
        // Diretório para salvar a imagem
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Cria um arquivo com nome único
        File imageFile = new File(directory, "image_" + System.currentTimeMillis() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            // Usa o método compress para salvar a imagem no formato JPEG com qualidade 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retorna o caminho absoluto do arquivo salvo
        return imageFile.getAbsolutePath();
    }

    private void saveImageToDatabase(String imageString) {
        // Aqui você deve implementar o código para salvar a string 'imageString' no banco de dados
        Toast.makeText(getActivity(), "Image saved to database!", Toast.LENGTH_SHORT).show();

    }
}
