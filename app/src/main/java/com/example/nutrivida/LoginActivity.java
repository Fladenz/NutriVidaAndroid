package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.Executors;
import com.example.nutrivida.data.DatabaseClient;
import com.example.nutrivida.data.User;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(LoginActivity.this, "Validando usuário...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "iniciando validação para: " + email);

                // validate against local DB on background thread
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DatabaseClient dbClient = DatabaseClient.getInstance(LoginActivity.this);
                            com.example.nutrivida.data.UserDao userDao = dbClient.getAppDatabase().userDao();

                            Log.d(TAG, "Consultando usuário no DB...");
                            User user = userDao.findByEmail(email);

                            if (user == null) {
                                Log.w(TAG, "Usuário não encontrado para email=" + email);
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show());
                                return;
                            }

                            if (user.password == null) {
                                Log.w(TAG, "Senha do usuário é nula no DB para email=" + email);
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Senha inválida no cadastro.", Toast.LENGTH_SHORT).show());
                                return;
                            }

                            if (!user.password.equals(password)) {
                                Log.w(TAG, "Senha incorreta para email=" + email);
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Senha incorreta.", Toast.LENGTH_SHORT).show());
                                return;
                            }

                            Log.d(TAG, "Login bem-sucedido, preparando para abrir QuestionarioActivity");
                            runOnUiThread(() -> {
                                try {
                                    Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, QuestionarioActivity.class);
                                    Log.d(TAG, "Iniciando QuestionarioActivity intent");
                                    startActivity(intent);
                                    // Nota: não finalizamos a LoginActivity aqui para facilitar depuração se a próxima Activity falhar
                                } catch (Exception ex) {
                                    Log.e(TAG, "Erro ao iniciar QuestionarioActivity", ex);
                                    Toast.makeText(LoginActivity.this, "Erro ao abrir Questionario: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "Exceção durante autenticação", e);
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erro ao autenticar: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        }
                    }
                });
            }
        });
    }
}
