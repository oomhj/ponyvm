package com.ponyvm.soc.riscvcore;

import com.ponyvm.soc.internal.sysbus.Addressable;

public class CPU {
    int pc = 0;                     // Program counter
    int prevPc;                     // Previous pc
    int[] reg = new int[32];        // RISC-V registers x0 to x3
    private Addressable SYS_BUS;          // SYS_BUS
    public boolean stop = true;

    /**
     * CPU constructor
     * Sets stack pointer to last address in memory (last index of byte array memory.getMemory()).
     * Initializes memory and program to input parameters.
     */
    public CPU(Addressable bus, int SP) {
        this.SYS_BUS = bus;                      // Initialize Memory object
        reg[2] = SP; // Initialize stack pointer to point at last address.
    }

    public void poweron() {
        this.stop = false;
    }

    private Instruction InstructionDecode(int pc) {
        return new Instruction(SYS_BUS.getWord(pc));
    }

    public int launch(int EntryAddr) {
        pc = EntryAddr;
        stop = false;
        while (!this.stop) {
            executeInstruction();
        }
        return 0;
    }


    /**
     * Executes one instruction given by the Instruction array 'program' at index given by the program counter 'pc'.
     * Uses the opcode field of the instruction to determine which type of instruction it is and call that method.
     */
    public boolean executeInstruction() {
        prevPc = pc;
        Instruction inst = InstructionDecode(pc);
        String instAddr = Integer.toUnsignedString(pc, 16);
//        打印指令操作码
        System.out.println(Integer.toUnsignedString(pc, 16) + ":" + inst.assemblyString);

//        if (inst.assemblyString.trim().equals("jal x0 0(x0)")) {
//            return ;
//        }
        switch (inst.opcode) {
            // R-type instructions
            case 0b0110011: // ADD / SUB / SLL / SLT / SLTU / XOR / SRL / SRA / OR / AND
                rType(inst);
                break;

            // J-type instruction
            case 0b1101111: //JAL
                reg[inst.rd] = (pc + 4); // Store address of next instruction in bytes
                pc += inst.imm;
                break;

            // I-type instructions
            case 0b1100111: // JALR
                reg[inst.rd] = (pc + 4);
                pc = (reg[inst.rs1] + inst.imm);
                break;
            case 0b0000011: // LB / LH / LW / LBU / LHU
                iTypeLoad(inst);
                break;
            case 0b0010011: // ADDI / SLTI / SLTIU / XORI / ORI / ANDI / SLLI / SRLI / SRAI
                iTypeInteger(inst);
                break;
            case 0b1110011: // ECALL
                iTypeEcall();
                break;

            //S-type instructions
            case 0b0100011: //SB / SH / SW
                sType(inst);
                break;

            //B-type instructions
            case 0b1100011: // BEQ / BNE / BLT / BGE / BLTU / BGEU
                bType(inst);
                break;

            //U-type instructions
            case 0b0110111: //LUI
                reg[inst.rd] = inst.imm;
                pc += 4;
                break;
            case 0b0010111: //AUIPC
                reg[inst.rd] = pc + inst.imm;
                pc += 4;
                break;
        }
        reg[0] = 0; // x0 must always be 0
        return this.stop;
    }

    /**
     * Handles execution of r-Type instructions:
     * ADD / SUB / SLL / SLT / SLTU / XOR / SRL / SRA / OR / AND
     */
    private void rType(Instruction inst) {
        switch (inst.funct3) {
            case 0b000: // ADD / SUB
                switch (inst.funct7) {
                    case 0b0000000: // ADD
                        reg[inst.rd] = reg[inst.rs1] + reg[inst.rs2];
                        break;
                    case 0b0100000: // SUB
                        reg[inst.rd] = reg[inst.rs1] - reg[inst.rs2];
                        break;
                }
                break;
            case 0b001: // SLL
                reg[inst.rd] = reg[inst.rs1] << reg[inst.rs2];
                break;
            case 0b010: // SLT
                if (reg[inst.rs1] < reg[inst.rs2]) reg[inst.rd] = 1;
                else reg[inst.rd] = 0;
                break;
            case 0b011: // SLTU
                if (Integer.toUnsignedLong(reg[inst.rs1]) < Integer.toUnsignedLong(reg[inst.rs2])) reg[inst.rd] = 1;
                else reg[inst.rd] = 0;
                break;
            case 0b100: // XOR
                reg[inst.rd] = reg[inst.rs1] ^ reg[inst.rs2];
                break;
            case 0b101: // SRL / SRA
                switch (inst.funct7) {
                    case 0b0000000: // SRL
                        reg[inst.rd] = reg[inst.rs1] >>> reg[inst.rs2];
                        break;
                    case 0b0100000: // SRA
                        reg[inst.rd] = reg[inst.rs1] >> reg[inst.rs2];
                        break;
                }
                break;
            case 0b110: // OR
                reg[inst.rd] = reg[inst.rs1] | reg[inst.rs2];
                break;
            case 0b111: // AND
                reg[inst.rd] = reg[inst.rs1] & reg[inst.rs2];
                break;
        }
        pc += 4;
    }

