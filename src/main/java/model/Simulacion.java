package model;

import java.util.Random;

public class Simulacion {
    private Poblacion poblacion;
    private int[][] plato;
    private Random random = new Random();

    public Simulacion(Poblacion poblacion) {
        this.poblacion = poblacion;
        this.plato = new int[20][20];
        inicializarPlato();
    }

    private void inicializarPlato() {
        int centro = 10;
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
            simularDia();
            // Repartir comida al final del día
            repartirComida(poblacion.getPlanAlimentacion().get(day));
        }
    }

    private void simularDia() {
        int[][] nuevoPlato = new int[20][20];

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int bacterias = plato[i][j];
                if (bacterias > 0) {
                    for (int k = 0; k < bacterias; k++) {
                        simularBacteria(i, j, nuevoPlato);
                    }
                }
            }
        }

        plato = nuevoPlato;
    }

    private void simularBacteria(int x, int y, int[][] nuevoPlato) {
        for (int step = 0; step < 10; step++) {
            int comidaEnCelda = plato[x][y];
            if (comidaEnCelda >= 100) {
                comidaEnCelda -= 20;
                plato[x][y] = comidaEnCelda;
                int fate = random.nextInt(100);
                if (fate < 3) return; // Muere
                else if (fate >= 60 && fate < 100) moverBacteria(x, y, fate, nuevoPlato);
            } else if (comidaEnCelda >= 10) {
                comidaEnCelda -= 10;
                plato[x][y] = comidaEnCelda;
                int fate = random.nextInt(100);
                if (fate < 6) return; // Muere
                else if (fate >= 20 && fate < 100) moverBacteria(x, y, fate, nuevoPlato);
            } else {
                int fate = random.nextInt(100);
                if (fate < 20) return; // Muere
                else if (fate >= 60 && fate < 100) moverBacteria(x, y, fate, nuevoPlato);
            }
        }
        nuevoPlato[x][y]++;
    }

    private void moverBacteria(int x, int y, int fate, int[][] nuevoPlato) {
        int newX = x, newY = y;
        if (fate >= 60 && fate < 65) newX = x - 1;
        else if (fate >= 65 && fate < 70) newX = x + 1;
        else if (fate >= 70 && fate < 75) newY = y - 1;
        else if (fate >= 75 && fate < 80) newY = y + 1;

        if (newX >= 0 && newX < 20 && newY >= 0 && newY < 20) {
            nuevoPlato[newX][newY]++;
        } else {
            nuevoPlato[x][y]++;
        }
    }

    private void repartirComida(int comidaTotal) {
        int comidaPorCelda = comidaTotal / (20 * 20);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                plato[i][j] += comidaPorCelda;
            }
        }
    }

    public int[][] getPlato() {
        return plato;
    }
}



