package com.example.facturas;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Facturas> listaFacturas;
    private RequestQueue rq;
    private RecyclerView rv1;
    private AdaptadorFacturas adaptadorFacturas;
    private MainActivity instance=this;
    public static Double maxImporte = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaFacturas = new ArrayList<>();
        rq = Volley.newRequestQueue(this);

        cargarFactura();
        rv1 = findViewById(R.id.rv1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv1.setLayoutManager(linearLayoutManager);
        adaptadorFacturas = new AdaptadorFacturas();
        rv1.setAdapter(adaptadorFacturas);






        //boton filtros
        MenuHost menu=this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_filtros, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.botonFiltro:
                        Intent intent=new Intent(instance, filtros.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });





    }




    private void cargarFactura() {
        String url = "https://viewnextandroid.wiremockapi.cloud/facturas";
        JsonObjectRequest requerimiento = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String valor = response.get("facturas").toString();
                            JSONArray arreglo = new JSONArray(valor);
                            for (int f = 0; f < arreglo.length(); f++) {
                                JSONObject objeto = new JSONObject(arreglo.get(f).toString());
                                String descEstado = objeto.getString("descEstado");
                                String importeOrdenacion = objeto.getString("importeOrdenacion");
                                String fecha = objeto.getString("fecha");
                                Facturas factura = new Facturas(descEstado, importeOrdenacion, fecha);
                                listaFacturas.add(factura);

                            }
                            maxImporte = Double.valueOf(listaFacturas.stream().max(Comparator.comparing(Facturas::getImporteOrdenacion)).get().getImporteOrdenacion());

                            Bundle extras = getIntent().getExtras();

                            if (extras != null) {
                                ArrayList<Facturas> listFiltro = new ArrayList<>();

                                double importeFiltro = getIntent().getDoubleExtra("importe", maxImporte);

                                for (Facturas factura : listaFacturas) {
                                    if (Double.parseDouble(factura.getImporteOrdenacion()) < importeFiltro) {
                                        listFiltro.add(factura);
                                    }
                                }
                                //TEXTO SI NO HAY NADA
                                TextView textView = new TextView(instance);
                                textView.setText("No hay facturas");
                                textView.setTextSize(24);
                                textView.setVisibility(View.INVISIBLE);

                                boolean checkBoxPagadas = getIntent().getBooleanExtra("Pagada", false);
                                boolean checkBoxPagadas2 = getIntent().getBooleanExtra("Pendiente de pago", false);
                                boolean checkBoxPagadas3 = getIntent().getBooleanExtra("Anulada", false);
                                boolean checkBoxPagadas4 = getIntent().getBooleanExtra("Cuota Fija", false);
                                boolean checkBoxPagadas5 = getIntent().getBooleanExtra("Plan de pago", false);
                                //checkbox

                                if (checkBoxPagadas || checkBoxPagadas2 || checkBoxPagadas3 || checkBoxPagadas4 || checkBoxPagadas5) {
                                    ArrayList<Facturas> listFiltro2 = new ArrayList<>();

                                    for (Facturas factura : listFiltro) {
                                        if (factura.getDescEstado().equals("Pagada") && checkBoxPagadas) {
                                            listFiltro2.add(factura);
                                        }
                                        if (factura.getDescEstado().equals("Pendiente de pago") && checkBoxPagadas2) {
                                            listFiltro2.add(factura);
                                        }
                                        if (factura.getDescEstado().equals("Anulada") && checkBoxPagadas3) {
                                            listFiltro2.add(factura);
                                        }
                                        if (factura.getDescEstado().equals("Cuota Fija") && checkBoxPagadas4) {
                                            listFiltro2.add(factura);
                                        }
                                        if (factura.getDescEstado().equals("Plan de pago") && checkBoxPagadas5) {
                                            listFiltro2.add(factura);
                                        }
                                    }

                                    listFiltro = listFiltro2;
                                }

                                if (!getIntent().getStringExtra("fechaDesde").equals("dia/mes/año") && !getIntent().getStringExtra("fechaHasta").equals("dia/mes/año")) {
                                    ArrayList<Facturas> facturasFiltradas = new ArrayList<>();


                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy");
                                    Date fechaDesde = null;
                                    Date fechaHasta = null;

                                    try {
                                        fechaDesde = sdf.parse(getIntent().getStringExtra("fechaDesde"));
                                        fechaHasta = sdf.parse(getIntent().getStringExtra("fechaHasta"));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    for (Facturas factura : listFiltro) {
                                        Date fechaFactura = sdf.parse(factura.getFecha());
                                        if (fechaFactura.after(fechaDesde) && fechaFactura.before(fechaHasta)) {
                                            facturasFiltradas.add(factura);
                                        }
                                    }

                                    listFiltro = facturasFiltradas;
                                }

                                listaFacturas = listFiltro;

                                if (listaFacturas.isEmpty()) {
                                    textView.setVisibility(View.VISIBLE);
                                    RelativeLayout relativeLayout = new RelativeLayout(instance);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                                            RelativeLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                                    relativeLayout.addView(textView, params);
                                    setContentView(relativeLayout);
                                }
                            }

                            adaptadorFacturas.notifyItemRangeInserted(listaFacturas.size(), 1);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        rq.add(requerimiento);
    }

    private class AdaptadorFacturas extends RecyclerView.Adapter<AdaptadorFacturas.AdaptadorFacturasHolder> {


        @Override
        public AdaptadorFacturasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorFacturasHolder(getLayoutInflater().inflate(R.layout.layout_factura, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorFacturasHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return listaFacturas.size();
        }

        class AdaptadorFacturasHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
            Dialog dialogo;
            TextView textFecha, textImporte, textEstado;

            public AdaptadorFacturasHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                textFecha = itemView.findViewById(R.id.textFecha);
                textImporte = itemView.findViewById(R.id.textImporte);
                textEstado = itemView.findViewById(R.id.textEstado);
                dialogo =new Dialog(itemView.getContext());
                }



            public void imprimir(int position) {
                textFecha.setText(listaFacturas.get(position).getFecha());
                textImporte.setText(listaFacturas.get(position).getImporteOrdenacion()+" €");
                textEstado.setText(listaFacturas.get(position).getDescEstado());
                if (listaFacturas.get(position).getDescEstado().equals("Pendiente de pago")) {
                    textEstado.setTextColor(Color.RED);
                } else {
                    textEstado.setTextColor(getResources().getColor(R.color.verde));
                }
            }

//popup
            @Override
            public void onClick(View view) {
                dialogo.setContentView(R.layout.layout_popup);
                dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView mensajePopup = dialogo.findViewById(R.id.mensaje);
                mensajePopup.setText("Esta funcionalidad aún no está disponible.");
                dialogo.show();
                Button cerrarButton = dialogo.findViewById(R.id.boton);
                cerrarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogo.dismiss(); // Cierra el popup
                    }
                });


            }



        }



    }
}