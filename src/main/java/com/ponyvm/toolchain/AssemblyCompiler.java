package com.ponyvm.toolchain;

public class AssemblyCompiler {
    public static byte[] compile(String program) {
        byte[] bin = new byte[128];
        String[] instructions = program.split(";");
        byte i = 0;
        for (String ins : instructions) {
            String[] insArray = ins.split(",");
            OpCodes opCode = OpCodes.valueOf(insArray[0]);
            bin[i++] = opCode.getIndex();
            switch (opCode) {
                case ADD:
                    break;
                case CMP:
                    break;
                case POP:
                    break;
                case END:
                    break;
                default:
                    bin[i++] = HexToByte(insArray[1]);
                    break;
            }
        }
        return bin;
    }

    public static byte HexToByte(String hex) {
        byte tt = (byte) Integer.parseInt(hex.substring(2), 16);
        return tt;
    }
}
