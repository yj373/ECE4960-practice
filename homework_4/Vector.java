package homework_4;

import java.util.ArrayList;

public class Vector {
	double[] value;
	int length;
	public Vector(double[] v) {
		value= v.clone();
		length=v.length;
	}
	public Vector(ArrayList<Double> v) {
		value= new double[v.size()];
		for(int i=0; i<value.length; i++) {
			value[i]= v.get(i);
		}
		length= value.length;
	}
	public Vector() {
		
	}
	
	//Switch row[x] and row[y] for the vector
	public void rowPermute(int x, int y) {
		if(x<1||y<1||x>=length||y>=length) return;
		double temp= value[x];
		value[x]= value[y];
		value[y]= temp;
		return;
	}
	
	//Add a*row[x] to row[y] for the vector
	public void rowScale(int x, int y, double a) {
		double val= value[x]*a+value[y];
		value[y]= val;
		return;
	}

}
