package com.example.nutrivida;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;


public class PlanoAlimentarActivity extends AppCompatActivity {

    private static final String TAG = "PlanoAlimentarActivity";

    private TextView caloriasTextView;
    private RecyclerView planoAlimentarRecyclerView;
    private Button voltarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityplanoalimentar_);

        Log.d(TAG, "onCreate start");
        Toast.makeText(this, "Abrindo Plano Alimentar...", Toast.LENGTH_SHORT).show();

        caloriasTextView = findViewById(R.id.tv_calorias_recomendadas);
        planoAlimentarRecyclerView = findViewById(R.id.rv_plano_alimentar);
        voltarButton = findViewById(R.id.btn_voltar);

        List<Refeicao> planoOriginal = getPlanoAlimentarSimulado();

        String alergiasRaw = getIntent() != null ? getIntent().getStringExtra("alergias") : "";
        Log.d(TAG, "Alergias recebidas: " + alergiasRaw);

        List<String> alergias = parseAlergias(alergiasRaw);
        Log.d(TAG, "Alergias normalizadas: " + alergias);

        List<Refeicao> planoFiltrado = filterPlanoByAlergias(planoOriginal, alergias);

        if (planoFiltrado.isEmpty()) {
            Toast.makeText(this, "Nenhuma refeição compatível com suas alergias foi encontrada.", Toast.LENGTH_LONG).show();
        }

        int totalCalorias = 0;
        for (Refeicao r : planoFiltrado) {
            totalCalorias += r.getCalorias();
        }

        // Prefer the calculated daily calories if passed via Intent
        int caloriasDiariasExtra = getIntent() != null ? getIntent().getIntExtra("calorias_diarias", -1) : -1;
        try {
            if (caloriasDiariasExtra > 0) {
                caloriasTextView.setText("Recomendado: " + caloriasDiariasExtra + " kcal/dia");
            } else {
                caloriasTextView.setText(getString(R.string.meta_diaria).replace("3000 ml", totalCalorias + " kcal"));
            }
        } catch (Exception e){
            if (caloriasDiariasExtra > 0) caloriasTextView.setText("Recomendado: " + caloriasDiariasExtra + " kcal/dia");
            else caloriasTextView.setText(totalCalorias + " kcal");
        }


        PlanoAlimentarAdapter adapter = new PlanoAlimentarAdapter(planoFiltrado);
        planoAlimentarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        planoAlimentarRecyclerView.setAdapter(adapter);


        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Log.d(TAG, "onCreate end");
    }

    private List<String> parseAlergias(String raw) {
        if (raw == null || raw.trim().isEmpty()) return new ArrayList<>();
        // Normalize input and replace Portuguese connectors with commas
        String normalizedRaw = raw.trim().toLowerCase(Locale.ROOT);
        normalizedRaw = normalizedRaw.replaceAll("\\s+e\\s+", ",");
        normalizedRaw = normalizedRaw.replaceAll("\\s+ou\\s+", ",");

        String[] parts;
        // If user used explicit separators, split on them; otherwise, split on whitespace so "ovo leite" also works
        if (normalizedRaw.contains(",") || normalizedRaw.contains(";") || normalizedRaw.contains("/") || normalizedRaw.contains("\n")) {
            parts = normalizedRaw.split("[,;\\n/]+");
        } else {
            parts = normalizedRaw.split("\\s+");
        }

        List<String> out = new ArrayList<>();
        for (String p : parts) {
            String t = p.trim();
            if (t.isEmpty()) continue;
            String n = normalize(t);
            if (!n.isEmpty() && !n.equals("e") && !n.equals("ou")) out.add(n);
        }
        return out;
    }

    private String normalize(String s) {
        if (s == null) return "";
        String noAccents = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return noAccents.toLowerCase(Locale.ROOT).trim();
    }

    private List<Refeicao> filterPlanoByAlergias(List<Refeicao> plano, List<String> alergias) {
        if (alergias == null || alergias.isEmpty()) return plano;

        // substitutions map: allergen(normalized) -> suggested substitute (text)
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("ovo", "tofu");
        substitutions.put("ovos", "tofu");
        substitutions.put("gema", "tofu");
        substitutions.put("clara", "tofu");
        substitutions.put("leite", "bebida vegetal");
        substitutions.put("leite de vaca", "bebida vegetal");
        substitutions.put("iogurte", "iogurte vegetal");
        substitutions.put("iogurte natural", "iogurte vegetal");
        substitutions.put("frango", "proteína vegetal grelhada (tofu/tempeh)");
        substitutions.put("peito de frango", "proteína vegetal grelhada (tofu/tempeh)");
        substitutions.put("peixe", "proteína vegetal (tofu)");
        substitutions.put("salmão", "proteína vegetal (tofu)");
        substitutions.put("mel", "geleia de frutas");
        substitutions.put("aveia", "flocos de quinoa");
        substitutions.put("amendoim", "sementes de girassol");
        substitutions.put("amendoins", "sementes de girassol");
        substitutions.put("amendoa", "sementes de girassol");
        substitutions.put("amendoas", "sementes de girassol");
        substitutions.put("castanha", "sementes mistas");
        substitutions.put("castanhas", "sementes mistas");
        substitutions.put("noz", "sementes mistas");
        substitutions.put("queijo", "queijo vegetal");
        substitutions.put("creme de leite", "creme vegetal");
        substitutions.put("manteiga", "margarina vegetal");
        substitutions.put("leite condensado", "doce vegetal");
        substitutions.put("trigo", "quinoa/cozinha sem glúten");
        substitutions.put("gluten", "opções sem glúten");

        List<Refeicao> out = new ArrayList<>();

        for (Refeicao r : plano) {
            String nomeN = normalize(r.getNome());
            String descN = normalize(r.getDescricao());

            boolean containsAllergen = false;
            boolean canSubstituteAll = true;
            StringBuilder subsNote = new StringBuilder();

            for (String a : alergias) {
                if (a.isEmpty()) continue;
                // if allergy token appears inside meal name or description
                if (nomeN.contains(a) || descN.contains(a)) {
                    containsAllergen = true;
                    String sub = null;
                    // try direct match
                    if (substitutions.containsKey(a)) sub = substitutions.get(a);
                    else {
                        // try to find key that contains the allergen token (e.g., 'leite' in 'leite de vaca')
                        for (Map.Entry<String, String> e : substitutions.entrySet()) {
                            if (e.getKey().contains(a) || a.contains(e.getKey())) {
                                sub = e.getValue();
                                break;
                            }
                        }
                    }

                    if (sub != null) {
                        subsNote.append(a).append(" -> ").append(sub).append("; ");
                    } else {
                        // no substitute available for this allergen in this meal
                        canSubstituteAll = false;
                        break;
                    }
                }
            }

            if (!containsAllergen) {
                // meal safe — keep original
                out.add(r);
            } else {
                if (canSubstituteAll) {
                    // produce an adapted meal: append substitutions notes to description
                    String novoNome = r.getNome() + " (adaptado)";
                    String novaDesc = r.getDescricao() + " (Substituições: " + subsNote.toString().trim() + ")";
                    // create adapted gramatura note — keep same gramatura for now
                    out.add(new Refeicao(novoNome, novaDesc, r.getCalorias(), r.getGramatura()));
                } else {
                    // unable to substitute, skip this meal
                    Log.d(TAG, "Removendo refeição '" + r.getNome() + "' por conter alergênicos sem substituto.");
                }
            }
        }

        return out;
    }


    private List<Refeicao> getPlanoAlimentarSimulado() {
        List<Refeicao> refeicoes = new ArrayList<>();
        // nome, descricao, calorias, gramatura (porção sugerida)
        refeicoes.add(new Refeicao("Café da Manhã", "Ovos mexidos, aveia e suco de laranja.", 430, "2 ovos (100g), 40g aveia, 200ml suco"));
        refeicoes.add(new Refeicao("Almoço", "Frango grelhado, arroz integral e salada.", 625, "120g frango, 150g arroz, 80g salada"));
        refeicoes.add(new Refeicao("Lanche da Tarde", "Iogurte natural e mel.", 214, "170g iogurte, 10g mel"));
        refeicoes.add(new Refeicao("Jantar", "Peixe grelhado e batata doce.", 460, "150g peixe, 150g batata doce"));
        return refeicoes;
    }


    public static class Refeicao {
        private final String nome;
        private final String descricao;
        private final int calorias;
        private final String gramatura;


        public Refeicao(String nome, String descricao, int calorias, String gramatura) {
            this.nome = nome;
            this.descricao = descricao;
            this.calorias = calorias;
            this.gramatura = gramatura;
        }


        public String getNome() {
            return nome;
        }


        public String getDescricao() {
            return descricao;
        }


        public int getCalorias() {
            return calorias;
        }

        public String getGramatura() { return gramatura; }
    }


    public static class PlanoAlimentarAdapter extends RecyclerView.Adapter<PlanoAlimentarAdapter.RefeicaoViewHolder> {
        private final List<Refeicao> listaRefeicoes;


        public PlanoAlimentarAdapter(List<Refeicao> listaRefeicoes) {
            this.listaRefeicoes = listaRefeicoes;
        }


        @NonNull
        @Override
        public RefeicaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refeicao, parent, false);
            return new RefeicaoViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull RefeicaoViewHolder holder, int position) {
            Refeicao refeicao = listaRefeicoes.get(position);
            holder.nomeRefeicao.setText(refeicao.getNome());
            holder.descricaoRefeicao.setText(refeicao.getDescricao() + " - " + refeicao.getCalorias() + " kcal");
            holder.gramaturaRefeicao.setText("Porção: " + refeicao.getGramatura());
        }


        @Override
        public int getItemCount() {
            return listaRefeicoes.size();
        }


        public static class RefeicaoViewHolder extends RecyclerView.ViewHolder {
            public final TextView nomeRefeicao;
            public final TextView descricaoRefeicao;
            public final TextView gramaturaRefeicao;


            public RefeicaoViewHolder(View itemView) {
                super(itemView);
                nomeRefeicao = itemView.findViewById(R.id.tv_nome_refeicao);
                descricaoRefeicao = itemView.findViewById(R.id.tv_descricao_refeicao);
                gramaturaRefeicao = itemView.findViewById(R.id.tv_gramatura_refeicao);
            }
        }
    }
}
