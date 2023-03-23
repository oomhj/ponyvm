package com.ponyvm.soc.riscvcore;

import com.ponyvm.soc.peripheral.TTY;

public class Bus {
    //32位总线，最大寻址范围4G
    //内存映射表
    //section：SRAM(包括IRAM，DRAM),地址：0x00010000
    private int SRAM_OFFSET = 0x01_0000;
    private Memory SRAM = new Memory(65536);
    //section：TTY 打印机，地址 255
    private byte PrintAddr = (byte) 0xFF;

    //外设
    private TTY tty;

    public Bus() {
        this.tty = new TTY();
    }

    public byte load(int addr) {
        if (addr == PrintAddr) {
            System.out.println();
        }

        return this.SRAM.getByte(addr - SRAM_OFFSET);
    }

    public void store(int addr, byte data) {
        if (addr == PrintAddr) {
            tty.print(data);
            return;
        }
        this.SRAM.storeByte(addr - SRAM_OFFSET, data);
    }
}
