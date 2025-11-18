package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DashboardActivity extends AppCompatActivity {


    private Button planoAlimentarButton, receitasButton, monitoramentoButton, configuracoesButton;
    private TextView welcomeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        planoAlimentarButton = findViewById(R.id.btn_plano_alimentar);
        receitasButton = findViewById(R.id.btn_receitas);
        monitoramentoButton = findViewById(R.id.btn_monitoramento);
        configuracoesButton = findViewById(R.id.btn_configuracoes);
        welcomeTextView = findViewById(R.id.tv_welcome_dashboard);


        String nomeUsuario = "Arthur";
        welcomeTextView.setText("Olá, " + nomeUsuario + "!");


        planoAlimentarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PlanoAlimentarActivity.class);
                startActivity(intent);
            }
        });


//        receitasButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(DashboardActivity.this, ReceitasActivity.class);
////                startActivity(intent);
//            }
//        });


//        monitoramentoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DashboardActivity.this, MonitoramentoActivity.class);
//                startActivity(intent);
//            }
//        });


//        configuracoesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DashboardActivity.this, ConfiguracoesActivity.class);
//                startActivity(intent);
//            }
//        });


//        // declarar e inicializar btnRefeicoesDia antes de usar
//        Button btnRefeicoesDia = findViewById(R.id.btn_refeicoes_dia);
//        btnRefeicoesDia.setOnClickListener(v -> {
//            Intent intent = new Intent(DashboardActivity.this, RefeicoesDiaActivity.class);
//            startActivity(intent);
//        });


//        Button btnHidratacao = findViewById(R.id.btn_hidratacao);
//        btnHidratacao.setOnClickListener(v -> {
//            Intent intent = new Intent(DashboardActivity.this, HidratacaoActivity.class);
//            startActivity(intent);
//        });


    }
}
