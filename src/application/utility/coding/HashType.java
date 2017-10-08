package application.utility.coding;

public enum HashType {
	PLAIN("Hash..."), SHA_384("SHA-384"), SHA_224("SHA-224"), SHA_256("SHA-256"),
	MD2("MD2"), SHA("SHA"), SHA_512("SHA-512"), MD5("MD5");

	private String algorithm;

	HashType(String algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public String toString() {
		return algorithm;
	}
}
