/* File: Main.java
 * Authors: Marc Sun Bøg & Simon Amtoft Pedersen
 *
 * The following file is the main file for the RISCV-Simulator of the RV32I instructions.
 * The file starts the GUI application, and loads Layout.fxml.
 */
package com.ponyvm;

import com.ponyvm.vm.CPU;
import com.ponyvm.vm.Memory;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    private static final int BYTES_PR_PAGE = 256;    // 64 words
    private static final int MEMORY_SIZE = 10485760;    // 10MiB memory

    public static void main(String[] args) throws IOException {
        Memory mem = new Memory(MEMORY_SIZE);
        byte[] rom = getInstructions(new File(ClassLoader.getSystemResource("loop.bin").getFile()));
        CPU cpu = new CPU(mem);
        cpu.loadBinaryProgram(rom);
        boolean end = false;
        while (!end) {
            end = cpu.executeInstruction();
        }
    }

    public static byte[] getInstructions(File f) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(f));
        int len = (int) f.length();                     // Number of instructions
        byte[] rom = new byte[len];   // Instruction array
        for (int i = 0; i < len; i++) {
            rom[i] = dis.readByte();
        }
        dis.close();
        return rom;
    }
}