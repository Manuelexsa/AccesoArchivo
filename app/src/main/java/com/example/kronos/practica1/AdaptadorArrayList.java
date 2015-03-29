package com.example.kronos.practica1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kronos on 16/03/2015.
 */
public class AdaptadorArrayList extends ArrayAdapter<Datos> {
    private Context contexto;
    private ArrayList<Datos> lista;
    private int recurso;
    private static LayoutInflater i;
    private ViewHolder vh;

    static class ViewHolder{
        public TextView tvNivel;
        public int posicion;
    }
    public AdaptadorArrayList(Context context, int resource, ArrayList<Datos> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvNivel=(TextView)convertView.findViewById(R.id.tvTexto);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.posicion=position;
        vh.tvNivel.setText(lista.get(position).getNivel().toString());
        return convertView;
    }
}