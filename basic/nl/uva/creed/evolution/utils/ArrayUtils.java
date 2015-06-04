package nl.uva.creed.evolution.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ArrayUtils {
	
	public static void printoFile(List list, String fileName ){
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
	    for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			Object object =  iterator.next();
		    out.write(object.toString() + "\n");   
		}
		out.close();
	    
		} catch (IOException e) {
	    System.out.println("Exception " + e);
		}
	    
	}

}
