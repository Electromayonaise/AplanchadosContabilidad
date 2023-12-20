package ui;

import model.ArrayList;
import model.Controller;
import model.CreditSales;
import model.InmediateSales;

import javax.swing.*;
import java.awt.*;

public class EntriesPanel extends BasePanel {

    /*
    * - Ventas de contado: Compuestas por Documento, Detalle, Valor, Medio de pago, y Nombre del cliente
      - Ventas a crédito: Compuestas por Documento, Detalle, Valor, y Nombre del cliente
      - Total de ventas de contado: Sumatoria de todas las ventas de contado, se dividen entre efectivo y tarjeta
      - Total de ventas a crédito: Sumatoria de todas las ventas a crédito
      - Total de ventas del día: Sumatoria de todas las ventas del día (contado + crédito)
    */
    // idea: cambiar el toggle display para que rote entre ventas de contado y ventas de credito

    private final Controller controller;

    private final Color myRed = new Color(255, 175, 175);
    private final Color TASKCOLOR = new Color(90, 90, 90);

    private JPanel displayPanel;

    private final CardLayout cardLayout = new CardLayout();
    private DefaultListModel<String> entriesListModel;
    private DefaultListModel<String> entriesIDListModel; // List to store entry IDs
    private boolean displayInmediate = true; // Track the current display mode

    /**
     * Constructs a DataPanel with a reference to the container panel.
     *
     * @param containerPanel The container panel that holds this DataPanel.
     */
    public EntriesPanel(JPanel containerPanel) {
        super(containerPanel);
        controller = Controller.getInstance();
        initUI();
    }

