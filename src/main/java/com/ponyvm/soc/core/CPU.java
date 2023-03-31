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
                instruction = decodeInstruction32(instrCode);
            } else {
                instruction = decodeInstruction16(instrCode & 0xFFFF);
            }
            if (instruction != null) {
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

    public Instruction decodeInstruction32(int instrCode) {
        Instruction instruction = null;
        prevPc = pc;
        Instruction32 inst = new Instruction32(instrCode);
        String instAddr = Integer.toUnsignedString(pc, 16);
//        打印指令操作码
//        System.out.println(instAddr + ":" + Integer.toUnsignedString(instruction, 16) + ":" + inst.toAssemblyString());
        switch (inst.opcode) {
            // R-type instructions 包含 RVI RVM
            case 0b0110011: // ADD / SUB / SLL / SLT / SLTU / XOR / SRL / SRA / OR / AND /MUL /MULH /MULHSU /MULHU /DIV /DIVU /REM /REMU
                instruction = rType(inst);
                break;

            // J-type instruction
            case 0b1101111: //JAL
                instruction = new Instruction(inst.rd, 0, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = (pc + 4); // Store address of next instruction in bytes
                        pc += p3;
                    }
                };
                break;

            // I-type instructions
            case 0b1100111: // JALR
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = (pc + 4);
                        pc = (reg[p2] + p3);
                    }
                };
                break;
            case 0b0000011: // LB / LH / LW / LBU / LHU
                instruction = iTypeLoad(inst);
                break;
            case 0b0010011: // ADDI / SLTI / SLTIU / XORI / ORI / ANDI / SLLI / SRLI / SRAI
                instruction = iTypeInteger(inst);
                break;
            case 0b1110011: // ECALL
                instruction = iTypeEcall();
                break;

            //S-type instructions
            case 0b0100011: //SB / SH / SW
                instruction = sType(inst);
                break;

            //B-type instructions
            case 0b1100011: // BEQ / BNE / BLT / BGE / BLTU / BGEU
                instruction = bType(inst);
                break;

            //U-type instructions
            case 0b0110111: //LUI
                instruction = new Instruction(inst.rd, 0, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = p3;
                        pc += 4;
                    }
                };

                break;
            case 0b0010111: //AUIPC
                instruction = new Instruction(inst.rd, 0, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = pc + p3;
                        pc += 4;
                    }
                };
                break;
        }
        reg[0] = 0; // x0 must always be 0
        return instruction;
    }

    /**
     * r-Type instructions:
     * ADD / SUB / SLL / SLT / SLTU / XOR / SRL / SRA / OR / AND
     */
    private Instruction rType(Instruction32 inst) {
        Instruction instruction = null;
        switch (inst.funct3) {
            case 0b000: // ADD / SUB /MUL
                switch (inst.funct7) {
                    case 0b0000000: // ADD
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] + reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0100000: // SUB
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] - reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // MUL 有符号*有符号 低32位
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] * reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
            case 0b001: // SLL /MULH
                switch (inst.funct7) {
                    case 0b0000000: // SLL
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] << reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // MULH 有符号*有符号 高32位
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = (int) (((long) reg[p2]) * reg[p3] >> 32);
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
            case 0b010://SLT MULHSU
                switch (inst.funct7) {
                    case 0b0000000: // SLT
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                if (reg[p2] < reg[p3]) reg[p1] = 1;
                                else reg[p1] = 0;
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // MULHSU 有符号*无符号 高32位
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = (int) ((reg[p2] * Integer.toUnsignedLong(reg[p3])) >> 32);
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
            case 0b011: // SLTU
                switch (inst.funct7) {
                    case 0b0000000: //SLTU
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                if (Integer.toUnsignedLong(reg[p2]) < Integer.toUnsignedLong(reg[p3])) reg[p1] = 1;
                                else reg[p1] = 0;
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // MULHU 无符号*无符号 高32位
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = (int) ((Integer.toUnsignedLong(reg[p2]) * Integer.toUnsignedLong(reg[p3])) >> 32);
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
            case 0b100: // XOR /DIV
                switch (inst.funct7) {
                    case 0b0000000: //XOR
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] ^ reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // DIV
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] / reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
            case 0b101: // SRL / SRA /DIV
                switch (inst.funct7) {
                    case 0b0000000: // SRL
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] >>> reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0100000: // SRA
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] >> reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // DIVU
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = (int) ((Integer.toUnsignedLong(reg[p2]) / Integer.toUnsignedLong(reg[p3])));
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
            case 0b110: // OR
                switch (inst.funct7) {
                    case 0b0000000: // OR
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] | reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // REM
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] % reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
            case 0b111: // AND /REMU
                switch (inst.funct7) {
                    case 0b0000000: // AND
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] & reg[p3];
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0000001: // REMU
                        instruction = new Instruction(inst.rd, inst.rs1, inst.rs2) {
                            @Override
                            public void execute() {
                                reg[p1] = (int) ((Integer.toUnsignedLong(reg[p2]) % Integer.toUnsignedLong(reg[p3])));
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
        }
        return instruction;
    }

    /**
     * i-Type load instructions:
     * LB / LH / LW / LBU / LHU
     */
    private Instruction iTypeLoad(Instruction32 inst) {
        Instruction instruction = null;
//        int addr = reg[inst.rs1] + inst.imm; // Byte address

        switch (inst.funct3) {
            case 0b000: // LB
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = SYS_BUS.getByte(reg[p2] + p3);
                        pc += 4;
                    }
                };
                break;
            case 0b001: // LH
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = SYS_BUS.getHalfWord(reg[p2] + p3);
                        pc += 4;
                    }
                };

                break;
            case 0b010: // LW
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = SYS_BUS.getWord(reg[p2] + p3);
                        pc += 4;
                    }
                };
                break;
            case 0b100: // LBU
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = SYS_BUS.getByte(reg[p2] + p3) & 0xFF; //Remove sign bits
                        pc += 4;
                    }
                };
                break;
            case 0b101: // LHU
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = SYS_BUS.getHalfWord(reg[p2] + p3) & 0xFFFF;
                        pc += 4;
                    }
                };
                break;
            default:
                break;
        }
        return instruction;
    }

    /**
     * I-type integer instructions:
     * ADDI / SLTI / SLTIU / XORI / ORI / ANDI / SLLI / SRLI / SRAI
     */
    private Instruction iTypeInteger(Instruction32 inst) {
        Instruction instruction = null;
        switch (inst.funct3) {
            case 0b000: // ADDI
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = reg[p2] + p3;
                        pc += 4;
                    }
                };

                break;
            case 0b010: // SLTI
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        if (reg[p2] < p3) reg[p1] = 1;
                        else reg[p1] = 0;
                        pc += 4;
                    }
                };
                break;
            case 0b011: // SLTIU
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        if (Integer.toUnsignedLong(reg[p2]) < Integer.toUnsignedLong(p3)) reg[p1] = 1;
                        else reg[p1] = 0;
                        pc += 4;
                    }
                };
                break;
            case 0b100: // XORI
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = reg[p2] ^ p3;
                        pc += 4;
                    }
                };
                break;
            case 0b110: // ORI
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = reg[p2] | p3;
                        pc += 4;
                    }
                };
                break;
            case 0b111: // ANDI
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = reg[p2] & p3;
                        pc += 4;
                    }
                };
                break;
            case 0b001: // SLLI
                instruction = new Instruction(inst.rd, inst.rs1, inst.imm) {
                    @Override
                    public void execute() {
                        reg[p1] = reg[p2] << p3;
                        pc += 4;
                    }
                };
                break;
            case 0b101: // SRLI / SRAI
                int ShiftAmt = inst.imm & 0x1F; // The amount of shifting done by SRLI or SRAI
                switch (inst.funct7) {
                    case 0b0000000: // SRLI
                        instruction = new Instruction(inst.rd, inst.rs1, ShiftAmt) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] >>> p3;
                                pc += 4;
                            }
                        };
                        break;
                    case 0b0100000: // SRAI
                        instruction = new Instruction(inst.rd, inst.rs1, ShiftAmt) {
                            @Override
                            public void execute() {
                                reg[p1] = reg[p2] >> p3;
                                pc += 4;
                            }
                        };
                        break;
                }
                break;
        }
        return instruction;
    }

    /**
     * i-Type ECALL instructions
     */
    private Instruction iTypeEcall() {
        return new Instruction(0, 0, 0) {
            @Override
            public void execute() {
                stop = true;
                System.out.println("ECALL x10:" + reg[10] + ",x11:" + reg[11]);
            }
        };
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
//        pc += 4;
    }

    /**
     * S-type instructions:
     * SB / SH / SW
     */
    private Instruction sType(Instruction32 inst) {
        Instruction instruction = null;
//        int addr = reg[inst.rs1] +inst.imm;
        switch (inst.funct3) {
            case 0b000: // SB
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        SYS_BUS.storeByte(reg[p1] + p3, (byte) reg[p2]);
                        pc += 4;
                    }
                };
                break;
            case 0b001: // SH
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        SYS_BUS.storeHalfWord(reg[p1] + p3, (short) reg[p2]);
                        pc += 4;
                    }
                };
                break;
            case 0b010: // SW
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        SYS_BUS.storeWord(reg[p1] + p3, reg[p2]);
                        pc += 4;
                    }
                };
                break;
        }
        return instruction;
    }

    /**
     * B-type instructions:
     * BEQ / BNE / BLT / BGE / BLTU / BGEU
     */
    private Instruction bType(Instruction32 inst) {
        Instruction instruction = null;
        switch (inst.funct3) {
            case 0b000: // BEQ
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        pc += (reg[p1] == reg[p2]) ? p3 : 4;
                    }
                };
                break;
            case 0b001: // BNE
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        pc += (reg[p1] != reg[p2]) ? p3 : 4;
                    }
                };

                break;
            case 0b100: // BLT
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        pc += (reg[p1] < reg[p2]) ? p3 : 4;
                    }
                };
                break;
            case 0b101: // BGE
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        pc += (reg[p1] >= reg[p2]) ? p3 : 4;
                    }
                };
                break;
            case 0b110: //BLTU
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        pc += (Integer.toUnsignedLong(reg[p1]) < Integer.toUnsignedLong(reg[p2])) ? p3 : 4;
                    }
                };
                break;
            case 0b111: //BLGEU
                instruction = new Instruction(inst.rs1, inst.rs2, inst.imm) {
                    @Override
                    public void execute() {
                        pc += (Integer.toUnsignedLong(reg[p1]) >= Integer.toUnsignedLong(reg[p2])) ? p3 : 4;
                    }
                };
                break;
        }
        return instruction;
    }
}