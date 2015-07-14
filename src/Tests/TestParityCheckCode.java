package Tests;

import blockcode.BlockCode;
import blockcode.ParityCheckCode;
import channels.PC_IterDecodeChannel;
import entity.BinaryWord;
import main.Modulator;

public class TestParityCheckCode {
	
	public static void main (String[] args) {
		BlockCode code = new ParityCheckCode(2);
		
		// test standards
		System.out.println( code.encode(new BinaryWord("00")) );
		System.out.println( code.encode(new BinaryWord("01")) );
		System.out.println( code.encode(new BinaryWord("10")) );
		System.out.println( code.encode(new BinaryWord("11")) );
		
		PC_IterDecodeChannel channel = new PC_IterDecodeChannel(code);
		
		channel.pushInput(0.9f);
		channel.pushInput(1.0f);
		channel.pushInput(2.0f);
		channel.pushInput(1.0f);
		channel.pushInput(0.5f);
		channel.pushInput(-0.9f);
		channel.pushInput(-0.1f);
		channel.pushInput(-2.5f);

		while(channel.hasOutput()) {
			float bit = channel.getOutput();
			System.out.print(Modulator.softToHard(bit) + " ");
		}
	}
}
