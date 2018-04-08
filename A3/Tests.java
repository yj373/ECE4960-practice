package A3;

import homework_4.*;
//This class includes all the functions doing tests and validations
public class Tests {
	//Full matrix solver test
	public static double computeSecondNorm(Vector v1, Vector v2) {
		if(v1.getLength()!=v2.getLength()) return -1;
		double res=0;
		for(int i=0; i<v1.getLength(); i++) {
			res=res+Math.pow(v1.getValue()[i]-v2.getValue()[i], 2);
		}
		res= Math.sqrt(res);
		return res;
	}
	public static void matrixSolverTest() {
		FullMatrixSolver ms= new FullMatrixSolver();
		double[][] aValue= {{1,2,0,0,3}, {4,5,6,0,0}, {0,7,8,0,9}, {0,0,0,10,0}, {11,0,0,0,12}};
		FullMatrix a= new FullMatrix(aValue);
		double[] bValue= {5,4,3,2,1};
		Vector b= new Vector(bValue);
		Vector x= ms.solve(a, b);
		double norm= computeSecondNorm(a.productAx(x), b);
		if(norm<Math.pow(10, -7)) {
			System.out.println("Matrix solver test: pass!"+" The second norm is: "+norm);
		}else {
			System.out.println("Matrix solver test: fail!"+" The second norm is: "+norm);
			System.out.println();
		}
		
	}
	
	//Newton power law validation
	public static void powerLawTest() {
		System.out.println("Power law prgram validation:******************************************************************************");
		double[] am_v= {20,-1};
		Vector am= new Vector(am_v);
		double[] x_v= {1.0, 4.5, 9.0, 20, 31, 36.5, 55, 71, 82.2, 100};
		double[] y_v= {10.1, 4.71, 3.33, 2.24, 1.80, 1.66, 1.35, 1.20, 1.09, 1};
		Vector x= new Vector(x_v);
		Vector y= new Vector(y_v);
		NewtonPowerLaw pl= new NewtonPowerLaw();
		am= pl.newtonPowerLaw(x, y, am);
		double[] v= am.getValue();
		System.out.println("The results of the power law parameter extraction: "+"a= "+v[0]+"\tm= "+v[1]);
		for(int i=0; i<x_v.length; i++) {
			x_v[i]= Math.log(x_v[i]);
			y_v[i]= Math.log(y_v[i]);
		}
		Vector x2= new Vector(x_v);
		Vector y2= new Vector(y_v);
		Vector ab= linearRegression(x2, y2);
		double[] ab_v= ab.getValue();
		double a= Math.exp(ab_v[1]);
		double m= ab_v[0];
		double[] nam_v= {a, m};
		Vector nam= new Vector(nam_v);
		System.out.println("The results of linear regression: "+"a= "+a+"\tm= "+m+"\tv= "+pl.vx(x, y, nam));
		
	}
	
	public static Vector linearRegression(Vector x, Vector y) {
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		if(x.getLength()!=y.getLength()) return new Vector();
		double len= x.getLength();
		double xAve= 0;
		double yAve= 0;
		
		for(int i=0; i<len; i++) {
			xAve+=x_v[i];
			yAve+=y_v[i];
		}
		xAve= xAve/(len+1);
		yAve= yAve/(len+1);
		double a=0;
		double b=0;
		double aNumerator=0;
		double aDenominator=0;
		for(int i=0; i<len; i++) {
			aNumerator= aNumerator+(x_v[i]-xAve)*(y_v[i]-yAve);
			aDenominator= aDenominator+(x_v[i]-xAve)*(x_v[i]-xAve);
		}
		a= aNumerator/aDenominator;
		b= yAve-xAve*a;
		double[] ab_v= {a, b};
		return new Vector(ab_v);
		
	}
	
	public void test() {
		matrixSolverTest();
		powerLawTest();
	}
	
