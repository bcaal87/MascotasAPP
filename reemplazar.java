package com.umg.ui;

import com.umg.db.DB;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdopcionMascotasForm extends JFrame {

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNombre = new JTextField(22);
    private final JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Perro","Gato","Otro"});
    private final JTextField txtEdad = new JTextField(8);
    private final JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Disponible","Adoptada"});
    private final JTextArea txtNotas = new JTextArea(3, 24);

    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnBuscar  = new JButton("Buscar por Id");
    private final JButton btnListar  = new JButton("Listar");
    private final JButton btnEliminar= new JButton("Eliminar por Id");
    private final JButton btnAdoptar = new JButton("Marcar Adoptada");

    private final JTextArea txtSalida = new JTextArea(10, 60);
    private final JLabel lblEstado = new JLabel("Estado: listo.");

    public AdopcionMascotasForm() {
        setTitle("Adopción de Mascotas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        setResizable(false);

        // ===== Título =====
        JLabel titulo = new JLabel("Adopción de Mascotas", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        add(titulo, BorderLayout.NORTH);

        // ===== Formulario (CENTER) =====
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        addRow(form, c, row++, "Id:", txtId);
        addRow(form, c, row++, "Nombre:", txtNombre);
        addRow(form, c, row++, "Tipo:", comboTipo);
        addRow(form, c, row++, "Edad:", txtEdad);
        addRow(form, c, row++, "Estado:", comboEstado);
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Notas:"), c);
        c.gridx = 1; c.gridy = row++; form.add(new JScrollPane(txtNotas), c);
        add(form, BorderLayout.CENTER);

        // ===== Contenedor inferior (SOUTH) =====
        JPanel southContainer = new JPanel(new BorderLayout(5,5));
        add(southContainer, BorderLayout.SOUTH);

        // 1) Botonera (NORTH del contenedor)
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        acciones.add(btnGuardar);
        acciones.add(btnBuscar);
        acciones.add(btnListar);
        acciones.add(btnEliminar);
        acciones.add(btnAdoptar);
        southContainer.add(acciones, BorderLayout.NORTH);

        // 2) Salida (CENTER del contenedor)
        txtSalida.setEditable(false);
        southContainer.add(new JScrollPane(txtSalida), BorderLayout.CENTER);

        // 3) Estado (SOUTH del contenedor)
        southContainer.add(lblEstado, BorderLayout.SOUTH);

        // ===== Listeners =====
        btnGuardar.addActionListener(e -> guardar());
        btnBuscar.addActionListener(e -> buscar());
        btnListar.addActionListener(e -> listar());
        btnEliminar.addActionListener(e -> eliminar());
        btnAdoptar.addActionListener(e -> adoptar());

        pack();
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent comp) {
        c.gridx = 0; c.gridy = row; c.weightx = 0; panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1; panel.add(comp, c);
    }

    private void setEstado(String m){ lblEstado.setText("Estado: " + m); }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String tipo = comboTipo.getSelectedItem().toString();
        String edad = txtEdad.getText().trim();
        String estado = comboEstado.getSelectedItem().toString();
        String notas = txtNotas.getText().trim();

        if (nombre.isEmpty() || edad.isEmpty()) {
            setEstado("Completa nombre y edad.");
            return;
        }

        final String sql = "INSERT INTO dbo.Mascotas(Nombre, Tipo, Edad, Estado, Notas) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setInt(3, Integer.parseInt(edad));
            ps.setString(4, estado);
            ps.setString(5, notas.isEmpty()? null : notas);
            int f = ps.executeUpdate();
            setEstado(f>0? "Mascota guardada." : "No se guardó.");
            limpiar(false);
        } catch (Exception ex) { setEstado("Error: " + ex.getMessage()); ex.printStackTrace(); }
    }

    private void buscar() {
        if (txtId.getText().trim().isEmpty()) { setEstado("Ingresa Id."); return; }
        final String sql = "SELECT Id,Nombre,Tipo,Edad,Estado,Notas FROM dbo.Mascotas WHERE Id = ?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtId.getText().trim()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtNombre.setText(rs.getString("Nombre"));
                    comboTipo.setSelectedItem(rs.getString("Tipo"));
                    txtEdad.setText(String.valueOf(rs.getInt("Edad")));
                    comboEstado.setSelectedItem(rs.getString("Estado"));
                    txtNotas.setText(rs.getString("Notas"));
                    setEstado("Mascota encontrada.");
                } else setEstado("Id no encontrado.");
            }
        } catch (Exception ex){ setEstado("Error: " + ex.getMessage()); ex.printStackTrace(); }
    }

    private void listar() {
        final String sql = "SELECT Id,Nombre,Tipo,Edad,Estado FROM dbo.Mascotas ORDER BY Id DESC";
        try (Connection con = DB.get();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            StringBuilder sb = new StringBuilder("=== Mascotas ===\n");
            while (rs.next()) {
                sb.append(rs.getInt("Id")).append(" | ")
                  .append(rs.getString("Nombre")).append(" | ")
                  .append(rs.getString("Tipo")).append(" | ")
                  .append(rs.getInt("Edad")).append(" años | ")
                  .append(rs.getString("Estado")).append("\n");
            }
            txtSalida.setText(sb.toString());
            setEstado("Listado OK.");
        } catch (Exception ex){ setEstado("Error: " + ex.getMessage()); ex.printStackTrace(); }
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) { setEstado("Ingresa Id."); return; }
        final String sql = "DELETE FROM dbo.Mascotas WHERE Id = ?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtId.getText().trim()));
            int f = ps.executeUpdate();
            setEstado(f>0? "Mascota eliminada." : "Id no existe.");
            limpiar(true);
        } catch (Exception ex){ setEstado("Error: " + ex.getMessage()); ex.printStackTrace(); }
    }

    private void adoptar() {
        if (txtId.getText().trim().isEmpty()) { setEstado("Ingresa Id."); return; }
        final String sql = "UPDATE dbo.Mascotas SET Estado='Adoptada' WHERE Id = ?";
        try (Connection con = DB.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtId.getText().trim()));
            int f = ps.executeUpdate();
            setEstado(f>0? "Marcada como Adoptada." : "Id no existe.");
        } catch (Exception ex){ setEstado("Error: " + ex.getMessage()); ex.printStackTrace(); }
    }

    private void limpiar(boolean limpiarId){
        if (limpiarId) txtId.setText("");
        txtNombre.setText("");
        comboTipo.setSelectedIndex(0);
        txtEdad.setText("");
        comboEstado.setSelectedIndex(0);
        txtNotas.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdopcionMascotasForm().setVisible(true));
    }
}
