package model;

public abstract class Entry {

    private String ID;
    private String detail;
    private String document;
    private Double value;
    private String clientName;

    public Entry(String detail, String document, Double value, String clientName, String ID) {
        this.detail = detail;
        this.document = document;
        this.value = value;
        this.clientName = clientName;
        this.ID = ID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public abstract ArrayList<String> getAttributes();

}
