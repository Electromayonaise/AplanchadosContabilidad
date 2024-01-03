package model;


import java.io.IOException;
import java.util.Random;

/**
 * The Controller class manages tasks in the Task Manager application.
 * It handles task modification, removal, addition, and provides access to task attributes.
 */
public class Controller {
    // Singleton
    private transient static  Controller instance;
    private HashTable<String,Entry> table;
    private DoublyLinkedList<Entry> queue;
    private MaxHeap<Entry>priorityQueue;
    private HashTable<String,Expenses> expensesTable;
    private DoublyLinkedList<Expenses> expensesQueue;
    private MaxHeap<Expenses>expensesPriorityQueue;

    private transient static final FileManager fileManager=FileManager.getInstance();

    private DoublyLinkedList<DoublyLinkedList<Entry>> doublyStack;

    private DoublyLinkedList<MaxHeap<Entry>> maxHeapStack;

    private DoublyLinkedList<DoublyLinkedList<Expenses>> expensesDoublyStack;

    private DoublyLinkedList<MaxHeap<Expenses>> expensesMaxHeapStack;

    /**
     * Private constructor to enforce singleton pattern and initialize data structures.
     */
    protected Controller(){
        table=new HashTable<>();
        queue=new DoublyLinkedList<>();
        priorityQueue=new MaxHeap<>();
        expensesTable=new HashTable<>();
        expensesQueue=new DoublyLinkedList<>();
        expensesPriorityQueue=new MaxHeap<>();
        doublyStack = new DoublyLinkedList<>();
        maxHeapStack = new DoublyLinkedList<>();

    }

