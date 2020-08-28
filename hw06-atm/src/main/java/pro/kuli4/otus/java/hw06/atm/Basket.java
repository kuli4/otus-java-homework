package pro.kuli4.otus.java.hw06.atm;

import java.util.Collection;

public interface Basket {
    void putBanknote(Banknote banknote);
    long getAmount();
    int getCount();
    Collection<Banknote> getBanknotes(int count);
}
