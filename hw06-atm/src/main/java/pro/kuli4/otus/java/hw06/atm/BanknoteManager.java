package pro.kuli4.otus.java.hw06.atm;

import java.util.Collection;

public interface BanknoteManager {
    long getBalance();
    boolean putBanknotes(Collection<Banknote> newBanknotes);
    Collection<Banknote> getBanknotes(long amount);
}
