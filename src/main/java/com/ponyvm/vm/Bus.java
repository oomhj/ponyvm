package com.ponyvm.vm;

import com.ponyvm.peripheral.TTY;

import java.util.ArrayList;

public class Bus {
    //32位总线，最大寻址范围4G
    //内存映射表
    //section：IRAM,地址：0x00010000



    private byte[] RAM = new byte[73728];
    //section：TTY 打印机，地址 255
    private byte PrintAddr = (byte) 0xFF;

    //外设
    private TTY tty;

    public Bus() {
        this.tty = new TTY();
    }

    public byte load(byte addr) {
        if (addr == PrintAddr) {
            System.out.println();
        }
        int index = Byte.toUnsignedInt(addr);
        return this.RAM[index];
    }

    public void store(byte addr, byte data) {
//        if (addr == PrintAddr) {
//            tty.print(data);
//            return;
//        }
        int index = Byte.toUnsignedInt(addr);
        this.RAM[index] = data;
    }
}
