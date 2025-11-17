package com.example.nutrivida;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class QuestionarioActivity extends AppCompatActivity {

    private static final String TAG = "QuestionarioActivity";
    private static final boolean DEBUG_SKIP_VALIDATION = true; // DEBUG: pular validação temporariamente para testar navegação

    private EditText pesoEditText;
    private EditText alturaEditText;
    private EditText idadeEditText;
    private Spinner sexoSpinner;
    private Spinner objetivoSpinner;
    private Spinner atividadeSpinner;
    private Button proximoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_questionario);


            pesoEditText = findViewById(R.id.et_peso);
            alturaEditText = findViewById(R.id.et_altura);
            idadeEditText = findViewById(R.id.et_idade);
            sexoSpinner = findViewById(R.id.sp_sexo);
            objetivoSpinner = findViewById(R.id.sp_objetivo);
            atividadeSpinner = findViewById(R.id.sp_atividade);
            proximoButton = findViewById(R.id.btn_proximo);


            ArrayAdapter<CharSequence> sexoAdapter = ArrayAdapter.createFromResource(this,
                    R.array.opcoes_sexo, android.R.layout.simple_spinner_item);
            sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sexoSpinner.setAdapter(sexoAdapter);


            ArrayAdapter<CharSequence> objetivoAdapter = ArrayAdapter.createFromResource(this,
                    R.array.opcoes_objetivo, android.R.layout.simple_spinner_item);
            objetivoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            objetivoSpinner.setAdapter(objetivoAdapter);


            ArrayAdapter<CharSequence> atividadeAdapter = ArrayAdapter.createFromResource(this,
                    R.array.opcoes_atividade, android.R.layout.simple_spinner_item);
            atividadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            atividadeSpinner.setAdapter(atividadeAdapter);


            proximoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "btn_proximo clicked");
                    // Debug: log current values so we can see why validarCampos may fail
                    String peso = pesoEditText.getText().toString();
                    String altura = alturaEditText.getText().toString();
                    String idade = idadeEditText.getText().toString();
                    int sexoPos = sexoSpinner.getSelectedItemPosition();
                    int objPos = objetivoSpinner.getSelectedItemPosition();
                    int attPos = atividadeSpinner.getSelectedItemPosition();
                    Log.d(TAG, "values -> peso:" + peso + " altura:" + altura + " idade:" + idade +
                            " sexoPos:" + sexoPos + " objPos:" + objPos + " attPos:" + attPos);

                    if (!DEBUG_SKIP_VALIDATION && !validarCampos()) {
                        // show reason via Toast is already done in validarCampos; keep extra log
                        Toast.makeText(QuestionarioActivity.this, "Validação falhou — verifique os campos.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Intent intent = new Intent(QuestionarioActivity.this, AlergiasActivity.class);
                        startActivity(intent);
                        Log.d(TAG, "Started AlergiasActivity");
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao iniciar AlergiasActivity", e);
                        Toast.makeText(QuestionarioActivity.this, "Erro ao abrir Alergias: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao iniciar QuestionarioActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "onCreate error", e);
        }
    }


    private boolean validarCampos() {
        if (pesoEditText.getText().toString().isEmpty() ||
                alturaEditText.getText().toString().isEmpty() ||
                idadeEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos numéricos.", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (sexoSpinner.getSelectedItemPosition() == 0 ||
                objetivoSpinner.getSelectedItemPosition() == 0 ||
                atividadeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Selecione uma opção para todos os campos.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
