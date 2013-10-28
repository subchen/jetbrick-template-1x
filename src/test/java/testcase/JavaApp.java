package testcase;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class JavaApp {

    public static void main(String[] args) {
        //rename();
        byte b = 0;
        char c = 'a';
        int i = 1;
        long l = 100000000000000000L;
        float f = 1.2345f;
        double d = 123.345e10;

        System.out.println(new File(System.getProperty("java.io.tmpdir"), "jetx").getPath());
        printClass(f++);
        printClass(f >= b);
        printClass(l >> l);
        printClass(b << i);

        int[] a = new int[0];
        for (int x : a) {
            d = c ^ c;
        }
        String x = 1 > 2 ? "a" : 2 > 1 ? "b" : "c";
        System.out.println(x);
        Arrays.asList(1, 2, "3");
        i++;
        int xxx = ~1;
        Long llll = 1L;

        boolean bb = 0xFF == 255L;
        System.out.println(bb);

        Object as = new int[] { 1, 2, new Integer(2) };
        List<Object> asList = Arrays.asList((Object[]) as);
        System.out.println(asList);

        for (int n = 0; n < 1; n++) {
            int xxxx = 1;
            for (int j = 0; j < 1; j++) {
            }
            for (int j = 0; j < 1; j++) {
            }
        }
    }

    static void rename() {
        File dir = new File("C:\\jetbrick-template\\src\\test\\resources\\template");
        for (File file : dir.listFiles()) {
            String name = file.getName();
            if (name.endsWith(".jetm")) {
                name = name.replaceAll("jetm", "jetx");
                file.renameTo(new File(dir, name));
            }
        }
    }

    static void printClass(boolean b) {
        System.out.println("boolean: " + b);
    }

    static void printClass(byte b) {
        System.out.println("byte: " + b);
    }

    static void printClass(char b) {
        System.out.println("char: " + b);
    }

    static void printClass(int b) {
        System.out.println("int: " + b);
    }

    static void printClass(long b) {
        System.out.println("long: " + b);
    }

    static void printClass(float b) {
        System.out.println("float: " + b);
    }

    static void printClass(double b) {
        System.out.println("double: " + b);
    }
}
