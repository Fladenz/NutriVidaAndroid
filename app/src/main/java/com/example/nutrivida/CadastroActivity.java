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
import com.example.nutrivida.data.User;
import com.example.nutrivida.data.DatabaseClient;


public class CadastroActivity extends AppCompatActivity {


    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private TextView loginLinkTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        fullNameEditText = findViewById(R.id.et_full_name);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        registerButton = findViewById(R.id.btn_cadastrar);
        loginLinkTextView = findViewById(R.id.tv_login_link);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();


                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!password.equals(confirmPassword)) {
                    Toast.makeText(CadastroActivity.this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // persist user using Room on background thread
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DatabaseClient dbClient = DatabaseClient.getInstance(CadastroActivity.this);
                            com.example.nutrivida.data.UserDao userDao = dbClient.getAppDatabase().userDao();

                            // check if email already exists
                            User existing = userDao.findByEmail(email);
                            if (existing != null) {
                                runOnUiThread(() -> Toast.makeText(CadastroActivity.this, "Email já cadastrado.", Toast.LENGTH_SHORT).show());
                                return;
                            }

                            User newUser = new User(fullName, email, password);
                            long id = userDao.insert(newUser);

                            runOnUiThread(() -> {
                                if (id > 0) {
                                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CadastroActivity.this, QuestionarioActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CadastroActivity.this, "Falha ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(CadastroActivity.this, "Erro ao salvar usuário: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        }
                    }
                });
            }
        });


        loginLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
