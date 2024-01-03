package ui;
import model.Controller;
import model.OtherExpenses;

import javax.swing.*;
import java.awt.*;

public class OtherExpensesPanel extends BasePanel {

    private final Controller controller;

    private final Color myRed = new Color(255, 175, 175);

    private JPanel displayPanel;
    private DefaultListModel<String> entriesListModel;
    private DefaultListModel<String> entriesIDListModel;

    /**
     * Constructs an OtherIncomesPanel with a reference to the container panel.
     *
     * @param containerPanel The container panel that holds this OtherIncomesPanel.
     */
    public OtherExpensesPanel(JPanel containerPanel) {
        super(containerPanel);
        controller = Controller.getInstance();
        initUI();
    }

    /**
     * Initializes the UI components of the OtherIncomesPanel.
     */
    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titleLabel = new JLabel("Other Incomes Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(myRed);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);

        // Create a panel for displaying Other Entries
        displayPanel = new JPanel(new BorderLayout());
        displayPanel.setOpaque(false);

        JLabel entriesLabel = new JLabel("Other Entries");
        entriesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        entriesLabel.setForeground(myRed);
        displayPanel.add(entriesLabel, BorderLayout.NORTH);

        entriesListModel = new DefaultListModel<>();
        entriesIDListModel = new DefaultListModel<>();
        updateEntriesListModels();

        JList<String> entriesList = new JList<>(entriesIDListModel);
        entriesList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(entriesList);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(displayPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2));
        buttonPanel.setOpaque(false);

        JList<String> entriesListForButtons = new JList<>(entriesIDListModel);
        entriesListForButtons.setFont(new Font("Arial", Font.PLAIN, 16));
        entriesListForButtons.setForeground(myRed);

