package edu.cerp.checkin.ui;

import edu.cerp.checkin.logic.SesionService;
import edu.cerp.checkin.model.Inscripcion;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CheckInGUI {

    public static void show(SesionService service) {

        final JFrame ventana = new JFrame("Check-in Aula");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 600);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        // Panel superior con campos
        final JPanel panel = new JPanel(new GridLayout(4, 2));
        final JTextField nombreF = new JTextField();
        final JTextField documentoF = new JTextField();
        final JComboBox<String> cursoB = new JComboBox<>(new String[]{"Prog 1", "Prog 2", "Bases de Datos"});
        final JTextField fechaF = new JTextField();
        fechaF.setEditable(false);
        fechaF.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        panel.add(new JLabel("Nombre:"));
        panel.add(nombreF);
        panel.add(new JLabel("Documento:"));
        panel.add(documentoF);
        panel.add(new JLabel("Curso:"));
        panel.add(cursoB);
        panel.add(new JLabel("Fecha de registro:"));
        panel.add(fechaF);

        ventana.add(panel, BorderLayout.NORTH);

        // Tabla de inscripciones
        final DefaultTableModel tablaModel = new DefaultTableModel(
                new Object[]{"Nombre", "Documento", "Curso", "Fecha"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ninguna celda editable
            }
        };

        final JTable jtInscripciones = new JTable(tablaModel);
        jtInscripciones.setFillsViewportHeight(true);
        jtInscripciones.setAutoCreateRowSorter(true);

        ventana.add(new JScrollPane(jtInscripciones), BorderLayout.CENTER);

        // Panel inferior con botones
        final JPanel botonesPanel = new JPanel(new FlowLayout());
        final JButton registrarBtn = new JButton("Registrar");
        final JButton listarBtn = new JButton("Listar");
        final JButton resumenBtn = new JButton("Resumen");
        final JButton buscarBtn = new JButton("Buscar");
        final JButton salirBtn = new JButton("Salir");

        botonesPanel.add(registrarBtn);
        botonesPanel.add(listarBtn);
        botonesPanel.add(resumenBtn);
        botonesPanel.add(buscarBtn);
        botonesPanel.add(salirBtn);

        ventana.add(botonesPanel, BorderLayout.SOUTH);

        // Cargar inscripciones iniciales
        List<Inscripcion> insc = service.listar();
        tablaModel.setRowCount(0);
        for (Inscripcion unaInscripcion : insc) {
            tablaModel.addRow(new Object[]{
                    unaInscripcion.getNombre(),
                    unaInscripcion.getDocumento(),
                    unaInscripcion.getCurso(),
                    unaInscripcion.getFechaHora()
            });
        }

        // Botón Registrar
        registrarBtn.addActionListener(e -> {
            String nombre = nombreF.getText();
            String doc = documentoF.getText();
            String curso = (String) cursoB.getSelectedItem();
            service.registrar(nombre, doc, curso);

            fechaF.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            JOptionPane.showMessageDialog(ventana, "Registrado");

            nombreF.setText("");
            documentoF.setText("");

            // refrescar tabla
            List<Inscripcion> inscActualizada = service.listar();
            tablaModel.setRowCount(0);
            for (Inscripcion unaInscripcion : inscActualizada) {
                tablaModel.addRow(new Object[]{
                        unaInscripcion.getNombre(),
                        unaInscripcion.getDocumento(),
                        unaInscripcion.getCurso(),
                        unaInscripcion.getFechaHora()
                });
            }
        });

        // Botón Listar
        listarBtn.addActionListener(e -> {
            List<Inscripcion> inscActualizada = service.listar();
            tablaModel.setRowCount(0);
            for (Inscripcion unaInscripcion : inscActualizada) {
                tablaModel.addRow(new Object[]{
                        unaInscripcion.getNombre(),
                        unaInscripcion.getDocumento(),
                        unaInscripcion.getCurso(),
                        unaInscripcion.getFechaHora()
                });
            }
        });

        // Botón Resumen
        resumenBtn.addActionListener(e -> {
            String resumen = service.resumen();
            JOptionPane.showMessageDialog(ventana, resumen, "Resumen", JOptionPane.INFORMATION_MESSAGE);
        });

        // Botón Buscar
        buscarBtn.addActionListener(e -> {
            String q = JOptionPane.showInputDialog(ventana, "Ingrese documento a buscar:");
            if (q != null && !q.isBlank()) {
                List<Inscripcion> res = service.buscar(q);
                tablaModel.setRowCount(0);
                for (Inscripcion unaInscripcion : res) {
                    tablaModel.addRow(new Object[]{
                            unaInscripcion.getNombre(),
                            unaInscripcion.getDocumento(),
                            unaInscripcion.getCurso(),
                            unaInscripcion.getFechaHora()
                    });
                }
            }
        });

        // Botón Salir
        salirBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    ventana,
                    "Confirmar salida",
                    "Salir",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                ventana.dispose();
                System.exit(0);
            }
        });

        ventana.setVisible(true);
    }
}
