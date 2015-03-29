package com.example.kronos.practica1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kronos on 16/03/2015.
 */
public class AdaptadorALGrupos  extends ArrayAdapter<Grupo> {
    private Context contexto;
    private ArrayList<Grupo> lista;
    private int recurso;
    private static LayoutInflater i;
    private ViewHolder vh;

    static class ViewHolder{
        public TextView  tvGrupos;
        public int posicion;
    }
    public AdaptadorALGrupos(Context context, int resource, ArrayList<Grupo> objects) {
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
          vh.tvGrupos=(TextView)convertView.findViewById(R.id.tvTexto);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.posicion=position;
       vh.tvGrupos.setText(lista.get(position).getNumero().toString()+" "+lista.get(position).getAcronimo().toString()+" "+lista.get(position).getLetra().toString());
        return convertView;
    }
}