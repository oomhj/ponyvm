package com.ponyvm.soc.peripheral.flashtool;

import com.ponyvm.tools.Utils;

/**
 * ELFProgramHeader 程序加载段头
 * <p>
 * 4字节 p_type;         Segment type
 * 4字节 p_offset;        Segment file offset
 * 4字节 p_vaddr;         Segment virtual address
 * 4字节 p_paddr;         Segment physical address
 * 4字节 p_filesz;        Segment size in file
 * 4字节 p_memsz;         Segment size in memory
 * 4字节 p_flags;         Segment flags
 * 4字节 p_align;         Segment alignment
 */
public class ELFProgramHeader {
    private byte[] ROM;
    private int POFFSET;
    private int PSIZE;

    public ELFProgramHeader(byte[] ROM, int offset, int size) {
        this.ROM = ROM;
        this.PSIZE = size;
        this.POFFSET = offset;
    }

    public int p_type() {
        int typeOffset = POFFSET + 0;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    public int p_offset() {
        int typeOffset = POFFSET + 4;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    public int p_vaddr() {
        int typeOffset = POFFSET + 8;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    public int p_paddr() {
        int typeOffset = POFFSET + 12;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    public int p_filesz() {
        int typeOffset = POFFSET + 16;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    public int p_memsz() {
        int typeOffset = POFFSET + 20;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    public int p_flags() {
        int typeOffset = POFFSET + 24;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    public int p_align() {
        int typeOffset = POFFSET + 28;
        return Utils.LittleEndianToInt(ROM, typeOffset);
    }

    @Override
    public String toString() {
        String ln = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("p_type:").append(Integer.toHexString(p_type())).append(ln);
        sb.append("p_offset:").append(Integer.toHexString(p_offset())).append(ln);
        sb.append("p_vaddr:").append(Integer.toHexString(p_vaddr())).append(ln);
        sb.append("p_paddr:").append(Integer.toHexString(p_paddr())).append(ln);
        sb.append("p_filesz:").append(Integer.toHexString(p_filesz())).append(ln);
        sb.append("p_memsz:").append(Integer.toHexString(p_memsz())).append(ln);
        sb.append("p_flags:").append(Integer.toHexString(p_flags())).append(ln);
        sb.append("p_align:").append(Integer.toHexString(p_align())).append(ln);
        return sb.toString();
    }
}
