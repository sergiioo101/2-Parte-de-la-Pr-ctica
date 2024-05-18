package model;

import java.util.Random;
import java.util.function.BiConsumer;

public class Simulacion {
    private Poblacion poblacion;
    private int[][] platoBacterias;
    private int[][] platoComida;
    private int[][][] resultadosBacterias;
    private int[][][] resultadosComida;
    private Random random = new Random();

    public Simulacion(Poblacion poblacion) {
        this.poblacion = poblacion;
        this.platoBacterias = new int[20][20];
        this.platoComida = new int[20][20];
        int days = poblacion.getFechaInicio().until(poblacion.getFechaFin()).getDays() + 1;
        this.resultadosBacterias = new int[days][20][20];
        this.resultadosComida = new int[days][20][20];
        inicializarPlato();
    }

    private void inicializarPlato() {
        int centro = 10;
        int tamanoSubcuadro = 4;
        int bacteriasPorCelda = poblacion.getNumBacterias() / (tamanoSubcuadro * tamanoSubcuadro);

        for (int i = centro - 2; i < centro + 2; i++) {
            for (int j = centro - 2; j < centro + 2; j++) {
                this.platoBacterias[i][j] = bacteriasPorCelda;
            }
        }

        int comidaTotal = poblacion.getComidaInicial();
        int comidaPorCelda = comidaTotal / (20 * 20);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                this.platoComida[i][j] = comidaPorCelda;
            }
        }
    }

    public void ejecutarSimulacionDinamica(BiConsumer<Integer, int[][]> callback) {
        int days = poblacion.getFechaInicio().until(poblacion.getFechaFin()).getDays() + 1;
        for (int day = 0; day < days; day++) {
            if (Thread.currentThread().isInterrupted()) return; // Check if interrupted
            simularDia();
            callback.accept(day, platoBacterias);
            guardarResultadoDia(day);
            repartirComida(poblacion.getPlanAlimentacion().get(day));
        }
    }

    private void simularDia() {
        int[][] nuevoPlatoBacterias = new int[20][20];
        int[][] nuevoPlatoComida = new int[20][20];

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int bacterias = platoBacterias[i][j];
                int comidaEnCelda = platoComida[i][j];
                for (int k = 0; k < bacterias; k++) {
                    simularBacteria(i, j, nuevoPlatoBacterias, nuevoPlatoComida);
                }
            }
        }

        platoBacterias = nuevoPlatoBacterias;
        platoComida = nuevoPlatoComida;
    }

    private void simularBacteria(int x, int y, int[][] nuevoPlatoBacterias, int[][] nuevoPlatoComida) {
        int comidaConsumida = 0;
        for (int step = 0; step < 10; step++) {
            int comidaEnCelda = platoComida[x][y];
            if (comidaEnCelda >= 100) {
                comidaEnCelda -= 20;
                comidaConsumida += 20;
                platoComida[x][y] = comidaEnCelda;
                int fate = random.nextInt(100);
                if (fate < 3) return; // Muere
                else if (fate >= 60 && fate < 100) moverBacteria(x, y, fate, nuevoPlatoBacterias);
            } else if (comidaEnCelda >= 10) {
                comidaEnCelda -= 10;
                comidaConsumida += 10;
                platoComida[x][y] = comidaEnCelda;
                int fate = random.nextInt(100);
                if (fate < 6) return; // Muere
                else if (fate >= 20 && fate < 100) moverBacteria(x, y, fate, nuevoPlatoBacterias);
            } else {
                int fate = random.nextInt(100);
                if (fate < 20) return; // Muere
                else if (fate >= 60 && fate < 100) moverBacteria(x, y, fate, nuevoPlatoBacterias);
            }
        }
        nuevoPlatoBacterias[x][y]++;
        reproducirBacterias(x, y, nuevoPlatoBacterias, comidaConsumida);
    }

    private void moverBacteria(int x, int y, int fate, int[][] nuevoPlatoBacterias) {
        int newX = x, newY = y;
        if (fate >= 60 && fate < 65) newX = x - 1;
        else if (fate >= 65 && fate < 70) newX = x + 1;
        else if (fate >= 70 && fate < 75) newY = y - 1;
        else if (fate >= 75 && fate < 80) newY = y + 1;

        if (newX >= 0 && newX < 20 && newY >= 0 && newY < 20) {
            nuevoPlatoBacterias[newX][newY]++;
        } else {
            nuevoPlatoBacterias[x][y]++;
        }
    }

    private void reproducirBacterias(int x, int y, int[][] nuevoPlatoBacterias, int comidaConsumida) {
        if (comidaConsumida >= 150) {
            nuevoPlatoBacterias[x][y] += 3;
        } else if (comidaConsumida >= 100) {
            nuevoPlatoBacterias[x][y] += 2;
        } else if (comidaConsumida >= 50) {
            nuevoPlatoBacterias[x][y] += 1;
        }
    }

    private void repartirComida(int comidaTotal) {
        int comidaPorCelda = comidaTotal / (20 * 20);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                platoComida[i][j] += comidaPorCelda;
            }
        }
    }

    private void guardarResultadoDia(int day) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                resultadosBacterias[day][i][j] = platoBacterias[i][j];
                resultadosComida[day][i][j] = platoComida[i][j];
            }
        }
    }

    public int[][][] getResultadosBacterias() {
        return resultadosBacterias;
    }

    public int[][][] getResultadosComida() {
        return resultadosComida;
    }
}























