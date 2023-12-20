package model;

public class InmediateSales extends Entry{

    private boolean isCash;

    public InmediateSales(String detail, String document, Double value, String clientName, String ID, boolean isCash) {
        super(detail, document, value, clientName, ID);
        this.isCash = isCash;
    }

    public boolean isCash() {
        return isCash;
    }

    public void setCash(boolean cash) {
        isCash = cash;
    }

    @Override
    public String toString() {
        return "InmediateSales{" +
                "ID='" + getID() + '\'' +
                "detail='" + getDetail() + '\'' +
                ", document='" + getDocument() + '\'' +
                ", value=" + getValue() +
                ", clientName='" + getClientName() + '\'' +
                ", isCash=" + isCash +
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
        attributes.add(String.valueOf(isCash));
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
}
