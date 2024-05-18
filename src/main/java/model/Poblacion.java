package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Poblacion implements Serializable {
    private static final long serialVersionUID = 5128373257598436884L;

    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int numBacterias;
    private double temperatura;
    private String luminosidad;
    private int comidaInicial; // En microgramos
    private int comidaFinal; // En microgramos
    private int diaIncremento; // Día hasta el cual se incrementa la comida
    private int comidaMaxima; // Cantidad máxima de comida en microgramos
    private List<Integer> planAlimentacion; // Lista dinámica para diferentes patrones de alimentación
    private String tipoAlimentacion; // "linear", "constant", "alternating"

    public Poblacion(String nombre, LocalDate fechaInicio, LocalDate fechaFin, int numBacterias,
                     double temperatura, String luminosidad, int comidaInicial, int comidaFinal, int diaIncremento, int comidaMaxima, String tipoAlimentacion) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numBacterias = numBacterias;
        this.temperatura = temperatura;
        this.luminosidad = luminosidad;
        this.comidaInicial = comidaInicial;
        this.comidaFinal = comidaFinal;
        this.diaIncremento = diaIncremento;
        this.comidaMaxima = comidaMaxima;
        this.tipoAlimentacion = tipoAlimentacion;
        this.planAlimentacion = new ArrayList<>();
        generarPlanAlimentacion();
    }

    public void generarPlanAlimentacion() {
        int days = fechaInicio.until(fechaFin).getDays() + 1;
        switch (tipoAlimentacion.toLowerCase()) {
            case "linear":
                double incrementRate = (double) (comidaMaxima - comidaInicial) / diaIncremento;
                double decrementRate = (double) (comidaMaxima - comidaFinal) / (days - diaIncremento);
                for (int i = 0; i < days; i++) {
                    if (i < diaIncremento) {
                        planAlimentacion.add(comidaInicial + (int) (i * incrementRate));
                    } else {
                        planAlimentacion.add(comidaMaxima - (int) ((i - diaIncremento) * decrementRate));
                    }
                }
                break;
            case "constant":
                for (int i = 0; i < days; i++) {
                    planAlimentacion.add(comidaInicial);
                }
                break;
            case "alternating":
                for (int i = 0; i < days; i++) {
                    planAlimentacion.add((i % 2 == 0) ? comidaInicial : 0);
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de alimentación desconocido: " + tipoAlimentacion);
        }
    }

    // Getters
    public String getNombre() { return nombre; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public int getNumBacterias() { return numBacterias; }
    public double getTemperatura() { return temperatura; }
    public String getLuminosidad() { return luminosidad; }
    public int getComidaInicial() { return comidaInicial; }
    public int getComidaFinal() { return comidaFinal; }
    public int getDiaIncremento() { return diaIncremento; }
    public int getComidaMaxima() { return comidaMaxima; }
    public String getTipoAlimentacion() { return tipoAlimentacion; }
    public List<Integer> getPlanAlimentacion() { return planAlimentacion; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public void setNumBacterias(int numBacterias) { this.numBacterias = numBacterias; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }
    public void setLuminosidad(String luminosidad) { this.luminosidad = luminosidad; }
    public void setComidaInicial(int comidaInicial) { this.comidaInicial = comidaInicial; }
    public void setComidaFinal(int comidaFinal) { this.comidaFinal = comidaFinal; }
    public void setDiaIncremento(int diaIncremento) { this.diaIncremento = diaIncremento; }
    public void setComidaMaxima(int comidaMaxima) { this.comidaMaxima = comidaMaxima; }
    public void setTipoAlimentacion(String tipoAlimentacion) { this.tipoAlimentacion = tipoAlimentacion; }
}











