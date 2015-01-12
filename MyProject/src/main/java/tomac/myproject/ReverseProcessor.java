
package tomac.myproject;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ReverseProcessor implements Processor {

    @Override
    public String process(String text) {
        StringBuilder sb = new StringBuilder();
        App.printBytes(text.getBytes());
        for (int i = text.length()-1; i >= 0; i--) {
            System.out.print((int)text.charAt(i)+" ");
            sb.append(text.charAt(i));
        }
        System.out.println();
        return sb.toString();
        
    }
    
}
