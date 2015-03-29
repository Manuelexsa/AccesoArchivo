package com.example.kronos.practica1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class Principal extends Activity {

    private ListView lv;

    public enum Estados {nivel, grupos};

    public static Estados estado = Estados.nivel;
    private ArrayList<Datos> ListaDatos;
    private ArrayList<Grupo> ListaGrupos;
    private AdaptadorArrayList adD;
    private AdaptadorALGrupos adG;
    private UtilIO util;
    private XmlIO xml;
    private String nombre="archivo.xml";
    private File f;

    /**********************************************************************************************/
    /* lifecycle */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        ListaDatos = new ArrayList<Datos>();
        ListaGrupos = new ArrayList<Grupo>();
        lv = (ListView) findViewById(R.id.lvVista);
        xml=new XmlIO(ListaDatos,0);
        f=new File(getExternalFilesDir(null),nombre);
        xml.leer(f);
        this.ListaDatos=xml.getListaDatos();
        listarDatos();
    }

    /**********************************************************************************************/
    /* menu opciones */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.anadirn) {
            addLevel();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/
    /* menu contextual */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (estado==Estados.nivel) {
            inflater.inflate(R.menu.contextualniveles, menu);
        } else if (estado==Estados.grupos) {
            inflater.inflate(R.menu.contextualgrupos, menu);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (estado==Estados.nivel) {
            if (id == R.id.editar) {
                editLevel(index);
            } else if (id == R.id.borra) {
                removeLevel(index);
            } else if (id == R.id.anadirG) {
                addGrup(index);
            }
        } else if (estado==Estados.grupos) {
            if (id == R.id.editar) {
                editGrup(index);
            } else if (id == R.id.borra) {
                removeGrup(index);
            }
        }
        return super.onContextItemSelected(item);
    }

    /**********************************************************************************************/
    /* Metodos Heredados */

    @Override
    public void onBackPressed() {
        if (estado==Estados.grupos) {
            estado=Estados.nivel;
            xml.escribir(f);
            listarDatos();
        } else if(estado==Estados.nivel){
            xml.escribir(f);
            super.onBackPressed();
        }
    }

    /**********************************************************************************************/
    /* Gestion de datos */

    public void listarDatos(){
        adD = new AdaptadorArrayList(this, R.layout.detalle_vista, ListaDatos);
        lv.setAdapter(adD);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                ListaGrupos=ListaDatos.get(pos).getGrupo();
                xml.setPosicion(pos);
                adG = new AdaptadorALGrupos(v.getContext(), R.layout.detalle_vista, ListaGrupos);
                lv.setAdapter(adG);
                Principal.estado= Principal.Estados.grupos;
            }
        });
        registerForContextMenu(lv);
    }

    /**********************************************************************************************/
    /* Gestion de niveles */

    public boolean addLevel() {
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.addlevel, null);
        final EditText et;
        final Spinner spnumeros;
        et = (EditText) vista.findViewById(R.id.etlevel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numeros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnumeros=(Spinner)vista.findViewById(R.id.spnumerosl);
        spnumeros.setAdapter(adapter);
        et.setHint(R.string.name);
        //dialogo*/
        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle(R.string.addl)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialogo.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        final ArrayList<Grupo> a = new ArrayList<Grupo>();
                        Datos d = new Datos();
                        Grupo g=new Grupo();
                        d.setNivel(spnumeros.getSelectedItem().toString() + " " + et.getText().toString().toUpperCase());
                        g.setNumero(spnumeros.getSelectedItem().toString());
                        g.setAcronimo(et.getText().toString().toUpperCase());
                        g.setLetra("A");
                        a.add(g);
                        d.setGrupo(a);
                        if (comprobarlevel(d)){
                            util.tostada(getApplicationContext(), "El nivel ya esta en uso" );
                            addLevel();
                        }else {
                            ListaDatos.add(d);
                            util.tostada(getApplicationContext(), "El nivel "+ d.getNivel() + " ha sido añadido");
                            xml.setListaDatos(ListaDatos);
                            xml.escribir(f);
                        }
                        dialogo.dismiss();
                    }


                });
            }
        });
        dialogo.show();
        adD.notifyDataSetChanged();
        listarDatos();
        return true;
    }

    public boolean editLevel(final int index){
        //cargamos vista
        ListaDatos=xml.getListaDatos();
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.addlevel, null);
        final EditText etnombre;
        final Spinner spnumero;
        etnombre=(EditText)vista.findViewById(R.id.etlevel);
        spnumero=(Spinner)vista.findViewById(R.id.spnumerosl);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numeros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnumero.setAdapter(adapter);
        etnombre.setText(ListaDatos.get(index).getGrupo().get(0).getAcronimo().toString());
        //dialogo
        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle(R.string.modifylevel)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialogo.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        final ArrayList<Grupo> a = new ArrayList<Grupo>();
                        Datos d = new Datos();
                        ListaGrupos = ListaDatos.get(index).getGrupo();
                        d.setNivel(spnumero.getSelectedItem().toString() + " " + etnombre.getText().toString().toUpperCase());
                        ListaGrupos=cambiarnum(spnumero.getSelectedItem().toString(),ListaGrupos);
                        d.setGrupo(ListaGrupos);
                        if (comprobarlevel(d)) {
                            util.tostada(getApplicationContext(), "El nivel ya esta en uso" );
                            editLevel(index);
                        } else {
                            ListaDatos.remove(index);
                            ListaDatos.add(d);
                            util.tostada(getApplicationContext(), "El nivel "+ d.getNivel() + " ha sido modificado");
                            xml.setListaDatos(ListaDatos);
                            xml.escribir(f);
                        }
                        adD.notifyDataSetChanged();
                        listarDatos();
                        dialogo.dismiss();
                    }


                });
            }
        });
        dialogo.show();
        return true;
    }

    public boolean removeLevel(final int index){
        //dialogo
        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle("Desea borrar el nivel " + ListaDatos.get(index).getNivel())
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialogo.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        util.tostada(getApplicationContext(), "El nivel " + ListaDatos.get(index).getNivel() + " ha sido borrado");
                        ListaDatos.remove(index);
                        xml.setListaDatos(ListaDatos);
                        xml.escribir(f);
                        adD.notifyDataSetChanged();
                        listarDatos();
                        dialogo.dismiss();
                    }


                });
            }
        });
        dialogo.show();
        return true;
    }

    private boolean comprobarlevel(Datos d){
        ListaDatos=xml.getListaDatos();
        for (int i = 0; i <ListaDatos.size() ; i++) {
            if (ListaDatos.get(i).equals(d)){return true;}
        }
        return false;
    }

    /**********************************************************************************************/
    /* Gestion de grupos */

    public boolean addGrup(final int index) {
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.addgrup, null);
        final TextView etnumero,etAcronimo;
        final Spinner spLetras;
        ListaDatos=xml.getListaDatos();
        etnumero=(TextView)vista.findViewById(R.id.tvNumero);
        etAcronimo=(TextView)vista.findViewById(R.id.tvAcronimo);
        spLetras=(Spinner) vista.findViewById(R.id.spLetras);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.letras, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLetras.setAdapter(adapter);
        etnumero.setText(ListaDatos.get(index).getGrupo().get(0).getNumero());
        etAcronimo.setText(ListaDatos.get(index).getGrupo().get(0).getAcronimo());
        //dialogo*/
        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle(R.string.addgrup)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialogo.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Grupo g = new Grupo();
                        g.setNumero(etnumero.getText().toString());
                        g.setAcronimo(etAcronimo.getText().toString());
                        g.setLetra(spLetras.getSelectedItem().toString());
                        if (comprobargrup(g)){
                            util.tostada(view.getContext(), "El grupo ya esta en uso");
                            addGrup(index);
                        }else {
                            ListaDatos.get(index).getGrupo().add(g);
                            util.tostada(view.getContext(), "El grupo " + g.toString() + " ha sido añadido");
                            xml.setListaDatos(ListaDatos);
                            xml.escribir(f);
                        }
                        adG.notifyDataSetChanged();
                        listarDatos();
                        dialogo.dismiss();
                    }


                });
            }
        });
        dialogo.show();
        return true;
    }

    public boolean editGrup(final int index){
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.addgrup, null);
        final TextView tvnumero,tvAcronimo;
        final Spinner spLetras;
        ListaDatos=xml.getListaDatos();
        ListaGrupos=ListaDatos.get(xml.getPosicion()).getGrupo();
        tvnumero=(TextView)vista.findViewById(R.id.tvNumero);
        tvAcronimo=(TextView)vista.findViewById(R.id.tvAcronimo);
        spLetras=(Spinner) vista.findViewById(R.id.spLetras);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.letras, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLetras.setAdapter(adapter);
        tvnumero.setText(ListaGrupos.get(index).getNumero());
        tvAcronimo.setText(ListaGrupos.get(index).getAcronimo());
        //dialogo*/
        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle(R.string.modifygrup)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialogo.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Grupo g=new Grupo(tvnumero.getText().toString(),tvAcronimo.getText().toString(),spLetras.getSelectedItem().toString());
                        if (comprobargrup(g)) {
                            util.tostada(getApplicationContext(), "El grupo ya esta en uso.");
                            editGrup(index);
                        } else {
                            ListaGrupos.remove(index);
                            ListaGrupos.add(g);
                            util.tostada(getApplicationContext(), "El grupo " + g.toString() + " ha sido modificado");
                            xml.setListaDatos(ListaDatos);
                            xml.escribir(f);
                        }
                        adG.notifyDataSetChanged();
                        adG = new AdaptadorALGrupos(Principal.this, R.layout.detalle_vista, ListaGrupos);
                        ListView lv = (ListView) findViewById(R.id.lvVista);
                        lv.setAdapter(adG);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

                            }
                        });
                        dialogo.dismiss();
                    }


                });
            }
        });
        dialogo.show();
        return true;
    }

    public boolean removeGrup(final int index){
        Log.v("id",index+"");
        ListaDatos=xml.getListaDatos();
     //dialogo
        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle("Eliminar grupo")
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialogo.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String grupo= ListaDatos.get(xml.getPosicion()).getGrupo().get(index).getNumero()+" "+
                                ListaDatos.get(xml.getPosicion()).getGrupo().get(index).getAcronimo()+" "+
                                ListaDatos.get(xml.getPosicion()).getGrupo().get(index).getLetra();
                        util.tostada(getApplicationContext(),"El grupo " + grupo + " ha sido eliminado");
                        ListaDatos.get(xml.getPosicion()).getGrupo().remove(index);
                        xml.setListaDatos(ListaDatos);
                        xml.escribir(f);
                        final ListView lv = (ListView) findViewById(R.id.lvVista);
                        ListaGrupos=ListaDatos.get(xml.getPosicion()).getGrupo();
                        adG.notifyDataSetChanged();
                        adG = new AdaptadorALGrupos(getApplicationContext(), R.layout.detalle_vista, ListaGrupos);
                        lv.setAdapter(adG);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                            }
                        });
                        dialogo.dismiss();
                    }
                });
            }
        });
        dialogo.show();
        return true;
    }

    private boolean comprobargrup(Grupo g){
        Datos o=xml.getListaDatos().get(xml.getPosicion());
        for (int i = 0; i <o.getGrupo().size() ; i++) {
            if (o.getGrupo().get(i).equals(g)){return true;}
        }
        return false;
    }

    private ArrayList<Grupo> cambiarnum(String s, ArrayList<Grupo> listaGrupos) {
        for (int i=0;i<ListaGrupos.size();i++){
            listaGrupos.get(i).setNumero(s);
        }
        return listaGrupos;
    }

}
