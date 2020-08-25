package pro.kuli4.otus.java.hw06.atm;

import java.util.Collection;

public interface ATM {
    public OperationResult<Boolean> putBanknotes(Collection<Banknote> banknotes);
    public OperationResult<Collection<Banknote>> getBanknotes(long amount);
    public OperationResult<Long> getBalance();

}
