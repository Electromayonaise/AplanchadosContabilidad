package model;

public abstract class Expenses implements HeapKeyProvider, Cloneable {

    public String document;
    public String detail;
    public Double value;

    public Expenses(String document, String detail, double value) {
        this.document = document;
        this.detail = detail;
        this.value = value;
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

    public void setDocument(String document) {
        this.document = document;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setValue(Double value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "OtherEntries{" +
                "document='" + document + '\'' +
                ", detail='" + detail + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String hash() {
        long key = document.hashCode();
        return Long.toString(key);
    }

    public int getHeapKey() {
        return document.hashCode();
    }

    public void setHeapKey(int newKey) {
        document = String.valueOf(newKey);
    }

    public abstract ArrayList<String> getAttributes();
}
