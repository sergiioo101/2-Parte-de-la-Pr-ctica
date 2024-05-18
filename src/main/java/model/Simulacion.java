package model;

import java.util.Random;

public class Simulacion {
    private Poblacion poblacion;
    private int[][] plato;
    private int[][][] resultados; // Para almacenar el estado del plato cada día
    private Random random = new Random();

    public Simulacion(Poblacion poblacion) {
        this.poblacion = poblacion;
        this.plato = new int[20][20];
        int days = poblacion.getFechaInicio().until(poblacion.getFechaFin()).getDays() + 1;
        this.resultados = new int[days][20][20];
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

    public void ejecutarSimulacion() {
        int days = poblacion.getFechaInicio().until(poblacion.getFechaFin()).getDays() + 1;
        for (int day = 0; day < days; day++) {
            simularDia();
            repartirComida(poblacion.getPlanAlimentacion().get(day));
            guardarResultadoDia(day);
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
        int comidaConsumida = 0;
        for (int step = 0; step < 10; step++) {
            int comidaEnCelda = plato[x][y];
            if (comidaEnCelda >= 100) {
                comidaEnCelda -= 20;
                comidaConsumida += 20;
                plato[x][y] = comidaEnCelda;
                int fate = random.nextInt(100);
                if (fate < 3) return; // Muere
                else if (fate >= 60 && fate < 100) moverBacteria(x, y, fate, nuevoPlato);
            } else if (comidaEnCelda >= 10) {
                comidaEnCelda -= 10;
                comidaConsumida += 10;
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
        // Reproducir bacterias según la comida consumida
        reproducirBacterias(x, y, nuevoPlato, comidaConsumida);
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

    private void reproducirBacterias(int x, int y, int[][] nuevoPlato, int comidaConsumida) {
        if (comidaConsumida >= 150) {
            nuevoPlato[x][y] += 3;
        } else if (comidaConsumida >= 100) {
            nuevoPlato[x][y] += 2;
        } else if (comidaConsumida >= 50) {
            nuevoPlato[x][y] += 1;
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

    private void guardarResultadoDia(int day) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                resultados[day][i][j] = plato[i][j];
            }
        }
    }

    public int[][][] getResultados() {
        return resultados;
    }
}











