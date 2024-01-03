package ui;

import model.Controller;
import model.Expenses;
import model.OperationExpenses;
import model.SupplyExpenses;
import model.ArrayList;

import javax.swing.*;
import java.awt.*;

public class ExpensesPanel extends BasePanel {

    private final Controller controller;

    private final Color myRed = new Color(255, 175, 175);

    private JPanel displayPanel;

    private final CardLayout cardLayout = new CardLayout();
    private DefaultListModel<String> expensesListModel;
    private DefaultListModel<String> expensesIDListModel;
    private boolean displaySupply = true;

    public ExpensesPanel(JPanel containerPanel) {
        super(containerPanel);
        controller = Controller.getInstance();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titleLabel = new JLabel("Expenses Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(myRed);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);

        JPanel leftColumnPanel = new JPanel(new BorderLayout());
        leftColumnPanel.setOpaque(false);

        JButton toggleDisplayButton = createStyledButton("Change visualization", myRed);
        toggleDisplayButton.addActionListener(e -> {
            displaySupply = !displaySupply;
            refreshDisplay();
        });

        leftColumnPanel.add(toggleDisplayButton, BorderLayout.NORTH);

        add(leftColumnPanel, BorderLayout.WEST);

        displayPanel = new JPanel(cardLayout);
        displayPanel.setOpaque(false);

        JPanel supplyPanel = new JPanel(new BorderLayout());
        JLabel supplyLabel = new JLabel("Supply Expenses");
        supplyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        supplyLabel.setForeground(myRed);
        supplyPanel.add(supplyLabel, BorderLayout.NORTH);
        expensesListModel = new DefaultListModel<>();
        expensesIDListModel = new DefaultListModel<>();
        updateExpensesListModels();
        JList<String> supplyExpensesList = new JList<>(expensesIDListModel);
        supplyExpensesList.setFont(new Font("Arial", Font.PLAIN, 16));
        displayPanel.add(supplyPanel, "Supply Expenses");

        JPanel operationPanel = new JPanel(new BorderLayout());
        JLabel operationLabel = new JLabel("Operation Expenses");
        operationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        operationLabel.setForeground(myRed);
        operationPanel.add(operationLabel, BorderLayout.NORTH);
        JList<String> operationExpensesList = new JList<>(expensesIDListModel);
        operationExpensesList.setFont(new Font("Arial", Font.PLAIN, 16));
        displayPanel.add(operationPanel, "Operation Expenses");

        leftColumnPanel.add(displayPanel, BorderLayout.CENTER);

        JPanel rightColumnPanel = new JPanel(new GridLayout(0, 2));
        rightColumnPanel.setOpaque(false);

        JList<String> expensesList = new JList<>(expensesIDListModel);
        expensesList.setFont(new Font("Arial", Font.PLAIN, 16));
        expensesList.setForeground(myRed);
        JScrollPane scrollPane = new JScrollPane(expensesList);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        expensesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = expensesList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedExpenseID = expensesIDListModel.getElementAt(selectedIndex);
                    showExpenseInfo(selectedExpenseID);
                }
            }
        });

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2));
        buttonPanel.setOpaque(false);

        JButton modifyButton = createStyledButton("Modificar gasto", myRed);
        modifyButton.addActionListener(e -> {
            int selectedIndex = expensesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedExpenseID = expensesIDListModel.getElementAt(selectedIndex);
                Expenses selectedExpense = displaySupply
                        ? controller.getSupplyExpense(selectedExpenseID)
                        : controller.getOperationExpense(selectedExpenseID);

                String[] options = {"Modificar Detalle", "Modificar Documento", "Modificar Valor"};
                int choice = JOptionPane.showOptionDialog(ExpensesPanel.this, "Que desea modificar?", "Modificar Gasto", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    // Modify Detail
                    String modifiedDetail = JOptionPane.showInputDialog("Modificar Detalle", selectedExpense.getDetail());
                    if (modifiedDetail != null && !modifiedDetail.isEmpty()) {
                        controller.modifyExpense(selectedExpenseID, 1, modifiedDetail);
                        expensesIDListModel.setElementAt(modifiedDetail, selectedIndex);
                    }
                } else if (choice == 1) {
                    // Modify Document
                    String modifiedDocument = JOptionPane.showInputDialog("Modificar Documento", selectedExpense.getDocument());
                    if (modifiedDocument != null && !modifiedDocument.isEmpty()) {
                        controller.modifyExpense(selectedExpenseID, 2, modifiedDocument);
                        expensesIDListModel.setElementAt(modifiedDocument, selectedIndex);
                    }
                } else if (choice == 2) {
                    // Modify Value
                    String modifiedValue = JOptionPane.showInputDialog("Modificar Valor", selectedExpense.getValue());
                    if (modifiedValue != null && !modifiedValue.isEmpty()) {
                        controller.modifyExpense(selectedExpenseID, 3, modifiedValue);
                        expensesIDListModel.setElementAt(modifiedValue, selectedIndex);
                    }
                }

                JOptionPane.showMessageDialog(ExpensesPanel.this, "Gasto modificado exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(ExpensesPanel.this, "Porfavor selecciona un gasto primero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton deleteButton = createStyledButton("Eliminar gasto", myRed);
        deleteButton.addActionListener(e -> {
            int selectedIndex = expensesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedExpenseID = expensesIDListModel.getElementAt(selectedIndex);
                expensesIDListModel.removeElementAt(selectedIndex);
                expensesListModel.removeElementAt(selectedIndex);
                controller.removeExpense(selectedExpenseID);
                JOptionPane.showMessageDialog(ExpensesPanel.this, "Gasto eliminado exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(ExpensesPanel.this, "Porfavor selecciona un gasto primero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        JPanel addExpensePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addExpensePanel.setOpaque(false);

        JButton addButton = createStyledButton("Añadir gasto", myRed);
        addButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.setOpaque(false);

            JTextField detailField = new JTextField();
            JTextField documentField = new JTextField();
            JTextField valueField = new JTextField();

            inputPanel.add(new JLabel("Detalle:"));
            inputPanel.add(detailField);
            inputPanel.add(new JLabel("Documento:"));
            inputPanel.add(documentField);
            inputPanel.add(new JLabel("Valor:"));
            inputPanel.add(valueField);

            int result = JOptionPane.showConfirmDialog(ExpensesPanel.this, inputPanel, "Añadir gasto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String detail = detailField.getText();
                String document = documentField.getText();
                String valueStr = valueField.getText();

                // Validate input
                if (detail.isEmpty() || document.isEmpty() || valueStr.isEmpty()) {
                    JOptionPane.showMessageDialog(ExpensesPanel.this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double value = Double.parseDouble(valueStr);

                    if (displaySupply) {
                        // Create a new supply expense
                        controller.addExpense(document, detail, value, false);
                        SupplyExpenses newSupplyExpense = controller.getSupplyExpense(document);
                        expensesIDListModel.addElement(newSupplyExpense.hash());
                        expensesListModel.addElement(newSupplyExpense.getDetail());
                    } else {
                        // Create a new operation expense
                        controller.addExpense(document, detail, value, false);
                        OperationExpenses newOperationExpense = controller.getOperationExpense(document);
                        expensesIDListModel.addElement(newOperationExpense.hash());
                        expensesListModel.addElement(newOperationExpense.getDetail());
                    }

                    JOptionPane.showMessageDialog(ExpensesPanel.this, "Gasto añadido exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(ExpensesPanel.this, "Por favor, introduce un valor numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addExpensePanel.add(addButton);

        JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        returnButtonPanel.setOpaque(false);

        JButton returnButton = getReturnButton();

        returnButtonPanel.add(returnButton);

        JPanel buttonsContainer = new JPanel(new BorderLayout());
        buttonsContainer.setOpaque(false);

        buttonsContainer.add(buttonPanel, BorderLayout.NORTH);
        buttonsContainer.add(addExpensePanel, BorderLayout.CENTER);
        buttonsContainer.add(returnButtonPanel, BorderLayout.SOUTH);

        add(buttonsContainer, BorderLayout.SOUTH);

        JButton undoButton = createStyledButton("Undo", myRed);
        undoButton.addActionListener(e -> {
            boolean undo = controller.undo();
            if (undo) {
                refreshDisplay();
                JOptionPane.showMessageDialog(ExpensesPanel.this, "Undo successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(ExpensesPanel.this, "No undo possible", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        leftColumnPanel.add(undoButton, BorderLayout.SOUTH);
    }

    private void updateExpensesListModels() {
        expensesIDListModel.clear();
        expensesListModel.clear();

        ArrayList<ArrayList<String>> expenses;
        if (displaySupply) {
            expenses = controller.getExpensesAttributes(false);
        } else {
            expenses = controller.getExpensesAttributes(true);
        }

        for (ArrayList<String> expense : expenses) {
            String expenseID = expense.get(0);
            expensesIDListModel.addElement(expenseID);

            String expenseDetails = expense.get(1) + " Document: " + expense.get(2) + " Value: " + expense.get(3);
            expensesListModel.addElement(expenseDetails);
        }
    }

    private void showExpenseInfo(String selectedExpenseID) {
        ArrayList<ArrayList<String>> expenses;
        if (displaySupply) {
            expenses = controller.getExpensesAttributes(false);
        } else {
            expenses = controller.getExpensesAttributes(true);
        }

        for (ArrayList<String> expense : expenses) {
            if (expense.get(0).equals(selectedExpenseID)) {
                String expenseDetails = "Expense ID: " + expense.get(0) + "\n"
                        + "Expense Detail: " + expense.get(1) + "\n"
                        + "Expense Document: " + expense.get(2) + "\n"
                        + "Expense Value: " + expense.get(3);

                JOptionPane.showMessageDialog(this, expenseDetails, "Expense Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void refreshDisplay() {
        cardLayout.show(displayPanel, displaySupply ? "Supply Expenses" : "Operation Expenses");
        updateExpensesListModels();
    }
}

