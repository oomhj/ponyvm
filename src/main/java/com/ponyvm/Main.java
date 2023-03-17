/* File: Main.java
 * Authors: Marc Sun Bøg & Simon Amtoft Pedersen
 *
 * The following file is the main file for the RISCV-Simulator of the RV32I instructions.
 * The file starts the GUI application, and loads Layout.fxml.
 */
package com.ponyvm;

import com.ponyvm.vm.CPU;
import com.ponyvm.vm.Instruction;
import com.ponyvm.vm.Memory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import static java.lang.ClassLoader.getSystemResource;

public class Main {
    private static final int BYTES_PR_PAGE = 256;    // 64 words
    private static final int MEMORY_SIZE = 10485760;    // 10MiB memory

    public static void main(String[] args) throws IOException {
        // 显示应用 GUI
//        EventQueue.invokeLater(() -> createAndShowGUI());
        Memory mem = new Memory(MEMORY_SIZE);
        byte[] rom = getInstructions(new File(ClassLoader.getSystemResource("loop.bin").getFile()));
        CPU cpu = new CPU(mem);
        cpu.loadBinaryProgram(rom);
        while (true) {
            cpu.executeInstruction();
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
    /**
     * This method starts the GUI
     *
     * @Override method in Application
     */
//    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout.fxml"));
//        Parent root = loader.load();
//        guiController controller = loader.getController();
//        controller.setStage(primaryStage);
//        primaryStage.setTitle("RV32I Simulator");
//        primaryStage.setScene(new Scene(root, 800, 500));
//        primaryStage.show();
//    }

    /**
     * {
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static void createAndShowGUI() {
        // 确保一个漂亮的外观风格
//        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        JFrame frame = new JFrame("RV32I Simulator");
        frame.setSize(900, 900);
        frame.setLayout(new FlowLayout());
//        frame.setBounds(500, 500, 900, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加 "Hello World" 标签
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        // 显示窗口
//        frame.pack();
        frame.setResizable(true);

        frame.setLayout(new BorderLayout()); // 设置为没有间隙的 Border 布局
        JButton btn1 = new JButton("上");
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("regname", new Vector<String>());
        model.addColumn("hexvalue", new Vector<Integer>());
        model.addColumn("binvalue", new Vector<Integer>());
        for (int k = 0; k < 32; k++) {
            Object[] row = new Object[]{"r" + k, 1024, 1014};
            model.addRow(row);
        }
        JTable table = new JTable(model);
//        JButton btn2 = new JButton("左");
        JButton btn3 = new JButton("中");
        JButton btn4 = new JButton("右");
        JButton btn5 = new JButton("下");
        frame.add(btn1, BorderLayout.NORTH);
        frame.add(table, BorderLayout.WEST);
        frame.add(btn3, BorderLayout.CENTER);
        frame.add(btn4, BorderLayout.EAST);
        frame.add(btn5, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}