package pro.kuli4.otus.java.hw06.atm.impl;

import pro.kuli4.otus.java.hw06.atm.Banknote;
import pro.kuli4.otus.java.hw06.atm.Denomination;

public class SimpleBanknote implements Banknote {
    private final Denomination denomination;
    private final String id;

    public SimpleBanknote(Denomination denomination, String id) {
        this.denomination = denomination;
        this.id = id;
    }

    @Override
    public Denomination getDenomination() {
        return this.denomination;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", denomination = " + denomination.getValue() + "}";
    }
}
