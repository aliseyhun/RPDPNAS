package nl.uva.creed.invasionfinder;

import java.util.ArrayList;

import org.apache.commons.math.linear.RealMatrixImpl;

public class ArrayListStringBuffer {
	
	private ArrayList<String[]> arreglo = new ArrayList<String[]>();
	private int columnas = 0;

	public RealMatrixImpl process() {
		RealMatrixImpl matrix = new RealMatrixImpl(arreglo.size(), columnas);
		for (int i = 0; i < arreglo.size(); i++) {
			String[] columna = arreglo.get(i);
			for (int j = 0; j < columna.length; j++) {
				try {
					matrix.getDataRef()[i][j] = Double.parseDouble(columna[j]);
				} catch (NumberFormatException e) {
					if (columna[j].equalsIgnoreCase("true")) {
						matrix.getDataRef()[i][j] = 1;
					}else if (columna[j].equalsIgnoreCase("false")){
						matrix.getDataRef()[i][j] = 0;
					}else{
						throw new RuntimeException("Non numeric data " +  columna[j]);
					}	
				}	
				
					
			}
			
		}
	return matrix;
	}
	
	public void clear(){
		arreglo.clear();
		columnas = 0;
	}
	
	public int size(){
		return this.arreglo.size();
	}

	

	public void add(String[] nextLine) {
		if (arreglo.isEmpty()) {
			columnas = nextLine.length;
		}else{
			if (nextLine.length != columnas) {
				throw new RuntimeException("String[] added to the buffer must retain same size");
			}
		}
		arreglo.add(nextLine);
	}
	
	

}
