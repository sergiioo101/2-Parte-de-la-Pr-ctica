import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String tipoAlimentacion; // "linear", "constant", "alternating"

    public Poblacion(String nombre, LocalDate fechaInicio, LocalDate fechaFin, int numBacterias,
                     double temperatura, String luminosidad, int comidaInicial, int comidaFinal, String tipoAlimentacion) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numBacterias = numBacterias;
        this.temperatura = temperatura;
        this.luminosidad = luminosidad;
        this.comidaInicial = comidaInicial;
        this.comidaFinal = comidaFinal;
        this.tipoAlimentacion = tipoAlimentacion;
        this.planAlimentacion = new ArrayList<>();
        generarPlanAlimentacion();
    }

    private void generarPlanAlimentacion() {
        int days = fechaInicio.until(fechaFin).getDays() + 1;
        int comidaMaxima = 300000; // Ejemplo de comida máxima en microgramos
        switch (tipoAlimentacion.toLowerCase()) {
            case "linear":
                double incrementRate = (double) (comidaMaxima - comidaInicial) / days;
                for (int i = 0; i < days; i++) {
                    planAlimentacion.add(comidaInicial + (int) (i * incrementRate));
                }
                break;
            case "constant":
                planAlimentacion.addAll(Arrays.asList(new Integer[days]));
                Arrays.fill(planAlimentacion.toArray(new Integer[0]), comidaInicial);
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

    // Getters y setters
    public String getNombre() { return nombre; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public int getNumBacterias() { return numBacterias; }
    public double getTemperatura() { return temperatura; }
    public String getLuminosidad() { return luminosidad; }
    public List<Integer> getPlanAlimentacion() { return planAlimentacion; }
}






