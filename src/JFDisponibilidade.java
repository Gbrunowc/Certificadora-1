
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JFDisponibilidade extends JFrame {

    private JTextField cpfField;
    private JSpinner horaInicio, minutoInicio, horaFim, minutoFim;
    private JComboBox<String> diaComboBox;
    private DefaultTableModel tableModel;
    private JTable table;
    private Disponibilidade disponibilidadeDAO;

    public JFDisponibilidade(long cpf) {
        try {
            setTitle("Cadastro de Disponibilidade");
            setSize(700, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            disponibilidadeDAO = new Disponibilidade();

            JPanel inputPanel = new JPanel(new GridLayout(3, 2));

            cpfField = new JTextField();
            cpfField.setText(String.valueOf(cpf));
            inputPanel.add(new JLabel("CPF:"));
            inputPanel.add(cpfField);

            diaComboBox = new JComboBox<>(new String[]{"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"});
            inputPanel.add(new JLabel("Dia da Semana:"));
            inputPanel.add(diaComboBox);

            JPanel horarioPanel = new JPanel(new GridLayout(2, 4));

            horaInicio = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
            minutoInicio = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
            horaFim = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
            minutoFim = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

            horarioPanel.add(new JLabel("Hora Início:"));
            horarioPanel.add(horaInicio);
            horarioPanel.add(new JLabel("Minuto Início:"));
            horarioPanel.add(minutoInicio);
            horarioPanel.add(new JLabel("Hora Fim:"));
            horarioPanel.add(horaFim);
            horarioPanel.add(new JLabel("Minuto Fim:"));
            horarioPanel.add(minutoFim);

            JButton addButton = new JButton("Adicionar");
            JButton searchButton = new JButton("Buscar");
            JButton deleteButton = new JButton("Excluir");

            addButton.addActionListener(e -> adicionarDisponibilidade());
            searchButton.addActionListener(e -> buscarDisponibilidades());
            deleteButton.addActionListener(e -> {
                try {
                    excluirDisponibilidade();
                } catch (Exception ex) {
                    Logger.getLogger(JFDisponibilidade.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            tableModel = new DefaultTableModel(new String[]{"ID", "Dia", "Início", "Fim"}, 0);
            table = new JTable(tableModel);

            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(0).setWidth(0);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addButton);
            buttonPanel.add(searchButton);
            buttonPanel.add(deleteButton);

            add(inputPanel, BorderLayout.NORTH);
            add(horarioPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
            add(new JScrollPane(table), BorderLayout.EAST);
            setLocationRelativeTo(null);
        } catch (Exception ex) {
            Logger.getLogger(JFDisponibilidade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JFDisponibilidade() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void adicionarDisponibilidade() {
        try {
            long cpf = Long.parseLong(cpfField.getText().trim());

            if (!cpfExisteNoBanco(cpf)) {
                JOptionPane.showMessageDialog(this, "CPF não encontrado na base de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int dia = diaComboBox.getSelectedIndex() + 1;
            Time inicio = Time.valueOf(String.format("%02d:%02d:00", (int) horaInicio.getValue(), (int) minutoInicio.getValue()));
            Time fim = Time.valueOf(String.format("%02d:%02d:00", (int) horaFim.getValue(), (int) minutoFim.getValue()));

            Disponibilidade disponibilidade = new Disponibilidade(0, cpf, dia, inicio, fim);
            disponibilidadeDAO.inserirDisponibilidade(disponibilidade);
            buscarDisponibilidades();

            JOptionPane.showMessageDialog(this, "Disponibilidade adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CPF inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar disponibilidade!\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean cpfExisteNoBanco(long cpf) throws SQLException, ClassNotFoundException {
        try (Connection cn = connector.getConnector()) {
            String sql = "SELECT 1 FROM voluntario WHERE vol_cpf = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setLong(1, cpf);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    private void buscarDisponibilidades() {
        try {
            long cpf = Long.parseLong(cpfField.getText().trim());
            List<Disponibilidade> disponibilidades = disponibilidadeDAO.consultarDisponibilidades(cpf);
            tableModel.setRowCount(0);
            for (Disponibilidade d : disponibilidades) {
                tableModel.addRow(new Object[]{
                    d.getDis_id(),
                    diaComboBox.getItemAt(d.getDis_dia() - 1),
                    d.getDis_inicio(),
                    d.getDis_fim()
                });
            }
        } catch (NumberFormatException | SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar disponibilidades!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirDisponibilidade() throws Exception {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int dis_id = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                disponibilidadeDAO.excluirDisponibilidade(dis_id);
                buscarDisponibilidades();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir disponibilidade!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFDisponibilidade(12345678901L).setVisible(true));
    }

}
