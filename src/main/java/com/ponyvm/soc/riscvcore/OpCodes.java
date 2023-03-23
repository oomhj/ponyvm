package com.ponyvm.soc.riscvcore;

public enum OpCodes {
    PUSH0((byte) 0, "PUSH0"), //    PUSH, OPE
    PUSH((byte) 1, "PUSH"), //    POP
    POP((byte) 2, "POP"), //    LDR, ADDR
    LDR((byte) 3, "LDR"), //    STR,ADDR
    STR((byte) 4, "STR"), //    ADD
    ADD((byte) 5, "ADD"), //    MUL
    MUL((byte) 6, "MUL"), //    SUB
    SUB((byte) 7, "SUB"), //    DIV
    DIV((byte) 8, "DIV"), //    JMP, PC
    JMP((byte) 10, "JMP"), //    JMP0, PC
    JMP0((byte) 11, "JMP0"), //    JMP1, PC
    JMP1((byte) 12, "JMP1"), //    CMP
    CMP((byte) 15, "CMP"), //    DUP
    DUP((byte) 16, "DUP"), //    RS
    RS((byte) 17, "RS"), //    LS
    LS((byte) 18, "LS"),
    END((byte) 0xFF, "END");

    OpCodes(Byte index, String code) {
        this.index = index;
        this.code = code;
    }

    private Byte index;
    private String code;

    public Byte getIndex() {
        return index;
    }

    public void setIndex(Byte index) {
        this.index = index;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static OpCodes byIndex(byte index) {
        for (OpCodes m : OpCodes.values()) {
            if (m.getIndex() == index) {
                return m;
            }
        }
        return null;
    }
}
