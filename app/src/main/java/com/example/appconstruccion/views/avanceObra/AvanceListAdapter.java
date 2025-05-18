package com.example.appconstruccion.views.avanceObra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appconstruccion.R;
import com.example.appconstruccion.views.avanceObra.AvanceObraData;

import java.util.List;

public class AvanceListAdapter extends RecyclerView.Adapter<AvanceListAdapter.AvanceViewHolder> {

    private List<AvanceObraData> listaAvances;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditarClick(int position);
        void onEliminarClick(int avanceId); // Solo pasa el ID
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AvanceListAdapter(List<com.example.appconstruccion.views.avanceObra.AvanceObraData> listaAvances) {
        this.listaAvances = listaAvances;
    }

    @Override
    public AvanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_avance, parent, false);
        return new AvanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AvanceViewHolder holder, int position) {
        com.example.appconstruccion.views.avanceObra.AvanceObraData avance = listaAvances.get(position);
        holder.textViewFechaAvance.setText("Fecha: " + avance.getFecha());
        holder.textViewPorcentajeAvance.setText("Avance: " + avance.getPorcentajeAvance() + "%");
        holder.textViewDescripcionAvance.setText("Descripción: " + avance.getDescripcion());

        holder.buttonEditarAvance.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(position);
            }
        });

        holder.buttonEliminarAvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onEliminarClick(listaAvances.get(adapterPosition).getId());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAvances.size();
    }

    public static class AvanceViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFechaAvance;
        public TextView textViewPorcentajeAvance;
        public TextView textViewDescripcionAvance;
        public Button buttonEditarAvance;
        public Button buttonEliminarAvance;

        public AvanceViewHolder(View itemView) {
            super(itemView);
            textViewFechaAvance = itemView.findViewById(R.id.textViewFechaAvance);
            textViewPorcentajeAvance = itemView.findViewById(R.id.textViewPorcentajeAvance);
            textViewDescripcionAvance = itemView.findViewById(R.id.textViewDescripcionAvance);
            buttonEditarAvance = itemView.findViewById(R.id.buttonEditarAvance);
            buttonEliminarAvance = itemView.findViewById(R.id.buttonEliminarAvance);
        }
    }
}