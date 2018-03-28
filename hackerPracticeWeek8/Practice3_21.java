package hackerPracticeWeek8;
import java.util.ArrayList;
import A2.Iterative_solver;

import homework_4.*;

public class Practice3_21 {

	public static void main(String[] args) {
		// The first hacker practice of Note 6
		//Quasi Newton to extract parameters for power-law function
		//initial point ()
		System.out.println("The first hacker practice of Note 6:******************************************************************");
		double[] am_v= {170,1};
		Vector am= new Vector(am_v);
		double[] x_v= {1.0, 4.5, 9.0, 20, 74, 181};
		double[] y_v= {3.0, 49.4, 245, 1808, 22000, 73000};
		Vector x= new Vector(x_v);
		Vector y= new Vector(y_v);
		newtonPowerLaw(x, y, am);
		
		// The second hacker practice of Note 6
		//Construct a B-Spline curve
		System.out.println("The second hacker practice of Note 6:************************************************************");
		double[] x_v2= {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
		double[] y_v2= {0.0, 1.0, 0.0, -1.0, 0.0, 1.0};
		Vector x2= new Vector(x_v2);
		Vector y2= new Vector(y_v2);
		SparseMatrix coeffMatrix= getCoefficientsMatrix(x2, y2);
		Vector coeffVector= getCoefficientsVector(x2, y2);
		Iterative_solver is= new Iterative_solver(coeffMatrix, coeffVector);
		Vector k= is.iterate();
		for(int i=0; i<k.getLength(); i++) {
			System.out.println("k"+i+"= "+k.getValue()[i]);
		}
		

	}
	//Module1: Compute the objective functions
	//f1=0; f2=0
	public static Vector fx(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return new Vector();
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		double f1=0;
		double f2=0;
		for(int i=0; i<x.getLength(); i++) {
			f1=f1+(Math.pow(x_v[i], 2*m)*a-Math.pow(x_v[i], m)*y_v[i]);
			f2=f2+(Math.log(x_v[i])*Math.pow(x_v[i], 2*m)*a*a-Math.log(x_v[i])*Math.pow(x_v[i], m)*y_v[i]*a);
		}
		double[] value= {f1,f2};
		return new Vector(value);
	}
	//Module2: Compute delta_para
	public static Vector computeDeltaPara(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return new Vector();
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		double h11=0;
		for(int i=0; i<x.getLength(); i++) {
			h11+=Math.pow(x_v[i], 2*m);
		}
		double h21=0;
		for(int i=0; i<x.getLength(); i++) {
			h21+=Math.log(x_v[i])*Math.pow(x_v[i], 2*m)*2*a-Math.log(x_v[i])*Math.pow(x_v[i], m)*y_v[i];
		}
		double[] amPerturb_v= {a, 1.5*m};
		Vector amPerturb= new Vector(amPerturb_v);
		Vector vxPerturb= fx(x, y, amPerturb);
		double f1_P=vxPerturb.getValue()[0];
		double f2_P=vxPerturb.getValue()[1];
		Vector vx0= fx(x, y, am);
		double f1=vx0.getValue()[0];
		double f2=vx0.getValue()[1];
		double h12= (f1_P-f1)/(amPerturb.getValue()[1]-am.getValue()[1]);
		double h22= (f2_P-f2)/(amPerturb.getValue()[1]-am.getValue()[1]);
		double determinant= h11*h22-h12*h21;
		double delta_a= -(h22*f1+(-h12)*f2)/determinant;
		double delta_m= -((-h21)*f1+h11*f2)/determinant;
		double[] deltaPara_v= {delta_a, delta_m};
		return new Vector(deltaPara_v);

	}
	//Module3: Compute Loss function
	public static double vx(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return 0;
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		double res=0;
		for(int i=0; i<x.getLength(); i++) {
			double ele= a*Math.pow(x_v[i], m)-y_v[i];
			res= res+Math.pow(ele, 2);
		}
		return res;
	}
	//Module4:Quasi-Newton+line search
	public static void newtonPowerLaw(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return;
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		Vector delta_para= computeDeltaPara(x, y, am);
		double norm0= computeNorm(x,y,am,0,delta_para);
		double t=1;
		double norm1= computeNorm(x,y,am,t,delta_para);
		double norm2= computeNorm(x,y,am,t/2,delta_para);
		while(norm1>norm2&&norm1>Math.pow(10, -7)) {
			t=t/2;
			norm1=norm2;
			norm2=computeNorm(x,y,am,t/2,delta_para);
		}
		delta_para= delta_para.scaling(t);
		double deltaNorm= delta_para.computeSecondNorm();
		System.out.println("a= "+a+"\tm= "+m+"\tV(a,m)= "+norm0+ "\tThe second norm of delta_para: "+deltaNorm);
		am=am.add(delta_para);
		while(norm0>Math.pow(10, -7)&&deltaNorm>Math.pow(10, -7)) {
			a=am.getValue()[0];
			m=am.getValue()[1];
			delta_para= computeDeltaPara(x, y, am);
			norm0= computeNorm(x,y,am,0,delta_para);
			t=1;
			norm1= computeNorm(x,y,am,t,delta_para);
			norm2= computeNorm(x,y,am,t/2,delta_para);
			while(norm1>norm2&&norm1>Math.pow(10, -7)) {
				t=t/2;
				norm1=norm2;
				norm2=computeNorm(x,y,am,t/2,delta_para);
			}
			delta_para= delta_para.scaling(t);
			deltaNorm= delta_para.computeSecondNorm();
			System.out.println("a= "+a+"\tm= "+m+"\tV(a,m)= "+norm0+ "\tThe second norm of delta_para: "+deltaNorm);
			am=am.add(delta_para);
			
		}
	}
	//Module5: Compute norm
	public static double computeNorm(Vector x, Vector y, Vector am, double t, Vector delta_para) {
		Vector newAm= am.add(delta_para.scaling(t));
		return vx(x, y, newAm);
	}
	
	
	
	
	//Module1: Get the coefficient matrix 
	public static SparseMatrix getCoefficientsMatrix(Vector x, Vector y) {
		if(x.getLength()!=y.getLength()) return new SparseMatrix();
		ArrayList<Double> value= new ArrayList<Double>();
		ArrayList<Integer> rowPtr= new ArrayList<Integer>();
		ArrayList<Integer> colInd= new ArrayList<Integer>();
		int rowNum= x.getLength();
		int colNum= y.getLength();
		rowPtr.add(0);
		double[] x_v= x.getValue();
		for(int i=0; i<rowNum; i++) {
			if(i==0) {
				double a1= -4/(x_v[1]-x_v[0]);
				double a2= -2/(x_v[1]-x_v[0]);
				value.add(a1);
				value.add(a2);
				rowPtr.add(2);
				colInd.add(0);
				colInd.add(1);
				continue;
			}
			if(i==rowNum-1) {
				double a1= 4/(x_v[i]-x_v[i-1]);
				double a2= 2/(x_v[i]-x_v[i-1]);
				value.add(a1);
				value.add(a2);
				rowPtr.add(rowPtr.get(i)+2);
				colInd.add(i-1);
				colInd.add(i);
				continue;
			}
			double a1= 1/(x_v[i]-x_v[i-1]);
			double a2= 2*(1/(x_v[i]-x_v[i-1])+1/(x_v[i+1]-x_v[i]));
			double a3= 1/(x_v[i+1]-x_v[i]);
			value.add(a1);
			value.add(a2);
			value.add(a3);
			rowPtr.add(rowPtr.get(i)+3);
			colInd.add(i-1);
			colInd.add(i);
			colInd.add(i+1);
		}
		return new SparseMatrix(value, rowPtr, colInd, colNum);
	}
	
	//Get the coefficient vector
	public static Vector getCoefficientsVector(Vector x, Vector y) {
		if(x.getLength()!=y.getLength()) return new Vector();
		int len= x.getLength();
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		double[] value= new double[len];
		for(int i=0; i<len; i++) {
			if (i==0) {
				 value[i]= -6*(y_v[1]-y_v[0])/Math.pow((x_v[1]-x_v[0]), 2);
				 continue;
			}
			if (i==len-1) {
				value[i]= 6*(y_v[i]-y_v[i-1])/Math.pow((x_v[i]-x_v[i-1]), 2);
				continue;
			} else {
				double term1= (y_v[i]-y_v[i-1])/Math.pow((x_v[i]-x_v[i-1]), 2);
				double term2= (y_v[i+1]-y_v[i])/Math.pow((x_v[i+1]-x_v[i]), 2);
				value[i]= 3*(term1+term2);
			}
		}
		return new Vector(value);
	}
	

	

}
