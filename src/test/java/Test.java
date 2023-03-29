public class Test {

    public static void main(String[] args) {
//        imm[11|4|9:8|10|6|7|3:1|5]
        int instruction = 0x2215;
        int b3_1 = ((instruction >> 1) & (7 << 1));
        System.out.println(Integer.toUnsignedString(b3_1, 2).equals("1000"));
        int b4 = ((instruction << 4) & (1 << 9));
        System.out.println(Integer.toUnsignedString(b4, 2).equals("1000000000"));
        int b5 = ((instruction >> 6) & (1));
        System.out.println(Integer.toUnsignedString(b5, 2).equals("1"));
        int b6 = ((instruction >> 2) & (1 << 5));
        System.out.println(Integer.toUnsignedString(b6, 2).equals("0"));
        int b7 = ((instruction >> 4) & (1 << 4));
        System.out.println(Integer.toUnsignedString(b7, 2).equals("10000"));
        int b9_8 = ((instruction >> 2) & (3 << 7));
        System.out.println(Integer.toUnsignedString(b9_8, 2).equals("110000000"));
        int b10 = ((instruction >> 5) & (1 << 6));
        System.out.println(Integer.toUnsignedString(b10, 2).equals("1000000"));
        int b11 = ((instruction >> 2) & (1 << 10));
        System.out.println(Integer.toUnsignedString(b11, 2).equals("10000000000"));

        int imm11 = ((instruction >> 2) & (1 << 10)) | ((instruction << 4) & (1 << 9)) | ((instruction >> 2) & (3 << 7)) | ((instruction >> 5) & (1 << 6)) | ((instruction >> 2) & (1 << 5)) | ((instruction >> 4) & (1 << 4)) | ((instruction >> 1) & (7 << 1)) | ((instruction >> 6) & (1));
        System.out.println(imm11);

        //        imm[11|4|9:8|10|6|7|3:1|5]
        int a1 = (instruction >> 2) & (0x7 << 1);//3-1 -> 3-1
        System.out.println(Integer.toUnsignedString(a1,2));
        int a2 = (instruction >> 7) & (1 << 4);//10 -> 5
        System.out.println(Integer.toUnsignedString(a2,2));
        int a3 = (instruction << 3) & (1 << 5);//1 -> 6
        System.out.println(Integer.toUnsignedString(a3,2));
        int a4 = (instruction >> 1) & (0x2d << 6);//11,9,8,6 -> 12
        System.out.println(Integer.toUnsignedString(a4,2));
        int a5 = (instruction << 1) & (1 << 7);//5 -> 8
        System.out.println(Integer.toUnsignedString(a5,2));
        int a6 = (instruction << 2) & (1 << 10);//7 -> 11
        System.out.println(Integer.toUnsignedString(a6,2));
        int imm12 = a1 | a2 | a3 | a4 | a5 | a6;
        System.out.println(imm12);
    }
}
