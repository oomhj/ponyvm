package com.ponyvm.soc.core;

public class Instruction16 extends Instruction {
    int instruction, opcode, rd, rs1, rs2, rs1s, rs2s, rds, imm6, imm7, imm8, lwspimm8, swspimm8, imm9, imm10, imm12, imm18, funct2, funct3;

    public Instruction16(int instruction) {
        this.instruction = instruction;
        this.opcode = (instruction >> 0) & 0x3;
        this.rd = (instruction >> 7) & 0x1f;
        this.rs1 = (instruction >> 7) & 0x1f;
        this.rs2 = (instruction >> 2) & 0x1f;
        this.rs1s = (instruction >> 7) & 0x7;
        this.rs2s = (instruction >> 2) & 0x7;
        this.rds = (instruction >> 2) & 0x7;
        //12 nzimm[5] 6-2 nzimm[4:0] c.addi,c.li c.srli c.andi
        this.imm6 = ((instruction >> 2) & 0x1f) | ((instruction >> 7) & (1 << 5));//pass
        this.imm7 = ((instruction >> 4) & (1 << 2)) | ((instruction >> 7) & (0x7 << 3)) | ((instruction << 1) & (1 << 6));//pass
        this.imm8 = ((instruction >> 7) & (0x7 << 3)) | ((instruction << 1) & (0x3 << 6));//pass
        this.lwspimm8 = ((instruction >> 2) & (0x7 << 2)) | ((instruction >> 7) & (1 << 5)) | ((instruction << 4) & (0x3 << 6));//pass
        this.swspimm8 = ((instruction >> 7) & (0xf << 2)) | ((instruction >> 1) & (0x3 << 6));//pass
        this.imm9 = ((instruction >> 2) & (0x3 << 1)) | ((instruction >> 7) & (0x3 << 3)) | ((instruction << 3) & (1 << 5)) | ((instruction << 1) & (0x3 << 6)) | ((instruction >> 4) & (1 << 8));//pass
        this.imm10 = ((instruction >> 4) & (1 << 2)) | ((instruction >> 2) & (1 << 3)) | ((instruction >> 7) & (0b11 << 4)) | ((instruction >> 1) & (0b1111 << 6));//pass
        this.imm12 = ((instruction >> 2) & (0x7 << 1)) | ((instruction >> 7) & (1 << 4)) | ((instruction << 3) & (1 << 5)) | ((instruction >> 1) & (0x2d << 6)) | ((instruction << 1) & (1 << 7)) | ((instruction << 2) & (1 << 10));//pass
        this.imm18 = ((instruction << 5) & (1 << 17)) | ((instruction << 10) & (0x1f << 12));//pass
        this.funct2 = (instruction >> 10) & 0x3;
        this.funct3 = (instruction >> 13) & 0x7;
    }

