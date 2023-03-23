package com.ponyvm.soc.internal.sysbus;

public interface Addressable {

    int getCapacity();

    byte getByte(int addr);

    int getHalfWord(int addr);

    int getWord(int addr);

    void storeByte(int addr, int data);

    void storeHalfWord(int addr, short data);

    void storeWord(int addr, int data);
}
