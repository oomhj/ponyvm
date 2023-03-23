package com.ponyvm.soc.board;

import com.ponyvm.soc.internal.ram.Memory;
import com.ponyvm.soc.internal.sysbus.BusSecion;
import com.ponyvm.soc.internal.sysbus.SysBus;
import com.ponyvm.soc.peripheral.TTY;
import com.ponyvm.soc.peripheral.flashtool.ELFFile;
import com.ponyvm.soc.peripheral.flashtool.ELFLoader;
import com.ponyvm.soc.riscvcore.CPU;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PonySoc {
    private String NAME;

    private String VERSION;

    private SysBus SYS_BUS;

    private Memory SRAM;

    private int SRAM_SIZE = 196608;//192MB

    private com.ponyvm.soc.riscvcore.CPU CPU;

    public PonySoc() {
        this.NAME = "PonySoc";
        this.VERSION = "01";
        initMachine();
    }

    private void initMachine() {
        this.SRAM = new Memory(SRAM_SIZE);
        this.SYS_BUS = new SysBus();
        //指令&数据地址
        SYS_BUS.attachSection(new BusSecion(2, 0x01_0000, this.SRAM));
        SYS_BUS.attachSection(new BusSecion(3, 0x00_0000, new TTY()));
        this.CPU = new CPU(SYS_BUS, 0x03_FFFC);
    }

    public int launchROM(File rom) throws IOException {
        ELFFile elfFile = loadELFFile(rom);
//        String elfinfo = elfFile.toString();
//        System.out.println(elfinfo);

        ELFLoader.loadElf(elfFile, this.SYS_BUS);
        return this.CPU.launch(elfFile.HEADER.e_entry());
    }

    private ELFFile loadELFFile(File f) throws IOException {
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
