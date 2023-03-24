package com.ponyvm.soc.internal.ram;

import com.ponyvm.soc.internal.sysbus.Addressable;

public class Memory implements Addressable {
    private byte[] memory;

    /**
     * Constructor for Memory
     * Initializes as a byte array of size given by argument
     */
    public Memory(int capacity) {
        memory = new byte[capacity];
    }

    @Override
    // Stores a single byte in the memory array
    public void storeByte(int addr, int data) {
        memory[addr] = (byte) (data & 0xFF);
    }

    @Override
    // Stores a half word in the memory array
    public void storeHalfWord(int addr, short data) {
        memory[addr] = (byte) ((data & 0x00FF));
        memory[addr + 1] = (byte) ((data & 0xFF00) >>> 8);
    }

    @Override
    // Stores a word in the memory array
    public void storeWord(int addr, int data) {
        memory[addr] = (byte) ((data & 0x000000FF));
        memory[addr + 1] = (byte) ((data & 0x0000FF00) >>> 8);
        memory[addr + 2] = (byte) ((data & 0x00FF0000) >>> 16);
        memory[addr + 3] = (byte) ((data & 0xFF000000) >>> 24);
    }

    @Override
    public int getCapacity() {
        return memory.length;
    }

    @Override
    // Returns the byte in the memory given by the address.
    public byte getByte(int addr) {
        return memory[addr];
    }

    @Override
    // Returns half word from memory given by address
    public int getHalfWord(int addr) {
        return (getByte(addr + 1) << 8) | (getByte(addr) & 0xFF);
    }

    @Override
    // Returns word from memory given by address
    public int getWord(int addr) {
        return (getHalfWord(addr + 2) << 16) | (getHalfWord(addr) & 0xFFFF);
    }

    // Returns string starting at the address given and ends when next memory address is zero.
    String getString(int addr) {
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