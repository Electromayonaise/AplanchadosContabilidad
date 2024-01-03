package model;

import model.ArrayList;
import java.util.List;

public class OtherExpenses extends Expenses {

    public String document;
    public String detail;
    public Double value;

    public OtherExpenses( String document, String detail, double value) {
        super(document, detail, value);
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


    @Override
    public String toString() {
        return "OtherEntries{" +
                "document='" + document + '\'' +
                ", detail='" + detail + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ArrayList<String> getAttributes() {
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add(getDetail());
        attributes.add(getDocument());
        attributes.add(getValue().toString());
        return attributes;
    }
}