    /**
     * Handles execution of i-Type load instructions:
     * LB / LH / LW / LBU / LHU
     */
    private void iTypeLoad(Instruction inst) {
        int addr = reg[inst.rs1] + inst.imm; // Byte address

        switch (inst.funct3) {
            case 0b000: // LB
                reg[inst.rd] = SYS_BUS.getByte(addr);
                break;
            case 0b001: // LH
                reg[inst.rd] = SYS_BUS.getHalfWord(addr);
                break;
            case 0b010: // LW
                reg[inst.rd] = SYS_BUS.getWord(addr);
                break;
            case 0b100: // LBU
                reg[inst.rd] = SYS_BUS.getByte(addr) & 0xFF; //Remove sign bits
                break;
            case 0b101: // LHU
                reg[inst.rd] = SYS_BUS.getHalfWord(addr) & 0xFFFF;
                break;
            default:
                break;
        }
        pc += 4;
    }

    /**
     * Handles execution of I-type integer instructions:
     * ADDI / SLTI / SLTIU / XORI / ORI / ANDI / SLLI / SRLI / SRAI
     */
    private void iTypeInteger(Instruction inst) {
        switch (inst.funct3) {
            case 0b000: // ADDI
                reg[inst.rd] = reg[inst.rs1] + inst.imm;
                break;
            case 0b010: // SLTI
                if (reg[inst.rs1] < inst.imm) reg[inst.rd] = 1;
                else reg[inst.rd] = 0;
                break;
            case 0b011: // SLTIU
                if (Integer.toUnsignedLong(reg[inst.rs1]) < Integer.toUnsignedLong(inst.imm)) reg[inst.rd] = 1;
                else reg[inst.rd] = 0;
                break;
            case 0b100: // XORI
                reg[inst.rd] = reg[inst.rs1] ^ inst.imm;
                break;
            case 0b110: // ORI
                reg[inst.rd] = reg[inst.rs1] | inst.imm;
                break;
            case 0b111: // ANDI
                reg[inst.rd] = reg[inst.rs1] & inst.imm;
                break;
            case 0b001: // SLLI
                reg[inst.rd] = reg[inst.rs1] << inst.imm;
                break;
            case 0b101: // SRLI / SRAI
                int ShiftAmt = inst.imm & 0x1F; // The amount of shifting done by SRLI or SRAI
                switch (inst.funct7) {
                    case 0b0000000: // SRLI
                        reg[inst.rd] = reg[inst.rs1] >>> ShiftAmt;
                        break;
                    case 0b0100000: // SRAI
                        reg[inst.rd] = reg[inst.rs1] >> ShiftAmt;
                        break;
                }
                break;
        }
        pc += 4;
    }

    /**
     * Handles execution of i-Type ECALL instructions
     */
    private void iTypeEcall() {
        System.out.println("ECALL x10:" + reg[10] + ",x11:" + reg[11]);
//        switch (reg[10]) {
//            case 0:     // print_int
//                System.out.println("ECALL x10" + reg[10] + ",x11" + reg[11]);
//                this.stop = true;
//                break;
//            case 1:     // print_int
//                System.out.println(reg[11]);
//                break;
//            case 4:     // print_string
//                System.out.println(memory.getString(reg[11]));
//                break;
//            case 9:     // sbrk
//                // not sure if we can do this?
//                break;
//            case 10:    // exit
//                this.stop = true; // Sets program counter to end of program, to program loop
//                return;              // Exits 'iTypeStatus' function and returns to loop.
//            case 11:    // print_character
//                System.out.println((char) reg[11]);
//                break;
//            case 17:    // exit2
//                pc = -1;
//                //System.out.println("Return code: " + reg[11]); // Prints a1 (should be return?)
//                return;
//            default:
//                System.out.println("ECALL " + reg[10] + " not implemented");
//                break;
//        }
        this.stop = true;
//        pc += 4;
    }

    /**
     * Handles the S-type instructions:
     * SB / SH / SW
     */
    private void sType(Instruction inst) {
        int addr = reg[inst.rs1] + inst.imm;
        switch (inst.funct3) {
            case 0b000: // SB
                SYS_BUS.storeByte(addr, (byte) reg[inst.rs2]);
                break;
            case 0b001: // SH
                SYS_BUS.storeHalfWord(addr, (short) reg[inst.rs2]);
                break;
            case 0b010: // SW
                SYS_BUS.storeWord(addr, reg[inst.rs2]);
                break;
        }
        pc += 4;
    }

    /**
     * Handles the B-type instructions:
     * BEQ / BNE / BLT / BGE / BLTU / BGEU
     */
    private void bType(Instruction inst) {
        int Imm = inst.imm;
        switch (inst.funct3) {
            case 0b000: // BEQ
                pc += (reg[inst.rs1] == reg[inst.rs2]) ? Imm : 4;
                break;
            case 0b001: // BNE
                pc += (reg[inst.rs1] != reg[inst.rs2]) ? Imm : 4;
                break;
            case 0b100: // BLT
                pc += (reg[inst.rs1] < reg[inst.rs2]) ? Imm : 4;
                break;
            case 0b101: // BGE
                pc += (reg[inst.rs1] >= reg[inst.rs2]) ? Imm : 4;
                break;
            case 0b110: //BLTU
                pc += (Integer.toUnsignedLong(reg[inst.rs1]) < Integer.toUnsignedLong(reg[inst.rs2])) ? Imm : 4;
                break;
            case 0b111: //BLGEU
                pc += (Integer.toUnsignedLong(reg[inst.rs1]) >= Integer.toUnsignedLong(reg[inst.rs2])) ? Imm : 4;
                break;
        }
    }
}