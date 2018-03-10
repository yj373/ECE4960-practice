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
	public double[] getValue() {
		return value;
	}
	public void setValue(double[] val) {
		value= new double[val.length];
		for(int i=0; i<val.length; i++) {
			value[i]=val[i];
		}
		length= val.length;
		return;
	}
	public int getLength() {
		return length;
	}
	
	//Switch row[x] and row[y] for the vector
	public void rowPermute(int x, int y) {
		if(x<1||y<1||x>length||y>length) return;
		double temp= this.value[x-1];
		this.value[x-1]= this.value[y-1];
		this.value[y-1]= temp;
		return;
	}
	
	//Add a*row[x] to row[y] for the vector
	public void rowScale(int x, int y, double a) {
		double val= value[x]*a+value[y];
		value[y]= val;
		return;
	}
	//Compute the second norm with the other vector
	public double computeSecondNorm(Vector v2) {
		if(this.length!= v2.getLength()) return -1;
		double[] val1= this.getValue();
		double[] val2= v2.getValue();
		double norm=0;
		for(int i=0; i<this.length; i++) {
			norm=norm+Math.pow(val1[i]-val2[i], 2);
		}
		return Math.sqrt(norm);
	}
	//Return the sum of two vectors v1+v2
	public Vector add(Vector v2) {
		if(this.length!= v2.getLength()) return new Vector();
		double[] val1= this.getValue();
		double[] val2= v2.getValue();
		double[] val= new double[val1.length];
		for(int i=0; i<val.length; i++) {
			val[i]= val1[i]+val2[i];
		}
		return new Vector(val);
	}
	

}
