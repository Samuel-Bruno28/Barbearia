package com.barbearia;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etSenha, etConfirmaSenha;
    private Button btCadastrar, btVoltarCadastro;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);


        mAuth = FirebaseAuth.getInstance();


        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etConfirmaSenha = findViewById(R.id.etConfirmaSenha);
        btCadastrar = findViewById(R.id.btCadastrar);
        btVoltarCadastro = findViewById(R.id.btVoltarCadastro);

        // Botão para voltar à tela de introdução
        btVoltarCadastro.setOnClickListener(view -> {
            Intent intent = new Intent(CadastroActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();
        });

        // Botão para cadastrar usuário
        btCadastrar.setOnClickListener(view -> {
            String nome = etNome.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();
            String confirmaSenha = etConfirmaSenha.getText().toString().trim();

            if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmaSenha)) {
                Toast.makeText(CadastroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmaSenha)) {
                Toast.makeText(CadastroActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Encerra a tela de cadastro
                        } else {
                            String mensagemTraduzida = "Erro ao cadastrar. Tente novamente.";
                            Exception exception = task.getException();

                            if (exception instanceof FirebaseAuthException) {
                                String errorCode = ((FirebaseAuthException) exception).getErrorCode();

                                switch (errorCode) {
                                    case "ERROR_INVALID_EMAIL":
                                        mensagemTraduzida = "O e-mail digitado é inválido.";
                                        break;
                                    case "ERROR_WEAK_PASSWORD":
                                        mensagemTraduzida = "A senha é muito fraca. Ela deve ter pelo menos 6 caracteres.";
                                        break;
                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        mensagemTraduzida = "Este e-mail já está cadastrado.";
                                        break;
                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        mensagemTraduzida = "Cadastro com e-mail e senha está desativado.";
                                        break;
                                    case "ERROR_NETWORK_REQUEST_FAILED":
                                        mensagemTraduzida = "Erro de conexão. Verifique sua internet.";
                                        break;
                                    default:
                                        mensagemTraduzida = "Erro: " + errorCode;
                                        break;
                                }
                            }

                            Toast.makeText(CadastroActivity.this, mensagemTraduzida, Toast.LENGTH_LONG).show();
                        }
                    });
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
