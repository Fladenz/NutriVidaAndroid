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

        try {
            caloriasTextView.setText(getString(R.string.meta_diaria).replace("3000 ml", totalCalorias + " kcal"));
        } catch (Exception e){
            caloriasTextView.setText(totalCalorias + " kcal");
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
        // split por vírgula, ponto-e-vírgula ou espaço, e normalize
        String[] parts = raw.split("[;,\\n]+|\\s+");
        List<String> out = new ArrayList<>();
        for (String p : parts) {
            String n = normalize(p);
            if (!n.isEmpty()) out.add(n);
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
        List<Refeicao> out = new ArrayList<>();
        for (Refeicao r : plano) {
            String nome = normalize(r.getNome());
            String desc = normalize(r.getDescricao());
            boolean containsAlergia = false;
            for (String a : alergias) {
                if (a.isEmpty()) continue;
                if (nome.contains(a) || desc.contains(a)) {
                    containsAlergia = true;
                    break;
                }
            }
            if (!containsAlergia) out.add(r);
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
