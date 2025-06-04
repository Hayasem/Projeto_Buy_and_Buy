package com.example.tela_login_projetointegrador.fragments;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;

public class FragmentCadastrarProdutos extends Fragment {


    private TextInputEditText edit_nome_produto, edit_preco_produto;
    private TextView hint_image;
    private EditText editDescricaoProduto, editQuantidadeProduto;
    private Spinner spinner_categoria_produto;
    private Button btn_confirmar_cadastro_produto;
    private ImageView imageView;
    private Uri selectedImageUri;
    private com.google.android.material.floatingactionbutton.FloatingActionButton btnAdicionarImagem;

    private static final String TAG = "CadastroProdutosLog";
    private DatabaseReference productsDataRef;
    private static final int PERMISSION_REQUEST_CODE_READ_STORAGE = 100;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 101; // Novo código de requisição para câmera
    private static final int REQUEST_IMAGE_CAPTURE = 1; // Código para capturar imagem da câmera
    String[] mensagens = {"Preencha todos os campos",
            "Produto Cadastrado com sucesso"};

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    selectedImageUri = imageUri;
                    Log.d(TAG, "Image URI obtida: " + imageUri); // Log 1: A URI da imagem
                    try {
                        imageView.setImageURI(selectedImageUri);
                        hint_image.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "IOException ao obter bitmap da URI: " + e.getMessage(), e); // Log 4: Erro de IO
                        Toast.makeText(requireContext(), "Erro ao carregar a imagem: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                        hint_image.setVisibility(View.VISIBLE);
                        selectedImageUri = null; // Garante que imageString seja nulo em caso de erro
                    }
                } else {
                    Log.d(TAG, "Seleção de imagem cancelada ou dados inválidos."); // Log 5: Seleção cancelada
                    selectedImageUri = null;
                }
            });

    // ActivityResultLauncher para a captura da câmera
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Salva o bitmap em um arquivo temporário para obter uma URI
                    Uri tempUri = getImageUri(requireContext(), imageBitmap);
                    selectedImageUri = tempUri;

                    if (selectedImageUri != null) {
                        imageView.setImageURI(selectedImageUri);
                        hint_image.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(requireContext(), "Erro ao obter a imagem da câmera.", Toast.LENGTH_LONG).show();
                        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                        hint_image.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.d(TAG, "Captura da câmera cancelada ou dados inválidos.");
                }
            });

    public static Fragment newInstance() {
        return new FragmentCadastrarProdutos();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_de_produtos, container, false);
        inicializaComponentes(view);
        solicitarPermissaoArmazenamento();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return view;
        }

        String userId = auth.getCurrentUser().getUid();
        productsDataRef = FirebaseDatabase.getInstance().getReference("produtos_globais");

        configurarSpinnerCategorias();
        configurarBotaoCadastrarProduto(userId);
        configurarImageView();
        configurarBotaoAdicionarImagem();

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle("Cadastro de Produto");

        return view;
    }

    private void inicializaComponentes(View view) {
        edit_nome_produto = view.findViewById(R.id.edit_nome_produto);
        edit_preco_produto = view.findViewById(R.id.edit_preco_produto);
        spinner_categoria_produto = view.findViewById(R.id.spinner_categoria_produto);
        editDescricaoProduto = view.findViewById(R.id.edit_descricao_produto);
        editQuantidadeProduto = view.findViewById(R.id.edit_quantidade_produto);
        btn_confirmar_cadastro_produto = view.findViewById(R.id.btn_confirmar_cadastro_produto);
        imageView = view.findViewById(R.id.img_produto); // adicione isso
        hint_image = view.findViewById(R.id.tv_hint_imagem);
        btnAdicionarImagem = view.findViewById(R.id.btn_adicionar_imagem);
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
                    || selectedImageUri == null || categoriaProduto.getId() == 0) {
                exibirSnackbar(mensagens[0], v);
                Log.d(TAG, "Validação falhou: algum campo está vazio ou imagem não selecionada.");
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
                produto.setQuantidade(quantidadeProd);
                produto.setEmEstoque(true);
                produto.setIdVendedor(userID);

                uploadImageAndSaveProduct(produto, v);
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

    private void uploadImageAndSaveProduct(Produto produto, View view) {
        if (selectedImageUri != null) {
            // Mostra um indicador para o usuário que o upload está acontecendo
            Toast.makeText(getContext(), "Fazendo upload da imagem...", Toast.LENGTH_LONG).show();
            btn_confirmar_cadastro_produto.setEnabled(false); // Desabilita o botão para evitar cliques múltiplos

            // Gera um ID único para o produto ANTES do upload da imagem
            String userId;
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d(TAG, "UID do usuário para upload: " + userId); // <-- ESTE É O LOG CRÍTICO!
            } else {
                Log.e(TAG, "ERRO CRÍTICO: Usuário não autenticado no momento do upload. userID é nulo.");
                exibirSnackbar("Erro: Usuário não autenticado. Faça login novamente.", view);
                btn_confirmar_cadastro_produto.setEnabled(true);
                return; // Interrompe o processo se não houver usuário autenticado
            }
            // Assim, o caminho da imagem no Storage pode incluir o ID do produto
            String productId = productsDataRef.push().getKey();
            if (productId == null) {
                exibirSnackbar("Erro ao gerar ID do produto.", view);
                btn_confirmar_cadastro_produto.setEnabled(true); // Reabilita o botão
                return;
            }
            produto.setIdProduto(productId);

            // Define o caminho no Firebase Storage: product_images/userId/productId_image.jpg
            StorageReference storageRef = FirebaseStorage.getInstance("gs://buy-and-buy-atualizado.firebasestorage.app").getReference();
            StorageReference imageRef = storageRef.child("product_images")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(productId + "_product_image.jpg");
            UploadTask uploadTask = imageRef.putFile(selectedImageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Imagem carregada com sucesso, agora obtém a URL de download
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    Log.d(TAG, "Imagem carregada com sucesso. URL de Download: " + downloadUrl);

                    // Atribui a URL de download ao campo 'imagem' do objeto Produto
                    produto.setImagem(downloadUrl);

                    // Agora que a imagem está carregada e temos a URL, salvamos os dados do produto no Realtime Database
                    salvarProdutosFirebase(produto, view); // Chama o método para salvar no DB
                }).addOnFailureListener(e -> {
                    exibirSnackbar("Falha ao obter URL da imagem: " + e.getMessage(), view);
                    Log.e(TAG, "Erro ao obter URL da imagem.", e);
                    btn_confirmar_cadastro_produto.setEnabled(true); // Reabilita o botão
                });
            }).addOnFailureListener(e -> {
                exibirSnackbar("Falha no upload da imagem: " + e.getMessage(), view);
                Log.e(TAG, "Erro no upload da imagem para o Storage: " + e.getMessage(), e);
                btn_confirmar_cadastro_produto.setEnabled(true); // Reabilita o botão
            });
        } else {
            exibirSnackbar("Nenhuma imagem selecionada para upload.", view);
        }
    }
    private void configurarBotaoAdicionarImagem() {
        btnAdicionarImagem.setOnClickListener(v -> {
            final CharSequence[] options = {"Tirar Foto", "Escolher da Galeria", "Cancelar"};
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Adicionar Imagem do Produto");
            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals("Tirar Foto")) {
                    abrirCameraParaFoto();
                } else if (options[item].equals("Escolher da Galeria")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*"); // Garante que apenas tipos de imagem sejam mostrados
                    imagePickerLauncher.launch(pickPhoto);
                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss(); // Fecha o diálogo
                }
            });
            builder.show();
        });
        imageView.setOnClickListener(v -> {
            final CharSequence[] options = {"Tirar Foto", "Escolher da Galeria", "Cancelar"};

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Adicionar Imagem do Produto");
            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals("Tirar Foto")) {
                    abrirCameraParaFoto();
                } else if (options[item].equals("Escolher da Galeria")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    imagePickerLauncher.launch(pickPhoto);
                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            });
            builder.show();
        });
    }
    private void abrirCameraParaFoto() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                cameraLauncher.launch(takePictureIntent);
            } else {
                Toast.makeText(requireContext(), "Nenhum aplicativo de câmera encontrado.", Toast.LENGTH_SHORT).show();
            }
        } else {
            solicitaPermissaoCamera(); // Solicita a permissão se ainda não foi concedida
        }
    }

    private void salvarProdutosFirebase(Produto produto, View view){
        if (produto.getIdProduto() != null){
            productsDataRef.child(produto.getIdProduto()).setValue(produto.toMap())

                    .addOnSuccessListener(aVoid -> {
                        exibirSnackbar(mensagens[1],view); // "Produto Cadastrado com sucesso"
                        limparCampos(); // Limpa os campos APENAS após o sucesso total
                        btn_confirmar_cadastro_produto.setEnabled(true); // Reabilita o botão
                    })
                    .addOnFailureListener(e -> {
                        exibirSnackbar("Ocorreu um erro ao salvar o produto no banco de dados: " + e.getMessage(), view);
                        Log.e(TAG, "Erro ao salvar o produto no Realtime Database: " + e.getMessage(), e);
                        btn_confirmar_cadastro_produto.setEnabled(true); // Reabilita o botão
                    });
        } else {
            exibirSnackbar("Erro interno: ID do produto não gerado para salvar.", view);
            btn_confirmar_cadastro_produto.setEnabled(true); // Reabilita o botão
        }
    }
    private void limparCampos(){
        edit_nome_produto.setText("");
        editDescricaoProduto.setText("");
        edit_preco_produto.setText("");
        editQuantidadeProduto.setText("");
        spinner_categoria_produto.setSelection(0);
        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        hint_image.setVisibility(View.VISIBLE);
        selectedImageUri= null;
    }
    private void solicitarPermissaoArmazenamento(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE_READ_STORAGE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_READ_STORAGE);
            }
        }
    }
    private void solicitaPermissaoCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
        }
    }
    private void exibirSnackbar(String erro, View view) {
        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.GREEN);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão de armazenamento concedida
            } else {
                // Permissão de armazenamento negada
                Toast.makeText(getActivity(), "Permissão negada para ler o armazenamento externo.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão da câmera concedida
            } else {
                // Permissão da câmera negada
                Toast.makeText(getActivity(), "Permissão negada para acessar a câmera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
