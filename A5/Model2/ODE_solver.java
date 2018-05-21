package A5;

import java.util.ArrayList;

import homework_4.Vector;

public class ODE_solver {
	private int rank;
	private boolean constructed= false;
	private static int fx_num=2;
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
	
	//Set the values of R0 and R1 according to the state of switch and the load of the circuit
//	public static double[] getResistors(double r, double freq, double t) {
//		double small = r*1E-3;
//		double big = r*1E3;
//		double T = 1/freq;
//		double n = t/T;
//		double sign = n - Math.floor(n);
//		if(sign<0.5 && sign>=0) {
//			double[] results = {big, small};
//			return results;
//		}else if(sign>=0.5 && sign<1){
//			double[] results = {small, big};
//			return results;
//		}else return new double[2];
//	}
	//The objective functions
	private static double fxDirectory(double t, double freq, Vector xi, int fxNum) {
		double E = 12;
		double L = 15.91E-3;
		double R = 52;
		double C = 50E-6;
		if(xi.getLength()!=2) return Double.NaN;
		double i = xi.getValue()[0];
		double v = xi.getValue()[1];
		double id = id_ekv(vgs(t, freq), E);
		double diode = 0.0;
		//if(diode!=0) id =i;
		if(id>i) diode = 1E8;
		switch(fxNum) {
		case 0: return (E-v-(i-id)*diode)/L;
		case 1: return (-v/R+i-id)/C;
		default: return Double.NaN; 
		}
	}
	private static double id_ekv(double Vgs, double Vds) {
		double Is= 5E-6;
		double k= 0.7;
		double Vth= 1;
		double Vt= 0.026;
		double term1= 1+Math.exp(k*(Vgs-Vth)/(2*Vt));
		double term2= 1+Math.exp((k*(Vgs-Vth)-Vds)/(2*Vt));
		double res = Is*(Math.log(term1)*Math.log(term1)-Math.log(term2)*Math.log(term2));
		return res;
		
	}
	//Vgs, 50% square wave
	private static double vgs(double t, double freq) {
		double T = 1/freq;
		double n = t/T;
		double sign = n - Math.floor(n);
		if(sign<0.8 && sign>=0) {
			return 0.0;
		}else if(sign>=0.8 && sign<1){
			return 28.0;
		}else return Double.NaN;
	}
	private static double diode(double t, double freq) {
		double T = 1/freq;
		double n = t/T;
		double sign = n - Math.floor(n);
		if(sign<0.5 && sign>=0) {
			return 0.0;
		}else if(sign>=0.5 && sign<1){
			return 1E8;
		}else return Double.NaN;
	}
	public static Vector computeK1(double ti, double freq, Vector xi) {
		ArrayList<Double> k1= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double k1_ele= fxDirectory(ti, freq, xi, i);
			k1.add(k1_ele);
		}
		return new Vector(k1);
	}
	public static Vector computeK2(double ti, double freq, Vector xi, double h, Vector k1) {
		ArrayList<Double> k2= new ArrayList<Double>();
		double[] x_v= new double[xi.getLength()];
		for(int i=0; i<xi.getLength(); i++) {
			x_v[i]= xi.getValue()[i]+k1.getValue()[i]*h*0.5;
		}
		Vector x= new Vector(x_v);
		for(int i=0; i<xi.getLength(); i++) {
			double k2_ele= fxDirectory(ti+0.5*h, freq, x, i);
			k2.add(k2_ele);
		}
		return new Vector(k2);
	}
	public static Vector computeK3(double ti, double freq, Vector xi, double h, Vector k2) {
		ArrayList<Double> k3= new ArrayList<Double>();
		double[] x_v= new double[xi.getLength()];
		for(int i=0; i<xi.getLength(); i++) {
			x_v[i]= xi.getValue()[i]+k2.getValue()[i]*h*0.75;
		}
		Vector x= new Vector(x_v);
		for(int i=0; i<xi.getLength(); i++) {
			double k3_ele= fxDirectory(ti+0.75*h, freq, x, i);
			k3.add(k3_ele);
		}
		return new Vector(k3);
	}
	public static Vector computeK4(double ti, double freq, Vector xi, double h, Vector k3) {
		ArrayList<Double> k4= new ArrayList<Double>();
		double[] x_v= new double[xi.getLength()];
		for(int i=0; i<xi.getLength(); i++) {
			x_v[i]= xi.getValue()[i]+k3.getValue()[i]*h;
		}
		Vector x= new Vector(x_v);
		for(int i=0; i<xi.getLength(); i++) {
			double k4_ele= fxDirectory(ti+h, freq, x, i);
			k4.add(k4_ele);
		}
		return new Vector(k4);
	}
		
	//Module2: compute increment function for RK3 and RK4
	public static Vector RK3(double ti, double freq, Vector xi, double h) {
		Vector k1= computeK1(ti, freq, xi);
		Vector k2= computeK2(ti, freq, xi, h, k1);
		Vector k3= computeK3(ti, freq, xi, h, k2);
		ArrayList<Double> x_v= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double x_ele= xi.getValue()[i]+(2*k1.getValue()[i]+3*k2.getValue()[i]+4*k3.getValue()[i])*h/9.0;
			x_v.add(x_ele);
		}
		return new Vector(x_v); 
	}
	public static Vector RK4(double ti, double freq, Vector xi, double h) {
		Vector k1= computeK1(ti, freq, xi);
		Vector k2= computeK2(ti, freq, xi, h, k1);
		Vector k3= computeK3(ti, freq, xi, h, k2);
		Vector k4= computeK4(ti, freq, xi, h, k3);
		ArrayList<Double> x_v= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double x_ele= xi.getValue()[i]+(7*k1.getValue()[i]+6*k2.getValue()[i]+8*k3.getValue()[i]+3*k4.getValue()[i])*h/24.0;
			x_v.add(x_ele);
		}
		return new Vector(x_v);
	}
	//Module 3: RK4 with adaptive h to compute the value of x at ti+interval
	public static double computeNormalizedErr(double x_rk3, double x_rk4, double eR, double eA) {
		double E = Math.abs(x_rk3-x_rk4);
		double k = (x_rk4+eA)*eR;
		return E/k;
	}
	//interval=step size
	public static double RK34_adaptiveH_n(double ti, double freq, Vector xi, double h, double eR, double eA, int n, double interval) {
		double x_rk3= 0;
		double x_rk4= 0;
		double r= 0;
		double t=0;
		while(t<interval) {
			x_rk3= RK3(ti, freq, xi, h).getValue()[n];
			x_rk4= RK4(ti, freq, xi, h).getValue()[n];
			r= computeNormalizedErr(x_rk3, x_rk4, eR, eA);
			while((r-1)>0.01) {
				double k= Math.pow(Math.abs(r), 1.0/3.0);
				h= h/k;
				if(h+t>interval) {
					break;
				};
				x_rk3= RK3(ti, freq, xi, h).getValue()[n];
				x_rk4= RK4(ti, freq, xi, h).getValue()[n];
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
	public static Vector RK34_adaptiveH(double ti, double freq, Vector xi, double h, double eR, double eA, double interval) {
		ArrayList<Double> x= new ArrayList<Double>();
		for(int i=0; i<xi.getLength(); i++) {
			double x_ele= RK34_adaptiveH_n(ti, freq, xi, h, eR, eA, i, interval);
			x.add(x_ele);
		}
		return new Vector(x);
	}
	public Vector solve(double ti, double freq, Vector xi, double interval) {
		
		return RK34_adaptiveH(ti, freq, xi, interval, 1E-4, 1E-7, interval);
			
	}
	
	
}
