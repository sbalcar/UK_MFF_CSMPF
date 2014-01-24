package mff.cuni.monteCarlo;

import java.util.ArrayList;
import java.util.Random;

import mff.cuni.config.ConstantsMonteCarlo;


public class DiskettesModel {
	
	private ArrayList <Diskette> diskettes = new ArrayList<Diskette>();

	public DiskettesModel() {		
	}

	public DiskettesModel(ArrayList <Integer> files) {
		
		for (int fileSize : files) {
			insert(fileSize);
		}
	}

	public void generateFiles(int count) {

		Random generator = new Random();

		for (int i = 0; i < count; i++) {

			int fileSize = (generator.nextInt(
					ConstantsMonteCarlo.maxSizeOfFiles) +1);
			insert(fileSize);
		}
	}
	
	private boolean insert(int fileSize) {
		
		if (fileSize > Diskette.size) {
			return false;
		}
		
		if (diskettes.size() == 0) {
			diskettes.add(new Diskette());
		}

		Diskette actualDiskette = diskettes.get(diskettes.size() -1);
		if (actualDiskette.add(fileSize)) {
		   return true;
		}

		Diskette nextDiskette = new Diskette();
		nextDiskette.add(fileSize);
		diskettes.add(nextDiskette);

		return true;
	}

	public int usedDiskettes() {
		return diskettes.size();
	}

	public ArrayList <Integer> getFiles() {
		
		ArrayList <Integer> files = new ArrayList<Integer>();
		
		for (Diskette disketteI : diskettes) {
			
			files.addAll( disketteI.getFiles() );
		}
		
		return files;
	}
		
	public double fitness() {
		
		int freeSpace = 0;
		
		for (int i = 0; i <= this.diskettes.size() -2; i++) {
			Diskette disketteI = this.diskettes.get(i);
			freeSpace += disketteI.getFreeSpace();
		}
		
		int totalSpace = (this.diskettes.size() -2) * Diskette.size;
		double freeSpaceRatio = (double)freeSpace / (double)totalSpace;
		
		return freeSpaceRatio;
	}
	
	public int getNumOfDiskettes() {
		return this.diskettes.size();
	}

	public void print() {
		
		System.out.println(" NumOfDiskettes: " + getNumOfDiskettes());
		for (Diskette disk : this.diskettes) {
			disk.print();
		}
	}
	
}
