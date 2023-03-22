
package com.ponyvm.vm;

import com.ponyvm.peripheral.TTY;

public class /**/Memory {
    //section：TTY 打印机，地址 255
    private int PrintAddr = 0x000000FF;

    private int SRAM_OFFSET = 0x01_0000;
    private byte[] memory;

    /**
     * Constructor for Memory
     * Initializes as a byte array of size given by argument
     */
    public Memory(int MEMORY_SIZE_IN_BYTES) {
        memory = new byte[MEMORY_SIZE_IN_BYTES];
    }

    // Stores a single byte in the memory array
    void storeByte(int addr, int data) {
        addr = addr - SRAM_OFFSET;
        memory[addr] = (byte) (data & 0xFF);
    }

    // Stores a half word in the memory array
    void storeHalfWord(int addr, short data) {
        addr = addr - SRAM_OFFSET;
        memory[addr] = (byte) ((data & 0x00FF));
        memory[addr + 1] = (byte) ((data & 0xFF00) >>> 8);
    }

    // Stores a word in the memory array
    void storeWord(int addr, int data) {
        if (addr == PrintAddr) {
            TTY.print(data);
            return;
        }
        addr = addr - SRAM_OFFSET;
        memory[addr] = (byte) ((data & 0x000000FF));
        memory[addr + 1] = (byte) ((data & 0x0000FF00) >>> 8);
        memory[addr + 2] = (byte) ((data & 0x00FF0000) >>> 16);
        memory[addr + 3] = (byte) ((data & 0xFF000000) >>> 24);
    }

    // Returns the byte in the memory given by the address.
    byte getByte(int addr) {
        addr = addr - SRAM_OFFSET;
        return memory[addr];
    }

    // Returns half word from memory given by address
    int getHalfWord(int addr) {
        return (getByte(addr + 1) << 8) | (getByte(addr) & 0xFF);
    }

    // Returns word from memory given by address
    int getWord(int addr) {
        return (getHalfWord(addr + 2) << 16) | (getHalfWord(addr) & 0xFFFF);
    }

    // Returns string starting at the address given and ends when next memory address is zero.
    String getString(int addr) {
        addr = addr - SRAM_OFFSET;
        String returnValue = "";
        int i = 0;
        while (memory[addr + i] != 0) {
            returnValue += (char) (memory[addr + i]);
            i++;
        }
        return returnValue;
    }

    byte[] getMemory() {
        return memory;
    }

    void setMemory(byte[] mem) {
        this.memory = mem;
    }
}