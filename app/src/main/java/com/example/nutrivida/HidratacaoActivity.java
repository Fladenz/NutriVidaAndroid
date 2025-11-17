package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class HidratacaoActivity extends AppCompatActivity {

    private TextView volumeAtualTextView;
    private ProgressBar progressBar;
    private Button registrarCopoButton, voltarButton;

    private final int META_ML = 3000;
    private final int COPO_ML = 250;
    private int volumeConsumido = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidratacao);

        volumeAtualTextView = findViewById(R.id.tv_volume_atual);
        progressBar = findViewById(R.id.pb_hidratacao);
        registrarCopoButton = findViewById(R.id.btn_registrar_copo);
        voltarButton = findViewById(R.id.btn_voltar_hidratacao);


        atualizarProgresso();


        registrarCopoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volumeConsumido += COPO_ML;
                atualizarProgresso();
                Toast.makeText(HidratacaoActivity.this, COPO_ML + " ml de água registrados!", Toast.LENGTH_SHORT).show();
            }
        });


        voltarButton.setOnClickListener(v -> onBackPressed());
    }

    private void atualizarProgresso() {
        volumeAtualTextView.setText(volumeConsumido + " ml / " + META_ML + " ml");


        int progressoPercentual = (int) ((double) volumeConsumido / META_ML * 100);
        progressBar.setProgress(progressoPercentual);




        if (volumeConsumido >= META_ML) {
            Toast.makeText(this, "Parabéns! Meta de hidratação atingida!", Toast.LENGTH_LONG).show();
        }
    }
}
