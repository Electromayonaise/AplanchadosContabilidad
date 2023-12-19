package model;

public class CreditSales extends Entry {

    public CreditSales(String detail, String document, Double value, String clientName, String ID) {
        super(detail, document, value, clientName, ID);
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
}
