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
        int centro = 8;
        int tamanoSubcuadro = 4;
        int bacteriasPorCelda = poblacion.getNumBacterias() / (tamanoSubcuadro * tamanoSubcuadro);

        for (int i = centro; i < centro + tamanoSubcuadro; i++) {
            for (int j = centro; j < centro + tamanoSubcuadro; j++) {
                this.platoBacterias[i][j] = bacteriasPorCelda;
            }
        }
    }

    public void ejecutarSimulacionDinamica(BiConsumer<Integer, int[][]> actualizarUI) {
        int days = poblacion.getFechaInicio().until(poblacion.getFechaFin()).getDays() + 1;
        for (int day = 0; day < days; day++) {
            repartirComida(poblacion.getPlanAlimentacion().get(day));
            simularDia();
            guardarResultadoDia(day);
            actualizarUI.accept(day, platoBacterias);
        }
    }

    private void simularDia() {
        int[][] nuevoPlato = new int[20][20];

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int bacterias = platoBacterias[i][j];
                if (bacterias > 0) {
                    for (int k = 0; k < bacterias; k++) {
                        simularBacteria(i, j, nuevoPlato);
                    }
                }
            }
        }

        platoBacterias = nuevoPlato;
    }

    private void simularBacteria(int x, int y, int[][] nuevoPlato) {
        int comidaConsumida = 0;
        for (int step = 0; step < 10; step++) {
            int comidaEnCelda = platoComida[x][y];
            if (comidaEnCelda >= 100) {
                comidaEnCelda -= 20;
                comidaConsumida += 20;
                platoComida[x][y] = comidaEnCelda;
                int fate = random.nextInt(100);
                if (fate < 3) return; // Muere
                else if (fate >= 60 && fate < 100) moverBacteria(x, y, fate, nuevoPlato);
            } else if (comidaEnCelda >= 10) {
                comidaEnCelda -= 10;
                comidaConsumida += 10;
                platoComida[x][y] = comidaEnCelda;
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
                platoComida[i][j] = comidaPorCelda;
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





















