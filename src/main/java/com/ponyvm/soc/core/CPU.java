package com.ponyvm.soc.core;

import com.ponyvm.soc.internal.sysbus.Addressable;

public class CPU {
    int pc = 0;                     // 程序上计数器
    int prevPc;                     // 上一条指令
    private Cache<Integer> I_CACHE; //指令缓存
    int[] reg = new int[32];        // 寄存器
    float[] fdreg = new float[32];  //浮点寄存器
    int[] csr = new int[0x1000];
    private Addressable SYS_BUS;    // 系统总线
    private boolean stop = true;

    public CPU(Addressable bus, int SP) {
        this.SYS_BUS = bus;//配置总线
        reg[2] = SP; // 栈指针
        I_CACHE = new Cache<>(16 * 1024);//16K条指令缓存（相当于64KB的机器指令）
    }

    public void poweron() {
        this.stop = false;
    }

//    private Instruction InstructionDecode(int pc) {
//        Instruction instr = I_CACHE.get(pc);
//        if (instr == null) {
//            instr = new Instruction(SYS_BUS.getWord(pc));
//            I_CACHE.put(pc, instr);
//        }
//        return instr;
//    }

    public int launch(int EntryAddr) {
        pc = EntryAddr;
        stop = false;
        while (true) {
            int instruction = SYS_BUS.getWord(pc);
            if ((instruction & 0x03) == 3) {
                executeInstruction32(instruction);
            } else {
                executeInstruction16(instruction);

            }
        }
    }