    /**
     * Initializes the UI components of the DataPanel.
     */
    private void initUI() {
        setLayout(new BorderLayout()); // Use BorderLayout for better element placement
        setOpaque(false); // Make the panel transparent

        // Create a title label with red color and centered
        JLabel titleLabel = new JLabel("Entries Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(myRed);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH); // Place the title at the top

        // Your content here
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout()); // Use BorderLayout to display tasks as a list
        contentPanel.setOpaque(false); // Make the content panel transparent

        // Create a panel for the left column
        JPanel leftColumnPanel = new JPanel(new BorderLayout());
        leftColumnPanel.setOpaque(false);

        // Create a button to switch display type
        JButton toggleDisplayButton = createStyledButton("Change visualization", myRed);
        toggleDisplayButton.addActionListener(e -> {
            displayInmediate = !displayInmediate; // Toggle the display mode
            refreshDisplay(); // Update the displayed tasks
        });

        // Add the "Toggle Display" button to the left column panel
        leftColumnPanel.add(toggleDisplayButton, BorderLayout.NORTH); // Add the button to the left column

        // Add the leftColumnPanel to the main panel
        add(leftColumnPanel, BorderLayout.WEST);

        // Create a panel for displaying tasks (by priority or arrival order)
        displayPanel = new JPanel(cardLayout);
        displayPanel.setOpaque(false);

        // Create a panel for displaying tasks by priority
        JPanel inmediatePanel = new JPanel(new BorderLayout());
        JLabel inmediateLabel = new JLabel("Ventas de contado");
        inmediateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inmediateLabel.setForeground(myRed);
        inmediatePanel.add(inmediateLabel, BorderLayout.NORTH);
        entriesListModel = new DefaultListModel<>();
        entriesIDListModel = new DefaultListModel<>(); // Initialize task name list model
        updateTaskListModels(); // Update the list models with task data
        JList<String> inmediateSalesList = new JList<>(entriesIDListModel);
        inmediateSalesList.setFont(new Font("Arial", Font.PLAIN, 16));
        displayPanel.add(inmediatePanel, "Ventas de contado");

        // Create a panel for displaying tasks by arrival order
        JPanel creditPanel = new JPanel(new BorderLayout());
        JLabel creditLabel = new JLabel("Ventas a credito");
        creditLabel.setFont(new Font("Arial", Font.BOLD, 16));
        creditLabel.setForeground(myRed);
        creditPanel.add(creditLabel, BorderLayout.NORTH);
        JList<String> creditSales = new JList<>(entriesIDListModel); // Use the same task name list model
        creditSales.setFont(new Font("Arial", Font.PLAIN, 16));
        displayPanel.add(creditPanel, "Ventas a credito");

        // Add the displayPanel to the left column
        leftColumnPanel.add(displayPanel, BorderLayout.CENTER);

        // Create a panel for the right column (buttons)
        JPanel rightColumnPanel = new JPanel(new GridLayout(0, 2));
        rightColumnPanel.setOpaque(false);

        JList<String> salesList = new JList<>(entriesIDListModel);
        salesList.setFont(new Font("Arial", Font.PLAIN, 16));
        salesList.setForeground(TASKCOLOR); // Set text color
        JScrollPane scrollPane = new JScrollPane(salesList);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add a selection listener to handle item click events
        salesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Handle the task selection, e.g., show further info
                int selectedIndex = salesList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedTaskName = entriesIDListModel.getElementAt(selectedIndex);
                    showTaskInfo(selectedTaskName);
                }
            }
        });

        add(contentPanel, BorderLayout.CENTER); // Place the content in the center

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2)); // 2 columns for modify and delete buttons
        buttonPanel.setOpaque(false);

        // Modify button
        JButton modifyButton = createStyledButton("Modificar ingreso", myRed);
        modifyButton.addActionListener(e -> {
            int selectedIndex = salesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedEntryID = entriesIDListModel.getElementAt(selectedIndex);
                // different options wether the entry is a credit sale or a inmediate sale
                if (displayInmediate) {
                    CreditSales selectedEntry = controller.getCreditSale(selectedEntryID);
                    String[] options = { "Modificar Detalle", "Modificar Documento", "Modificar Valor", "Modificar Nombre del cliente", "Modificar ID", "Modificar Prioridad"};
                    int choice = JOptionPane.showOptionDialog(EntriesPanel.this, "Que desea modificar?", "Modificar Ingreso", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    if (choice == 0) {
                        // Modify Detail
                        String modifiedDetail = JOptionPane.showInputDialog("Modificar Detalle", selectedEntry.getDetail());
                        if (modifiedDetail != null && !modifiedDetail.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyInmediateSale(selectedEntryID, 1, modifiedDetail);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedDetail, selectedIndex);
                        }
                    } else if (choice == 1) {
                        // Modify Document
                        String modifiedDocument = JOptionPane.showInputDialog("Modificar Documento", selectedEntry.getDocument());
                        if (modifiedDocument != null && !modifiedDocument.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyInmediateSale(selectedEntryID, 2, modifiedDocument);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedDocument, selectedIndex);
                        }
                    } else if (choice == 2) {
                        // Modify Value
                        String modifiedValue = JOptionPane.showInputDialog("Modificar Valor", selectedEntry.getValue());
                        if (modifiedValue != null && !modifiedValue.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyInmediateSale(selectedEntryID, 3, modifiedValue);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedValue, selectedIndex);
                        }
                    } else if (choice == 3) {
                        // Modify Client Name
                        String modifiedClientName = JOptionPane.showInputDialog("Modificar Nombre del cliente", selectedEntry.getClientName());
                        if (modifiedClientName != null && !modifiedClientName.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyInmediateSale(selectedEntryID, 4, modifiedClientName);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedClientName, selectedIndex);
                        }

                    } else if (choice ==4){
                        // Modify ID
                        String modifiedID = JOptionPane.showInputDialog("Modificar ID", selectedEntry.getID());
                        if (modifiedID != null && !modifiedID.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyInmediateSale(selectedEntryID, 5, modifiedID);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedID, selectedIndex);
                        }
                    } else if (choice == 5){
                        // Modify Priority
                        String modifiedPriority = JOptionPane.showInputDialog("Modificar Prioridad", selectedEntry.getPriorityLevel());
                        if (modifiedPriority != null && !modifiedPriority.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyInmediateSale(selectedEntryID, 6, modifiedPriority);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedPriority, selectedIndex);
                        }
                    }

                } else {
                    InmediateSales selectedEntry = controller.getInmediateSale(selectedEntryID);
                    String[] options = {"Modificar Detalle", "Modificar Documento", "Modificar Valor", "Modificar Nombre del cliente", "Modificar ID", "Modificar Medio de pago"};
                    int choice = JOptionPane.showOptionDialog(EntriesPanel.this, "Que desea modificar?", "Modificar Ingreso", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    if (choice == 0) {
                        // Modify Detail
                        String modifiedDetail = JOptionPane.showInputDialog("Modificar Detalle", selectedEntry.getDetail());
                        if (modifiedDetail != null && !modifiedDetail.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyCreditSale(selectedEntryID, 1, modifiedDetail);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedDetail, selectedIndex);
                        }
                    } else if (choice == 1) {
                        // Modify Document
                        String modifiedDocument = JOptionPane.showInputDialog("Modificar Documento", selectedEntry.getDocument());
                        if (modifiedDocument != null && !modifiedDocument.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyCreditSale(selectedEntryID, 2, modifiedDocument);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedDocument, selectedIndex);
                        }
                    } else if (choice == 2) {
                        // Modify Value
                        String modifiedValue = JOptionPane.showInputDialog("Modificar Valor", selectedEntry.getValue());
                        if (modifiedValue != null && !modifiedValue.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyCreditSale(selectedEntryID, 3, modifiedValue);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedValue, selectedIndex);
                        }
                    } else if (choice == 3) {
                        // Modify Client Name
                        String modifiedClientName = JOptionPane.showInputDialog("Modificar Nombre del cliente", selectedEntry.getClientName());
                        if (modifiedClientName != null && !modifiedClientName.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyCreditSale(selectedEntryID, 4, modifiedClientName);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedClientName, selectedIndex);
                        }
                    } else if (choice == 4){
                        // Modify ID
                        String modifiedID = JOptionPane.showInputDialog("Modificar ID", selectedEntry.getID());
                        if (modifiedID != null && !modifiedID.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyCreditSale(selectedEntryID, 5, modifiedID);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedID, selectedIndex);
                        }
                    } else if (choice == 5){
                        // Modify Cash
                        String modifiedCash = JOptionPane.showInputDialog("Modificar Medio de pago", selectedEntry.isCash());
                        if (modifiedCash != null && !modifiedCash.isEmpty()) {
                            // Update the task name in the controller
                            controller.modifyCreditSale(selectedEntryID, 6, modifiedCash);
                            // Update the task name in the taskNameListModel
                            entriesIDListModel.setElementAt(modifiedCash, selectedIndex);
                        }
                    }
                }

                JOptionPane.showMessageDialog(EntriesPanel.this, "Ingreso modificado exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(EntriesPanel.this, "Porfavor selecciona un ingreso primero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete button
        JButton deleteButton = createStyledButton("Eliminar ingreso", myRed);
        deleteButton.addActionListener(e -> {
            int selectedIndex = salesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedSaleID = entriesIDListModel.getElementAt(selectedIndex);
                // Remove the task from the list models
                entriesIDListModel.removeElementAt(selectedIndex);
                entriesListModel.removeElementAt(selectedIndex);
                // Remove the task from the controller
                controller.removeSale(selectedSaleID);
                JOptionPane.showMessageDialog(EntriesPanel.this, "Ingreso eliminado exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(EntriesPanel.this, "Porfavor selecciona un ingreso primero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        // Create a panel for the "Add Task" button
        JPanel addSalePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addSalePanel.setOpaque(false);

        // Add Task button
        JButton addButton = createStyledButton("Añadir ingreso", myRed);
        addButton.addActionListener(e -> {
            // Create a panel for the input fields
            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.setOpaque(false);

            // Create input fields
            JTextField detailField = new JTextField();
            JTextField documentField = new JTextField();
            JTextField valueField = new JTextField();
            JTextField clientNameField = new JTextField();

            // Add the input fields to the input panel
            inputPanel.add(new JLabel("Detalle:"));
            inputPanel.add(detailField);
            inputPanel.add(new JLabel("Documento:"));
            inputPanel.add(documentField);
            inputPanel.add(new JLabel("Valor:"));
            inputPanel.add(valueField);
            inputPanel.add(new JLabel("Nombre del cliente:"));
            inputPanel.add(clientNameField);

            // Create a panel for the radio buttons
            JPanel radioPanel = new JPanel(new GridLayout(0, 1));
            radioPanel.setOpaque(false);

            // Create radio buttons
            JRadioButton inmediateButton = new JRadioButton("Contado");
            JRadioButton creditButton = new JRadioButton("Credito");

            // Add the radio buttons to the radio panel
            radioPanel.add(inmediateButton);
            radioPanel.add(creditButton);

            // Create a button group for the radio buttons
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(inmediateButton);
            buttonGroup.add(creditButton);

            // Create a panel for the radio buttons and input fields
            JPanel inputContainer = new JPanel(new BorderLayout());
            inputContainer.setOpaque(false);

            // Add the input panel to the input container
            inputContainer.add(inputPanel, BorderLayout.CENTER);

            // Add the radio panel to the input container
            inputContainer.add(radioPanel, BorderLayout.SOUTH);


            // get selected option

            int result = JOptionPane.showConfirmDialog(EntriesPanel.this, inputContainer, "Añadir ingreso", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Get the input values
                String detail = detailField.getText();
                String document = documentField.getText();
                String value = valueField.getText();
                String clientName = clientNameField.getText();
                String ID = controller.generateID();
                boolean inmediate = inmediateButton.isSelected();

                if (inmediate) {
                    // Create a new inmediate sale
                    boolean cash = JOptionPane.showConfirmDialog(EntriesPanel.this, "Es en efectivo?", "Añadir ingreso", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION;
                    controller.addInmediateSale(ID, document, detail, Double.parseDouble(value), cash, clientName);
                    InmediateSales newInmediateSale = controller.getInmediateSale(ID);
                    entriesIDListModel.addElement(newInmediateSale.getID());
                    entriesListModel.addElement(newInmediateSale.getDetail());
                } else {
                    controller.addCreditSale(ID, document, detail, Double.parseDouble(value), clientName);
                    // Create a new credit sale
                    CreditSales newCreditSale = new CreditSales(detail, document, Double.parseDouble(value), clientName, controller.generateID());
                    // Add the new task to the task name list model
                    entriesIDListModel.addElement(newCreditSale.getDetail());
                    // Add the new task to the task list model
                    entriesListModel.addElement(newCreditSale.getDetail() + " Priority: " + newCreditSale.getPriorityLevel());
                }

                JOptionPane.showMessageDialog(EntriesPanel.this, "Ingreso añadido exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        addSalePanel.add(addButton);

        // Create a panel for the "Return to Menu" button
        JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        returnButtonPanel.setOpaque(false);

        // Return to Menu button
        JButton returnButton = getReturnButton();

        returnButtonPanel.add(returnButton);

        // Create a container panel for all buttons
        JPanel buttonsContainer = new JPanel(new BorderLayout());
        buttonsContainer.setOpaque(false);

        // Add the button panels to the wrapper panel
        buttonsContainer.add(buttonPanel, BorderLayout.NORTH);
        buttonsContainer.add(addSalePanel, BorderLayout.CENTER);
        buttonsContainer.add(returnButtonPanel, BorderLayout.SOUTH);

        // Add the buttons container to the south
        add(buttonsContainer, BorderLayout.SOUTH);

        // Button to undo the last action
        JButton undoButton = createStyledButton("Undo", myRed);
        undoButton.addActionListener(e -> {

            boolean undo = controller.undo();
            if(undo){
                refreshDisplay();
                JOptionPane.showMessageDialog(EntriesPanel.this, "Undo successful.", "Success", JOptionPane.INFORMATION_MESSAGE);

            }else{
                JOptionPane.showMessageDialog(EntriesPanel.this, "No undo possible", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add the undo button to the left column panel
        leftColumnPanel.add(undoButton, BorderLayout.SOUTH);
    }

    /**
     * Creates and returns a styled JSlider for selecting task priorities.
     *
     * @return A JSlider with specific styling.
     */
    private JSlider getjSlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 3, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setOpaque(false);
        slider.setForeground(myRed);
        slider.setBackground(Color.WHITE);
        slider.setPreferredSize(new Dimension(300, 100));
        slider.setFont(new Font("Arial", Font.BOLD, 16));
        slider.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return slider;
    }

    /**
     * Updates the task list models based on the current display mode.
     */
    private void updateTaskListModels() {
        entriesIDListModel.clear();
        entriesListModel.clear();

        // Get the tasks from the controller based on the current display mode
        ArrayList<ArrayList<String>> sales;
        if (displayInmediate) {
            sales = controller.getSalesAttributes(true);

        } else {
            sales = controller.getSalesAttributes(false);
        }

        for (ArrayList<String> sale : sales) {
            String taskName = sale.get(0);
            entriesIDListModel.addElement(taskName); // Only add task name to the list

            String taskDetails = sale.get(1) + " Priority: " + sale.get(2);
            entriesListModel.addElement(taskDetails);
        }

    }


    /**
     * Fetches tasks based on the specified display mode (priority or arrival order).
     *
     * @param byInmediate if tasks should be fetched by priority, false for arrival order.
     * @return A list of tasks with their attributes.
     */
    private ArrayList<ArrayList<String>> fetchSales(boolean byInmediate) {
        if (byInmediate) {
            return controller.getSalesAttributes(true);
        } else {
            return controller.getSalesAttributes(false);
        }
    }

    /**
     * Refreshes the display panel to update the displayed tasks.
     */
    private void refreshDisplay() {
        cardLayout.show(displayPanel, displayInmediate ? "Ventas de contado" : "Ventas a credito");
        updateTaskListModels();
    }

    /**
     * Shows detailed information about a selected task.
     *
     * @param selectedTaskName The name of the selected task.
     */
    private void showTaskInfo(String selectedTaskName) {
        ArrayList<ArrayList<String>> tasks = fetchSales(displayInmediate);
        for (ArrayList<String> task : tasks) {
            if (task.get(0).equals(selectedTaskName)) {
                String taskDetails = "Task Name: " + task.get(0) + "\n"
                        + "Task Description: " + task.get(1) + "\n"
                        + "Task Priority: " + task.get(2);
                JOptionPane.showMessageDialog(this, taskDetails, "Task Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }
}
