/* File: Main.java
 * Authors: Marc Sun Bøg & Simon Amtoft Pedersen
 *
 * The following file is the main file for the RISCV-Simulator of the RV32I instructions.
 * The file starts the GUI application, and loads Layout.fxml.
 */
package com.ponyvm;

import com.ponyvm.soc.board.Esp32c3;
import com.ponyvm.soc.board.PonySoc;

import java.io.File;
import java.io.IOException;

import com.ponyvm.soc.board.Soc;
import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) throws IOException {
// commons-cli命令行参数，需要带参数值
        Options options = new Options();
        options.addOption("soc", true, "soc");
        options.addOption("f", true, "romfile");


        // 解析命令行参数
        CommandLineParser parser = new DefaultParser();
        CommandLine cl = null;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        if (cl == null) {
            System.out.println("参数解析失败");
            return;
        }

        String rom = cl.getOptionValue("f");
        if (rom == null) {
            System.out.println("未指定文件");
            return;
        }
        Soc vm;
        String soc = cl.getOptionValue("soc");
        switch ((soc != null) ? soc : "PonySoc") {
            case "Esp32c3":
                vm = new Esp32c3();
                break;
            default:
                vm = new PonySoc();
                break;
        }
        File romFile = new File(rom).getAbsoluteFile();
        vm.launchROM(romFile);
    }
}