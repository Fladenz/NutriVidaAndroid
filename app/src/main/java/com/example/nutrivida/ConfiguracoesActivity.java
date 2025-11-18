package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ConfiguracoesActivity extends AppCompatActivity {


    private TextView editarPerfilTextView, notificacoesTextView, ajudaTextView, privacidadeTextView, voltarTextView;
    private Button logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);


        editarPerfilTextView = findViewById(R.id.tv_editar_perfil_text);
        notificacoesTextView = findViewById(R.id.tv_notificacoes_text);
        ajudaTextView = findViewById(R.id.tv_ajuda_text);
        privacidadeTextView = findViewById(R.id.tv_privacidade_text);
        voltarTextView = findViewById(R.id.tv_voltar);
        logoutButton = findViewById(R.id.btn_logout);


        editarPerfilTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfiguracoesActivity.this, "Tela de Editar Perfil em desenvolvimento.", Toast.LENGTH_SHORT).show();
            }
        });


        notificacoesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfiguracoesActivity.this, "Tela de Notificações em desenvolvimento.", Toast.LENGTH_SHORT).show();
            }
        });


        ajudaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfiguracoesActivity.this, "Tela de Ajuda em desenvolvimento.", Toast.LENGTH_SHORT).show();
            }
        });


        privacidadeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfiguracoesActivity.this, "Política de Privacidade em desenvolvimento.", Toast.LENGTH_SHORT).show();
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfiguracoesActivity.this, "Saindo da conta...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConfiguracoesActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });


        voltarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