    public boolean executeInstruction16(int instruction) {
        System.out.print(Integer.toUnsignedString(pc, 16) + ":" + Integer.toUnsignedString(instruction, 16)+":");
        short inst_opcode = (short) ((instruction >> 0) & 0x3);
        short inst_rd = (short) ((instruction >> 7) & 0x1f);
        short inst_rs1 = (short) ((instruction >> 7) & 0x1f);
        short inst_rs2 = (short) ((instruction >> 2) & 0x1f);
        short inst_rs1s = (short) ((instruction >> 7) & 0x7);
        short inst_rs2s = (short) ((instruction >> 2) & 0x7);
        short inst_rds = (short) ((instruction >> 2) & 0x7);
        short inst_imm6 = (short) (((instruction >> 2) & 0x1f) | ((instruction >> 7) & (1 << 5)));
        short inst_imm7 = (short) (((instruction >> 4) & (1 << 2)) | ((instruction >> 7) & (0x7 << 3)) | ((instruction << 1) & (1 << 6)));
        short inst_imm8 = (short) (((instruction >> 7) & (0x7 << 3)) | ((instruction << 1) & (0x3 << 6)));
        short inst_imm9 = (short) (((instruction >> 2) & (0x3 << 3)) | ((instruction >> 7) & (1 << 5)) | ((instruction << 4) & (0x7 << 6)));
        short inst_imm10 = (short) (((instruction >> 4) & (1 << 2)) | ((instruction >> 2) & (1 << 3)) | ((instruction >> 7) & (0x3 << 4)) | ((instruction >> 1) & (0x7 << 6)));
        short inst_imm12 = (short) (((instruction >> 2) & (0x7 << 1)) | ((instruction >> 7) & (1 << 4)) | ((instruction << 3) & (1 << 5))
                | ((instruction >> 1) & (0x2d << 6)) | ((instruction << 1) & (1 << 7)) | ((instruction << 2) & (1 << 10)));
        short inst_imm18 = (short) (((instruction << 5) & (1 << 17)) | ((instruction << 10) & (0x1f << 12)));
        short inst_funct2 = (short) ((instruction >> 10) & 0x3);
        short inst_funct3 = (short) ((instruction >> 13) & 0x7);
        int bflag = 0, temp;

        switch (inst_opcode) {
            case 0b00:
                switch (inst_funct3) {
                    case 0b000:
                        System.out.println("c.addi4spn");
                        reg[8 + inst_rds] = reg[2] + inst_imm10;
                        break; // c.addi4spn
//                    case 1: // c.fld
//                        fdreg[8 + inst_rds] = SYS_BUS.getWord(reg[8 + inst_rs1s] + inst_imm8 + 0) << 0;
//                        fdreg[8 + inst_rds] |= SYS_BUS.getWord(reg[8 + inst_rs1s] + inst_imm8 + 4) << 32;
//                        break;
                    case 0b010:
                        System.out.println("c.lw");
                        reg[8 + inst_rds] = SYS_BUS.getWord(reg[8 + inst_rs1s] + inst_imm7);
                        break; //
//                    case 0b011:
//                        fdreg[8 + inst_rds] = SYS_BUS.getWord(reg[8 + inst_rs1s] + inst_imm7);
//                        break; // c.flw
//                    case 5: // c.fsd
//                        SYS_BUS.storeWord(reg[8 + inst_rs1s] + inst_imm8 + 0, (int) (fdreg[8 + inst_rs2s] >> 0f));
//                        SYS_BUS.storeWord(reg[8 + inst_rs1s] + inst_imm8 + 4, (int) (fdreg[8 + inst_rs2s] >> 32f));
//                        break;
                    case 0b110:
                        System.out.println("c.sw");
                        SYS_BUS.storeWord(reg[8 + inst_rs1s] + inst_imm7, reg[8 + inst_rs2s]);
                        break; //
//                    case 7:
//                        SYS_BUS.storeWord(reg[8 + inst_rs1s] + inst_imm7, (int) fdreg[8 + inst_rs2s]);
//                        break; // c.fsw
                }
                break;
            case 0b01:
                switch (inst_funct3) {
                    case 0b000:
                        System.out.println("c.addi");
                        reg[inst_rd] += signed_extend(inst_imm6, 6);
                        break; //c.addi
                    case 0b001:
                        System.out.println("c.jal");
                        reg[1] = pc + 2;
                        pc += signed_extend(inst_imm12, 12);
                        bflag = 1;
                        break;// c.jal
                    case 0b010:
                        System.out.println("c.li");
                        reg[inst_rd] = signed_extend(inst_imm6, 6);
                        break; // c.li
                    case 0b011:
                        System.out.println("c.addi16sp");
                        if (inst_rd == 2) { // c.addi16sp
                            temp = ((instruction >> 2) & (1 << 4)) | ((instruction << 3) & (1 << 5)) | ((instruction << 1) & (1 << 6))
                                    | ((instruction << 4) & (0x3 << 7)) | ((instruction >> 3) & (1 << 9));
                            reg[inst_rd] += signed_extend(temp, 10);
                        } else { //
                            System.out.println("c.lui");
                            reg[inst_rd] = signed_extend(inst_imm18, 18);
                        }
                        break;
                    case 0b100:
                        switch (inst_funct2) {
                            case 0b00:
                                System.out.println("c.srli");
                                reg[8 + inst_rs1s] = (int) reg[8 + inst_rs1s] >> inst_imm6;
                                break; // c.srli
                            case 0b01:
                                System.out.println("c.srai");
                                reg[8 + inst_rs1s] = (int) reg[8 + inst_rs1s] >> inst_imm6;
                                break; // c.srai
                            case 0b10:
                                System.out.println("c.andi");
                                reg[8 + inst_rs1s] &= signed_extend(inst_imm6, 6);
                                break; // c.andi
                            case 0b11:
                                switch ((instruction >> 5) & 3) {
                                    case 0b00:
                                        System.out.println("c.sub");
                                        reg[8 + inst_rs1s] -= reg[8 + inst_rs2s];
                                        break; // c.sub
                                    case 0b01:
                                        System.out.println("c.xor");
                                        reg[8 + inst_rs1s] ^= reg[8 + inst_rs2s];
                                        break; // c.xor
                                    case 0b10:
                                        System.out.println("c.or");
                                        reg[8 + inst_rs1s] |= reg[8 + inst_rs2s];
                                        break; // c.or
                                    case 0b11:
                                        System.out.println("c.and");
                                        reg[8 + inst_rs1s] &= reg[8 + inst_rs2s];
                                        break; // c.and
                                }
                                break;
                        }
                        break;
                    case 0b101:
                        System.out.println("c.j");
                        pc += signed_extend(inst_imm12, 12);
                        bflag = 1;
                        break; // c.j
                    case 0b110: // c.beqz
                    case 0b111: // c.bnez
                        System.out.println("c.bnez");
                        if (inst_funct3 == 6 && reg[8 + inst_rs1s] == 0 || inst_funct3 == 7 && reg[8 + inst_rs1s] != 0) {
                            temp = ((instruction >> 2) & (0x3 << 1)) | ((instruction >> 7) & (0x3 << 3)) | ((instruction << 3) & (1 << 5))
                                    | ((instruction << 1) & (0x3 << 6)) | ((instruction >> 4) & (1 << 8));
                            pc += signed_extend(temp, 9);
                            bflag = 1;
                        }
                        break;
                }
                break;
            case 0b10:
                switch (inst_funct3) {
                    case 0b000:
                        System.out.println("c.slli");
                        reg[inst_rd] <<= inst_imm6;
                        break; // c.slli
//                    case 1: // c.fldsp
//                        fdreg[inst_rd] = SYS_BUS.getWord(reg[2] + inst_imm9 + 0) << 0;
//                        fdreg[inst_rd] |= SYS_BUS.getWord(reg[2] + inst_imm9 + 4) << 32;
//                        break;
                    case 0b010: // c.lwsp
//                    case 3: // c.flwsp
//                        temp = ((instruction >> 2) & (0x7 << 2)) | ((instruction >> 7) & (1 << 5)) | ((instruction << 4) & (0x3 << 6));
//                        if (inst_funct3 == 2) reg[inst_rd] = SYS_BUS.getWord(reg[2] + temp); // c.lwsp
//                        else fdreg[inst_rd] = SYS_BUS.getWord(reg[2] + temp); // c.flwsp
//                        break;
                    case 0b100:
                        if ((instruction & (1 << 12)) == 0) {
                            if (inst_rs2 == 0) { // c.jr
                                System.out.println("c.jr");
                                pc = reg[inst_rs1];
                                bflag = 1;
                            } else { // c.mv
                                System.out.println("c.mv");
                                reg[inst_rd] = reg[inst_rs2];
                            }
                        } else {
                            if (inst_rs1 == 0 && inst_rs2 == 0) { // c.ebreak;
                                System.out.println("c.ebreak");
                            } else if (inst_rs2 == 0) { // c.jalr
                                System.out.println("c.jalr");
                                temp = pc + 2;
                                pc = reg[inst_rs1];
                                reg[1] = temp;
                                bflag = 1;
                            } else { // c.add
                                System.out.println("c.add");
                                reg[inst_rd] += reg[inst_rs2];
                            }
                        }
                        break;
//                    case 5: // c.fsdsp
//                        temp = ((instruction >> 7) & (0x7 << 3)) | ((instruction >> 1) & (0x7 << 6));
//                        SYS_BUS.storeWord(reg[2] + temp + 0, (int) (fdreg[inst_rs2] >> 0));
//                        SYS_BUS.storeWord(reg[2] + temp + 4, (int) (fdreg[inst_rs2] >> 32));
//                        break;
                    case 0b110: // c.swsp
//                    case 7: // c.fswsp
//                        temp = ((instruction >> 7) & (0xf << 2)) | ((instruction >> 1) & (0x3 << 6));
//                        SYS_BUS.storeWord(reg[2] + temp, inst_funct3 == 6 ? reg[inst_rs2] : (int) fdreg[inst_rs2]);
                        break;
                }
                break;
        }
        pc += (bflag == 1 ? 0 : 2);
        return true;
    }