	//Task 7
	//validation 1: For Vgs<Vth, Id_model should be an exponential function of Vgs with k<1 and nearly insensitive to Vds 
	public void validation1(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		EKV_Model ekv= new EKV_Model();
		Vector para= ekv.newtonEKV(k, Vgs, Vth, Vds, Is, Id_measure);
		double Is_model= para.getValue()[0];
		double k_model= para.getValue()[1];
		double Vth_model= para.getValue()[2];
		Vector Id_model= ekv.computeId_model(k_model, Vgs, Vth_model, Vds, Is_model);
		double[] Id_model_v= Id_model.getValue();
		double norm=0;
		for(int i=0; i<Vgs_v.length; i++) {
			if(Vgs_v[i]<Vth_model) {
				double app= approximation1(k_model, Vgs_v[i], Vth_model, Vds_v[i], Is_model);
				norm=norm+Math.pow((Id_model_v[i]-app), 2);
			}
		}
		norm= Math.sqrt(norm);
		System.out.println("The norm of the first validation: "+norm);
		if(norm<0.1) System.out.println("The first validation: pass!");
		else System.out.println("The fist validation: fail!");
		
	}
	public static double approximation1(double k, double Vgs, double Vth, double Vds, double Is) {
		double Vt= 0.026;
		double x1= k*(Vgs-Vth)/Vt;
		double x2= (k*(Vgs-Vth)-Vds)/Vt;
		return Is*(Math.exp(x1)-Math.exp(x2));
	}
	
	//validation 2: For Vgs>Vth and Vds> Vdsat, Id_model should be quadratic to Vgs and insensitive to Vds 
	public void validation2(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		EKV_Model ekv= new EKV_Model();
		Vector para= ekv.newtonEKV(k, Vgs, Vth, Vds, Is, Id_measure);
		double Is_model= para.getValue()[0];
		double k_model= para.getValue()[1];
		double Vth_model= para.getValue()[2];
		Vector Id_model= ekv.computeId_model(k_model, Vgs, Vth_model, Vds, Is_model);
		double[] Id_model_v= Id_model.getValue();
		double norm=0;
		for(int i=0; i<Vgs_v.length; i++) {
			double Vd_sat= k*(Vgs_v[i]-Vth);
			if(Vgs_v[i]>Vth_model&&Vds_v[i]>Vd_sat) {
				double app= approximation2(k_model, Vgs_v[i], Vth_model, Vds_v[i], Is_model);
				norm=norm+Math.pow((Id_model_v[i]-app), 2);
			}
		}
		norm= Math.sqrt(norm);
		System.out.println("The norm of the second validation: "+norm);
		if(norm<0.1) System.out.println("The second validation: pass!");
		else System.out.println("The second validation: fail!");
	}
	public static double approximation2(double k,double Vgs, double Vth, double Vds, double Is) {
		double Vt= 0.026;
		double x1= k*(Vgs-Vth)/Vt;
		double x2= (k*(Vgs-Vth)-Vds)/Vt;
		return Is*Math.pow(x1/2, 2)-Is*Math.exp(x2);
	}
	
	//Validation 3: For Vgs>Vth and Vds<Vdsat, Id_model should be quadratic to Vds
	public void validation3(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		EKV_Model ekv= new EKV_Model();
		Vector para= ekv.newtonEKV(k, Vgs, Vth, Vds, Is, Id_measure);
		double Is_model= para.getValue()[0];
		double k_model= para.getValue()[1];
		double Vth_model= para.getValue()[2];
		Vector Id_model= ekv.computeId_model(k_model, Vgs, Vth_model, Vds, Is_model);
		double[] Id_model_v= Id_model.getValue();
		double norm=0;
		for(int i=0; i<Vgs_v.length; i++) {
			double Vd_sat= k*(Vgs_v[i]-Vth);
			if(Vgs_v[i]>Vth_model&&Vds_v[i]<Vd_sat) {
				double app= approximation3(k_model, Vgs_v[i], Vth_model, Vds_v[i], Is_model);
				norm=norm+Math.pow((Id_model_v[i]-app), 2);
			}
		}
		norm= Math.sqrt(norm);
		System.out.println("The norm of the second validation: "+norm);
		if(norm<0.1) System.out.println("The second validation: pass!");
		else System.out.println("The second validation: fail!");
	}
	public static double approximation3(double k,double Vgs, double Vth, double Vds, double Is) {
		double Vt= 0.026;
		double x1= k*(Vgs-Vth)/Vt;
		double x2= (k*(Vgs-Vth)-Vds)/Vt;
		return Is*Math.pow(x1/2, 2)-Is*Math.pow(x2/2, 2);
	}

}
