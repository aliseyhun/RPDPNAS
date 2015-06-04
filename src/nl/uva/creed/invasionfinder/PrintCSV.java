package nl.uva.creed.invasionfinder;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;


public class PrintCSV {

	public static String printNice(double[][] matrix){
		
		StringBuffer ans =  new StringBuffer(); 
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				
					ans.append(matrix[i][j] + "\t");
				
			}
			ans.append("\n");
		}
		return ans.toString();
	}

	public static String printNice(ArrayList<String> header) {
		StringBuffer ans = new StringBuffer();
		for (Iterator<String> iterator = header.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			ans.append(string+"\t");
		}
		return StringUtils.removeEnd(ans.toString(), "\t");
	}
	
	public static String printNice(double[] array) {
		StringBuffer ans = new StringBuffer();
		
		for (int i = 0; i < array.length; i++) {
			double numero  = array[i];
			ans.append(numero+"\t");
		}
		return StringUtils.removeEnd(ans.toString(), "\t");
	}
	
	
	public static String printNice(String[] array) {
		StringBuffer ans = new StringBuffer();
		
		for (int i = 0; i < array.length; i++) {
			String numero  = array[i];
			ans.append(numero+"\t");
		}
		return StringUtils.removeEnd(ans.toString(), "\t");
	}
	
	
	
	
	
}
