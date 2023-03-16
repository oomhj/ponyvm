package com.ponyvm;

import com.ponyvm.toolchain.AssemblyCompiler;
import com.ponyvm.vm.PonyVM;

public class Main {
    public static void main(String[] args) {
        String program = "PUSH,0x0a;PUSH,0x21;PUSH,0x64;PUSH,0x6c;PUSH,0x72;PUSH,0x6f;PUSH,0x57;PUSH,0x2c;PUSH,0x6f;PUSH,0x6c;PUSH,0x6c;PUSH,0x65;PUSH,0x48;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;STR,0xFF;POP;END;";
        byte[] ROM = AssemblyCompiler.compile(program);
        
        PonyVM vm = new PonyVM();
        vm.loadBinaryProgram(ROM);
        vm.runCPU();
    }
}