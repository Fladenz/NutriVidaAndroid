package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MonitoramentoActivity extends AppCompatActivity {


    private TextView pesoInicialTextView, pesoAtualTextView, pesoMetaTextView;
    private EditText novoPesoEditText;
    private Button registrarPesoButton, voltarButton;


    private double pesoInicial = 75.0;
    private double pesoAtual = 72.0;
    private double pesoMeta = 70.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoramento);


        pesoInicialTextView = findViewById(R.id.tv_peso_inicial);
        pesoAtualTextView = findViewById(R.id.tv_peso_atual);
        pesoMetaTextView = findViewById(R.id.tv_peso_meta);
        novoPesoEditText = findViewById(R.id.et_novo_peso);
        registrarPesoButton = findViewById(R.id.btn_registrar_peso);
        voltarButton = findViewById(R.id.btn_voltar);


        atualizarValores();


        registrarPesoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoPesoStr = novoPesoEditText.getText().toString().trim();
                if (!novoPesoStr.isEmpty()) {
                    try {
                        double novoPeso = Double.parseDouble(novoPesoStr);
                        pesoAtual = novoPeso;
                        atualizarValores();
                        Toast.makeText(MonitoramentoActivity.this, "Peso registrado com sucesso!", Toast.LENGTH_SHORT).show();
                        novoPesoEditText.setText("");
                    } catch (NumberFormatException e) {
                        Toast.makeText(MonitoramentoActivity.this, "Por favor, insira um peso válido.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MonitoramentoActivity.this, "Por favor, insira um peso.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void atualizarValores() {
        pesoInicialTextView.setText(String.format("%.1f kg", pesoInicial));
        pesoAtualTextView.setText(String.format("%.1f kg", pesoAtual));
        pesoMetaTextView.setText(String.format("%.1f kg", pesoMeta));
    }
}
