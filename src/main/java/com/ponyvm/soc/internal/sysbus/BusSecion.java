package com.ponyvm.soc.internal.sysbus;

public class BusSecion {
    //0:指令,1:数据,2:指令&数据,3外设
    private int type;
    private int addr;
    private Addressable module;

    public BusSecion(int type, int addr, Addressable module) {
        this.type = type;
        this.addr = addr;
        this.module = module;
    }

    public int getType() {
        return type;
    }

    public int getAddr() {
        return addr;
    }

    public Addressable getModule() {
        return module;
    }
}