    /**
     * Returns the singleton instance of the Controller class.
     *
     * @return The Controller instance.
     */
    public static Controller getInstance(){
        try{
            instance=fileManager.loadToJson();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(instance==null){
            instance=new Controller();
        }
        return instance;
    }

    /**
     * Undoes the last action by restoring the previous state of the controller from the stacks.
     *
     * @return True if the undo operation is successful, false if there's nothing to undo.
     */
    public boolean undo(){
        boolean stackThings = false;
        if(doublyStack.size() != 0){
            stackThings = true;
            loadFromStacks();
            saveData();
        }
        return stackThings;
    }

    /**
     * Saves the current state of the controller to the undo stacks.
     */
    public void saveToStacks(){

        // Cloning the Queue
        DoublyLinkedList<Entry> QueueToClone = getQueue();
        DoublyLinkedList<Entry> queue = new DoublyLinkedList<>();
        for(Entry entry : QueueToClone){
            if (entry instanceof InmediateSales) {
                InmediateSales newSale = new InmediateSales(entry.getDetail(), entry.getDocument(), entry.getValue(), entry.getClientName(), entry.getID(), ((InmediateSales) entry).isCash());
                queue.enqueue(newSale);
            }
        }

        // Cloning the Heap
        ArrayList<Entry> maxHeapToClone = getPriorityQueue().getElements();
        MaxHeap<Entry> heap =  new MaxHeap<>();
        for(Entry entry : maxHeapToClone){
            if (entry instanceof CreditSales) {
                CreditSales newSale = new CreditSales(entry.getDetail(), entry.getDocument(), entry.getValue(), entry.getClientName(), entry.getID());
                heap.insert(newSale);
            }
        }

        doublyStack.push(queue);

        maxHeapStack.push(heap);

        // Cloning the Expenses Queue
        DoublyLinkedList<Expenses> expensesQueueClone = new DoublyLinkedList<>();
        for (Expenses expense : expensesQueue) {
            if (expense instanceof SupplyExpenses) {
                SupplyExpenses newExpense = new SupplyExpenses(expense.getDocument(), expense.getDetail(), expense.getValue());
                expensesQueueClone.enqueue(newExpense);
            }
        }

        // Cloning the Expenses Heap
        ArrayList<Expenses> expensesHeapToClone = expensesPriorityQueue.getElements();
        MaxHeap<Expenses> expensesHeapClone = new MaxHeap<>();
        for (Expenses expense : expensesHeapToClone) {
            if (expense instanceof OperationExpenses) {
                OperationExpenses newExpense = new OperationExpenses(expense.getDocument(), expense.getDetail(), expense.getValue());
                expensesHeapClone.insert(newExpense);
            }
        }

        expensesDoublyStack.push(expensesQueueClone);
        expensesMaxHeapStack.push(expensesHeapClone);
    }

    /**
     * Loads the previous state of the controller from the undo stacks.
     */
    public void loadFromStacks(){
        DoublyLinkedList<Entry> loadedQueue = doublyStack.pop();
        MaxHeap<Entry> loadedHeap =  maxHeapStack.pop();
        DoublyLinkedList<Expenses> loadedExpensesQueue = expensesDoublyStack.pop();
        MaxHeap<Expenses> loadedExpensesHeap = expensesMaxHeapStack.pop();
        setQueue(loadedQueue);
        setPriorityQueue(loadedHeap);
        setExpensesQueue(loadedExpensesQueue);
        setExpensesPriorityQueue(loadedExpensesHeap);

        // Add the elements of the DoublyLinkedList to the HasTable
        HashTable<String,Entry> loadedTable = new HashTable<>();

        for (Entry entry : loadedQueue) {
            loadedTable.add(entry.getID(), entry);
        }

        // Add the elements of the MaxHeap to the HasTable
        for (Entry entry : loadedHeap.getElements()) {
            loadedTable.add(entry.getID(), entry);
        }

        // Add the HashTable to the new Controller
        setTable(loadedTable);

        // Add the elements of the Expenses DoublyLinkedList to the Expenses HasTable
        HashTable<String,Expenses> loadedExpensesTable = new HashTable<>();

        for (Expenses expense : loadedExpensesQueue) {
            loadedExpensesTable.add(expense.getDocument(), expense);
        }

        // Add the elements of the Expenses MaxHeap to the Expenses HasTable
        for (Expenses expense : loadedExpensesHeap.getElements()) {
            loadedExpensesTable.add(expense.getDocument(), expense);
        }

        setExpensesTable(loadedExpensesTable);


    }

    public boolean modifyCreditSale(String ID, int option, String newValue){
        boolean flag=false;
        Entry entry= table.get(ID);
        if(entry instanceof CreditSales){
            saveToStacks();
            switch (option){
                case 1:
                    ((CreditSales) entry).setDetail(newValue);
                    flag=true;
                    break;
                case 2:
                    ((CreditSales) entry).setDocument(newValue);
                    flag=true;
                    break;
                case 3:
                    ((CreditSales) entry).setValue(Double.parseDouble(newValue));
                    flag=true;
                    break;
                case 4:
                    ((CreditSales) entry).setClientName(newValue);
                    flag=true;
                    break;
                case 5:
                    flag=modifySaleID(ID,newValue);
                    break;
                case 6:
                    ((CreditSales) entry).setPriorityLevel(Integer.parseInt(newValue));
                    flag=true;
                    break;
            }
        }
        saveData();
        return flag;
    }

    public boolean modifyInmediateSale(String ID, int option, String newValue){
        boolean flag=false;
        Entry entry= table.get(ID);
        if(entry instanceof InmediateSales){
            saveToStacks();
            switch (option){
                case 1:
                    ((InmediateSales) entry).setDetail(newValue);
                    flag=true;
                    break;
                case 2:
                    ((InmediateSales) entry).setDocument(newValue);
                    flag=true;
                    break;
                case 3:
                    ((InmediateSales) entry).setValue(Double.parseDouble(newValue));
                    flag=true;
                    break;
                case 4:
                    ((InmediateSales) entry).setClientName(newValue);
                    flag=true;
                    break;
                case 5:
                    flag=modifySaleID(ID,newValue);
                    break;
                case 6:
                    ((InmediateSales) entry).setCash(Boolean.parseBoolean(newValue));
                    flag=true;
                    break;
            }
        }
        saveData();
        return flag;
    }



    private boolean modifySalePriority(String ID, int newPriority){
        boolean flag=false;
        Entry entry= table.get(ID);
        if(entry instanceof CreditSales){
            saveToStacks();
            ((CreditSales) entry).setPriorityLevel(newPriority);
            flag=true;
        }
        return flag;
    }

    private boolean modifySaleID(String ID,String newID){
        boolean flag=false;
        Entry entry= table.get(ID);
        if(!table.containsKey(newID)){
            entry.setID(newID);
            table.remove(ID);
            table.add(newID,entry);
            flag=true;
        }
        return flag;
    }

    public boolean removeSale(String ID){

        boolean flag=false;
        if(table.containsKey(ID)){
            saveToStacks();
            Entry entryToRemove=table.get(ID);
            if(entryToRemove instanceof InmediateSales){
                flag=removeFromQueue(ID);
            }else if(entryToRemove instanceof CreditSales){
                flag=removeFromHeap(ID);
            }
            table.remove(ID);
        }

        saveData();

        return flag;

    }

    /**
     * Retrieves attributes of non-prioritized tasks.
     *
     * @return List of attributes for non-prioritized tasks.
     */
    public ArrayList<ArrayList<String>> getSalesAttributes(boolean inmediate){

        ArrayList<ArrayList<String>> list=new ArrayList<>();
        for(Entry entry: (DoublyLinkedList<Entry>) queue){
            if(inmediate && entry instanceof InmediateSales){
                list.add(entry.getAttributes());
            }else if(!inmediate && entry instanceof CreditSales){
                list.add(entry.getAttributes());
            }
        }
        return list;
    }

    /**
     * Saves data to JSON files using the FileManager.
     */
    public void saveData(){
        try {
            fileManager.saveMaxHeapToJSON(priorityQueue);
        } catch (IOException e) {
            System.out.println("Error al guardar");
            throw new RuntimeException(e);
        }

        try {
            fileManager.saveDoublyLinkedListToJSON(queue);
        } catch (IOException e) {
            System.out.println("Error al guardar");
            throw new RuntimeException(e);
        }
    }

    public boolean addInmediateSale(String ID,String document, String detail, double value, boolean cash, String clientName ){

        boolean flag=false;
        if(!table.containsKey(ID)){
            saveToStacks();
            InmediateSales newSale=new InmediateSales(detail, document, value, clientName, ID, cash);
            table.add(newSale.getID(),newSale);
            queue.enqueue(newSale);
            flag=true;
        }
        saveData();
        return flag;

    }

    public boolean addCreditSale(String ID,String document, String detail, double value, String clientName){

        boolean flag=false;
        if(!table.containsKey(ID)){
            saveToStacks();
            CreditSales newSale=new CreditSales(detail, document, value, clientName, ID);
            table.add(newSale.getID(),newSale);
            queue.enqueue(newSale);
            flag=true;
        }
        saveData();
        return flag;

    }

    // Add these methods to your Controller class

    public void addOtherEntry(String ID, String document, String detail, double value) {
        boolean flag = false;
        if (!table.containsKey(ID)) {
            saveToStacks();
            OtherEntries newEntry = new OtherEntries(document, detail, value, ID);
            table.add(newEntry.getID(), newEntry);
            queue.enqueue(newEntry);
            flag = true;
        }
        saveData();
    }

    public boolean modifyOtherEntry(String ID, int option, String newValue) {
        boolean flag = false;
        Entry entry = table.get(ID);
        if (entry instanceof OtherEntries) {
            saveToStacks();
            switch (option) {
                case 1:
                    ((OtherEntries) entry).setDetail(newValue);
                    flag = true;
                    break;
                case 2:
                    ((OtherEntries) entry).setDocument(newValue);
                    flag = true;
                    break;
                case 3:
                    ((OtherEntries) entry).setValue(Double.parseDouble(newValue));
                    flag = true;
                    break;
            }
        }
        saveData();
        return flag;
    }

    public boolean removeOtherEntry(String ID) {
        boolean flag = false;
        if (table.containsKey(ID)) {
            saveToStacks();
            Entry entryToRemove = table.get(ID);
            if (entryToRemove instanceof OtherEntries) {
                flag = removeFromQueue(ID);
            }
            table.remove(ID);
        }
        saveData();
        return flag;
    }

    public ArrayList<OtherEntries> getOtherEntries() {
        ArrayList<OtherEntries> otherEntriesList = new ArrayList<>();
        for (Entry entry : queue) {
            if (entry instanceof OtherEntries) {
                otherEntriesList.add((OtherEntries) entry);
            }
        }
        return otherEntriesList;
    }

    public OtherEntries getOtherEntry(String ID) {
        return (OtherEntries) table.get(ID);
    }

    public void addExpense(String document, String detail, double value, boolean operationExpense) {
        boolean flag = false;
        String ID = document;
        if (!expensesTable.containsKey(ID)) {
            saveToStacks();
            if (operationExpense) {
                OperationExpenses newExpense = new OperationExpenses(document, detail, value);
                expensesTable.add(newExpense.getDocument(), newExpense);
                expensesQueue.enqueue(newExpense);
                expensesPriorityQueue.insert(newExpense);
            } else {
                SupplyExpenses newExpense = new SupplyExpenses(document, detail, value);
                expensesTable.add(newExpense.getDocument(), newExpense);
                expensesQueue.enqueue(newExpense);
                expensesPriorityQueue.insert(newExpense);
            }
            flag = true;
        }
        saveData();
    }

    public boolean modifyExpense(String ID, int option, String newValue) {
        boolean flag = false;
        Expenses expense = expensesTable.get(ID);
        if (expense != null) {
            saveToStacks();
            switch (option) {
                case 1:
                    expense.setDetail(newValue);
                    flag = true;
                    break;
                case 2:
                    expense.setDocument(newValue);
                    flag = true;
                    break;
                case 3:
                    expense.setValue(Double.parseDouble(newValue));
                    flag = true;
                    break;
            }
        }
        saveData();
        return flag;
    }

    public boolean removeExpense(String ID) {
        boolean flag = false;
        if (expensesTable.containsKey(ID)) {
            saveToStacks();
            Expenses expenseToRemove = expensesTable.get(ID);
            if (expenseToRemove != null) {
                flag = removeFromQueue(ID);
            }
            expensesTable.remove(ID);
        }
        saveData();
        return flag;
    }

    public ArrayList<Expenses> getExpenses() {
        ArrayList<Expenses> expensesList = new ArrayList<>();
        for (Expenses expense : expensesQueue) {
            if (expense instanceof Expenses) {
                expensesList.add((Expenses) expense);
            }
        }
        return expensesList;
    }

    public Expenses getExpense(String ID) {
        return expensesTable.get(ID);
    }

    public ArrayList<ArrayList<String>> getExpensesAttributes(boolean operationExpense) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for (Expenses expense : expensesQueue) {
            if (operationExpense && expense instanceof OperationExpenses) {
                list.add(expense.getAttributes());
            } else if (!operationExpense && expense instanceof SupplyExpenses) {
                list.add(expense.getAttributes());
            }
        }
        return list;
    }

