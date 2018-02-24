package homework_4;

import java.util.ArrayList;

public class FullMatrix {
	double[][] matrix;
	int rowNum;
	int colNum;
	public FullMatrix(double[][]m) {
		matrix=m.clone();
		rowNum= m.length;
		colNum= m[0].length;
	}
	//Switch row[x] and row[y] for the full matrix
	public void rowPermute(int x, int y) {
		if(x<1||y<1||x>rowNum||y>rowNum) return;
		for(int i=0; i<colNum; i++) {
			double temp= matrix[x-1][i];
			matrix[x-1][i]= matrix[y-1][i];
			matrix[y-1][i]= temp;
		}
		return;
	}
	//Add a*row[x] to row[y] for the full matrix
	public void rowScale(int x, int y, double a) {
		if(x<1||y<1||x>rowNum||y>rowNum) return;
		for(int i=0; i<colNum; i++) {
			matrix[y-1][i]= a*matrix[x-1][i]+matrix[y-1][i];
		}
		return;
	}
	//Return the product of Ax=b
	public Vector productAx(Vector x) {
		int len= x.length;
		ArrayList<Double> v= new ArrayList<Double>();
		if(len!=colNum) return new Vector();
		for(int i=0;i<rowNum; i++) {
			double val=0;
			for(int j=0; j<colNum; j++) {
				val=val+matrix[i][j]*x.value[j];
			}
			v.add(val);
		}
		Vector res= new Vector(v);
		return res;
	}
	//retrieve element
	public double retrieveElement(int x, int y) {
		return matrix[x-1][y-1];
	}
}
