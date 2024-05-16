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
    private List<Integer> planAlimentacion; // Lista dinámica para diferentes patrones de alimentación

    public Poblacion(String nombre, LocalDate fechaInicio, LocalDate fechaFin, int numBacterias,
                     double temperatura, String luminosidad, int comidaInicial, int comidaFinal) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numBacterias = numBacterias;
        this.temperatura = temperatura;
        this.luminosidad = luminosidad;
        this.comidaInicial = comidaInicial;
        this.comidaFinal = comidaFinal;
        this.planAlimentacion = new ArrayList<>();
        this.generarPlanAlimentacion(); // Método para generar el plan de alimentación según el patrón seleccionado
    }

    // Generación dinámica del plan de alimentación
    private void generarPlanAlimentacion() {
        // Ejemplo simple de un incremento lineal seguido de un decremento, ajustable a patrones específicos
        int duracion = fechaInicio.until(fechaFin).getDays() + 1;
        int comidaMaxima = 300000; // Ejemplo de comida máxima en microgramos
        double incrementoDiario = (double) (comidaMaxima - comidaInicial) / duracion / 2;
        double decrementoDiario = (double) (comidaMaxima - comidaFinal) / duracion / 2;

        for (int i = 0; i < duracion; i++) {
            if (i < duracion / 2) {
                planAlimentacion.add((int) (comidaInicial + i * incrementoDiario));
            } else {
                planAlimentacion.add((int) (comidaMaxima - (i - duracion / 2) * decrementoDiario));
            }
        }
    }

    // Getters y setters actualizados
    public String getNombre() { return nombre; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public int getNumBacterias() { return numBacterias; }
    public double getTemperatura() { return temperatura; }
    public String getLuminosidad() { return luminosidad; }
    public List<Integer> getPlanAlimentacion() { return planAlimentacion; }
}





