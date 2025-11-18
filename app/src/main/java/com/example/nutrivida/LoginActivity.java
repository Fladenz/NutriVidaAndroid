package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.Executors;
import com.example.nutrivida.data.DatabaseClient;
import com.example.nutrivida.data.User;


public class LoginActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        forgotPasswordTextView = findViewById(R.id.tv_forgot_password);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();


                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    // validate against local DB on background thread
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DatabaseClient dbClient = DatabaseClient.getInstance(LoginActivity.this);
                                com.example.nutrivida.data.UserDao userDao = dbClient.getAppDatabase().userDao();
                                User user = userDao.findByEmail(email);
                                if (user == null) {
                                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show());
                                    return;
                                }

                                if (!user.password.equals(password)) {
                                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Senha incorreta.", Toast.LENGTH_SHORT).show());
                                    return;
                                }

                                runOnUiThread(() -> {
                                    Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, QuestionarioActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erro ao autenticar: " + e.getMessage(), Toast.LENGTH_LONG).show());
                            }
                        }
                    });
                }
            }
        });


        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Funcionalidade de recuperação de senha em desenvolvimento.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


