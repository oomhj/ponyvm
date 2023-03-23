package com.ponyvm.soc.internal.sysbus;

import java.util.HashMap;

public class SysBus implements Addressable {

    private HashMap<Integer, BusSecion> ADDR_MAP;

    public SysBus() {
        this.ADDR_MAP = new HashMap<>();
    }

    @Override
    public int getCapacity() {
        return 0xFFFFFFFF;
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

    public void attachSection(BusSecion section) {
        this.ADDR_MAP.put(section.getAddr(), section);
    }
}
