package mff.cuni.algorithm.monteCarlo;

import java.util.ArrayList;
import java.util.Random;

import mff.cuni.config.ConstantsMonteCarlo;
import mff.cuni.diskette.Diskette;
import mff.cuni.diskette.DiskettesModel;

public class Generator {

	public static DiskettesModel generateFiles(int count) {
		
		Random generator = new Random();
		
		ArrayList<Integer> files = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			int fileSize = generator.nextInt(ConstantsMonteCarlo.maxSizeOfFiles);
			files.add(fileSize);
		}
		
		DiskettesModel dm = new DiskettesModel();
		dm.decode(files);
		
		return dm;
	}
}
