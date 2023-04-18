package com.example.facturas;

public class Facturas {
    private String descEstado;
    private String importeOrdenacion;
    private String fecha;

    public Facturas(String descEstado, String importeOrdenacion, String fecha) {
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
        this.fecha = fecha;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public String getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public String getFecha() {
        return fecha;
    }
}
