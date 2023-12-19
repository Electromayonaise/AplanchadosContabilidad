package ui;

import javax.swing.*;
import java.awt.*;

public class FrontDateSelector extends BasePanel {

    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> dayComboBox;
    public String date;

    public FrontDateSelector(JPanel containerPanel) {
        super(containerPanel);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        Color myRed = new Color(255, 175, 175);

        // Create a JLabel for the title
        JLabel titleLabel = new JLabel("CONTABILIDAD APLANCHADOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(myRed); // Soft red color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Create JPanel for date selection components with FlowLayout
        JPanel dateSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Add JComboBox for day
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        dayComboBox = new JComboBox<>(days);
        dayComboBox.setSelectedIndex(0);
        dateSelectionPanel.add(new JLabel("Dia: "));
        dateSelectionPanel.add(dayComboBox);

        // Add JComboBox for month
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(0);
        dateSelectionPanel.add(new JLabel("Mes: "));
        dateSelectionPanel.add(monthComboBox);

        // Add JComboBox for year
        Integer[] years = {2025, 2024, 2023};
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedIndex(0);
        dateSelectionPanel.add(new JLabel("Año: "));
        dateSelectionPanel.add(yearComboBox);

        // Create a panel to hold the date selection panel and button panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(dateSelectionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 30, 30, 30);

        // Create a button for data
        JButton dataButton = createStyledButton("Siguiente", myRed);
        dataButton.addActionListener(e -> saveDateAndShowPanel());
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(dataButton, gbc);

        // Add the button panel to the south of the center panel
        centerPanel.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        // Add the center panel to the center
        add(centerPanel, BorderLayout.CENTER);
    }

    private void saveDateAndShowPanel() {
        // Save the selected date to the data model
        int day = (int) dayComboBox.getSelectedItem();
        int month = monthComboBox.getSelectedIndex() + 1;
        int year = (int) yearComboBox.getSelectedItem();
        setDate(day + "/" + month + "/" + year);
        GeneralMenuPanel generalMenuPanel = new GeneralMenuPanel(containerPanel, getDate());
        containerPanel.add(generalMenuPanel, "GeneralMenuPanel");
        CardLayout cardLayout = (CardLayout) containerPanel.getLayout();
        cardLayout.show(containerPanel, "GeneralMenuPanel");

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}





