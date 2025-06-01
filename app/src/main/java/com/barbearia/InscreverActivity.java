package com.barbearia;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class InscreverActivity extends AppCompatActivity {

    private EditText etEmailLogin, etSenhaLogin;
    private Button btLogin, btVoltar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscrever);

        mAuth = FirebaseAuth.getInstance();

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etSenhaLogin = findViewById(R.id.etSenhaLogin);
        btLogin = findViewById(R.id.btLogin);
        btVoltar = findViewById(R.id.btVoltar);

        btVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(InscreverActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();
        });

        btLogin.setOnClickListener(view -> {
            String email = etEmailLogin.getText().toString().trim();
            String senha = etSenhaLogin.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                Toast.makeText(InscreverActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(InscreverActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(InscreverActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMessage = "Erro ao fazer login.";
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                errorMessage = "Usuário não cadastrado.";
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMessage = "E-mail ou senha inválidos.";
                            } else if (e != null && e.getMessage() != null) {
                                errorMessage = e.getMessage();
                            }
                            Toast.makeText(InscreverActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
