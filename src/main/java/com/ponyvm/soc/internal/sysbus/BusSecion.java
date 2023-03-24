package com.ponyvm.soc.internal.sysbus;

public class BusSecion implements Addressable {
    //0:指令,1:数据,2:指令&数据,3外设
    private int type;
    private int firstAddr;
    private int lastAddr;
    private Addressable module;

    public BusSecion(int type, int addr, Addressable module) {
        this.type = type;
        this.firstAddr = addr;
        this.lastAddr = addr + module.getCapacity() - 1;
        this.module = module;
    }

    public int getType() {
        return type;
    }

    public int getOffset() {
        return firstAddr;
    }

    public boolean contain(int addr) {
        return (addr >= firstAddr && addr <= lastAddr);
    }

    @Override
    public int getCapacity() {
        return module.getCapacity();
    }

    @Override
    public byte getByte(int addr) {
        return module.getByte(addr - firstAddr);
    }

    @Override
    public int getHalfWord(int addr) {
        return module.getHalfWord(addr - firstAddr);
    }

    @Override
    public int getWord(int addr) {
        return module.getWord(addr - firstAddr);
    }

    @Override
    public void storeByte(int addr, int data) {
        module.storeByte(addr - firstAddr, data);
    }

    @Override
    public void storeHalfWord(int addr, short data) {
        module.storeHalfWord(addr - firstAddr, data);
    }

    @Override
    public void storeWord(int addr, int data) {
        module.storeWord(addr - firstAddr, data);
    }
}
