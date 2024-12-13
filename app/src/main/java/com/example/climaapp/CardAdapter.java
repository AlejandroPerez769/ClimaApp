package com.example.climaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adaptador para mostrar tarjetas en un RecyclerView.
 * Cada tarjeta contiene una imagen, temperatura y hora.
 */
public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {

    /**
     * Lista de recursos de imagenes que se mostrarán en las tarjetas.
     */
    private List<Integer> listaImagenes;

    /**
     * Lista de cadenas que representan las temperaturas de cada tarjeta.
     */
    private List<String> listaTemp;

    /**
     * Lista de cadenas que representan las horas de cada tarjeta.
     */
    private List<String> listaHora;

    /**
     * Constructor que inicializa las listas de imágenes, temperaturas y horas.
     *
     * @param listaImagenes Lista de recursos de imágenes.
     * @param listaTemp Lista de temperaturas.
     * @param listaHora Lista de horas.
     */
    public CardAdapter(List<Integer> listaImagenes, List<String> listaTemp, List<String> listaHora) {
        this.listaImagenes = listaImagenes;
        this.listaTemp = listaTemp;
        this.listaHora = listaHora;
    }

    /**
     * Crea nuevas vistas (invocado por el administrador del RecyclerView).
     *
     * @param viewGroup Grupo de vistas al que se adjunta la nueva vista.
     * @param i Posición de la vista.
     * @return Un nuevo CardViewHolder que contiene la vista inflada.
     */
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflar la vista desde el archivo XML de diseño
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new CardViewHolder(view);
    }

    /**
     * Reemplaza el contenido de una vista (invocado por el administrador del RecyclerView).
     *
     * @param cardsViewHolder El holder que contiene las vistas a actualizar.
     * @param position La posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardsViewHolder, int position) {
        // Obtener los datos para la posición actual
        int imagen = listaImagenes.get(position);
        String temp = listaTemp.get(position);
        String hora = listaHora.get(position);

        // Asignar los datos a las vistas del holder
        cardsViewHolder.imageView.setImageResource(imagen);
        cardsViewHolder.textTemp.setText(temp);
        cardsViewHolder.textHora.setText(hora);
    }

    /**
     * Devuelve el número total de elementos en la lista.
     *
     * @return El tamaño de la lista de imágenes.
     */
    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }
}
