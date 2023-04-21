package com.example.facturas;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class filtros extends AppCompatActivity {
Context context=this;
DatePickerDialog datePickerDialog;
private Activity activity_filtros = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        //calendarios
        Button fechaDesde = (Button) findViewById(R.id.fechaDesde);
        Button fechaHasta= (Button) findViewById(R.id.fechaHasta);

        fechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(filtros.this, (view, year1, monthofyear, dayofmonth) ->
                        fechaDesde.setText(dayofmonth + "/" + (monthofyear+1) + "/" + year1), year, month, day);
                dpd.show();
            }

        });
        fechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(filtros.this, (view, year1, monthofyear, dayofmonth) ->
                        fechaHasta.setText(dayofmonth + "/" + (monthofyear+1) + "/" + year1), year, month, day);
                dpd.show();
            }
        });


        //slider
        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView maximo =findViewById(R.id.maximo);
        TextView central =findViewById(R.id.central);

        int maxImporte = MainActivity.maxImporte.intValue()+1;
        seekBar.setMax(maxImporte);
        seekBar.setProgress(maxImporte);
        maximo.setText(String.valueOf(maxImporte));
        central.setText(String.valueOf(maxImporte));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                central.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//botonCerrar
        MenuHost menu = this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_cerrar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.botonCerrar:
                        Intent intent = new Intent(activity_filtros, MainActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
//botonAplicar
        Button botonAplicar=(Button) findViewById(R.id.botonAplicar);
        CheckBox checkPagadas = findViewById(R.id.checkPagadas);
        CheckBox checkAnuladas = findViewById(R.id.checkAnuladas);
        CheckBox checkCuota = findViewById(R.id.checkCuota);
        CheckBox checkPendientes = findViewById(R.id.checkPendientes);
        CheckBox checkPlan = findViewById(R.id.checkPlan);
        Button botonDesde= (Button) findViewById(R.id.fechaDesde);
        Button botonHasta=(Button) findViewById(R.id.fechaHasta);

        botonAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity_filtros,MainActivity.class);
                intent.putExtra("importe", Double.parseDouble(central.getText().toString()));
                intent.putExtra("pagada", checkPagadas.isChecked());
                intent.putExtra("anulada", checkAnuladas.isChecked());
                intent.putExtra("cuota", checkCuota.isChecked());
                intent.putExtra("pendientes", checkPendientes.isChecked());
                intent.putExtra("plan", checkPlan.isChecked());
                intent.putExtra("fechaDesde", botonDesde.getText().toString());
                intent.putExtra("fechaHasta", botonHasta.getText().toString());

                startActivity(intent);
            }
        });

        //botonEliminarFiltros
        Button resetFiltrosButton = findViewById(R.id.botonEliminar);
        resetFiltrosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFiltros();
            }

            private void resetFiltros() {
//Restablecer valores de fecha

                fechaDesde.setText("dia/mes/año");

                fechaHasta.setText("dia/mes/año");

// Restablecer valor de seekBar
                seekBar.setMax(maxImporte);
                seekBar.setProgress(maxImporte);
                central.setText(String.valueOf(maxImporte));

// Restablecer valores de checkboxes
                CheckBox pagadas = findViewById(R.id.checkPagadas);
                pagadas.setChecked(false);
                CheckBox anuladas = findViewById(R.id.checkAnuladas);
                anuladas.setChecked(false);
                CheckBox cuotaFija = findViewById(R.id.checkCuota);
                cuotaFija.setChecked(false);
                CheckBox pendientesPago = findViewById(R.id.checkPendientes);
                pendientesPago.setChecked(false);
                CheckBox planPago = findViewById(R.id.checkPlan);
                planPago.setChecked(false);
            }
        });

    }

}