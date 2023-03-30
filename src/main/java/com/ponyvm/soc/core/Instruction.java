package com.ponyvm.soc.core;

public abstract class Instruction {
    public int p1, p2, p3;

    public Instruction(int p1, int p2, int p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public abstract void execute();
}
