package mff.cuni.diskette;


public class Diskette {
	
	public static int size = 144;
	private int cluttered = 0;
	
	public boolean add(int value) {
		
		int freeSpace = size - cluttered;
		if (freeSpace > value) {
			cluttered += value;
			return true;
		}
		
		return false;
	}
	
	public int getFreeSpace() {
		return this.size - this.cluttered;
	}
}
