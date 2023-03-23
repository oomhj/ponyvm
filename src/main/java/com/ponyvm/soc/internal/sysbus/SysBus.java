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
        return ADDR_MAP.get(0x01_0000).getModule().getByte(addr - 0x01_0000);
    }

    @Override
    public int getHalfWord(int addr) {
        return ADDR_MAP.get(0x01_0000).getModule().getHalfWord(addr - 0x01_0000);
    }

    @Override
    public int getWord(int addr) {
        return ADDR_MAP.get(0x01_0000).getModule().getWord(addr - 0x01_0000);
    }

    @Override
    public void storeByte(int addr, int data) {
        ADDR_MAP.get(0x01_0000).getModule().storeByte(addr - 0x01_0000, data);
    }

    @Override
    public void storeHalfWord(int addr, short data) {
        ADDR_MAP.get(0x01_0000).getModule().storeHalfWord(addr - 0x01_0000, data);
    }

    @Override
    public void storeWord(int addr, int data) {
        ADDR_MAP.get(0x01_0000).getModule().storeWord(addr - 0x01_0000, data);
    }

    public void attachSection(BusSecion section) {
        this.ADDR_MAP.put(section.getAddr(), section);
    }
}
