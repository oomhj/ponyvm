public class Test {

    public static void main(String[] args) {
        int i = 0x7FFFFFFF;
        System.out.println(String.valueOf(i));
        System.out.println(Integer.toUnsignedString(i, 16));
        System.out.println(Integer.toHexString(i));
        System.out.println(Integer.toUnsignedString(i));
        System.out.println(Integer.toString(i, 16));
        i++;
        System.out.println(String.valueOf(i));
        System.out.println(Integer.toUnsignedString(i, 16));
        System.out.println(Integer.toHexString(i));
        System.out.println(Integer.toUnsignedString(i));
        System.out.println(Integer.toString(i, 16));
    }
}