    public void addOtherExpense(String document, String detail, double value) {
        boolean flag = false;
        String ID = document;
        if (!expensesTable.containsKey(ID)) {
            saveToStacks();
            OtherExpenses newExpense = new OtherExpenses(document, detail, value);
            expensesTable.add(newExpense.getDocument(), newExpense);
            expensesQueue.enqueue(newExpense);
            expensesPriorityQueue.insert(newExpense);
            flag = true;
        }
        saveData();
    }

    public boolean modifyOtherExpense(String ID, int option, String newValue) {
        boolean flag = false;
        Expenses expense = expensesTable.get(ID);
        if (expense != null) {
            saveToStacks();
            switch (option) {
                case 1:
                    expense.setDetail(newValue);
                    flag = true;
                    break;
                case 2:
                    expense.setDocument(newValue);
                    flag = true;
                    break;
                case 3:
                    expense.setValue(Double.parseDouble(newValue));
                    flag = true;
                    break;
            }
        }
        saveData();
        return flag;
    }

    public boolean removeOtherExpense(String ID) {
        boolean flag = false;
        if (expensesTable.containsKey(ID)) {
            saveToStacks();
            Expenses expenseToRemove = expensesTable.get(ID);
            if (expenseToRemove != null) {
                flag = removeFromQueue(ID);
            }
            expensesTable.remove(ID);
        }
        saveData();
        return flag;
    }

