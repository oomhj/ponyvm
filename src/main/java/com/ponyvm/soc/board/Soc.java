package com.ponyvm.soc.board;

import com.ponyvm.soc.core.CPU;
import com.ponyvm.soc.internal.sysbus.SysBus;

import java.io.File;
import java.io.IOException;

public interface Soc {
    String getNAME();

    String getVERSION();

    SysBus getSYS_BUS();

    CPU getCORE();

    int launchROM(File rom) throws IOException;
}
