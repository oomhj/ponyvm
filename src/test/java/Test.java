public class Test {

    public static void main(String[] args) {
        int in = 0x7F6E5D4C;
        byte[] b = new byte[4];
        int low = in & 0xffff;
        int high = in >> 16;

        System.out.println(b);
    }
}
