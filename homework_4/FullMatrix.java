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
	public FullMatrix() {
		
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
	//Return the product of AA
	public FullMatrix productAB(FullMatrix b) {
		if(this.colNum!= b.getRowNum()) return new FullMatrix();
		double[][] resValue= new double[this.rowNum][b.colNum];
		for(int i1= 0; i1<resValue.length; i1++) {
			for(int i2= 0; i2<resValue[0].length; i2++) {
				double value=0;
				for(int i3=0; i3<this.colNum; i3++) {
					value= value+ this.getValue()[i1][i3]*b.getValue()[i3][i2];
				}
				resValue[i1][i2]= value;
			}
		}
		FullMatrix res= new FullMatrix(resValue);
		return res;
	}
	//retrieve element
	public double retrieveElement(int x, int y) {
		return matrix[x-1][y-1];
	}
	public double[][] getValue(){
		return matrix;
	}
	public int getRowNum() {
		return rowNum;
	}
	public int getColNum() {
		return colNum;
	}
	public void setValue(double[][] val) {
		matrix= val.clone();
		rowNum= matrix.length;
		colNum= matrix[0].length;
	}
}
