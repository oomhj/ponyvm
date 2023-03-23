package com.ponyvm.soc.board;

import com.ponyvm.soc.internal.ram.Memory;
import com.ponyvm.soc.internal.sysbus.BusSecion;
import com.ponyvm.soc.internal.sysbus.SysBus;
import com.ponyvm.soc.peripheral.flashtool.ELFFile;
import com.ponyvm.soc.peripheral.flashtool.ELFLoader;
import com.ponyvm.soc.riscvcore.CPU;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Esp32c3 {

    private String NAME;

    private String VERSION;

    private SysBus SYS_BUS;

    private Memory SRAM;//400KB

    private int SRAM_SIZE = 400 * 1024;//400KB

    private Memory RTC_RAM;

    private int RTC_RAM_SIZE = 8 * 1024;//8KB

    private CPU CPU;

    public Esp32c3() {
        this.NAME = "Esp32c3";
        this.VERSION = "01";
        initMachine();
    }

    private void initMachine() {
        this.SRAM = new Memory(SRAM_SIZE);
        this.RTC_RAM = new Memory(RTC_RAM_SIZE);
        this.SYS_BUS = new SysBus();
        //数据地址
        SYS_BUS.attachSection(new BusSecion(1, 0x3FC8_0000, this.SRAM));
        //指令地址
        SYS_BUS.attachSection(new BusSecion(0, 0x4037_C000, this.SRAM));

        SYS_BUS.attachSection(new BusSecion(2, 0x5000_0000, this.RTC_RAM));
        this.CPU = new CPU(SYS_BUS, 0x3FCD_FFFC);
    }

    public int launchROM(File rom) throws IOException {
        ELFFile elfFile = loadELFFile(new File(ClassLoader.getSystemResource("loop.bin").getFile()));
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
