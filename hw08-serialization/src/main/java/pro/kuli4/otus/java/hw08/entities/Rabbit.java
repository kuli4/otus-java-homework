package pro.kuli4.otus.java.hw08.entities;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
public class Rabbit {
    public static int gg;
    private int i;
    protected int j;
    int y;
    public int t;
    public short sho;

    public long v;

    public double d;
    public char c;

    public String s;

    public String[] ss;
    public List<String> sss;

    public List<PartOfRabbit> scl;

    public PartOfRabbit sc;

    public int[] ai;
}