        // Add a selection listener to handle item click events
        entriesListForButtons.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = entriesListForButtons.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedEntryID = entriesIDListModel.getElementAt(selectedIndex);
                    showEntryInfo(selectedEntryID);
                }
            }
        });

        buttonPanel.add(new JScrollPane(entriesListForButtons));

        // Create a panel for the "Add Entry" button
        JPanel addEntryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addEntryPanel.setOpaque(false);

        JButton addEntryButton = createStyledButton("Add Entry", myRed);
        addEntryButton.addActionListener(e -> {
            // Create a panel for the input fields
            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.setOpaque(false);

            JTextField detailField = new JTextField();
            JTextField documentField = new JTextField();
            JTextField valueField = new JTextField();

            inputPanel.add(new JLabel("Detail:"));
            inputPanel.add(detailField);
            inputPanel.add(new JLabel("Document:"));
            inputPanel.add(documentField);
            inputPanel.add(new JLabel("Value:"));
            inputPanel.add(valueField);

            int result = JOptionPane.showConfirmDialog(OtherExpensesPanel.this, inputPanel, "Add Expense", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String detail = detailField.getText();
                String document = documentField.getText();
                String value = valueField.getText();

                controller.addOtherExpense(document, detail, Double.parseDouble(value));
                OtherExpenses newOtherExpense = controller.getOtherExpense(document);
                entriesIDListModel.addElement(newOtherExpense.getDocument());
                entriesListModel.addElement(newOtherExpense.getDetail());

                JOptionPane.showMessageDialog(OtherExpensesPanel.this, "Entry added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        addEntryPanel.add(addEntryButton);
        buttonPanel.add(addEntryPanel);

        // Create a panel for the "Modify Entry" button
        JPanel modifyEntryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        modifyEntryPanel.setOpaque(false);

        JButton modifyEntryButton = createStyledButton("Modify Entry", myRed);
        modifyEntryButton.addActionListener(e -> {
            int selectedIndex = entriesListForButtons.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedExpenseID = entriesIDListModel.getElementAt(selectedIndex);
                OtherExpenses selectedExpense = controller.getOtherExpense(selectedExpenseID);

                String[] options = {"Modify Detail", "Modify Document", "Modify Value"};
                int choice = JOptionPane.showOptionDialog(
                        OtherExpensesPanel.this, "What do you want to modify?", "Modify Entry",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    // Modify Detail
                    String modifiedDetail = JOptionPane.showInputDialog("Modify Detail", selectedExpense.getDetail());
                    if (modifiedDetail != null && !modifiedDetail.isEmpty()) {
                        controller.modifyOtherEntry(selectedExpenseID, 1, modifiedDetail);
                        entriesIDListModel.setElementAt(modifiedDetail, selectedIndex);
                    }
                } else if (choice == 1) {
                    // Modify Document
                    String modifiedDocument = JOptionPane.showInputDialog("Modify Document", selectedExpense.getDocument());
                    if (modifiedDocument != null && !modifiedDocument.isEmpty()) {
                        controller.modifyOtherEntry(selectedExpenseID, 2, modifiedDocument);
                        entriesIDListModel.setElementAt(modifiedDocument, selectedIndex);
                    }
                } else if (choice == 2) {
                    // Modify Value
                    String modifiedValue = JOptionPane.showInputDialog("Modify Value", selectedExpense.getValue());
                    if (modifiedValue != null && !modifiedValue.isEmpty()) {
                        controller.modifyOtherEntry(selectedExpenseID, 3, modifiedValue);
                        entriesIDListModel.setElementAt(modifiedValue, selectedIndex);
                    }
                }

                JOptionPane.showMessageDialog(OtherExpensesPanel.this, "Entry modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(OtherExpensesPanel.this, "Please select an entry first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        modifyEntryPanel.add(modifyEntryButton);
        buttonPanel.add(modifyEntryPanel);

        // Create a panel for the "Delete Entry" button
        JPanel deleteEntryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deleteEntryPanel.setOpaque(false);

        JButton deleteEntryButton = createStyledButton("Delete Entry", myRed);
        deleteEntryButton.addActionListener(e -> {
            int selectedIndex = entriesListForButtons.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedEntryID = entriesIDListModel.getElementAt(selectedIndex);
                // Remove the entry from the list models
                entriesIDListModel.removeElementAt(selectedIndex);
                entriesListModel.removeElementAt(selectedIndex);
                // Remove the entry from the controller
                controller.removeOtherEntry(selectedEntryID);
                JOptionPane.showMessageDialog(OtherExpensesPanel.this, "Entry deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(OtherExpensesPanel.this, "Please select an entry first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteEntryPanel.add(deleteEntryButton);
        buttonPanel.add(deleteEntryPanel);

        // Create a panel for the "Return to Menu" button
        JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        returnButtonPanel.setOpaque(false);

        // Return to Menu button
        JButton returnButton = getReturnButton();
        returnButtonPanel.add(returnButton);

        // Add button panels to the main panel
        add(buttonPanel, BorderLayout.SOUTH);
        add(returnButtonPanel, BorderLayout.SOUTH);
    }

    /**
     * Updates the entry list models.
     */
    private void updateEntriesListModels() {
        entriesIDListModel.clear();
        entriesListModel.clear();

        // Get the entries from the controller
        for (OtherExpenses expense : controller.getOtherExpenses()) {
            String expenseID = expense.getDocument();
            entriesIDListModel.addElement(expenseID); // Only add entry ID to the list

            String expenseDetails = expense.getDetail();
            entriesListModel.addElement(expenseDetails);
        }
    }

    private void showEntryInfo(String selectedExpenseID) {
        OtherExpenses selectedExpense = controller.getOtherExpense(selectedExpenseID);

        String entryDetails = "Entry ID: " + selectedExpense.getDocument() + "\n"
                + "Entry Detail: " + selectedExpense.getDetail() + "\n"
                + "Entry Document: " + selectedExpense.getDocument() + "\n"
                + "Entry Value: " + selectedExpense.getValue();

        JOptionPane.showMessageDialog(this, entryDetails, "Entry Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
