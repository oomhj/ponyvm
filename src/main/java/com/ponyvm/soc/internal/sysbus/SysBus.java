package com.ponyvm.soc.internal.sysbus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SysBus implements Addressable, IBus {
    //以0xFFFF(65536)个地址为一个Page
    private HashMap<Integer, BusSecion> ADDR_MAP;
    private BusSecion ZERO_SECTION = new BusSecion(4, 0x7FFFFFFF, new ZeroAddr());

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
        int offset = section.getOffset();
        int capacity = section.getCapacity();
        //低位应该对齐为0
        if ((offset & 0xFFFF) == 0) {
            int pageIndex = offset >> 16;
            int pageSize = (capacity / 0x01_0000) + (capacity % 0x01_0000 == 0 ? 0 : 1);

            for (int i = 0; i < pageSize; i++) {
                this.ADDR_MAP.put(pageIndex + i, section);
            }
        }
    }

    private BusSecion route(int addr) {
        BusSecion secion = ADDR_MAP.get(addr >> 16);
        return secion == null ? ZERO_SECTION : secion;
    }
}
