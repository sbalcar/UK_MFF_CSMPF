package mff.cuni.diskette;

import java.util.ArrayList;
import java.util.Random;


public class DiskettesModel {
	
	private ArrayList <Integer> files = null;
	private ArrayList <Diskette> diskettes = null;
	private int numOfActualDiskette = -1;
	
	public DiskettesModel clone() {
		
		ArrayList <Integer> filesNew = new ArrayList<Integer>();
		for (int i = 0; i < this.files.size(); i++) {
			int valueI = this.files.get(i);
			filesNew.add(valueI);
		}

		DiskettesModel dm = new DiskettesModel();
		dm.decode(filesNew);
		
		return dm;
	}
	
	public void decode(ArrayList <Integer> files_) {

		this.files = files_;
		this.diskettes = new ArrayList<Diskette>();
		this.numOfActualDiskette = 0;
		
		for (int i = 0; i < this.files.size(); i++) {
			
			int fileSize = this.files.get(i);
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

		Diskette actualDiskette = diskettes.get(numOfActualDiskette);
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
		return this.files;
	}
	
	public void makeStep() {
		
		ArrayList <Integer> filesChanged = this.files;
		
		Random generator = new Random();
		int index1 = generator.nextInt(filesChanged.size());
		int index2 = generator.nextInt(filesChanged.size());
		
		int value1 = filesChanged.get(index1);
		int value2 = filesChanged.get(index2);
		
		filesChanged.set(index1, value2);
		filesChanged.set(index2, value1);
		
		decode(filesChanged);
	}
	
	public double fitness() {
		
		int freeSpace = 0;
		
		for (int i = 0; i <= this.diskettes.size() -2; i++) {
			Diskette disketteI = this.diskettes.get(i);
			freeSpace += disketteI.getFreeSpace();
		}
		
		int totalSpace = (this.diskettes.size() -2) * Diskette.size;
		return (double)freeSpace / (double)totalSpace;
	}
}
