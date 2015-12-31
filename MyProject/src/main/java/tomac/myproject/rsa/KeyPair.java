package tomac.myproject.rsa;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class KeyPair {
	
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
		BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
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
