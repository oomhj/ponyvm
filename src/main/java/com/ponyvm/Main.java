/* File: Main.java
 * Authors: Marc Sun Bøg & Simon Amtoft Pedersen
 *
 * The following file is the main file for the RISCV-Simulator of the RV32I instructions.
 * The file starts the GUI application, and loads Layout.fxml.
 */
package com.ponyvm;

import com.ponyvm.soc.board.Esp32c3;
import com.ponyvm.soc.peripheral.flashtool.ELFFile;
import com.ponyvm.soc.riscvcore.CPU;
import com.ponyvm.soc.peripheral.flashtool.ELFLoader;
import com.ponyvm.soc.internal.ram.Memory;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Esp32c3 vm = new Esp32c3();
        vm.launchROM(new File(ClassLoader.getSystemResource("loop.bin").getFile()));

    }
}