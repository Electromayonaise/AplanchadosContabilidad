package model;

import java.util.List;
public class OtherEntries extends Entry implements HeapKeyProvider, Cloneable{

    public String document;
    public String detail;
    public Double value;
    public String ID;

    public OtherEntries( String document, String detail, double value, String ID) {
        super(detail, document, value, "N/A", ID);
        this.document = document;
        this.detail = detail;
        this.value = value;
        this.ID = ID;
    }

    public String getDocument() {
        return document;
    }

    public String getDetail() {
        return detail;
    }

    public Double getValue() {
        return value;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "OtherEntries{" +
                "document='" + document + '\'' +
                ", detail='" + detail + '\'' +
                ", value='" + value + '\'' +
                ", ID='" + ID + '\'' +
                '}';
    }

    public String hash() {
        long key = ID.hashCode();
        return Long.toString(key);
    }

    public int getHeapKey() {
        return ID.hashCode();
    }

    public void setHeapKey(int newKey) {
        ID = String.valueOf(newKey);
    }

    public ArrayList<String> getAttributes() {
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add(getDetail());
        attributes.add(getDocument());
        attributes.add(String.valueOf(getValue()));
        attributes.add(getID());
        return attributes;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
