package com.ponyvm.peripheral;

public class TTY {
    public void print(byte c) {
        System.out.println(Byte.toUnsignedInt(c));
    }
}
