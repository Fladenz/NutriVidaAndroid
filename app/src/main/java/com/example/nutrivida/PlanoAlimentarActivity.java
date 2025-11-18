package com.example.nutrivida;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class PlanoAlimentarActivity extends AppCompatActivity {


    private TextView caloriasTextView;
    private RecyclerView planoAlimentarRecyclerView;
    private Button voltarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityplanoalimentar_);


        caloriasTextView = findViewById(R.id.tv_calorias_recomendadas);
        planoAlimentarRecyclerView = findViewById(R.id.rv_plano_alimentar);
        voltarButton = findViewById(R.id.btn_voltar);


        List<Refeicao> plano = getPlanoAlimentarSimulado();
        int totalCalorias = 0;
        for (Refeicao r : plano) {
            totalCalorias += r.getCalorias();
        }
        // Use resource string if available, fallback to concatenation
        try {
            caloriasTextView.setText(getString(R.string.meta_diaria).replace("3000 ml", totalCalorias + " kcal"));
        } catch (Exception e){
            caloriasTextView.setText(totalCalorias + " kcal");
        }


        PlanoAlimentarAdapter adapter = new PlanoAlimentarAdapter(plano);
        planoAlimentarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        planoAlimentarRecyclerView.setAdapter(adapter);


        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private List<Refeicao> getPlanoAlimentarSimulado() {
        List<Refeicao> refeicoes = new ArrayList<>();
        refeicoes.add(new Refeicao("Café da Manhã", "Ovos mexidos, aveia e suco de laranja.", 430));
        refeicoes.add(new Refeicao("Almoço", "Frango grelhado, arroz integral e salada.", 625));
        refeicoes.add(new Refeicao("Lanche da Tarde", "Iogurte natural e mel.", 214));
        refeicoes.add(new Refeicao("Jantar", "Peixe grelhado e batata doce.", 460));
        return refeicoes;
    }


    public static class Refeicao {
        private final String nome;
        private final String descricao;
        private final int calorias;


        public Refeicao(String nome, String descricao, int calorias) {
            this.nome = nome;
            this.descricao = descricao;
            this.calorias = calorias;
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
        }


        @Override
        public int getItemCount() {
            return listaRefeicoes.size();
        }


        public static class RefeicaoViewHolder extends RecyclerView.ViewHolder {
            public final TextView nomeRefeicao;
            public final TextView descricaoRefeicao;


            public RefeicaoViewHolder(View itemView) {
                super(itemView);
                nomeRefeicao = itemView.findViewById(R.id.tv_nome_refeicao);
                descricaoRefeicao = itemView.findViewById(R.id.tv_descricao_refeicao);
            }
        }
    }
}
