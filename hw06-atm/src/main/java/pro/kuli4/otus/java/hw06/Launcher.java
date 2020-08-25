package pro.kuli4.otus.java.hw06;

import pro.kuli4.otus.java.hw06.atm.ATM;
import pro.kuli4.otus.java.hw06.atm.Banknote;
import pro.kuli4.otus.java.hw06.atm.Denomination;
import pro.kuli4.otus.java.hw06.atm.impl.SimpleATM;
import pro.kuli4.otus.java.hw06.atm.impl.SimpleBanknote;

import java.util.HashSet;
import java.util.Set;

/*
 * ./gradlew :hw06-atm:clean build
 * java -jar hw06-atm/build/libs/hw06-atm-0.0.1.jar
 */

public class Launcher {
    public static void main(String[] args) {
        ATM atm = SimpleATM.builder("First");
        System.out.println(atm);
        Set<Banknote> banknotes = new HashSet<>();
        banknotes.add(new SimpleBanknote(Denomination.D10, "RR001"));
        banknotes.add(new SimpleBanknote(Denomination.D50, "RR002"));
        banknotes.add(new SimpleBanknote(Denomination.D50, "RR003"));
        banknotes.add(new SimpleBanknote(Denomination.D100, "RR004"));
        atm.putBanknotes(banknotes);
        System.out.println(atm);
        System.out.println(atm.getBanknotes(100));
        System.out.println(atm);
        System.out.println(atm.getBanknotes(100));
        System.out.println(atm);
        System.out.println(atm.getBanknotes(100));
        System.out.println(atm.getBalance());
    }
}
