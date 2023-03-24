package com.ponyvm.soc.peripheral.flashtool;

import java.util.Arrays;

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
public class ELFFile {
    public byte[] ROM;
    public ELFHeader HEADER;
    public ELFProgramHeader[] PROGRAM_HEADERS;

    public ELFFile(byte[] ROM) {
        this.ROM = ROM;
        this.HEADER = new ELFHeader(ROM);
        int ps_num = this.HEADER.e_ph_num();
        int ps_ent_size = this.HEADER.e_ph_ent_size();
        int ps_offset = this.HEADER.e_ph_offset();
        this.PROGRAM_HEADERS = new ELFProgramHeader[ps_num];
        for (int i = 0; i < ps_num; i++) {
            this.PROGRAM_HEADERS[i] = new ELFProgramHeader(ROM, ps_offset + i * ps_ent_size, ps_ent_size);
        }
    }

    @Override
    public String toString() {
        return "ELFFile{\n" + "HEADER=\n" + HEADER + "PROGRAM_SEGMENTS=\n" + Arrays.toString(PROGRAM_HEADERS) + '}';
    }
}

