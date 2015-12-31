package tomac.myproject.rsa;

import java.math.BigInteger;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tomac.myproject.rsa.Rsa.KeyPair.Key;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class Rsa {

	private static final Pattern REGEX  = Pattern.compile("\\((.*?)\\)");

	public static class KeyPair {

		public static class Key {

			private final BigInteger exponent;
			private final BigInteger modulo;

			public Key(BigInteger exponent, BigInteger modulo) {
				this.exponent = exponent;
				this.modulo = modulo;
			}

			public BigInteger getExponent() {
				return exponent;
			}

			public BigInteger getModulo() {
				return modulo;
			}

			@Override
			public String toString() {
				return "Key{" + "exponent=" + exponent + ", modulo=" + modulo + '}';
			}
		}

		private static final Random RANDOM = new Random();

		private final Key publicKey;
		private final Key privateKey;

		public KeyPair(Key publicKey, Key privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}

		public Key getPublicKey() {
			return publicKey;
		}

		public Key getPrivateKey() {
			return privateKey;
		}

		@Override
		public String toString() {
			return "KeyPair{" + "publicKey=" + publicKey + ", privateKey=" + privateKey + '}';
		}

		public static KeyPair generatePair() {
			BigInteger p = BigInteger.probablePrime(60, RANDOM);
			BigInteger q = BigInteger.probablePrime(100, RANDOM);
			BigInteger n = p.multiply(q);
			BigInteger totient = p.subtract(BigInteger.ONE)
					.multiply(q.subtract(BigInteger.ONE));
			BigInteger e;
			while (true) {
				e = BigInteger.probablePrime(totient.bitLength(), RANDOM);
				if (!totient.mod(e).equals(BigInteger.ZERO)) {
					break;
				}
			}
			BigInteger d = e.modInverse(totient);
			Key publicKey = new Key(e, n);
			Key privateKey = new Key(d, n);
			return new KeyPair(publicKey, privateKey);
		}

	}

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
		KeyPair keyPairA = KeyPair.generatePair();
		KeyPair keyPairB = KeyPair.generatePair();
		String textOriginal = "Antonio Tomac čašađž";
		String encryptedA = Rsa.encrypt(textOriginal, keyPairA.getPublicKey());
		String encryptedAB = Rsa.encrypt(encryptedA, keyPairB.getPrivateKey());
		String decryptedA = Rsa.decrypt(encryptedAB, keyPairB.getPublicKey());
		String decrypted = Rsa.decrypt(decryptedA, keyPairA.getPrivateKey());
		System.out.println(textOriginal);
		System.out.println(decrypted);
	}

}