    public String toAssemblyString() {
        String as = "";
        switch (opcode) {
            case 0b00:
                switch (funct3) {
                    case 0b000:
                        as = "c.addi4spn " + rds + " x2 " + imm10 + " (addi x" + (8 + rds) + " x2 " + imm10 + ")";
                        break; // c.addi4spn Expansion:addi rd’,x2,nzuimm
                    case 0b010:
                        as = "c.lw";
                        break; //
                    case 0b110:
                        as = "c.sw x" + (8 + rs2s) + " " + imm7 + "(x" + (8 + rs1s) + ") (sw x" + (8 + rs2s) + " " + imm7 + "(x" + (8 + rs1s) + "))";
                        break; //c.sw Expansion:sw rs2’,offset[6:2](rs1’)
                    default:
                        as = "未知指令";
                        break;
                }
                break;
            case 0b01:
                switch (funct3) {
                    case 0b000:
                        int sext_imm6 = signed_extend(imm6, 6);
                        as = "c.addi x" + rd + " " + sext_imm6 + " (addi x" + rd + " x" + rd + " " + sext_imm6 + ")";
                        break; //c.addi Expansion:addi rd, rd, nzimm[5:0]
                    case 0b001:
                        int sext_imm12 = signed_extend(imm12, 12);
                        as = "c.jal " + sext_imm12 + " (jal x1 " + sext_imm12 + "(x0))";
                        break;// c.jal Expansion:jal x1, offset[11:1]
                    case 0b010:
                        sext_imm6 = signed_extend(imm6, 6);
                        as = "c.li x" + rd + " " + sext_imm6 + " (addi x" + rd + " x0 " + sext_imm6 + ")";
                        break; // c.li Expansion:addi rd,x0,imm[5:0]
                    case 0b011:
                        if (rd == 2) { // c.addi16sp Expansion:addi x2,x2, nzimm[9:4]
                            int temp = ((instruction >> 2) & (1 << 4)) | ((instruction << 3) & (1 << 5)) | ((instruction << 1) & (1 << 6)) | ((instruction << 4) & (0x3 << 7)) | ((instruction >> 3) & (1 << 9));
                            int sext_imm10 = signed_extend(temp, 10);
                            as = "c.addi16sp " + sext_imm10 + " (addi x2 x2 " + sext_imm10 + ")";
                        } else { //c.lui Expansion: lui rd,nzuimm[17:12]
                            int sext_imm18 = signed_extend(imm18, 18);
                            as = "c.lui x" + rd + " " + sext_imm18;
                        }
                        break;
                    case 0b100:
                        switch (funct2) {
                            case 0b00:
                                as = "c.srli x" + (8 + rs1s) + " " + imm6 + " (srli x" + (8 + rs1s) + " x" + (8 + rs1s) + " " + imm6 + ")";
                                break; // c.srli Expansion:srli rd’,rd’,shamt[5:0]
                            case 0b01:
                                as = "c.srai";
                                break; // c.srai
                            case 0b10:
                                sext_imm6 = signed_extend(imm6, 6);
                                as = "c.andi x" + (8 + rs1s) + " " + sext_imm6 + " (andi x" + (8 + rs1s) + " x" + (8 + rs1s) + " " + sext_imm6 + ")";
                                break; // c.andi Expansion:andi rd’,rd’,imm[5:0]
                            case 0b11:
                                switch ((instruction >> 5) & 3) {
                                    case 0b00:
                                        as = "c.sub x" + (8 + rs1s) + " x" + (8 + rs1s) + " x" + (8 + rs2s);
                                        break; // c.sub
                                    case 0b01:
                                        as = "c.xor";
                                        break; // c.xor
                                    case 0b10:
                                        as = "c.or";
                                        break; // c.or
                                    case 0b11:
                                        as = "c.and";
                                        break; // c.and
                                }
                                break;
                        }
                        break;
                    case 0b101:// c.j Expansion:jal x0,offset[11:1]
                        sext_imm12 = signed_extend(imm12, 12);
                        as = "c.j " + sext_imm12 + " (jal x0 " + sext_imm12 + ")";
                        break;
                    case 0b110: // c.beqz Expansion: beq rs1’,x0,offset[8:1]
                    case 0b111: // c.bnez Expansion: bne rs1’,x0,offset[8:1]
                        int temp = ((instruction >> 2) & (0x3 << 1)) | ((instruction >> 7) & (0x3 << 3)) | ((instruction << 3) & (1 << 5)) | ((instruction << 1) & (0x3 << 6)) | ((instruction >> 4) & (1 << 8));
                        int sext_temp9 = signed_extend(temp, 9);
                        as = "c." + (funct3 == 6 ? "beqz" : "bnez") + " x" + (8 + rs1s) + " " + sext_temp9 + " (" + (funct3 == 6 ? "beq" : "bne") + " x" + (8 + rs1s) + " x0 " + sext_temp9 + ")";
                        break;
                    default:
                        as = "未知指令";
                        break;
                }
                break;
            case 0b10:
                switch (funct3) {
                    case 0b000:
                        as = "c.slli";
                        break; // c.slli
                    case 0b010: // c.lwsp
                        as = "c.lwsp";
                        int temp = ((instruction >> 2) & (0x7 << 2)) | ((instruction >> 7) & (1 << 5)) | ((instruction << 4) & (0x3 << 6));
                        break;
                    case 0b100:
                        if ((instruction & (1 << 12)) == 0) {
                            if (rs2 == 0) { // c.jr Expansion:jalr x0,rs1,0
                                as = "c.jr x" + rs1 + " (jalr x0 x" + rs1 + " 0)";
                            } else { // c.mv Expansion:add rd, x0, rs2
                                as = "c.mv x" + rd + " x" + rs2 + " (add x" + rd + " x0 x" + rs2 + ")";
                            }
                        } else {
                            if (rs1 == 0 && rs2 == 0) { // c.ebreak;
                                as = "c.ebreak";
                            } else if (rs2 == 0) { // c.jalr
                                as = "c.jalr";
                            } else { // c.add Expansion:add rd,rd,rs2
                                as = "c.add x" + rd + " x" + rs2 + " (add x" + rd + " x" + rd + " x" + rs2 + ")";
                            }
                        }
                        break;
                    case 0b110: // c.swsp Expansion: sw rs2,offset[7:2](x2)
                        temp = ((instruction >> 7) & (0xf << 2)) | ((instruction >> 1) & (0x3 << 6));
                        as = "c.swsp x" + rs2 + " " + temp + "(x2) (sw x" + rs2 + " " + temp + "(x2))";
                        break;
                    default:
                        as = "未知指令";
                        break;
                }
                break;
        }
        return as;
    }

    private int signed_extend(int a, int size) {
//        return (a & (1 << (size - 1))) == 1 ? (a | ~((1 << size) - 1)) : a;
        return (a >> (size - 1) & 1) == 1 ? (a | ~((1 << size) - 1)) : a;
    }
}
