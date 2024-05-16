import data.FileManager;
import model.Experimento;
import model.Poblacion;
import model.Poblacion;
import model.Simulacion;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Experimento currentExperiment;
    private static JFrame frame;
    private static JList<String> listPoblaciones; // Lista para mostrar nombres de poblaciones
    private static DefaultListModel<String> listModel; // Modelo de datos para la lista
    private static final String[] LUMINOSIDAD_OPTIONS = {"Alta", "Media", "Baja"};

    public static void main(String[] args) {
        // Mensaje de bienvenida
        JOptionPane.showMessageDialog(null, "¡Bienvenido al Gestor de Experimentos de Cultivo de Bacterias!", "Bienvenida", JOptionPane.INFORMATION_MESSAGE);
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Gestión de Experimentos de Cultivo de Bacterias");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Experimentos", createExperimentPanel());
        tabbedPane.addTab("Poblaciones", createPopulationPanel());
        tabbedPane.addTab("Detalles", createDetailsPanel());

        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createExperimentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnLoad = new JButton("Cargar Experimento");
        JButton btnSave = new JButton("Guardar Experimento");
        JButton btnNew = new JButton("Nuevo Experimento");

        btnLoad.addActionListener(e -> loadExperiment());
        btnSave.addActionListener(e -> saveExperiment());
        btnNew.addActionListener(e -> createNewExperiment());

        panel.add(btnLoad);
        panel.add(btnSave);
        panel.add(btnNew);

        return panel;
    }

    private static void loadExperiment() {
        JFileChooser fileChooser = new JFileChooser(FileManager.DIRECTORY);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getName();
            currentExperiment = FileManager.loadExperiment(filename);
            if (currentExperiment != null) {
                JOptionPane.showMessageDialog(frame, "Experimento cargado correctamente.", "Cargar", JOptionPane.INFORMATION_MESSAGE);
                updatePoblacionesList();
            } else {
                JOptionPane.showMessageDialog(frame, "Error al cargar el experimento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void saveExperiment() {
        if (currentExperiment == null) {
            JOptionPane.showMessageDialog(frame, "No hay experimento activo para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String filename = JOptionPane.showInputDialog(frame, "Ingrese el nombre del archivo para guardar el experimento:");
        if (filename != null && !filename.isEmpty()) {
            currentExperiment.setArchivo(filename);
            FileManager.saveExperiment(currentExperiment, filename);
            JOptionPane.showMessageDialog(frame, "Experimento guardado correctamente.", "Guardar", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void createNewExperiment() {
        String filename = JOptionPane.showInputDialog(frame, "Ingrese el nombre del archivo para el nuevo experimento:");
        if (filename != null && !filename.isEmpty()) {
            currentExperiment = new Experimento(filename);
            JOptionPane.showMessageDialog(frame, "Nuevo experimento creado.", "Nuevo Experimento", JOptionPane.INFORMATION_MESSAGE);
            updatePoblacionesList();
        }
    }

    private static JPanel createPopulationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<>();
        listPoblaciones = new JList<>(listModel);
        listPoblaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listPoblaciones);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton btnAdd = new JButton("Añadir Población");
        JButton btnRemove = new JButton("Eliminar Población");

        btnAdd.addActionListener(e -> addPopulation());
        btnRemove.addActionListener(e -> removePopulation());

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnRemove);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void addPopulation() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField nameField = new JTextField();
        JTextField numDaysField = new JTextField();
        JTextField numBacteriasField = new JTextField();
        JTextField temperaturaField = new JTextField();
        JComboBox<String> luminosidadComboBox = new JComboBox<>(LUMINOSIDAD_OPTIONS);
        JTextField comidaInicialField = new JTextField();
        JTextField comidaFinalField = new JTextField();
        JComboBox<String> tipoAlimentacionComboBox = new JComboBox<>(new String[]{"linear", "constant", "alternating"});
        JTextField diaIncrementoField = new JTextField();
        JTextField comidaMaximaField = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Número de Días (máximo 30):"));
        panel.add(numDaysField);
        panel.add(new JLabel("Número de Bacterias:"));
        panel.add(numBacteriasField);
        panel.add(new JLabel("Temperatura:"));
        panel.add(temperaturaField);
        panel.add(new JLabel("Luminosidad:"));
        panel.add(luminosidadComboBox);
        panel.add(new JLabel("Comida Inicial (en microgramos):"));
        panel.add(comidaInicialField);
        panel.add(new JLabel("Comida Final (en microgramos):"));
        panel.add(comidaFinalField);
        panel.add(new JLabel("Tipo de Alimentación:"));
        panel.add(tipoAlimentacionComboBox);
        panel.add(new JLabel("Día de Incremento:"));
        panel.add(diaIncrementoField);
        panel.add(new JLabel("Comida Máxima:"));
        panel.add(comidaMaximaField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Añadir Población", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nameField.getText();
                LocalDate fechaInicio = LocalDate.now();
                int numDias = Integer.parseInt(numDaysField.getText());
                LocalDate fechaFin = fechaInicio.plusDays(numDias);
                int numBacterias = Integer.parseInt(numBacteriasField.getText());
                double temperatura = Double.parseDouble(temperaturaField.getText());
                String luminosidad = luminosidadComboBox.getSelectedItem().toString();
                int comidaInicial = Integer.parseInt(comidaInicialField.getText());
                int comidaFinal = Integer.parseInt(comidaFinalField.getText());
                String tipoAlimentacion = tipoAlimentacionComboBox.getSelectedItem().toString();
                int diaIncremento = Integer.parseInt(diaIncrementoField.getText());
                int comidaMaxima = Integer.parseInt(comidaMaximaField.getText());

                Poblacion nuevaPoblacion = new Poblacion(nombre, fechaInicio, fechaFin, numBacterias, temperatura, luminosidad, comidaInicial, comidaFinal, diaIncremento, comidaMaxima, tipoAlimentacion);
                currentExperiment.addPoblacion(nuevaPoblacion);
                updatePoblacionesList();
                JOptionPane.showMessageDialog(frame, "Población añadida correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, introduzca números válidos en los campos numéricos.", "Error de Número", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void removePopulation() {
        String selected = listPoblaciones.getSelectedValue();
        if (selected != null) {
            currentExperiment.removePoblacion(selected);
            updatePoblacionesList();
            JOptionPane.showMessageDialog(frame, "Población eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione una población para eliminar.");
        }
    }

    private static void updatePoblacionesList() {
        listModel.clear();
        if (currentExperiment != null && currentExperiment.getPoblaciones() != null) {
            for (Poblacion p : currentExperiment.getPoblaciones()) {
                listModel.addElement(p.getNombre());
            }
        }
    }

    private static JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);

        JButton btnShowDetails = new JButton("Mostrar Detalles");
        JButton btnRunSimulation = new JButton("Ejecutar Simulación");

        btnShowDetails.addActionListener(e -> {
            Poblacion selectedPoblacion = currentExperiment.getPoblacion(listPoblaciones.getSelectedValue());
            if (selectedPoblacion != null) {
                detailsArea.setText(getPopulationDetails(selectedPoblacion));
            } else {
                detailsArea.setText("Seleccione una población para ver detalles.");
            }
        });

        btnRunSimulation.addActionListener(e -> runSimulation());

        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(btnShowDetails);
        buttonsPanel.add(btnRunSimulation);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }


    private static String getPopulationDetails(Poblacion poblacion) {
        StringBuilder details = new StringBuilder();
        details.append("Detalles de la Población:\n");
        details.append("Nombre: ").append(poblacion.getNombre()).append("\n");
        details.append("Fecha de Inicio: ").append(poblacion.getFechaInicio()).append("\n");
        details.append("Fecha de Fin: ").append(poblacion.getFechaFin()).append("\n");
        details.append("Número de Bacterias: ").append(poblacion.getNumBacterias()).append("\n");
        details.append("Temperatura: ").append(poblacion.getTemperatura()).append("\n");
        details.append("Luminosidad: ").append(poblacion.getLuminosidad()).append("\n");
        details.append("Comida Inicial: ").append(poblacion.getComidaInicial()).append("\n");
        details.append("Día de Incremento Máximo: ").append(poblacion.getDiaIncremento()).append("\n");
        details.append("Comida Máxima en el Día de Incremento: ").append(poblacion.getComidaMaxima()).append("\n");
        details.append("Comida Final en Día 30: ").append(poblacion.getComidaFinal()).append("\n");
        details.append("\n");

        details.append("Cálculo estimado de la cantidad de comida por día:\n");
        List<Integer> comidaPorDia = calcularComidaPorDia(poblacion);
        for (int i = 0; i < comidaPorDia.size(); i++) {
            details.append("Día ").append(i + 1).append(": ").append(comidaPorDia.get(i)).append(" unidades\n");
        }
        return details.toString();
    }

    private static List<Integer> calcularComidaPorDia(Poblacion poblacion) {
        List<Integer> comidaPorDia = new ArrayList<>();
        int comidaInicial = poblacion.getComidaInicial();
        int diaIncremento = poblacion.getDiaIncremento();
        int comidaMaxima = poblacion.getComidaMaxima();
        int comidaFinal = poblacion.getComidaFinal();
        int numDias = poblacion.getFechaInicio().until(poblacion.getFechaFin()).getDays() + 1;

        // Asegurarse de que la duración de la población no supere los 30 días
        if (numDias > 30) {
            numDias = 30;
        }

        for (int dia = 1; dia <= numDias; dia++) {
            int comidaDia;
            if (dia <= diaIncremento) {
                // Hasta el día de incremento máximo, la comida aumenta gradualmente hasta la comida máxima en el día de incremento
                double incrementoDiario = (double) (comidaMaxima - comidaInicial) / diaIncremento;
                comidaDia = (int) (comidaInicial + (dia - 1) * incrementoDiario);
            } else {
                // Después del día de incremento máximo, la comida disminuye gradualmente hasta la comida final en el último día
                double decrementoDiario = (double) (comidaMaxima - comidaFinal) / (30 - diaIncremento);
                comidaDia = (int) (comidaMaxima - (dia - diaIncremento) * decrementoDiario);
            }
            comidaPorDia.add(comidaDia);
        }
        return comidaPorDia;
    }
    private static void runSimulation() {
        Poblacion selectedPoblacion = currentExperiment.getPoblacion(listPoblaciones.getSelectedValue());
        if (selectedPoblacion != null) {
            Simulacion simulacion = new Simulacion(selectedPoblacion);
            simulacion.ejecutarSimulacion(selectedPoblacion.getFechaInicio().until(selectedPoblacion.getFechaFin()).getDays() + 1);
            JOptionPane.showMessageDialog(frame, "Simulación completada para la población seleccionada.");
        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione una población para simular.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}















