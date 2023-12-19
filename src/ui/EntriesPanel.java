package ui;

import model.ArrayList;
import model.Controller;
import model.CreditSales;

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
                String selectedTaskName = entriesIDListModel.getElementAt(selectedIndex);
                String[] options = {"Modify Name", "Modify Priority", "Modify Description"};
                int choice = JOptionPane.showOptionDialog(EntriesPanel.this, "What would you like to modify?", "Modify Task", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    // Modify Name
                    String modifiedName = JOptionPane.showInputDialog("Modify Task Name", selectedTaskName);
                    if (modifiedName != null && !modifiedName.isEmpty()) {
                        // Update the task name in the controller
                        controller.modifyTask(selectedTaskName, 1, modifiedName);
                        // Update the task name in the taskNameListModel
                        entriesIDListModel.setElementAt(modifiedName, selectedIndex);
                    }
                } else if (choice == 1) {
                    // Modify Priority
                    JSlider slider = getjSlider();
                    JOptionPane.showMessageDialog(EntriesPanel.this, slider, "Select new Task Priority", JOptionPane.QUESTION_MESSAGE);
                    String modifiedPriority = String.valueOf(slider.getValue());
                    // Update the task priority in the controller
                    controller.modifyTask(selectedTaskName, 3, modifiedPriority);
                    // No need to update the task name in the taskNameListModel for priority modification
                } else if (choice == 2) {
                    // Modify Description
                    String modifiedDescription = JOptionPane.showInputDialog("Modify Task Description", selectedTaskName);
                    if (modifiedDescription != null) {
                        // Update the task description in the controller
                        controller.modifyTask(selectedTaskName, 2, modifiedDescription);
                        // No need to update the task name in the taskNameListModel for description modification
                    }
                }
                JOptionPane.showMessageDialog(EntriesPanel.this, "Task modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the display panel to update the displayed tasks
                refreshDisplay();
            } else {
                JOptionPane.showMessageDialog(EntriesPanel.this, "Please select a task first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete button
        JButton deleteButton = createStyledButton("Eliminar ingreso", myRed);
        deleteButton.addActionListener(e -> {
            int selectedIndex = salesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedTaskName = entriesIDListModel.getElementAt(selectedIndex);
                // Remove the task from the list models
                entriesIDListModel.removeElementAt(selectedIndex);
                entriesListModel.removeElementAt(selectedIndex);
                // Remove the task from the controller
                controller.removeTask(selectedTaskName);
                JOptionPane.showMessageDialog(EntriesPanel.this, "Ingreso eliminado exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(EntriesPanel.this, "Porfavor selecciona un ingreso primero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        // Create a panel for the "Add Task" button
        JPanel addTaskPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addTaskPanel.setOpaque(false);

        // Add Task button
        JButton addButton = createStyledButton("Añadir ingreso", myRed);
        addButton.addActionListener(e -> {
            String taskName = JOptionPane.showInputDialog("Ingrese el detalle del ingreso");
            if (taskName != null && !taskName.isEmpty()) {
                JSlider slider = getjSlider();
                JOptionPane.showMessageDialog(EntriesPanel.this, slider, "Select Task Priority", JOptionPane.QUESTION_MESSAGE);
                int taskPriority = slider.getValue();
                String taskDescription = JOptionPane.showInputDialog("Enter Task Description");
                if (taskDescription != null) {
                    boolean success = controller.addTask(taskName, taskDescription, taskPriority);
                    if (success) {
                        refreshDisplay();
                        JOptionPane.showMessageDialog(EntriesPanel.this, "Ingreso añadido exitosamente.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(EntriesPanel.this, "Task with the same name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        addTaskPanel.add(addButton);

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
        buttonsContainer.add(addTaskPanel, BorderLayout.CENTER);
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
            sales = controller.getInmediateSalesAttributes();

        } else {
            sales = controller.getCreditSalesAttributes();
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
            return controller.getInmediateSalesAttributes();
        } else {
            return controller.getCreditSalesAttributes();
        }
    }

    /**
     * Refreshes the display panel to update the displayed tasks.
     */
    private void refreshDisplay() {
        cardLayout.show(displayPanel, displayInmediate ? "Ventas de contado" : "Ventas a crédito");
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
