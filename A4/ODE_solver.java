package A4;

import java.util.ArrayList;
import java.util.HashMap;
import homework_4.*;
//This is a class for solving ODE, and there are fields in this class to construct an object of ODE_solver
//The field "rank" is a desired rank of output vector
//The field "constructed" represents the status of whether the ODE_solver is successfully constructed or not. (Only if the value of
//"rank" is equal to the number of f(x)s stored in the fxDirectory, this solver can be considered as successfully constructed and the
//value of this field will be set as "True")
public class ODE_solver {
	private int rank;
	private boolean constructed= false;
	
	//A Hashmap constant to transform the user input to the corresponding number of a method 
	private static HashMap<String, Integer> directory;
	static {
		directory= new HashMap<String, Integer>();
		directory.put("Forward_Euler", 1);
		directory.put("RK4", 2);
		directory.put("RK34_adaptive", 3);
	}
	
	//A constant that records the number of f(x)s stored in the fxDirectory
	//This part must be changed manually when solving different ODEs
	
	//The objective function for the validation
//	private static int fx_num=1;
//	private static double fxDirectory(double t, Vector x, int fxNum) {
//		switch(fxNum) {
//			case 0:{
//				return 4*Math.exp(0.8*t)-0.5*x.getValue()[0];
//			} 
//			default: return Double.NaN; 
//		}
//	}
	
	//The objective functions for the Figure 3 circuit simulation
//	private static int fx_num=2;
	//Current source
	//input unit: ns
	//output unit: A
	private static double it(double ti) {
		double k= ti%20;
		if(k<1&&k>=0) return k*0.1*0.001;
		else if(k<10&&k>=1) return 0.1*0.001;
		else if(k<11&&k>=10) return (-0.1*k+1.1)*0.001;
		else return 0;
	}
//	private static double fxDirectory(double ti, Vector xi, int fxNum) {
//		double r1=10000;
//		double r2=10000;
//		double r3=10000;
//		double c1=1E-12;
//		double c2=1E-12;
//		double[] v= xi.getValue();
//		if(v.length!=2) return Double.NaN;
//		switch(fxNum) {
//			case 0: return (v[0]/r1+(v[0]-v[1])/r2-it(ti*1E9))/(-c1);
//			case 1: return ((v[1]-v[0])/r2+v[1]/r3)/(-c2);
//			default: return Double.NaN; 
//		}
//	}
	
	private static int fx_num=2;
	private static double id_ekv(double Vgs, double Vds) {
		double Is= 5E-6;
		double k= 0.7;
		double Vth= 1;
		double Vt= 0.026;
		double term1= 1+Math.exp(k*(Vgs-Vth)/(2*Vt));
		double term2= 1+Math.exp((k*(Vgs-Vth)-Vds)/(2*Vt));
		return Is*(Math.log(term1)*Math.log(term1)-Math.log(term2)*Math.log(term2));
		
	}
	private static double fxDirectory(double ti, Vector xi, int fxNum) {
		double Vdd= 5;
		double Rg=10000;
		double Rl=10000;
		double C1=1E-12;
		double C2=1E-12;
		double[] v= xi.getValue();
		if(v.length!=2) return Double.NaN;
		switch(fxNum) {
			case 0: return it(ti*1E9)/C1-v[0]/(Rg*C1);
			case 1: return Vdd/(Rl*C2)-id_ekv(v[0], v[1])/C2-v[1]/(Rl*C2);
			default: return Double.NaN; 
		}
	}
	//Constructor
	public ODE_solver() {
		
	}
	public ODE_solver( int r) {
		rank= r;
		if(r==fx_num) constructed= true;
	}
	
	//Getters and setters
	public boolean isConstructed() {
		return constructed;
	}
	public void setConstructed(boolean constructed) {
		this.constructed = constructed;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		if(rank==fx_num) constructed= true;
		else constructed= false;
		this.rank = rank;
	}
	
	//Three one-step methods to solve an ODE
	//Forward Euler method
	private static Vector forwardEuler(double ti, Vector xi, double h) {
		ArrayList<Double> x_v= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double incrementalFunction= fxDirectory(ti, xi, i);
			double x_ele= xi.getValue()[i]+incrementalFunction*h;
			x_v.add(x_ele);
		}
		
