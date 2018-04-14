package hackerPracticeWeek11;

public class Practice4_10 {

	public static void main(String[] args) {
		// The forth hacker practice of the Note 6
		// RK4 with adaptive h
		double x=2;
		double h=1;
		double t=0;
		double absoluteErr= computeAbsoluteErr(t,x);
		double estimatedErr= 0;
		System.out.println("t= "+t+"\tx_rk4: "+x+"\tAbsolute Error: "+absoluteErr+"\tEstimated error: "+estimatedErr+"\ttime step:"+h);
		while(t<4) {
			double[] rk4Res= RK4_adaptiveH(t, x, h, 0.0001, 0.0000001);
			x=rk4Res[0];
			estimatedErr= rk4Res[2];
			t++;
			absoluteErr= computeAbsoluteErr(t,x);
			System.out.println("t= "+t+"\tx_rk4: "+x+"\tAbsolute Error: "+absoluteErr+"\tEstimated error: "+estimatedErr+"\ttime step:"+rk4Res[1]);
			
		}
		

	}
	public static double fx(double t, double x) {
		return 4*Math.exp(0.8*t)-0.5*x;
	}
	public static double groundTruth(double t) {
		double term1= Math.exp(0.8*t)-Math.exp(-0.5*t);
		double term2= Math.exp(-0.5*t);
		double res= term1*4/1.3+2*term2;
		return res;
	}
	//Module1:compute k1, k2, k3, k4 
	public static double computeK1(double ti, double xi) {
		return fx(ti, xi);
	}
	public static double computeK2(double ti, double xi, double h, double k1) {
		return fx(ti+h*0.5, xi+k1*h*0.5);
	}
	public static double computeK3(double ti, double xi, double h, double k2) {
		return fx(ti+h*0.75, xi+k2*h*0.75);
	}
	public static double computeK4(double ti, double xi, double h, double k3) {
		return fx(ti+h, xi+k3*h);
	}
	
	//Module2: compute increment function for RK3 and RK4
	public static double RK3(double ti, double xi, double h) {
		double k1= computeK1(ti, xi);
		double k2= computeK2(ti, xi, h, k1);
		double k3= computeK3(ti, xi, h, k2);
		return xi+(2*k1+3*k2+4*k3)*h/9.0; 
	}
	public static double RK4(double ti, double xi, double h) {
		double k1= computeK1(ti, xi);
		double k2= computeK2(ti, xi, h, k1);
		double k3= computeK3(ti, xi, h, k2);
		double k4= computeK4(ti, xi, h, k3);
		return xi+(7*k1+6*k2+8*k3+3*k4)*h/24.0;
	}
	//Module 3: RK4 with adaptive h to compute the value of x at ti+1
	public static double computeNormalizedErr(double x_rk3, double x_rk4, double eR, double eA) {
		double E= Math.abs(x_rk3-x_rk4);
		return E/((x_rk4+eA)*eR);
	}
	public static double[] RK4_adaptiveH(double ti, double xi, double h, double eR, double eA) {
		double x_rk3= 0;
		double x_rk4= 0;
		double r= 0;
		double t=0;
		double lastStep=0;
		while(t<1) {
			x_rk3= RK3(ti, xi, h);
			x_rk4= RK4(ti, xi, h);
			r= computeNormalizedErr(x_rk3, x_rk4, eR, eA);
			while((Math.abs(r-1)>0.01||Math.abs(r-1)<0.000001)) {
				double k= Math.pow(r, 1.0/3.0);
				h= h/k;
				if(h+t>1) {
					lastStep=h*k;
					break;
				};
				x_rk3= RK3(ti, xi, h);
				x_rk4= RK4(ti, xi, h);
				r= computeNormalizedErr(x_rk3, x_rk4, eR, eA);
			}
			ti=ti+h;
			t=t+h;
			xi=x_rk4;
			h=1-t; 
		}
		
		double estimatedErr= Math.abs(x_rk3-x_rk4);
		double[] res= {x_rk4, lastStep, estimatedErr};
		return res;
	}
	//Module 4: Compute relative error
	public static double computeAbsoluteErr(double t, double x) {
		double truth= groundTruth(t);
		return Math.abs(x-truth);
	}

}
