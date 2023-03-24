package com.ponyvm.soc.internal.sysbus;

public class ZeroAddr implements Addressable {
    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public byte getByte(int addr) {
        return 0;
    }

    @Override
    public int getHalfWord(int addr) {
        return 0;
    }

    @Override
    public int getWord(int addr) {
        return 0;
    }

    @Override
    public void storeByte(int addr, int data) {

    }

    @Override
    public void storeHalfWord(int addr, short data) {

    }

    @Override
    public void storeWord(int addr, int data) {

    }
}
