package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AlergiasActivity extends AppCompatActivity {

    private static final String TAG = "AlergiasActivity";

    private EditText alergiasEditText;
    private Button concluirButton;
    private Button voltarButton;
    private Button semAlergiasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "onCreate start");
            setContentView(R.layout.activity_alergias);

            alergiasEditText = findViewById(R.id.et_alergias_usuario);
            concluirButton = findViewById(R.id.btn_concluir);
            voltarButton = findViewById(R.id.tv_voltar);
            semAlergiasButton = findViewById(R.id.tv_sem_alergias);

            Log.d(TAG, "views bound successfully");

            concluirButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String alergiasDigitadas = alergiasEditText.getText().toString().trim();

                    if (alergiasDigitadas.isEmpty()) {
                        Toast.makeText(AlergiasActivity.this, "Nenhuma alergia informada. Prosseguindo...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AlergiasActivity.this, "Alergias salvas: " + alergiasDigitadas, Toast.LENGTH_LONG).show();
                    }

                    navegarParaPlanoAlimentar(alergiasDigitadas);
                }
            });

            semAlergiasButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AlergiasActivity.this, "Prosseguindo sem restrições de alergia.", Toast.LENGTH_SHORT).show();
                    navegarParaPlanoAlimentar("");
                }
            });

            voltarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            Log.d(TAG, "onCreate end");
        } catch (Exception e) {
            Log.e(TAG, "Exception in onCreate", e);
            Toast.makeText(this, "Erro ao iniciar AlergiasActivity: " + e.toString(), Toast.LENGTH_LONG).show();
            // keep the Activity alive briefly to allow user to see the Toast, then finish
            e.printStackTrace();
            finish();
        }
    }

    private void navegarParaPlanoAlimentar(String alergias) {
        // Inicia a activity do plano alimentar e finaliza esta
        try {
            Intent intent = new Intent(this, PlanoAlimentarActivity.class);
            intent.putExtra("alergias", alergias != null ? alergias : "");
            // forward calorias_diarias if present
            int calorias = getIntent() != null ? getIntent().getIntExtra("calorias_diarias", -1) : -1;
            if (calorias > 0) intent.putExtra("calorias_diarias", calorias);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao iniciar PlanoAlimentarActivity", e);
            Toast.makeText(this, "Erro ao abrir Plano Alimentar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
