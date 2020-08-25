package pro.kuli4.otus.java.hw06.atm.impl;

import pro.kuli4.otus.java.hw06.atm.*;

import java.util.Collection;

public class SimpleATM implements ATM {
    private final BanknoteManager banknoteManager;
    private final String id;

    public static SimpleATM builder(String id) {
        return new SimpleATM(new SimpleBanknoteManager(), id);
    }

    private SimpleATM(BanknoteManager banknoteManager, String id) {
        this.banknoteManager = banknoteManager;
        this.id = id;
    }

    @Override
    public OperationResult<Boolean> putBanknotes(Collection<Banknote> banknotes) {
        if (banknoteManager.putBanknotes(banknotes)) {
            return new OperationResult<>(OperationStatus.SUCCESS, "", true);
        }
        return new OperationResult<>(OperationStatus.ERROR, "I can't put more banknotes!", false);
    }

    @Override
    public OperationResult<Collection<Banknote>> getBanknotes(long amount) {
        if (amount <= 0) {
            return new OperationResult<>(OperationStatus.ERROR, "Bad amount!", null);
        }
        Collection<Banknote> result = banknoteManager.getBanknotes(amount);
        if (result.isEmpty()) {
            return new OperationResult<>(OperationStatus.ERROR, "I can't issue the specified amount = " + amount, null);
        }
        return new OperationResult<>(OperationStatus.SUCCESS, "", result);
    }

    @Override
    public OperationResult<Long> getBalance() {
        return new OperationResult<>(OperationStatus.SUCCESS, "", banknoteManager.getBalance());
    }

    @Override
    public String toString() {
        return "ATM id: " + this.id + ", balance: " + banknoteManager.getBalance();
    }

}
