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

                    int caloriasDiarias = calcularCaloriasDiarias(peso, altura, idade, sexoPos, objPos, attPos);
                    Log.d(TAG, "Calorias diarias calculadas: " + caloriasDiarias);

                    try {
                        Intent intent = new Intent(QuestionarioActivity.this, AlergiasActivity.class);
                        intent.putExtra("calorias_diarias", caloriasDiarias);
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

    /**
     * Calcula calorias diárias usando Mifflin-St Jeor e ajuste por nível de atividade e objetivo.
     * parâmetros: sexoPos/objPos/attPos seguem os índices do array (0 = placeholder).
     */
    private int calcularCaloriasDiarias(String pesoStr, String alturaStr, String idadeStr, int sexoPos, int objPos, int attPos) {
        try {
            double peso = pesoStr.isEmpty() ? 70.0 : Double.parseDouble(pesoStr); // kg
            double altura = alturaStr.isEmpty() ? 170.0 : Double.parseDouble(alturaStr); // cm
            int idade = idadeStr.isEmpty() ? 30 : Integer.parseInt(idadeStr);

            // Mifflin-St Jeor
            double s = 5; // default male
            if (sexoPos == 2) s = -161; // female
            else if (sexoPos == 1) s = 5;
            else if (sexoPos > 2) s = 0; // 'Outro' fallback

            double bmr = 10.0 * peso + 6.25 * altura - 5.0 * idade + s;

            // activity factor mapping (arrays.xml has placeholder at index 0)
            double activityFactor = 1.2; // sedentary
            switch (attPos) {
                case 1: activityFactor = 1.2; break; // trabalho sedentario
                case 2: activityFactor = 1.375; break; // leve
                case 3: activityFactor = 1.55; break; // moderado
                case 4: activityFactor = 1.725; break; // intenso
                default: activityFactor = 1.2; break;
            }

            double tdee = bmr * activityFactor;

            // objetivo adjustment
            int ajuste = 0;
            switch (objPos) {
                case 1: ajuste = -500; break; // perder gordura
                case 2: ajuste = 0; break; // manter
                case 3: ajuste = 500; break; // ganhar massa
                default: ajuste = 0; break;
            }

            double resultado = tdee + ajuste;
            if (resultado < 1200) resultado = 1200; // mínimo recomendado
            return (int) Math.round(resultado);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao calcular calorias, usando fallback 2000", e);
            return 2000;
        }
    }
}
