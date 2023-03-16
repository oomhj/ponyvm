package com.ponyvm;

import com.ponyvm.toolchain.AssemblyCompiler;
import com.ponyvm.vm.PonyVM;

public class Main {
    public static void main(String[] args) {
//Hello，World！程序
//        String program = "PUSH,0x0a;PUSH,0x21;PUSH,0x64;PUSH,0x6c;PUSH,0x72;PUSH,0x6f;PUSH,0x57;PUSH,0x2c;PUSH,0x6f;PUSH,0x6c;PUSH,0x6c;PUSH,0x65;PUSH,0x48;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;END;";

//累加程序 1+2+……+20
//        int r1 = 0,r2 =0;
//        while(r1 != 20){
//            r2 =++r1+r2;
//        }
//        System.out.println(r2);
        String program = "PUSH,0x14;STR,0x82;POP;PUSH,0x00;STR,0x80;STR,0x81;POP;PUSH,0x01;LDR,0x80;LDR,0x82;CMP;JMP1,0x25;POP;POP;ADD;STR,0x80;LDR,0x81;ADD;STR,0x81;POP;POP;POP;POP;JMP,0x0E;LDR,0x81;STR,0xFF;END;";
        byte[] ROM = AssemblyCompiler.compile(program);

        PonyVM vm = new PonyVM();
        vm.loadBinaryProgram(ROM);
        vm.runCPU();
    }
}