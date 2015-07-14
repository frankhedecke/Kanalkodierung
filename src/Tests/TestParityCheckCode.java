package Tests;

import channels.PC_HardIterDecodeChannel;
import entity.BinaryWord;
import blockcode.BlockCode;
import blockcode.ParityCheckCode;

public class TestParityCheckCode {
	
	public static void main (String[] args) {
		BlockCode code = new ParityCheckCode(2);
		
		// test standards
		System.out.println( code.encode(new BinaryWord("00")) );
		System.out.println( code.encode(new BinaryWord("01")) );
		System.out.println( code.encode(new BinaryWord("10")) );
		System.out.println( code.encode(new BinaryWord("11")) );
		
		PC_HardIterDecodeChannel channel = new PC_HardIterDecodeChannel(code);
		
		channel.pushTestInput(0.9f);
		channel.pushTestInput(1.0f);
		channel.pushTestInput(2.0f);
		channel.pushTestInput(1.0f);
		channel.pushTestInput(0.5f);
		channel.pushTestInput(-0.9f);
		channel.pushTestInput(-0.1f);
		channel.pushTestInput(-2.5f);

		while(channel.hasOutput()) {
			int bit = channel.getOutput();
			System.out.print(bit + " ");
		}
	}
}
