package model;

import model.utility.Utility;

public class Literal {

    private String type;
    private String value;
    private String address;

    public Literal(String operand, String address){

        this.type = "" + operand.charAt(0);
        this.type = this.type.toUpperCase();
        this.value = operand.substring(2, operand.length()-2);      // W'1234'
        this.address = address;
    }

    public int calculateLength(){

        int length = 0;
        switch (type){
            case "W":
                length = 3;
                break;
            case "C":
                length = value.length() - 3;
                break;
            case "X":
                length = (int) Math.ceil( ((double)value.length()) / 2 );
                break;
        }
        return length;
    }

    public int getNumericValue() {
        int numericValue = 0;
        switch (type){
            case "W":
                if (value.charAt(0) == '-')
                    numericValue = -1 * Integer.parseInt(value.substring(1));
                else
                    numericValue = Integer.parseInt(value);
                break;
            case "C":
                break;
            case "X":
                break;
        }
        return numericValue;
    }

    @Override
    public String toString() {
        return value + Utility.getSpaces(12-value.length()) + address + "\n";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