    public ArrayList<OtherExpenses> getOtherExpenses() {
        ArrayList<OtherExpenses> expensesList = new ArrayList<>();
        for (Expenses expense : expensesQueue) {
            if (expense instanceof OtherExpenses) {
                expensesList.add((OtherExpenses) expense);
            }
        }
        return expensesList;
    }

    public OtherExpenses getOtherExpense(String ID) {
        return (OtherExpenses) expensesTable.get(ID);
    }

    public ArrayList<ArrayList<String>> getOtherExpensesAttributes() {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for (Expenses expense : expensesQueue) {
            if (expense instanceof OtherExpenses) {
                list.add(expense.getAttributes());
            }
        }
        return list;
    }





    /**
     * Copies a list of tasks.
     *
     * @param list The list of tasks to be copied.
     * @return A new list containing copied tasks.
     */
    private <T> ArrayList<T> copySales(ArrayList<T> list){
        ArrayList<T> copy=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            copy.add(list.get(i));
        }
        return copy;
    }


    private boolean removeFromHeap(String ID){
        ArrayList<Entry> heapTasks=((MaxHeap<Entry>) priorityQueue).getElements();
        boolean flag=false;
        for (int i = 0; i < heapTasks.size()&&!flag; i++) {
            String saleID=heapTasks.get(i).getID();
            if(saleID.equals(ID)){
                flag=((MaxHeap<Entry>)priorityQueue).remove(i);
            }
        }
        return flag;
    }

    private boolean removeFromQueue(String ID){
        BiPredicate<Entry,String> equals= (t,u)->t.getID().equals(u);
        return ((DoublyLinkedList<Entry>) queue).removeFirstInstance(ID,equals);

    }

    // Getters and Setters
    public void setTable(HashTable<String, Entry> table) {
        this.table = table;
    }


    public void setQueue(DoublyLinkedList<Entry> queue) {
        this.queue = queue;
    }


    public void setPriorityQueue(MaxHeap<Entry> priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    public void setExpensesTable(HashTable<String, Expenses> expensesTable) {
        this.expensesTable = expensesTable;
    }

    public void setExpensesQueue(DoublyLinkedList<Expenses> expensesQueue) {
        this.expensesQueue = expensesQueue;
    }

    public void setExpensesPriorityQueue(MaxHeap<Expenses> expensesPriorityQueue) {
        this.expensesPriorityQueue = expensesPriorityQueue;
    }

    public DoublyLinkedList<Entry> getQueue() {
        return queue;
    }

    public MaxHeap<Entry> getPriorityQueue() {
        return priorityQueue;
    }

    public HashTable<String, Entry> getTable() {
        return table;
    }

    public DoublyLinkedList<DoublyLinkedList<Entry>> getDoublyStack() {
        return doublyStack;
    }

    public void setDoublyStack(DoublyLinkedList<DoublyLinkedList<Entry>> doublyStack) {
        this.doublyStack = doublyStack;
    }

    public DoublyLinkedList<MaxHeap<Entry>> getMaxHeapStack() {
        return maxHeapStack;
    }

    public void setMaxHeapStack(DoublyLinkedList<MaxHeap<Entry>> maxHeapStack) {
        this.maxHeapStack = maxHeapStack;
    }

    public void setInstance(Controller instance) {
        this.instance = instance;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public CreditSales getCreditSale(String ID){
        return (CreditSales) table.get(ID);
    }

    public InmediateSales getInmediateSale(String ID){
        return (InmediateSales) table.get(ID);
    }

    public SupplyExpenses getSupplyExpense(String ID) {
        return (SupplyExpenses) expensesTable.get(ID);
    }

    public OperationExpenses getOperationExpense(String ID) {
        return (OperationExpenses) expensesTable.get(ID);
    }


    public String generateID(){
        // Generate a random number
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);
        String ID = String.valueOf(randomNumber);
        // Check if the ID is already in use
        while(table.containsKey(ID)){
            randomNumber = random.nextInt(1000000);
            ID = String.valueOf(randomNumber);
        }
        return ID;
    }
}
