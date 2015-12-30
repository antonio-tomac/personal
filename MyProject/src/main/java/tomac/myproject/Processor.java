
package tomac.myproject;

/**
 * This is a text processor
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public interface Processor {
	
	/**
	 * method processes given text
	 * @param text
	 * @return processed text
	 */
    String process(String text);
}
