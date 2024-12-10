package com.example.climaapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView textHora, textTemp;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imagen);
        textHora = itemView.findViewById(R.id.hora);
        textTemp = itemView.findViewById(R.id.tempTexto);
    }
}
