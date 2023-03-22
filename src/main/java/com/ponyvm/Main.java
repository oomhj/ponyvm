/* File: Main.java
 * Authors: Marc Sun Bøg & Simon Amtoft Pedersen
 *
 * The following file is the main file for the RISCV-Simulator of the RV32I instructions.
 * The file starts the GUI application, and loads Layout.fxml.
 */
package com.ponyvm;

import com.ponyvm.peripheral.ELFFile;
import com.ponyvm.vm.CPU;
import com.ponyvm.vm.ELFLoader;
import com.ponyvm.vm.Memory;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    private static final int BYTES_PR_PAGE = 256;    // 64 words
//    private static final int MEMORY_SIZE = 65536;    // 64KB memory
    private static final int MEMORY_SIZE = 262144;    // 64KB memory

    public static void main(String[] args) throws IOException {
        Memory mem = new Memory(MEMORY_SIZE);
//        byte[] rom = getInstructions(new File(ClassLoader.getSystemResource("loop.bin").getFile()));
//        byte[] rom = getESPRom(new File(ClassLoader.getSystemResource("loop1.bin").getFile()));
        ELFFile elfFile = loadELFFile(new File(ClassLoader.getSystemResource("loop1").getFile()));
        String elfinfo = elfFile.toString();
        System.out.println(elfinfo);


        ELFLoader.loadElf(elfFile, mem);


        CPU cpu = new CPU(mem, elfFile.HEADER.e_entry());
        boolean end = false;
        while (!end) {
            end = cpu.executeInstruction();
        }
    }

    public static byte[] getESPRom(File f) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(f));
        int index = 0;
        int binlength = 0;
        byte[] rom = null;
        int data;
        while ((data = dis.read()) != -1) {
            if (index == 28) {
                binlength = data;
            } else if (index > 28 && index < 32) {
                binlength = ((data & 0xFF) << 8) + binlength;
            } else if (index == 32) {
                rom = new byte[binlength];
                rom[0] = (byte) data;
            } else if (index > 32) {
                if (index - 32 < binlength) {
                    rom[index - 32] = (byte) data;
                } else {
                    break;
                }
            }
            ++index;
        }
        dis.close();
        return rom;
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

    public static ELFFile loadELFFile(File f) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(f));
        int len = (int) f.length();   // ELFFile Size
        byte[] rom = new byte[len];
        for (int i = 0; i < len; i++) {
            rom[i] = dis.readByte();
        }
        dis.close();
        return new ELFFile(rom);
    }

}