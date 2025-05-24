package com.example.dead.model;

public enum Attributes {
    UNIVERSITY(1), FIELD(1), WORKPLACE(1), SPECIALTIES(1), CONNECTION(1);

    private int value;

    Attributes(int value) {
        this.value = value;
    }

    public void setValue(int newValue) {
        this.value = newValue;
    }

    public int getValue() {
        return value;
    }

    public static void updateAllValues(int newUni, int newField, int newWorkPlace, int newSpecialties, int newConnection) {
        UNIVERSITY.setValue(newUni);
        FIELD.setValue(newField);
        WORKPLACE.setValue(newWorkPlace);
        SPECIALTIES.setValue(newSpecialties);
        CONNECTION.setValue(newConnection);
    }
}
