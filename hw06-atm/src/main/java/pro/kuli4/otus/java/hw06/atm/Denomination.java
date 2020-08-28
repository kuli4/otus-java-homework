package pro.kuli4.otus.java.hw06.atm;

public enum Denomination {
    D10(10),
    D50(50),
    D100(100);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
