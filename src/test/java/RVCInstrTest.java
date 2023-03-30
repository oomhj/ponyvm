public class RVCInstrTest {
    public static void main(String[] args) {
        RVCInstrTest t = new RVCInstrTest();
        System.out.println("test_nzuimm_10: " + (t.test_nzuimm_10() ? "pass" : "fail"));
        System.out.println("test_uimm_7: " + (t.test_uimm_7() ? "pass" : "fail"));
        System.out.println("test_nzimm_6: " + (t.test_nzimm_6() ? "pass" : "fail"));
        System.out.println("test_sign_extend: " + (t.test_sign_extend() ? "pass" : "fail"));
        System.out.println("test_uimm12: " + (t.test_uimm12() ? "pass" : "fail"));
        System.out.println("test_imm18: " + (t.test_imm18() ? "pass" : "fail"));
        System.out.println("test_imm8: " + (t.test_imm8() ? "pass" : "fail"));
        System.out.println("test_lwspimm8: " + (t.test_lwspimm8() ? "pass" : "fail"));
        System.out.println("test_swspimm8: " + (t.test_swspimm8() ? "pass" : "fail"));
        System.out.println("test_imm9: " + (t.test_imm9() ? "pass" : "fail"));
    }

    /**
     * c.beqz c.bnez
     * 15-13	12-10	9-7	6-2	1-0
     * 110	offset[8|4:3]	rs1’	offset[7:6|2:1|5]	01
     * @return
     */
    private boolean test_imm9() {
        boolean pass = true;
        int instruction = 0b000_1_11_000_11_11_1_00;
        int imm9 = imm9(instruction);
        pass = pass & (imm9 == 0b111111110);
        instruction = 0b000_1_00_000_00_00_0_00;
        imm9 = imm9(instruction);
        pass = pass & (imm9 == 0b100000000);
        instruction = 0b000_0_11_000_00_00_0_00;
        imm9 = imm9(instruction);
        pass = pass & (imm9 == 0b000011000);
        instruction = 0b000_0_00_000_11_00_0_00;
        imm9 = imm9(instruction);
        pass = pass & (imm9 == 0b011000000);
        instruction = 0b000_0_00_000_00_11_0_00;
        imm9 = imm9(instruction);
        pass = pass & (imm9 == 0b000000110);
        instruction = 0b000_0_00_000_00_00_1_00;
        imm9 = imm9(instruction);
        pass = pass & (imm9 == 0b000100000);
        return pass;
    }

    private int imm9(int instruction) {
        return ((instruction >> 2) & (0x3 << 1)) | ((instruction >> 7) & (0x3 << 3)) | ((instruction << 3) & (1 << 5)) | ((instruction << 1) & (0x3 << 6)) | ((instruction >> 4) & (1 << 8));
    }

    /**
     * c.ld
     * 15-13	12-10	9-7	6-5	4-2	1-0
     * 011	uimm[5:3]	rs1’	uimm[7:6]	rd’	00
     *
     * @return
     */
    private boolean test_imm8() {
        boolean pass = true;
        int instruction = 0b000_111_000_11_000_00;
        int imm8 = imm8(instruction);
        pass = pass & (imm8 == 0b11111000);
        instruction = 0b000_111_000_00_000_00;
        imm8 = imm8(instruction);
        pass = pass & (imm8 == 0b00111000);
        instruction = 0b000_000_000_11_000_00;
        imm8 = imm8(instruction);
        pass = pass & (imm8 == 0b11000000);
        return pass;
    }

    /**
     * c.lwsp
     * 15-13	12	11-7	6-2	1-0
     * 010	uimm[5]	rd	uimm[4:2|7:6]	10
     *
     * @return
     */
    private boolean test_lwspimm8() {
        boolean pass = true;
        int instruction = 0b000_1_00000_111_11_00;
        int imm8 = lwspimm8(instruction);
        pass = pass & (imm8 == 0b11111100);
        instruction = 0b000_1_00000_000_00_00;
        imm8 = lwspimm8(instruction);
        pass = pass & (imm8 == 0b00100000);
        instruction = 0b000_0_00000_111_00_00;
        imm8 = lwspimm8(instruction);
        pass = pass & (imm8 == 0b00011100);
        instruction = 0b000_0_00000_000_11_00;
        imm8 = lwspimm8(instruction);
        pass = pass & (imm8 == 0b11000000);
        return pass;
    }

    /**
     * c.swsp
     * 15-13	12-7	6-2	1-0
     * 110	uimm[5:2|7:6]	rs2	10
     *
     * @return
     */
    private boolean test_swspimm8() {
        boolean pass = true;
        int instruction = 0b000_1111_11_00000_00;
        int imm8 = swspimm8(instruction);
        pass = pass & (imm8 == 0b11111100);
        instruction = 0b000_1111_00_00000_00;
        imm8 = swspimm8(instruction);
        pass = pass & (imm8 == 0b00111100);
        instruction = 0b000_0000_11_00000_00;
        imm8 = swspimm8(instruction);
        pass = pass & (imm8 == 0b11000000);
        return pass;
    }

    private int imm8(int instruction) {
        return ((instruction >> 7) & (0x7 << 3)) | ((instruction << 1) & (0x3 << 6));
    }

    private int lwspimm8(int instruction) {
        return ((instruction >> 2) & (0x7 << 2)) | ((instruction >> 7) & (1 << 5)) | ((instruction << 4) & (0x3 << 6));
    }

    private int swspimm8(int instruction) {
        return ((instruction >> 7) & (0xf << 2)) | ((instruction >> 1) & (0x3 << 6));
    }


    /**
     * 15-13	12	11-7	6-2	1-0
     * 011	imm[17]	rd	imm[16:12]	01
     *
     * @return
     */
    private boolean test_imm18() {
        boolean pass = true;
        int instruction = 0b000_1_00000_11111_00;
        int imm18 = imm18(instruction);
        pass = pass & (imm18 == 0b11_1111_0000_0000_0000);
        instruction = 0b000_1_00000_00000_00;
        imm18 = imm18(instruction);
        pass = pass & (imm18 == 0b10_0000_0000_0000_0000);
        instruction = 0b000_0_00000_11111_00;
        imm18 = imm18(instruction);
        pass = pass & (imm18 == 0b01_1111_0000_0000_0000);
        return pass;
    }

    private int imm18(int instruction) {
        return ((instruction << 5) & (1 << 17)) | ((instruction << 10) & (0x1f << 12));
    }

    /**
     * 12-2
     * imm[11|4|9:8|10|6|7|3:1|5]
     *
     * @return
     */
    private boolean test_uimm12() {
        boolean pass = true;
        int instruction = 0b000_11111111111_00;
        int uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b111111111110);

        instruction = 0b000_1_0_00_0_0_0_000_0_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b1000_0000_0000);
        instruction = 0b000_0_1_00_0_0_0_000_0_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b0000_0001_0000);
        instruction = 0b000_0_0_11_0_0_0_000_0_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b0011_0000_0000);
        instruction = 0b000_0_0_00_1_0_0_000_0_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b0100_0000_0000);
        instruction = 0b000_0_0_00_0_1_0_000_0_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b0000_0100_0000);
        instruction = 0b000_0_0_00_0_0_1_000_0_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b0000_1000_0000);
        instruction = 0b000_0_0_00_0_0_0_111_0_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b0000_0000_1110);
        instruction = 0b000_0_0_00_0_0_0_000_1_00;
        uimm_12 = uimm_12(instruction);
        pass = pass & (uimm_12 == 0b0000_0010_0000);
        return pass;
    }

    private int uimm_12(int instruction) {
        return ((instruction >> 2) & (0x7 << 1)) | ((instruction >> 7) & (1 << 4)) | ((instruction << 3) & (1 << 5)) | ((instruction >> 1) & (0x2d << 6)) | ((instruction << 1) & (1 << 7)) | ((instruction << 2) & (1 << 10));
    }

    private boolean test_sign_extend() {
        boolean pass = true;
        short imm = -4034;
        int imm1 = imm & 0xFFFF;
        int sext_nzimm_6 = signed_extend(imm1 & 0xFFFF, 16);
        pass = pass & (sext_nzimm_6 == -4034);
        return pass;
    }


    /**
     * 有符号扩展
     * 15-13	12	11-7	6-2	1-0
     * 000	nzimm[5]	rs1/rd!=0	nzimm[4:0]	01
     *
     * @return
     */
    private boolean test_nzimm_6() {
        boolean pass = true;
        int instruction = 0b000_1_00000_11111_00;
        int nzimm_6 = nzimm_6(instruction);
        pass = pass & (nzimm_6 == 0b111111);

        instruction = 0b000_1_00000_00000_00;
        nzimm_6 = nzimm_6(instruction);
        pass = pass & (nzimm_6 == 0b100000);
        instruction = 0b000_0_00000_11111_00;
        nzimm_6 = nzimm_6(instruction);
        pass = pass & (nzimm_6 == 0b011111);


        return pass;
    }

    private int nzimm_6(int instruction) {
        return ((instruction >> 2) & 0x1f) | ((instruction >> 7) & (1 << 5));
    }


    /**
     * 15-13	12-10	 9-7	 6-5	 4-2  1-0
     * 010	  uimm[5:3]	 rs1’  uimm[2|6]  rd’  00
     */

    private boolean test_uimm_7() {
        boolean pass = true;
        int instruction = 0b000_11100011_000_00;
        int uimm_7 = uimm_7(instruction);
        pass = pass & (uimm_7 == 0b1111100);

        instruction = 0b000_11100000_000_00;
        uimm_7 = uimm_7(instruction);
        pass = pass & (uimm_7 == 0b0111000);

        instruction = 0b000_00000011_000_00;
        uimm_7 = uimm_7(instruction);
        pass = pass & (uimm_7 == 0b1000100);
        return pass;
    }

    /**
     * nzuimm[5:4|9:6|2|3]
     */
    private boolean test_nzuimm_10() {
        boolean pass = true;
        int instruction = 0b000_11111111_000_00;

        int nzuimm_10 = nzuimm_10(instruction);
        pass = pass & (nzuimm_10 == 0b1111111100);
        instruction = 0b000_11000000_000_00;
        nzuimm_10 = nzuimm_10(instruction);
        pass = pass & (nzuimm_10 == 0b110000);
        instruction = 0b000_00111100_000_00;
        nzuimm_10 = nzuimm_10(instruction);
        pass = pass & (nzuimm_10 == 0b1111000000);
        instruction = 0b000_00000010_000_00;
        nzuimm_10 = nzuimm_10(instruction);
        pass = pass & (nzuimm_10 == 0b0000000100);
        instruction = 0b000_00000001_000_00;
        nzuimm_10 = nzuimm_10(instruction);
        pass = pass & (nzuimm_10 == 0b0000001000);
        return pass;
    }

    private int uimm_7(int instruction) {
        return ((instruction >> 4) & (1 << 2)) | ((instruction >> 7) & (0x7 << 3)) | ((instruction << 1) & (1 << 6));
    }

    private int nzuimm_10(int instruction) {
        return ((instruction >> 4) & (1 << 2)) | ((instruction >> 2) & (1 << 3)) | ((instruction >> 7) & (0b11 << 4)) | ((instruction >> 1) & (0b1111 << 6));
    }

    private int signed_extend(int a, int size) {
//        return (a & (1 << (size - 1))) == 1 ? (a | ~((1 << size) - 1)) : a;
        return (a >> (size - 1) & 1) == 1 ? (a | ~((1 << size) - 1)) : a;
    }
}
