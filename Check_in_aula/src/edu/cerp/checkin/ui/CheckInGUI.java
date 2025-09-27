package edu.cerp.checkin.ui;

import edu.cerp.checkin.logic.SesionService;
import edu.cerp.checkin.model.Inscripcion;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckInGUI {

    public static void show(SesionService service) {
        final JFrame ventana = new JFrame("Check-in Aula");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 600);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        // Panel central con campos
        final JPanel panel = new JPanel(new GridLayout(4, 2));//4 filas, 2 columnas
        final JTextField nombreF = new JTextField();
        final JTextField documentoF = new JTextField();
        final JComboBox<String> cursoB = new JComboBox<>(new String[]{"Prog 1", "Prog 2", "Bases de Datos"});
        final JTextField fechaField = new JTextField();
        fechaField.setEditable(false);//no deja cambiar la fecha de registro
        fechaField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        panel.add(new JLabel("Nombre:")); panel.add(nombreF);
        panel.add(new JLabel("Documento:")); panel.add(documentoF);
        panel.add(new JLabel("Curso:")); panel.add(cursoB);
        panel.add(new JLabel("Fecha de registro:")); panel.add(fechaField);

        //situados arriba
        ventana.add(panel, BorderLayout.NORTH);

        // Área para mostrar inscripciones
        final JTextArea listaArea = new JTextArea();
        listaArea.setEditable(false);
        ventana.add(new JScrollPane(listaArea), BorderLayout.CENTER);

        // Panel inferior con botones
        final JPanel botonesPanel = new JPanel(new FlowLayout()); //organiza los componentes dentro del panel como un párrafo
        final JButton registrarBtn = new JButton("Registrar");
        final JButton listarBtn = new JButton("Listar");
        final JButton buscarBtn = new JButton("Buscar");
        final JButton salirBtn = new JButton("Salir");
       
        botonesPanel.add(registrarBtn);
        botonesPanel.add(listarBtn);
        botonesPanel.add(buscarBtn);
        botonesPanel.add(salirBtn);
        
        
        //situados abajo
        ventana.add(botonesPanel, BorderLayout.SOUTH);

        // Botón Registrar
        registrarBtn.addActionListener(e -> {
            String nombre = nombreF.getText();
            String doc = documentoF.getText();
            String curso = (String) cursoB.getSelectedItem();
            service.registrar(nombre, doc, curso);

            // Actualizar fecha
            fechaField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            JOptionPane.showMessageDialog(ventana, "Registrado");

            // Limpiar campos
            nombreF.setText("");
            documentoF.setText("");
        });

        // Botón Listar
        listarBtn.addActionListener(e -> {
            List<Inscripcion> insc = service.listar();
            StringBuilder sb = new StringBuilder("Nombre | Documento | Curso | Hora\n");
            for (Inscripcion i : insc) {
                sb.append(i.getNombre()).append(" | ")
                  .append(i.getDocumento()).append(" | ")
                  .append(i.getCurso()).append(" | ")
                  .append(i.getFechaHora()).append("\n");
            }
            listaArea.setText(sb.toString());
        });

        // Botón Buscar
        buscarBtn.addActionListener(e -> {
            String q = JOptionPane.showInputDialog(ventana, "Ingrese nombre o documento a buscar:");
            if (q != null && !q.isBlank()) {
                List<Inscripcion> res = service.buscar(q);
                StringBuilder sb = new StringBuilder("Nombre | Documento | Curso | Hora\n");
                for (Inscripcion i : res) {
                    sb.append(i.getNombre()).append(" | ")
                      .append(i.getDocumento()).append(" | ")
                      .append(i.getCurso()).append(" | ")
                      .append(i.getFechaHora()).append("\n");
                }
                listaArea.setText(sb.toString());
            }
        });
        
        // Botón Salir
        salirBtn.addActionListener(e -> {
            
            //Cuadro de diálogo
            int confirm = JOptionPane.showConfirmDialog(
                ventana, //ventana padre
                "Confirmar salida", //mensaje
                "Salir", //Nombre d ela ventana
                JOptionPane.YES_NO_OPTION //el cuadro de diálogo tien los botones si y no
            );
            if (confirm == JOptionPane.YES_OPTION) { //si el ususario escribe si
                ventana.dispose(); // cierra la ventana
                System.exit(0);    // termina el programa
            }
        });

        ventana.setVisible(true);
    }
}
