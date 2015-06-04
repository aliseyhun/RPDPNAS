package nl.uva.creed.invasionfinder;

import java.util.ArrayList;

import org.apache.commons.math.linear.RealMatrixImpl;

public class GameMatrixManipulation {

	public static  RealMatrixImpl takeOutStronglyDominatedStrategies(RealMatrixImpl matrix){
		if (matrix.getColumnDimension() != matrix.getRowDimension()) {
			throw new IllegalArgumentException("Matrix should be square");
		}
		RealMatrixImpl copy = new RealMatrixImpl(100,100); 
		while (!copy.equals(matrix)) {
			copy = (RealMatrixImpl) matrix.copy();
			matrix = eliminateOneRow(matrix);	
		}
		return matrix;
	}
	
	protected static RealMatrixImpl eliminateOneRow(RealMatrixImpl matrix){
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getRowDimension(); j++) {
				if (i ==j)break;
				if (doesIStronglyDominatesJ(i,j, matrix)){
					matrix = removeJRowAndColumn(matrix, j);
					return matrix;
				}
				
			}	
		}
		return matrix;
	}

	protected static  RealMatrixImpl removeJRowAndColumn(RealMatrixImpl matrix, int j) {
		ArrayList<Integer> included = new ArrayList<Integer>();
		for (int i = 0; i < matrix.getColumnDimension(); i++) {
			if (i !=j) {
				included.add(i);
			}
		}
		int includedArray[] = new int[matrix.getColumnDimension() -1];
		for (int i = 0; i < includedArray.length; i++) {
			includedArray[i] = included.get(i);
		}
		return (RealMatrixImpl) matrix.getSubMatrix(includedArray, includedArray);
	}

	protected static  boolean doesIStronglyDominatesJ(int i, int j, RealMatrixImpl matrix) {
		double[] i_row = matrix.getRow(i);
		double[] j_row = matrix.getRow(j);
		for (int k = 0; k < i_row.length; k++) {
			if (i_row[k]<j_row[k]) {
				return false;
			}
		}
		return true;
	}

	public static RealMatrixImpl reduceOneEqualRow(RealMatrixImpl matrix) {
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getRowDimension(); j++) {
				if (i ==j)break;
				if (equalRows(i,j, matrix)){
					matrix = removeJRowAndColumn(matrix, j);
					return matrix;
				}
				
			}	
		}
		return matrix;
	}
	
	private static boolean equalRows(int i, int j, RealMatrixImpl matrix) {
		double[] vector_i = matrix.getRow(i);
		double[] vector_j = matrix.getRow(j);
		if (vector_i.length != vector_j.length) {
			return false;
		}
		for (int k = 0; k < vector_j.length; k++) {
			if (vector_j[k] != vector_i[k]) {
				return false;
			}
		}
		return true;
	}

	public static  RealMatrixImpl takeOutEqualRows(RealMatrixImpl matrix){
		if (matrix.getColumnDimension() != matrix.getRowDimension()) {
			throw new IllegalArgumentException("Matrix should be square");
		}
		RealMatrixImpl copy = new RealMatrixImpl(100,100); 
		while (!copy.equals(matrix)) {
			copy = (RealMatrixImpl) matrix.copy();
			matrix = reduceOneEqualRow(matrix);	
		}
		return matrix;
	}
	
	
	
}