    static int signed_extend(int a, int size) {
        return ((a & (1 << (size - 1))) == 1) ? (a | ~((1 << size) - 1)) : a;
    }

    public boolean executeInstruction32(int instruction) {
        prevPc = pc;
        Instruction inst = new Instruction(instruction);
        String instAddr = Integer.toUnsignedString(pc, 16);
//        打印指令操作码
        System.out.println(Integer.toUnsignedString(pc, 16) + ":" + inst.toAssemblyString());
        switch (inst.opcode) {
            // R-type instructions 包含 RVI RVM
            case 0b0110011: // ADD / SUB / SLL / SLT / SLTU / XOR / SRL / SRA / OR / AND /MUL /MULH /MULHSU /MULHU /DIV /DIVU /REM /REMU
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
     * r-Type instructions:
     * ADD / SUB / SLL / SLT / SLTU / XOR / SRL / SRA / OR / AND
     */
    private void rType(Instruction inst) {
        switch (inst.funct3) {
            case 0b000: // ADD / SUB /MUL
                switch (inst.funct7) {
                    case 0b0000000: // ADD
                        reg[inst.rd] = reg[inst.rs1] + reg[inst.rs2];
                        break;
                    case 0b0100000: // SUB
                        reg[inst.rd] = reg[inst.rs1] - reg[inst.rs2];
                        break;
                    case 0b0000001: // MUL 有符号*有符号 低32位
                        reg[inst.rd] = reg[inst.rs1] * reg[inst.rs2];
                        break;
                }
                break;
            case 0b001: // SLL /MULH
                switch (inst.funct7) {
                    case 0b0000000: // SLL
                        reg[inst.rd] = reg[inst.rs1] << reg[inst.rs2];
                        break;
                    case 0b0000001: // MULH 有符号*有符号 高32位
                        reg[inst.rd] = (int) (((long) reg[inst.rs1]) * reg[inst.rs2] >> 32);
                        break;
                }
                break;
            case 0b010://SLT MULHSU
                switch (inst.funct7) {
                    case 0b0000000: // SLT
                        if (reg[inst.rs1] < reg[inst.rs2]) reg[inst.rd] = 1;
                        else reg[inst.rd] = 0;
                        break;
                    case 0b0000001: // MULHSU 有符号*无符号 高32位
                        reg[inst.rd] = (int) ((reg[inst.rs1] * Integer.toUnsignedLong(reg[inst.rs2])) >> 32);
                        break;
                }
                break;
            case 0b011: // SLTU
                switch (inst.funct7) {
                    case 0b0000000: //SLTU
                        if (Integer.toUnsignedLong(reg[inst.rs1]) < Integer.toUnsignedLong(reg[inst.rs2]))
                            reg[inst.rd] = 1;
                        else reg[inst.rd] = 0;
                        break;
                    case 0b0000001: // MULHU 无符号*无符号 高32位
                        reg[inst.rd] = (int) ((Integer.toUnsignedLong(reg[inst.rs1]) * Integer.toUnsignedLong(reg[inst.rs2])) >> 32);
                        break;
                }
                break;
            case 0b100: // XOR /DIV
                switch (inst.funct7) {
                    case 0b0000000: //XOR
                        reg[inst.rd] = reg[inst.rs1] ^ reg[inst.rs2];
                        break;
                    case 0b0000001: // DIV
                        reg[inst.rd] = reg[inst.rs1] / reg[inst.rs2];
                        break;
                }
                break;
            case 0b101: // SRL / SRA /DIV
                switch (inst.funct7) {
                    case 0b0000000: // SRL
                        reg[inst.rd] = reg[inst.rs1] >>> reg[inst.rs2];
                        break;
                    case 0b0100000: // SRA
                        reg[inst.rd] = reg[inst.rs1] >> reg[inst.rs2];
                        break;
                    case 0b0000001: // DIVU
                        reg[inst.rd] = (int) ((Integer.toUnsignedLong(reg[inst.rs1]) / Integer.toUnsignedLong(reg[inst.rs2])));
                        break;
                }
                break;
            case 0b110: // OR
                switch (inst.funct7) {
                    case 0b0000000: // OR
                        reg[inst.rd] = reg[inst.rs1] | reg[inst.rs2];
                        break;
                    case 0b0000001: // REM
                        reg[inst.rd] = reg[inst.rs1] % reg[inst.rs2];
                        break;
                }
                break;
            case 0b111: // AND /REMU
                switch (inst.funct7) {
                    case 0b0000000: // AND
                        reg[inst.rd] = reg[inst.rs1] & reg[inst.rs2];
                        break;
                    case 0b0000001: // REMU
                        reg[inst.rd] = (int) ((Integer.toUnsignedLong(reg[inst.rs1]) % Integer.toUnsignedLong(reg[inst.rs2])));
                        break;
                }
                break;
        }
        pc += 4;
    }

    /**
     * i-Type load instructions:
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
     * I-type integer instructions:
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
     * i-Type ECALL instructions
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
     * S-type instructions:
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
     * B-type instructions:
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