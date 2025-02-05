import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadeFrame extends JFrame {

    private JTextField pessoaIdField;
    private JComboBox<String> diaSemanaCombo;
    private JSpinner horarioInicioSpinner;
    private JSpinner horarioFimSpinner;
    private JButton adicionarButton;
    private JButton salvarButton;
    private JTextArea logArea;

    private List<Disponibilidade> disponibilidades;

    public DisponibilidadeFrame() {
        setTitle("Cadastro de Disponibilidade");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        disponibilidades = new ArrayList<>();

        // Painel de entrada de dados
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));

        inputPanel.add(new JLabel("ID da Pessoa:"));
        pessoaIdField = new JTextField();
        inputPanel.add(pessoaIdField);

        inputPanel.add(new JLabel("Dia da Semana:"));
        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};
        diaSemanaCombo = new JComboBox<>(dias);
        inputPanel.add(diaSemanaCombo);

        inputPanel.add(new JLabel("Horário Início:"));
        horarioInicioSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor inicioEditor = new JSpinner.DateEditor(horarioInicioSpinner, "HH:mm");
        horarioInicioSpinner.setEditor(inicioEditor);
        inputPanel.add(horarioInicioSpinner);

        inputPanel.add(new JLabel("Horário Fim:"));
        horarioFimSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor fimEditor = new JSpinner.DateEditor(horarioFimSpinner, "HH:mm");
        horarioFimSpinner.setEditor(fimEditor);
        inputPanel.add(horarioFimSpinner);

        adicionarButton = new JButton("Adicionar Disponibilidade");
        inputPanel.add(adicionarButton);

        salvarButton = new JButton("Salvar no Banco");
        inputPanel.add(salvarButton);

        add(inputPanel, BorderLayout.NORTH);

        // Área de log
        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        // Listeners
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarDisponibilidade();
            }
        });

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarDisponibilidades();
            }
        });
    }

    private void adicionarDisponibilidade() {
        try {
            int pessoaId = Integer.parseInt(pessoaIdField.getText());
            int diaSemana = diaSemanaCombo.getSelectedIndex() + 1; // +1 para corresponder ao banco de dados
            java.sql.Time horarioInicio = java.sql.Time.valueOf(horarioInicioSpinner.getValue().toString());
            java.sql.Time horarioFim = java.sql.Time.valueOf(horarioFimSpinner.getValue().toString());

            Disponibilidade disp = new Disponibilidade(pessoaId, diaSemana, horarioInicio, horarioFim);
            disponibilidades.add(disp);

            logArea.append("Disponibilidade adicionada: " + disp + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID da pessoa inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarDisponibilidades() {
        if (disponibilidades.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disponibilidade para salvar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO Disponibilidade (pessoa_id, dia_semana, horario_inicio, horario_fim) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seu_banco", "usuario", "senha");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Disponibilidade disp : disponibilidades) {
                pstmt.setInt(1, disp.getPessoaId());
                pstmt.setInt(2, disp.getDiaSemana());
                pstmt.setTime(3, disp.getHorarioInicio());
                pstmt.setTime(4, disp.getHorarioFim());
                pstmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Disponibilidades salvas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            disponibilidades.clear();
            logArea.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DisponibilidadeFrame().setVisible(true);
            }
        });
    }
}

class Disponibilidade {
    private int pessoaId;
    private int diaSemana;
    private java.sql.Time horarioInicio;
    private java.sql.Time horarioFim;

    public Disponibilidade(int pessoaId, int diaSemana, java.sql.Time horarioInicio, java.sql.Time horarioFim) {
        this.pessoaId = pessoaId;
        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }

    @Override
    public String toString() {
        return String.format("Pessoa ID: %d, Dia: %d, Horário: %s - %s", pessoaId, diaSemana, horarioInicio, horarioFim);
    }

    // Getters
    public int getPessoaId() { return pessoaId; }
    public int getDiaSemana() { return diaSemana; }
    public java.sql.Time getHorarioInicio() { return horarioInicio; }
    public java.sql.Time getHorarioFim() { return horarioFim; }
}