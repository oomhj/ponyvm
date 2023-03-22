package com.ponyvm.vm;

import com.ponyvm.peripheral.ELFFile;
import com.ponyvm.peripheral.ELFProgramHeader;

public class ELFLoader {

    public static void loadElf(ELFFile bin, Memory memory) {
        int phl = bin.PROGRAM_HEADERS.length;
        for (int i = 0; i < phl; i++) {
            ELFProgramHeader eph = bin.PROGRAM_HEADERS[i];
            if (eph.p_type() == 1) {
                int vaddr = eph.p_vaddr();
                int offset = eph.p_offset();
                int size = eph.p_filesz();
                for (int j = 0; j < size; j = j + 4) {
                    int word = (int) ((bin.ROM[offset + j + 3] & 0xFF) << 24) + ((bin.ROM[offset + j + 2] & 0xFF) << 16) + ((bin.ROM[offset + j + 1] & 0xFF) << 8) + ((bin.ROM[offset + j] & 0xFF) << 0);
                    memory.storeWord(j + vaddr, word);
                }
            }
        }
    }
}
