import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.plot.XYPlot;

public class JFRelatorio extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel chartPanel;
    private JComboBox<String> chartTypeComboBox;

    public JFRelatorio() {
        setTitle("Disponibilidade Interativa");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        tableModel = new DefaultTableModel(new String[]{"Dia", "Início", "Fim", "Total"}, 0);
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(400, 600));
        add(tableScrollPane, BorderLayout.WEST);

        
        chartPanel = new JPanel(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        
        chartTypeComboBox = new JComboBox<>(new String[]{"Barras", "Linhas"});
        chartTypeComboBox.addActionListener(e -> carregarDados());
        add(chartTypeComboBox, BorderLayout.NORTH);

        setLocationRelativeTo(null); 
        carregarDados();
    }
    
 private void carregarDados() {
    try (Connection cn = connector.getConnector()) {
        String sql = "SELECT dis_dia, dis_inicio, dis_fim, " +
                     "(SELECT COUNT(*) FROM disponibilidade d2 " +
                     "WHERE d2.dis_dia = d1.dis_dia AND d2.dis_inicio <= d1.dis_fim AND d2.dis_fim >= d1.dis_inicio) AS total " +
                     "FROM disponibilidade d1 " +
                     "GROUP BY dis_dia, dis_inicio, dis_fim " +
                     "ORDER BY dis_dia ASC, dis_inicio ASC";
        PreparedStatement ps = cn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        XYSeries lineSeries = new XYSeries("Disponibilidade");
        tableModel.setRowCount(0);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        ArrayList<String> labelsList = new ArrayList<>();
        int counter = 0;

        while (rs.next()) {
            int dia = rs.getInt("dis_dia");
            Time inicio = rs.getTime("dis_inicio");
            Time fim = rs.getTime("dis_fim");
            int total = rs.getInt("total");

            String diaSemana = getDiaSemana(dia);
            String horarioLabel = diaSemana + " " + timeFormat.format(inicio) + " - " + timeFormat.format(fim);
            barDataset.addValue(total, "Disponibilidade", horarioLabel);

            lineSeries.add(counter++, total);
            labelsList.add(horarioLabel);
            tableModel.addRow(new Object[]{diaSemana, timeFormat.format(inicio), timeFormat.format(fim), total});
        }

        String[] labels = labelsList.toArray(new String[0]);

        JFreeChart chart;
        if (chartTypeComboBox.getSelectedItem().equals("Barras")) {
            chart = ChartFactory.createBarChart("Horários Mais Disponíveis", "Horário", "Total", barDataset, PlotOrientation.VERTICAL, false, true, false);
            CategoryAxis xAxis = chart.getCategoryPlot().getDomainAxis();
            xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        } else {
            XYSeriesCollection lineDataset = new XYSeriesCollection(lineSeries);
            chart = ChartFactory.createXYLineChart("Disponibilidade ao Longo da Semana", "Índice", "Total", lineDataset);

           
            DateAxis axis = new DateAxis("Hora");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE HH:mm"); 
            axis.setDateFormatOverride(dateFormat);

            XYPlot plot = chart.getXYPlot();
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setDomainAxis(axis); 
        }

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 500));
        chartPanel.removeAll();
        chartPanel.add(panel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();

    } catch (SQLException | ClassNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Erro ao carregar dados!", "Erro", JOptionPane.ERROR_MESSAGE);
    }
}



    private String getDiaSemana(int dia) {
        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};
        return dias[dia - 1];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFRelatorio().setVisible(true));
    }
}
