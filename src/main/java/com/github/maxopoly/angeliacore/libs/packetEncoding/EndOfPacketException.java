package com.github.maxopoly.angeliacore.libs.packetEncoding;

public class EndOfPacketException extends Exception {

	private static final long serialVersionUID = 8034583615230325207L;

	public EndOfPacketException() {
		super();
	}

	public EndOfPacketException(String msg) {
		super(msg);
	}

}
