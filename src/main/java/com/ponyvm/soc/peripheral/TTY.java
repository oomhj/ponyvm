package com.ponyvm.soc.peripheral;

import com.ponyvm.soc.internal.sysbus.Addressable;

public class TTY implements Addressable {

    public void print(int c) {
        System.out.println(c);
    }

    @Override
    public int getCapacity() {
        return 4;
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
        print(data);
    }

    @Override
    public void storeHalfWord(int addr, short data) {
        print(data);
    }

    @Override
    public void storeWord(int addr, int data) {
        print(data);
    }
}
