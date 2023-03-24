package com.ponyvm.tools;

public class Utils {
    /**
     * 大小端反序
     *
     * @param ob
     * @return
     */
    public static byte[] swapEndian(byte[] ob, int offset, int l) {
        byte[] nb = new byte[l];
        for (int i = 0; i < l; i++) {
            nb[l - 1 - i] = ob[offset + i];
        }
        return nb;
    }

    public static int LittleEndianToInt(byte[] ob, int offset) {
        int i;
        i = (int) ((ob[offset] & 0xFF)
                | ((ob[offset + 1] & 0xFF) << 8)
                | ((ob[offset + 2] & 0xFF) << 16)
                | ((ob[offset + 3] & 0xFF) << 24));
        return i;
    }
}
