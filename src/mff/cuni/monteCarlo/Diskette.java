package mff.cuni.monteCarlo;

import java.util.ArrayList;

import mff.cuni.config.ConstantsMonteCarlo;


public class Diskette {
	
	public static int size = ConstantsMonteCarlo.DISKETTE_SIZE;
	private ArrayList <Integer> files = new ArrayList<Integer>();
	
	public Diskette() {
	}
	
	public Diskette(Diskette diskette) {
		
		ArrayList <Integer> filesOld = diskette.getFiles();
		
		for (int fileSize: filesOld) {
			this.add(fileSize);
		}

	}

	public boolean add(int value) {
		
		if (getFreeSpace() >= value) {
			files.add(value);
			return true;
		}
		
		return false;
	}

	public ArrayList <Integer> getFiles() {
		
		return this.files;
	}

	public int getFreeSpace() {
		
		return Diskette.size - this.getUsedSpace();
	}
	
	public int getUsedSpace() {
		
		int cluttered = 0; 
		for (int fileSize : files) {
			cluttered += fileSize;
		}
		
		return cluttered;
	}

	public void print() {
		
		System.out.println("Size: " + size);
		System.out.print("Files: [");
		for (int fileSize : files) {
			System.out.print(fileSize + ", ");
		}
		System.out.println("]");

	}
}
