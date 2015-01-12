
package tomac.myproject;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class DecryptProcessor implements Processor {

    private final String key;
    
    public DecryptProcessor(String key) {
        this.key = key;
    }
    
    @Override
    public String process(String text) {
        StringBuilder sb = new StringBuilder();
        App.printBytes(text.getBytes());
        for (int i = 0, j = 0; i < text.length(); i++, j++) {
            j = j >= key.length() ? 0 : j;
            System.out.print((int)text.charAt(i)+" ["+(int) (App.normalize(text.charAt(i) + key.charAt(j))) + "] ");
            sb.append((char)(App.normalize(text.charAt(i)-key.charAt(j))));
        }
        System.out.println();
        return sb.toString();
    }
}
