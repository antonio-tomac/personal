package tomac.myproject.rsa;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class Rsa {

	private static final Pattern REGEX  = Pattern.compile("\\((.*?)\\)");

	public static BigInteger applyModuloPower(BigInteger val, Key key) {
		return val.modPow(key.getExponent(), key.getModulo());
	}

	public static String encrypt(String text, Key key) {
		StringBuilder sb = new StringBuilder();
		int maxNumBytes = key.getModulo().bitLength() / 16;
		int numBytes = 0;
		BigInteger accumulated = BigInteger.ZERO;
		for (char c : text.toCharArray()) {
			BigInteger val = BigInteger.valueOf(c);
			accumulated = accumulated.shiftLeft(16).add(val);
			numBytes++;
			if (numBytes >= maxNumBytes) {
				BigInteger enc = Rsa.applyModuloPower(accumulated, key);
				sb.append("(").append(enc.toString()).append(")");
				numBytes = 0;
				accumulated = BigInteger.ZERO;
			}
		}
		if (numBytes > 0) {
			BigInteger enc = Rsa.applyModuloPower(accumulated, key);
			sb.append("(").append(enc.toString()).append(")");
		}
		return sb.toString();
	}

	public static String decrypt(String encrypted, Key key) {
		Matcher matcher = REGEX.matcher(encrypted);
		StringBuilder sb = new StringBuilder();
		BigInteger mod = BigInteger.valueOf(1 << 16);
		while (matcher.find()) {
			BigInteger enc = new BigInteger(matcher.group(1));
			BigInteger dec = Rsa.applyModuloPower(enc, key);
			String chunk = "";
			while (dec.compareTo(BigInteger.ZERO) == 1) {
				BigInteger last = dec.mod(mod);
				int charCode = last.intValueExact();
				char c = (char) charCode;
				chunk = c + chunk;
				dec = dec.divide(mod);
			}
			sb.append(chunk);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		KeyPair keyPair = KeyPair.generatePair();
		String textOriginal = "Antonio Tomac čašađž";
		String encrypted = Rsa.encrypt(textOriginal, keyPair.getPublicKey());
		String decrypted = Rsa.decrypt(encrypted, keyPair.getPrivateKey());
		System.out.println(textOriginal);
		System.out.println(encrypted);
		System.out.println(decrypted);
	}

}
