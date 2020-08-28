package pro.kuli4.otus.java.hw06.atm.impl;

import pro.kuli4.otus.java.hw06.atm.Banknote;
import pro.kuli4.otus.java.hw06.atm.Basket;
import pro.kuli4.otus.java.hw06.atm.Denomination;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SimpleBasket implements Basket {
    private final Denomination denomination;
    private final Set<Banknote> banknotes;

    public SimpleBasket(Denomination denomination) {
        this.denomination = denomination;
        this.banknotes = new HashSet<>();
    }

    @Override
    public void putBanknote(Banknote banknote) {
        banknotes.add(banknote);
    }

    @Override
    public Collection<Banknote> getBanknotes(int count) {
        Set<Banknote> resultSet = new HashSet<>();
        if (count == banknotes.size()) {
            resultSet = Set.copyOf(banknotes);
        } else {
            for (Banknote banknote : banknotes) {
                resultSet.add(banknote);
                if (resultSet.size() == count) break;
            }
        }
        banknotes.removeAll(resultSet);
        return resultSet;
    }

    @Override
    public long getAmount() {
        return banknotes.size() * denomination.getValue();
    }

    @Override
    public int getCount() {
        return banknotes.size();
    }
}
