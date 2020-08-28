package pro.kuli4.otus.java.hw06.atm.impl;

import pro.kuli4.otus.java.hw06.atm.Banknote;
import pro.kuli4.otus.java.hw06.atm.BanknoteManager;
import pro.kuli4.otus.java.hw06.atm.Basket;
import pro.kuli4.otus.java.hw06.atm.Denomination;

import java.util.*;

public class SimpleBanknoteManager implements BanknoteManager {
    private final Map<Denomination, Basket> baskets;

    SimpleBanknoteManager() {
        this.baskets = new HashMap<>();
        for (Denomination denomination : Denomination.values()) {
            baskets.put(denomination, new SimpleBasket(denomination));
        }
    }

    @Override
    public boolean putBanknotes(Collection<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            baskets.get(banknote.getDenomination()).putBanknote(banknote);
        }
        return true;
    }

    @Override
    public Collection<Banknote> getBanknotes(long amount) {
        class Eval {
            int bestCount = Integer.MAX_VALUE;
            void eval(
                    Map<Denomination, Integer> present,
                    Map<Denomination, Integer> current,
                    Map<Denomination, Integer> best,
                    long currentVal,
                    long totalAmount
            ) {
                if (currentVal == totalAmount) {
                    int currentBanknoteCount = 0;
                    for (Denomination den : current.keySet()) {
                        currentBanknoteCount += current.get(den);
                    }
                    if (currentBanknoteCount < bestCount) {
                        best.clear();
                        best.putAll(current);
                        bestCount = currentBanknoteCount;
                    }
                } else if (currentVal < totalAmount) {
                    for (Denomination den : present.keySet()) {
                        if (present.get(den) > current.get(den)) {
                            current.put(den, current.get(den) + 1);
                            eval(present, current, best, currentVal + den.getValue(), totalAmount);
                            current.put(den, current.get(den) - 1);
                        }
                    }
                }
            }
        }
        Map<Denomination, Integer> present = new HashMap<>();
        Map<Denomination, Integer> current = new HashMap<>();
        Map<Denomination, Integer> best = new HashMap<>();
        for (Denomination denomination : baskets.keySet()) {
            present.put(denomination, baskets.get(denomination).getCount());
            current.put(denomination, 0);
            best.put(denomination, 0);
        }
        new Eval().eval(present, current, best, 0, amount);
        Set<Banknote> resultSet = new HashSet<>();
        for (Denomination denomination : best.keySet()) {
            if (best.get(denomination) > 0) {
                resultSet.addAll(baskets.get(denomination).getBanknotes(best.get(denomination)));
            }
        }
        return resultSet;
    }

    @Override
    public long getBalance() {
        return baskets.values().stream().reduce(0L, (a, b) -> a + b.getAmount(), Long::sum);
    }
}
