package A4;
import homework_4.*;

public class Testor {
	//Perform a validation of the ODE solver
	public static double groundTruth(double t) {
		double term1= Math.exp(0.8*t)-Math.exp(-0.5*t);
		double term2= Math.exp(-0.5*t);
		double res= term1*4/1.3+2*term2;
		return res;
	}
	//validation
	public void validate() {
		ODE_solver solver= new ODE_solver(1);
		System.out.println("Forward Euler method validation:******************************");
		double x= 2;
		double t= 0;
		double err= computeRelativeErr(t, x);
		double interval = 1;
		double[] xi_v= {2};
		double ti= 0;
		Vector xi= new Vector(xi_v);
		System.out.println("t= "+t+"\tx= "+x+"\terr= "+err);
		//ti is the initial time value of each iteration
		//t is the time of the last prediction
		while(t<10) {
			xi_v[0]=x;
			ti=t;
			xi.setValue(xi_v);
			Vector nextX= solver.solve(ti, xi, interval, "Forward_Euler");
			x= nextX.getValue()[0];
			t= t+interval;
			err= computeRelativeErr(t,x);
			System.out.println("t= "+t+"\tx= "+x+"\terr= "+err);
		}
		System.out.println("RK34 method without time adaptation validation:******************************");
		x= 2;
		t= 0;
		err= computeRelativeErr(t, x);
		xi_v[0]= 2;
		ti= 0;
		xi.setValue(xi_v);
		System.out.println("t= "+t+"\tx= "+x+"\terr= "+err);
		while(t<10) {
			xi_v[0]=x;
			ti=t;
			xi.setValue(xi_v);
			Vector nextX= solver.solve(ti, xi, interval, "RK4");
			x= nextX.getValue()[0];
			t= t+interval;
			err= computeRelativeErr(t,x);
			System.out.println("t= "+t+"\tx= "+x+"\terr= "+err);
		}
		System.out.println("RK34 method with time adaptation validation:******************************");
		x= 2;
		t= 0;
		err= computeRelativeErr(t, x);
		xi_v[0]= 2;
		ti= 0;
		xi.setValue(xi_v);
		System.out.println("t= "+t+"\tx= "+x+"\terr= "+err);
		while(t<10) {
			xi_v[0]=x;
			ti=t;
			xi.setValue(xi_v);
			Vector nextX= solver.solve(ti, xi, interval, "RK34_adaptive");
			x= nextX.getValue()[0];
			t= t+interval;
			err= computeRelativeErr(t,x);
			System.out.println("t= "+t+"\tx= "+x+"\terr= "+err);
		}
	}
	//Compute error
	public static double computeRelativeErr(double t, double x) {
		double truth= groundTruth(t);
		return Math.abs((x-truth)/truth);
	}

}
