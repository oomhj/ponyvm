package com.ponyvm.peripheral;

public class ELFHeader {
    private byte[] ROM;

    public ELFHeader(byte[] ROM) {
        this.ROM = ROM;
    }

    //ELF Header：e_ident
    public String e_ident_magic() {
        return new String(new byte[]{ROM[1], ROM[2], ROM[3]});
    }

    public int e_ident_class() {
        return ROM[4];
    }

    //1小端，2大端
    public int e_ident_data() {
        return ROM[5];
    }

    public int e_ident_version() {
        return ROM[6];
    }

    public int e_ident_OSABI() {
        return ROM[7];
    }

    //ELF Header：e_type
    public int e_type() {
        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[17] & 0xFF) << 8) + (ROM[16] & 0xFF);
        } else {
            return ((ROM[16] & 0xFF) << 8) + (ROM[17] & 0xFF);
        }
    }

    public int e_machine() {
        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[19] & 0xFF) << 8) + (ROM[18] & 0xFF);
        } else {
            return ((ROM[18] & 0xFF) << 8) + (ROM[19] & 0xFF);
        }
    }

    public int e_vision() {
        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[23] & 0xFF) << 32) + ((ROM[22] & 0xFF) << 16) + ((ROM[21] & 0xFF) << 8) + ((ROM[20] & 0xFF) << 0);
        } else {
            return ((ROM[20] & 0xFF) << 32) + ((ROM[21] & 0xFF) << 16) + ((ROM[22] & 0xFF) << 8) + ((ROM[23] & 0xFF) << 0);
        }
    }

    public int e_entry() {
        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[27] & 0xFF) << 32) + ((ROM[26] & 0xFF) << 16) + ((ROM[25] & 0xFF) << 8) + ((ROM[24] & 0xFF) << 0);
        } else {
            return ((ROM[24] & 0xFF) << 32) + ((ROM[25] & 0xFF) << 16) + ((ROM[26] & 0xFF) << 8) + ((ROM[27] & 0xFF) << 0);
        }
    }

    public int e_ph_offset() {
        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[31] & 0xFF) << 32) + ((ROM[30] & 0xFF) << 16) + ((ROM[29] & 0xFF) << 8) + ((ROM[28] & 0xFF) << 0);
        } else {
            return ((ROM[28] & 0xFF) << 32) + ((ROM[29] & 0xFF) << 16) + ((ROM[30] & 0xFF) << 8) + ((ROM[31] & 0xFF) << 0);
        }
    }

    /**
     * 跳过 e_sh_off(section头位置) 4字节，e_flags 4字节，e_eh_size（ELF头大小）2字节，
     * 单个program头内容大小
     * 2字节
     *
     * @return
     */

    public int e_ph_ent_size() {
        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[43] & 0xFF) << 8) + ((ROM[42] & 0xFF) << 0);
        } else {
            return ((ROM[42] & 0xFF) << 8) + ((ROM[43] & 0xFF) << 0);
        }
    }

    /**
     * program头数量
     * 2字节
     */
    public int e_ph_num() {
        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[45] & 0xFF) << 8) + ((ROM[44] & 0xFF) << 0);
        } else {
            return ((ROM[44] & 0xFF) << 8) + ((ROM[45] & 0xFF) << 0);
        }
    }

    /**
     * program头类型
     * 4字节
     */
    public int e_ph_type() {

        //如果小端，需要转一下
        if (e_ident_data() == 1) {
            return ((ROM[45] & 0xFF) << 8) + ((ROM[44] & 0xFF) << 0);
        } else {
            return ((ROM[44] & 0xFF) << 8) + ((ROM[45] & 0xFF) << 0);
        }
    }

    /**
     * Magic:   7f 45 4c 46 01 01 01 00 00 00 00 00 00 00 00 00
     * Class:                             ELF32
     * Data:                              2's complement, little endian
     * Version:                           1 (current)
     * OS/ABI:                            UNIX - System V
     * ABI Version:                       0
     * Type:                              EXEC (Executable file)
     * Machine:                           Intel 80386
     * Version:                           0x1
     * Entry point address:               0x80482e0
     * Start of program headers:          52 (bytes into file)
     * Start of section headers:          5944 (bytes into file)
     * Flags:                             0x0
     * Size of this header:               52 (bytes)
     * Size of program headers:           32 (bytes)
     * Number of program headers:         9
     * Size of section headers:           40 (bytes)
     * Number of section headers:         31
     * Section header string table index: 30
     * ————————————————
     */
    @Override
    public String toString() {
        String ln = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("Magic:").append(e_ident_magic()).append(ln);
        sb.append("Class:").append(Integer.toHexString(e_ident_class())).append(ln);
        sb.append("Data:").append(Integer.toHexString(e_ident_data())).append(ln);
        sb.append("Version:").append(Integer.toHexString(e_ident_version())).append(ln);
        sb.append("OS/ABI:").append(Integer.toHexString(e_ident_OSABI())).append(ln);
        sb.append("Type:").append(Integer.toHexString(e_type())).append(ln);
        sb.append("Machine:").append(Integer.toHexString(e_machine())).append(ln);
        sb.append("Version:").append(Integer.toHexString(e_vision())).append(ln);
        sb.append("Entry point address:").append(Integer.toHexString(e_entry())).append(ln);
        sb.append("Start of program headers:").append(Integer.toHexString(e_ph_offset())).append(ln);
        sb.append("Size of program headers:").append(Integer.toHexString(e_ph_ent_size())).append(ln);
        sb.append("Number of program headers:").append(Integer.toHexString(e_ph_num())).append(ln);
        return sb.toString();
    }
}
