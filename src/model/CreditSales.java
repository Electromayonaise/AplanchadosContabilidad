package model;

public class CreditSales extends Entry {

    private int priorityLevel;

    public CreditSales(String detail, String document, Double value, String clientName, String ID) {
        super(detail, document, value, clientName, ID);
        this.priorityLevel = 1;
    }

    @Override
    public String toString() {
        return "CreditSales{" +
                "ID='" + getID() + '\'' +
                "detail='" + getDetail() + '\'' +
                ", document='" + getDocument() + '\'' +
                ", value=" + getValue() +
                ", clientName='" + getClientName() + '\'' +
                '}';
    }

    @Override
    public ArrayList<String> getAttributes() {
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add(getDetail());
        attributes.add(getDocument());
        attributes.add(String.valueOf(getValue()));
        attributes.add(getClientName());
        attributes.add(getID());
        return attributes;
    }

    public String hash() {
        long key = getID().hashCode();
        return Long.toString(key);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getHeapKey() {
        return getID().hashCode();
    }

    public void setHeapKey(int newKey) {
        setID(String.valueOf(newKey));
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }


}
