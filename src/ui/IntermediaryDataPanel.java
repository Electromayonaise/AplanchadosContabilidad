package ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class IntermediaryDataPanel extends BasePanel {
    /**
     * Constructs a GeneralMenuPanel.
     *
     * @param containerPanel the container panel for managing card layout
     */
    public IntermediaryDataPanel(JPanel containerPanel) {
        super(containerPanel);
        initUI();
    }

    /**
     * Initializes the user interface components.
     */
    private void initUI() {
        setLayout(new BorderLayout());
        Color myRed = new Color(255, 175, 175);

        // Create a JLabel for the title
        JLabel titleLabel = new JLabel("CONTABILIDAD APLANCHADOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(myRed); // Soft red color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add some padding
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 30, 30, 30);

        // Create a button for data
        JButton entriesButton = createStyledButton("Entradas", myRed);
        entriesButton.addActionListener(e -> showEntriesPanel());
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(entriesButton, gbc);

        // Create a button for day resume
        JButton otherEntriesButton = createStyledButton("Otros ingresos", myRed);
        otherEntriesButton.addActionListener(e -> showOtherIncomesPanel());
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(otherEntriesButton, gbc);

        // Create a button for sells resume
        JButton expensesButton = createStyledButton("Salidas", myRed);
        expensesButton.addActionListener(e -> showExpensesPanel());
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(expensesButton, gbc);

        // Create a button for saving the data of the day to an Excel file
        JButton otherExpensesButton = createStyledButton("Otras salidas", myRed);
        otherExpensesButton.addActionListener(e -> showOtherExpensesPanel());
        gbc.gridx = 0;
        gbc.gridy = 3;
        buttonPanel.add(otherExpensesButton, gbc);

        // Create a button to return to the main menu
        JButton returnButton = getReturnButton();
        gbc.gridx = 0;
        gbc.gridy = 4;
        buttonPanel.add(returnButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Creates a styled JButton with specific text and background color.
     *
     * @param text   the text to be displayed on the button
     * @param bgColor the background color of the button
     * @return the styled JButton
     */
    protected JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 60));
        return button;
    }

    /**
     * Helper method to show a specific panel using card layout.
     */
    private void showEntriesPanel() {
        EntriesPanel entriesPanel = new EntriesPanel(containerPanel);
        containerPanel.add(entriesPanel, "EntriesPanel");
        CardLayout cardLayout = (CardLayout) containerPanel.getLayout();
        cardLayout.show(containerPanel, "EntriesPanel");
    }

    private void showOtherIncomesPanel() {
        OtherIncomesPanel otherIncomesPanel = new OtherIncomesPanel(containerPanel);
        containerPanel.add(otherIncomesPanel, "OtherIncomesPanel");
        CardLayout cardLayout = (CardLayout) containerPanel.getLayout();
        cardLayout.show(containerPanel, "OtherIncomesPanel");
    }

    private void showExpensesPanel() {
        ExpensesPanel expensesPanel = new ExpensesPanel(containerPanel);
        containerPanel.add(expensesPanel, "ExpensesPanel");
        CardLayout cardLayout = (CardLayout) containerPanel.getLayout();
        cardLayout.show(containerPanel, "ExpensesPanel");
    }

    private void showOtherExpensesPanel() {
        OtherExpensesPanel otherExpensesPanel = new OtherExpensesPanel(containerPanel);
        containerPanel.add(otherExpensesPanel, "OtherExpensesPanel");
        CardLayout cardLayout = (CardLayout) containerPanel.getLayout();
        cardLayout.show(containerPanel, "OtherExpensesPanel");
    }


}
