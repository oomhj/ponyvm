package com.ponyvm.soc.internal.sysbus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SysBus implements Addressable, IBus {

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
        return route(addr).getByte(addr);
    }

    @Override
    public int getHalfWord(int addr) {
        return route(addr).getHalfWord(addr);
    }

    @Override
    public int getWord(int addr) {
        return route(addr).getWord(addr);
    }

    @Override
    public void storeByte(int addr, int data) {
        route(addr).storeByte(addr, data);
    }

    @Override
    public void storeHalfWord(int addr, short data) {
        route(addr).storeHalfWord(addr, data);
    }

    @Override
    public void storeWord(int addr, int data) {
        route(addr).storeWord(addr, data);
    }

    @Override
    public void attachSection(BusSecion section) {
        this.ADDR_MAP.put(section.getOffset(), section);
    }

    private BusSecion route(int addr) {
        Iterator<Map.Entry<Integer, BusSecion>> inter = ADDR_MAP.entrySet().iterator();
        while (inter.hasNext()) {
            Map.Entry<Integer, BusSecion> entry = inter.next();
            BusSecion secion = entry.getValue();
            if (secion.contain(addr)) {
                return secion;
            }
        }
        return null;
    }
}
