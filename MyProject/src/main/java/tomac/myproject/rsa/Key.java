package tomac.myproject.rsa;

import java.math.BigInteger;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class Key {
	
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
