package model;

public class OperationExpenses  extends Expenses{

        public OperationExpenses(String document, String detail, double value) {
            super(document, detail, value);
        }

        @Override
        public String toString() {
            return "OperationExpenses{" +
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

        public ArrayList<String> getAttributes() {
            ArrayList<String> attributes = new ArrayList<>();
            attributes.add(getDetail());
            attributes.add(getDocument());
            attributes.add(String.valueOf(getValue()));
            return attributes;
        }
}
