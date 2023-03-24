package com.ponyvm.soc.board;

import com.ponyvm.soc.internal.ram.Memory;
import com.ponyvm.soc.internal.sysbus.BusSecion;
import com.ponyvm.soc.internal.sysbus.SysBus;
import com.ponyvm.soc.core.CPU;

public class Esp32c3 extends RV32I {


    private Memory SRAM;//400KB

    private int SRAM_SIZE = 400 * 1024;//400KB

    private Memory RTC_RAM;

    private int RTC_RAM_SIZE = 8 * 1024;//8KB

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
        this.CORE = new CPU(SYS_BUS, 0x3FCD_FFFC);
    }
}
