package com.ponyvm.soc.core;

import com.ponyvm.soc.internal.sysbus.Addressable;

//符号扩展sign-extend（$：用最左面的bit(bit15)填充扩展的高16bit），也可能是零扩展zero-extend（$：用0填充扩展的高16bit）——这取决于具体的指令。一般而言，算术指令进行符号扩展sign-extend，而逻辑指令进行零扩展zero-extend。
public class CPU {
    int pc = 0;                     // 程序上计数器
    int prevPc;                     // 上一条指令
    private Cache<Instruction> I_CACHE; //指令缓存

    int[] reg = new int[32];        // 寄存器
    float[] fdreg = new float[32];  //浮点寄存器
    int[] csr = new int[0x1000];
    private Addressable SYS_BUS;    // 系统总线
    private boolean stop = true;

    public int frq = 0;

    public CPU(Addressable bus, int SP) {
        this.SYS_BUS = bus;//配置总线
        reg[2] = SP; // 栈指针
        I_CACHE = new Cache<>(16 * 1024);//16K条指令缓存（相当于64KB的机器指令）
    }

    public void poweron() {
        this.stop = false;
    }

    public boolean isRunning() {
        return !this.stop;
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
        while (isRunning()) {
            Instruction instruction = instructionFetch(pc);
            frq++;
            if (instruction != null) {
                instruction.execute();
            }
        }
        return reg[10];
    }

    private Instruction instructionFetch(int pc) {
        Instruction instruction = I_CACHE.get(pc);
        if (instruction == null) {
            int instrCode = SYS_BUS.getWord(pc);
            if ((instrCode & 0x03) == 3) {
                executeInstruction32(instrCode);
            } else {
                instruction = decodeInstruction16(instrCode & 0xFFFF);
            }
            if (instruction!=null){
                I_CACHE.put(pc, instruction);
            }
        }
        return instruction;
    }

