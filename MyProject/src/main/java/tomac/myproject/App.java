package tomac.myproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void printBytes(byte[] array) {
        for (byte b : array) {
            System.out.print(((int)b)+" ");
        }
        System.out.println();
    }
    
    public static int normalize(int x) {
        if (x >= -128 && x < 128) {
            return x;
        } else if (x < -128) {
            while (x < -128) {
                x += 256;
            }
            return x;
        } else {
            while (x >= 128) {
                x -= 256;
            }
            return x;            
        }
    }

    public static void main(String[] args) {
        List<String> texts = new ArrayList<>();
        //texts.add("H");
        texts.add("He");
        //texts.add("Hello world");
        for (String text : texts) {
            System.out.println("=========================");
            String key = "password";
            List<Processor> processors = new ArrayList<>();
            processors.add(new EncryptProcessor(key));
            processors.add(new ReverseProcessor());
            processors.add(new DecryptProcessor(key));
            processors.add(new ReverseProcessor());

            printBytes(text.getBytes());
            System.out.println("Original: " + text +" ("+text.length()+"-"+text.toCharArray().length+")");
            String temp = text;
            for (Processor p : processors) {
                temp = p.process(temp);
                System.out.println(p.getClass().getSimpleName() + ": " + temp+" ("+temp.length()+"-"+temp.toCharArray().length+")");
            }
            if (temp.equals(text)) {
                System.out.println("OK");
            } else {
                System.out.println("FAIL");
            }
        }
        
        System.out.println("////////////////////////");
        System.out.println(normalize(12));
        System.out.println(normalize(-62));
        System.out.println(normalize(212));
        System.out.println(normalize(-312));
    }
}