		return new Vector(x_v);
	}
	//RK4 method with or without time adaptibility
	//Module1:compute k1, k2, k3, k4 
	public static Vector computeK1(double ti, Vector xi) {
		ArrayList<Double> k1= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double k1_ele= fxDirectory(ti, xi, i);
			k1.add(k1_ele);
		}
		return new Vector(k1);
	}
	public static Vector computeK2(double ti, Vector xi, double h, Vector k1) {
		ArrayList<Double> k2= new ArrayList<Double>();
		double[] x_v= new double[xi.getLength()];
		for(int i=0; i<xi.getLength(); i++) {
			x_v[i]= xi.getValue()[i]+k1.getValue()[i]*h*0.5;
		}
		Vector x= new Vector(x_v);
		for(int i=0; i<xi.getLength(); i++) {
			double k2_ele= fxDirectory(ti+0.5*h, x,i);
			k2.add(k2_ele);
		}
		return new Vector(k2);
	}
	public static Vector computeK3(double ti, Vector xi, double h, Vector k2) {
		ArrayList<Double> k3= new ArrayList<Double>();
		double[] x_v= new double[xi.getLength()];
		for(int i=0; i<xi.getLength(); i++) {
			x_v[i]= xi.getValue()[i]+k2.getValue()[i]*h*0.75;
		}
		Vector x= new Vector(x_v);
		for(int i=0; i<xi.getLength(); i++) {
			double k3_ele= fxDirectory(ti+0.75*h, x, i);
			k3.add(k3_ele);
		}
		return new Vector(k3);
	}
	public static Vector computeK4(double ti, Vector xi, double h, Vector k3) {
		ArrayList<Double> k4= new ArrayList<Double>();
		double[] x_v= new double[xi.getLength()];
		for(int i=0; i<xi.getLength(); i++) {
			x_v[i]= xi.getValue()[i]+k3.getValue()[i]*h;
		}
		Vector x= new Vector(x_v);
		for(int i=0; i<xi.getLength(); i++) {
			double k4_ele= fxDirectory(ti+h, x, i);
			k4.add(k4_ele);
		}
		return new Vector(k4);
	}
		
	//Module2: compute increment function for RK3 and RK4
	public static Vector RK3(double ti, Vector xi, double h) {
		Vector k1= computeK1(ti, xi);
		Vector k2= computeK2(ti, xi, h, k1);
		Vector k3= computeK3(ti, xi, h, k2);
		ArrayList<Double> x_v= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double x_ele= xi.getValue()[i]+(2*k1.getValue()[i]+3*k2.getValue()[i]+4*k3.getValue()[i])*h/9.0;
			x_v.add(x_ele);
		}
		return new Vector(x_v); 
	}
	public static Vector RK4(double ti, Vector xi, double h) {
		Vector k1= computeK1(ti, xi);
		Vector k2= computeK2(ti, xi, h, k1);
		Vector k3= computeK3(ti, xi, h, k2);
		Vector k4= computeK4(ti, xi, h, k3);
		ArrayList<Double> x_v= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double x_ele= xi.getValue()[i]+(7*k1.getValue()[i]+6*k2.getValue()[i]+8*k3.getValue()[i]+3*k4.getValue()[i])*h/24.0;
			x_v.add(x_ele);
		}
		return new Vector(x_v);
	}
	//Module 3: RK4 with adaptive h to compute the value of x at ti+interval
	public static double computeNormalizedErr(double x_rk3, double x_rk4, double eR, double eA) {
		double E= Math.abs(x_rk3-x_rk4);
		return E/((x_rk4+eA)*eR);
	}
	//interval=step size
	public static double RK34_adaptiveH_n(double ti, Vector xi, double h, double eR, double eA, int n, double interval) {
		double x_rk3= 0;
		double x_rk4= 0;
		double r= 0;
		double t=0;
		while(t<interval) {
			x_rk3= RK3(ti, xi, h).getValue()[n];
			x_rk4= RK4(ti, xi, h).getValue()[n];
			r= computeNormalizedErr(x_rk3, x_rk4, eR, eA);
			while((Math.abs(r-1)>0.01||Math.abs(r-1)<0.000001)) {
				double k= Math.pow(r, 1.0/3.0);
				h= h/k;
				if(h+t>interval) {
					break;
				};
				x_rk3= RK3(ti, xi, h).getValue()[n];
				x_rk4= RK4(ti, xi, h).getValue()[n];
				r= computeNormalizedErr(x_rk3, x_rk4, eR, eA);
			}
			ti=ti+h;
			t=t+h;
			double[] xi_v= xi.getValue();
			xi_v[n]=x_rk4;
			xi.setValue(xi_v);
			h=interval-t; 
		}	
//		double estimatedErr= Math.abs(x_rk3-x_rk4);
//		double[] res= {x_rk4, lastStep, estimatedErr};
		return x_rk4;
	}
	public static Vector RK34_adaptiveH(double ti, Vector xi, double h, double eR, double eA, double interval) {
		ArrayList<Double> x= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double x_ele= RK34_adaptiveH_n(ti, xi, h, eR, eA, i, interval);
			x.add(x_ele);
		}
		return new Vector(x);
	}
	
	//Solve the ODEs with certain method
	//return the solution vector after a certain interval
	public Vector solve(double ti, Vector xi, double interval, String method) {
		if(directory.get(method)==null||constructed==false) return new Vector();
		int method_num= directory.get(method);
		//Make sure each ODE will have an initial value
		if(xi.getLength()!=rank) return new Vector();
		switch(method_num) {
			case 1: {
				return forwardEuler(ti, xi, interval);
			}
			case 2: {
				return RK4(ti, xi, interval);
			}
			case 3: {
				return RK34_adaptiveH(ti, xi, interval, 0.0001, 0.0000001, interval);
			}
			default: return new Vector();
		}
		
	}

}
