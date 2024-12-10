package com.example.climaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {

    List<Integer> listaImagenes;
    List<String> listaTemp, listaHora;


    public CardAdapter(List<Integer> listaImagenes, List<String> listaTemp, List<String> listaHora) {
        this.listaImagenes = listaImagenes;
        this.listaTemp = listaTemp;
        this.listaHora = listaHora;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardsViewHolder, int position) {

        int imagen = listaImagenes.get(position);
        String temp = listaTemp.get(position);
        String hora = listaHora.get(position);


        cardsViewHolder.imageView.setImageResource(imagen);
        cardsViewHolder.textTemp.setText(temp);
        cardsViewHolder.textHora.setText(hora);
    }

    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }
}
