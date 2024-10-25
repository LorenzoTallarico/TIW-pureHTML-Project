package it.polimi.tiw.utils;

import it.polimi.tiw.beans.File;
import java.util.ArrayList;

/**
 * @file TreeOrder.java
 * @brief This class manages the organization of files into a tree structure.
 * @class TreeOrder
 *
 * This utility class provides methods to arrange files in a hierarchical structure
 * based on their parent directory relationships.
 */
public class TreeOrder {
	
	/**
	 * @brief Organizes a list of files into a tree structure.
	 * @param lista an ArrayList of File objects to be organized.
	 * @return a File object representing the root of the organized tree.
	 *
	 * This method processes the input list of files, establishing parent-child relationships
	 * based on the directory IDs. Files with a parent directory ID of 0 are considered
	 * root elements and are added directly to the root of the tree. Other files are added
	 * as children to their respective parent directories.
	 */
	public static File getOrder(ArrayList<File> lista) {
		File root = new File();
		boolean find;
		for (int i = 0; i < lista.size(); i++) {
			find = false;
			if (lista.get(i).getIdDirPadre() == 0) {
				root.addFiglio(lista.get(i));
				find = true;
			}
			for (int j = 0; j < lista.size() && !find; j++) {
				if (lista.get(i).getIdDirPadre() == lista.get(j).getIdDocu()) {
					lista.get(j).addFiglio(lista.get(i));
					find = true;
				}
			}
		}
		return root;
	}
}
