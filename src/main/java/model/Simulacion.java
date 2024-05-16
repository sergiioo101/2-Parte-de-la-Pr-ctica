package model;
import java.util.Random;

public class Poblacion {
    private Poblacion poblacion;
    private int[][] plato;
    private Random random = new Random();

    public Poblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
        this.plato = new int[20][20];
        inicializarPlato();
    }

    private void inicializarPlato() {
        int centro = 10; // Centro del plato de 20x20
        int tamanoSubcuadro = 4;
        int bacteriasPorCelda = poblacion.getNumBacterias() / (tamanoSubcuadro * tamanoSubcuadro);

        for (int i = centro - 2; i < centro + 2; i++) {
            for (int j = centro - 2; j < centro + 2; j++) {
                this.plato[i][j] = bacteriasPorCelda;
            }
        }
    }

    public void ejecutarSimulacion(int days) {
        for (int day = 0; day < days; day++) {
            // Simular un día
            simularDia();
        }
    }

    private void simularDia() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int bacterias = plato[i][j];
                if (bacterias > 0) {
                    simularBacteriasEnCelda(i, j, bacterias);
                }
            }
        }
    }

    private void simularBacteriasEnCelda(int x, int y, int bacterias) {
        int bacteriasRestantes = bacterias;
        while (bacteriasRestantes-- > 0) {
            int fate = random.nextInt(100);
            if (fate < 3) {
                // Bacteria muere
                plato[x][y]--;
            } else if (fate >= 3 && fate < 60) {
                // Bacteria se queda
            } else {
                // Bacteria se mueve
                moverBacteria(x, y, fate);
            }
        }
    }

    private void moverBacteria(int x, int y, int fate) {
        // Logica para mover bacterias basada en 'fate'
        int newX = x, newY = y;
        if (fate >= 60 && fate < 65) newX = x - 1; // Mover hacia arriba
        else if (fate >= 65 && fate < 70) newX = x + 1; // Mover hacia abajo
        else if (fate >= 70 && fate < 75) newY = y - 1; // Mover hacia izquierda
        else if (fate >= 75 && fate < 80) newY = y + 1; // Mover hacia derecha

        // Verificar límites del plato
        if (newX >= 0 && newX < 20 && newY >= 0 && newY < 20) {
            plato[x][y]--;
            plato[newX][newY]++;
        }
    }
}

