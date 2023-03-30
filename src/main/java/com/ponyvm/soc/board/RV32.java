package com.ponyvm.soc.board;

import com.ponyvm.soc.internal.sysbus.SysBus;
import com.ponyvm.soc.peripheral.flashtool.ELFFile;
import com.ponyvm.soc.peripheral.flashtool.ELFLoader;
import com.ponyvm.soc.core.CPU;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RV32 implements Soc {
    public String NAME;

    public String VERSION;

    public SysBus SYS_BUS;

    public CPU CORE;

    @Override
    public String getNAME() {
        return this.NAME;
    }

    @Override
    public String getVERSION() {
        return this.VERSION;
    }

    @Override
    public SysBus getSYS_BUS() {
        return this.SYS_BUS;
    }

    @Override
    public CPU getCORE() {
        return this.CORE;
    }

    @Override
    public int launchROM(File rom) throws IOException {
        ELFFile elfFile = loadELFFile(rom);
//        String elfinfo = elfFile.toString();
//        System.out.println(elfinfo);
        System.out.println("加载程序：" + rom.getName());
        long time1 = System.currentTimeMillis();
        ELFLoader.loadElf(elfFile, this.SYS_BUS);
        long time2 = System.currentTimeMillis();
        System.out.println("程序加载完成，耗时(ms)：" + (time2 - time1));
        System.out.println("正在运行……");
        time1 = System.currentTimeMillis();
        int ret = this.CORE.launch(elfFile.HEADER.e_entry());
        time2 = System.currentTimeMillis();
        System.out.println("运行结束，耗时(ms)：" + (time2 - time1));
        float s = (time2 - time1) / 1000f;
        System.out.println("运行频率：" + this.CORE.frq / (1024 * 1024 * s) + "Mhz/s");
        return ret;
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
