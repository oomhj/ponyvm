package com.ponyvm.soc.board;

import com.ponyvm.soc.internal.ram.Memory;
import com.ponyvm.soc.internal.sysbus.BusSecion;
import com.ponyvm.soc.internal.sysbus.SysBus;
import com.ponyvm.soc.peripheral.TTY;
import com.ponyvm.soc.core.CPU;

public class PonySoc extends RV32I {

    private int SRAM_SIZE = 0x30000;//192MB

    public PonySoc() {
        this.NAME = "PonySoc";
        this.VERSION = "01";
        initMachine();
    }

    private void initMachine() {
        this.SYS_BUS = new SysBus();
        //指令&数据地址
        SYS_BUS.attachSection(new BusSecion(2, 0x01_0000, new Memory(SRAM_SIZE)));
        SYS_BUS.attachSection(new BusSecion(3, 0x00_00FC, new TTY()));
        this.CORE = new CPU(SYS_BUS, 0x03_FFFC);
    }

}