    public Instruction decodeInstruction16(int instruction) {
        Instruction instr = null;
        Instruction16 instr16 = new Instruction16(instruction);
        String instAddr = Integer.toUnsignedString(pc, 16);
//        System.out.println(instAddr + ":" + Integer.toUnsignedString(instruction, 16) + ":" + instr16.toAssemblyString());
        int bflag = 0, temp;
        int rd, rs1, rs2, rds, rs1s, rs2s, imm;
        switch (instr16.opcode) {
            case 0b00:
                switch (instr16.funct3) {
                    case 0b000:
                        // c.addi4spn Expansion:addi rd’,x2,nzuimm
                        rds = (instruction >> 2) & 0b111;
                        //nzuimm[5:4|9:6|2|3]
                        imm = instr16.imm10;
                        instr = new Instruction(rds, 0, imm) {
                            @Override
                            public void execute() {
                                reg[8 + p1] = reg[2] + p3;
                                pc += 2;
                            }
                        };
                        break;
                    case 0b010:
                        //c.lw Expansion:lw rd’,offset[6:2](rs1’)
                        imm = instr16.imm7;
                        instr = new Instruction(instr16.rds, instr16.rs1s, imm) {
                            @Override
                            public void execute() {
                                reg[8 + p1] = SYS_BUS.getWord(reg[8 + p2] + p3);
                                pc += 2;
                            }
                        };
                        break;
                    case 0b110:
                        //c.sw Expansion:sw rs2’,offset[6:2](rs1’)
                        imm = instr16.imm7;
                        instr = new Instruction(instr16.rs1s, instr16.rs2s, imm) {
                            @Override
                            public void execute() {
                                SYS_BUS.storeWord(reg[8 + p1] + p3, reg[8 + p2]);
                                pc += 2;
                            }
                        };
                        break;
                    default:
                        break;
                }
                break;
            case 0b01:
                switch (instr16.funct3) {
                    case 0b000:
                        //c.addi Expansion:addi rd, rd, nzimm[5:0]
                        int sext_imm6 = signed_extend(instr16.imm6, 6);
                        instr = new Instruction(instr16.rd, 0, sext_imm6) {
                            @Override
                            public void execute() {
                                reg[p1] += p3;
                                pc += 2;
                            }
                        };
                        break;
                    case 0b001:
                        // c.jal Expansion:jal x1, offset[11:1]
                        int sext_imm12 = signed_extend(instr16.imm12, 12);
                        instr = new Instruction(0, 0, sext_imm12) {
                            @Override
                            public void execute() {
                                reg[1] = pc + 2;
                                pc += p3;
                            }
                        };
                        break;
                    case 0b010:
                        // c.li Expansion:addi rd,x0,imm[5:0]
                        sext_imm6 = signed_extend(instr16.imm6, 6);
                        instr = new Instruction(instr16.rd, 0, sext_imm6) {
                            @Override
                            public void execute() {
                                reg[p1] = p3;
                                pc += 2;
                            }
                        };
                        break;
                    case 0b011:
                        if (instr16.rd == 2) {
                            // c.addi16sp Expansion:addi x2,x2, nzimm[9:4]
                            temp = ((instruction >> 2) & (1 << 4)) | ((instruction << 3) & (1 << 5)) | ((instruction << 1) & (1 << 6)) | ((instruction << 4) & (0x3 << 7)) | ((instruction >> 3) & (1 << 9));
                            int sext_imm10 = signed_extend(temp, 10);
                            instr = new Instruction(0, 0, sext_imm10) {
                                @Override
                                public void execute() {
                                    reg[2] += p3;
                                    pc += 2;
                                }
                            };
                        } else { //c.lui Expansion: lui rd,nzuimm[17:12]
                            int sext_imm18 = signed_extend(instr16.imm18, 18);
                            instr = new Instruction(instr16.rd, 0, sext_imm18) {
                                @Override
                                public void execute() {
                                    reg[p1] = p3;
                                    pc += 2;
                                }
                            };
                        }
                        break;
                    case 0b100:
                        switch (instr16.funct2) {
                            case 0b00:
                                // c.srli Expansion:srli rd’,rd’,shamt[5:0]
                                int ShiftAmt = instr16.imm6 & 0x1F;
                                instr = new Instruction(0, instr16.rs1s, ShiftAmt) {
                                    @Override
                                    public void execute() {
                                        reg[8 + p2] = reg[8 + p2] >>> p3;
                                        pc += 2;
                                    }
                                };
                                break;
                            case 0b01:
                                // c.srai
                                ShiftAmt = instr16.imm6 & 0x1F;
                                instr = new Instruction(0, instr16.rs1s, ShiftAmt) {
                                    @Override
                                    public void execute() {
                                        reg[8 + p2] = reg[8 + p2] >>> p3;
                                        pc += 2;
                                    }
                                };
//                                reg[8 + instr16.rs1s] = reg[8 + instr16.rs1s] >>> ShiftAmt;
                                break;
                            case 0b10:
                                // c.andi Expansion:andi rd’,rd’,imm[5:0]
                                imm = signed_extend(instr16.imm6, 6);
                                instr = new Instruction(0, instr16.rs1s, imm) {
                                    @Override
                                    public void execute() {
                                        reg[8 + p2] &= p3;
                                        pc += 2;
                                    }
                                };
                                break;
                            case 0b11:
                                switch ((instruction >> 5) & 3) {
                                    case 0b00:
                                        // c.sub
                                        instr = new Instruction(instr16.rs1s, instr16.rs2s, 0) {
                                            @Override
                                            public void execute() {
                                                reg[8 + p1] -= reg[8 + p2];
                                                pc += 2;
                                            }
                                        };
                                        break;
                                    case 0b01:
                                        // c.xor
                                        instr = new Instruction(instr16.rs1s, instr16.rs2s, 0) {
                                            @Override
                                            public void execute() {
                                                reg[8 + p1] ^= reg[8 + p2];
                                                pc += 2;
                                            }
                                        };
                                        break;
                                    case 0b10:
                                        // c.or
                                        instr = new Instruction(instr16.rs1s, instr16.rs2s, 0) {
                                            @Override
                                            public void execute() {
                                                reg[8 + p1] |= reg[8 + p2];
                                                pc += 2;
                                            }
                                        };
                                        break;
                                    case 0b11:
                                        // c.and
                                        instr = new Instruction(instr16.rs1s, instr16.rs2s, 0) {
                                            @Override
                                            public void execute() {
                                                reg[8 + p1] &= reg[8 + p2];
                                                pc += 2;
                                            }
                                        };
                                        break;
                                }
                                break;
                        }
                        break;
                    case 0b101:// c.j Expansion:jal x0,offset[11:1]
                        imm = signed_extend(instr16.imm12, 12);
                        instr = new Instruction(0, 0, imm) {
                            @Override
                            public void execute() {
                                pc += p3;
                            }
                        };
                        break;
                    case 0b110: // c.beqz Expansion: beq rs1’,x0,offset[8:1]
                        imm = signed_extend(instr16.imm9, 9);
                        instr = new Instruction(0, instr16.rs1s, imm) {
                            @Override
                            public void execute() {
                                if (reg[8 + p2] == 0) {
                                    pc += p3;
                                } else {
                                    pc += 2;
                                }
                            }
                        };
                        break;
                    case 0b111: // c.bnez Expansion: bne rs1’,x0,offset[8:1]
                        imm = signed_extend(instr16.imm9, 9);
                        instr = new Instruction(0, instr16.rs1s, imm) {
                            @Override
                            public void execute() {
                                if (reg[8 + p2] != 0) {
                                    pc += p3;
                                } else {
                                    pc += 2;
                                }
                            }
                        };
                        break;
                    default:
                        break;
                }
                break;
            case 0b10:
                switch (instr16.funct3) {
                    case 0b000:
                        // c.slli
                        instr = new Instruction(instr16.rd, 0, instr16.imm6) {
                            @Override
                            public void execute() {
                                reg[p1] <<= p3;
                                pc += 2;
                            }
                        };
                        break;
                    case 0b010: // c.lwsp
                        instr = new Instruction(instr16.rd, 0, instr16.lwspimm8) {
                            @Override
                            public void execute() {
                                reg[p1] = SYS_BUS.getWord(reg[2] + p3); // c.lwsp
                                pc += 2;
                            }
                        };
                        break;
                    case 0b100:
                        if ((instruction & (1 << 12)) == 0) {
                            if (instr16.rs2 == 0) { // c.jr Expansion:jalr x0,rs1,0
                                instr = new Instruction(0, instr16.rs1, 0) {
                                    @Override
                                    public void execute() {
                                        pc = reg[p2];
                                    }
                                };
                            } else { // c.mv Expansion:add rd, x0, rs2
                                instr = new Instruction(instr16.rd, instr16.rs2, 0) {
                                    @Override
                                    public void execute() {
                                        reg[p1] = reg[p2];
                                        pc += 2;
                                    }
                                };
                            }
                        } else {
                            if (instr16.rs1 == 0 && instr16.rs2 == 0) { // c.ebreak;
                            } else if (instr16.rs2 == 0) { // c.jalr
                                instr = new Instruction(0, instr16.rs1, 0) {
                                    @Override
                                    public void execute() {
                                        reg[1] = pc + 2;
                                        pc = reg[p2];
                                    }
                                };
                            } else { // c.add Expansion:add rd,rd,rs2
                                instr = new Instruction(instr16.rd, instr16.rs2, 0) {
                                    @Override
                                    public void execute() {
                                        reg[p1] += reg[p2];
                                        pc += 2;
                                    }
                                };
                            }
                        }
                        break;
                    case 0b110: // c.swsp Expansion: sw rs2,offset[7:2](x2)
                        instr = new Instruction(0, instr16.rs2, instr16.swspimm8) {
                            @Override
                            public void execute() {
                                SYS_BUS.storeWord(reg[2] + p3, reg[p2]);
                                pc += 2;
                            }
                        };
                        break;
                    default:
                        break;
                }
                break;
        }
        return instr;
    }

    private int signed_extend(int a, int size) {
//        return (a & (1 << (size - 1))) == 1 ? (a | ~((1 << size) - 1)) : a;
        return (a >> (size - 1) & 1) == 1 ? (a | ~((1 << size) - 1)) : a;
    }

    public boolean executeInstruction32(int instruction) {
        prevPc = pc;
        Instruction32 inst = new Instruction32(instruction);
        String instAddr = Integer.toUnsignedString(pc, 16);
//        打印指令操作码
//        System.out.println(instAddr + ":" + Integer.toUnsignedString(instruction, 16) + ":" + inst.toAssemblyString());
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
    private void rType(Instruction32 inst) {
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
    private void iTypeLoad(Instruction32 inst) {
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
    private void iTypeInteger(Instruction32 inst) {
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
        this.stop = true;
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
    private void sType(Instruction32 inst) {
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
    private void bType(Instruction32 inst) {
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