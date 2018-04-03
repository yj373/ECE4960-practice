package hackerPracticeWeek9;

public class Practice3_29 {

	public static void main(String[] args) {
		// The second Hacker practice of Note 6
		//Compare Forward Euler, the one-step Huen's method and the Huen's method with iteration
		System.out.println("The second Hacker practice of Note 6:********************************************************");
		double t=0;
		double forwardX= 2;
		double huenOneX= 2;
		double huenIterX= 2;
		double h=1;
		do {
			//Compute errors
			double forwardErr= computeErr(forwardX, t);
			double huenOneErr= computeErr(huenOneX, t);
			double huenIterErr= computeErr(huenIterX, t);
			//output
			System.out.println("t= "+t+"\tfoward x: "+forwardX+"\terr: "+forwardErr+"\tone step x: "+huenOneX+"\terr: "+huenOneErr+"\titerate x: "
			+huenIterX+"\terr: "+huenIterErr);
			//Refresh x's and time
			double preT= t;
			t= t+1;
			double preForwardX= forwardX;
			double preHuenOneX= huenOneX;
			double preHuenIterX= huenIterX;
			forwardX= predictor(preForwardX, h, preT);
			
			double lastX_one= predictor(preHuenOneX, h, preT); 
			huenOneX= corrector(preHuenOneX, lastX_one, preT, h);
			
			double norm= 1;
			double lastX_Iter= predictor(preHuenIterX, h, preT);
			huenIterX= corrector(preHuenIterX, lastX_Iter, preT, h);
			do {
				lastX_Iter= huenIterX;
				huenIterX= corrector(preHuenIterX, lastX_Iter, preT, h);
				norm= Math.abs((huenIterX-lastX_Iter)/huenIterX);
			}while(norm>=Math.pow(10, -7));
			
		}while(t<=4);
		
		//The third Hacker practice of Note 6
		//RK4 Runge Kutta
		System.out.println("The third hacker practice of Note 6:************************************************************************");
		t=0;
		h=1;
		double x=2;
		double k1= fx(x, t);
		double k2= fx(x+k1*h/2, t+h/2);
		double k3= fx(x+k2*h/2, t+h/2);
		double k4= fx(x+k3*h, t+h);
		do {
			double err= computeErr(x, t);
			System.out.println("t= "+t+"\tk1= "+k1+"\tk2= "+k2+"\tk3= "+k3+"\tk4= "+k4+"\tx= "+x+"\terr= "+err);
			t= t+1;
			x= x+(k1+2*k2+2*k3+k4)*h/6;
			k1= fx(x, t);
			k2= fx(x+k1*h/2, t+h/2);
			k3= fx(x+k2*h/2, t+h/2);
			k4= fx(x+k3*h, t+h);
		}while(t<=4);
		

	}
	
	//Huen's Method
	//Module 1: Compute the dx/dt=f(x,t)
	public static double fx(double x, double t) {
		return 4*Math.exp(0.8*t)-0.5*x;
	}
	//Module 2: predictor
	public static double predictor(double preX, double h, double preT) {
		double res= preX+fx(preX, preT)*h;
		return res;
	}
	//Module 3: corrector
	public static double corrector(double preX, double lastIterX, double preT, double h) {
		double preDeriv= fx(preX, preT);
		double t=preT+h;
		double res= preX+(preDeriv+fx(lastIterX, t))*h/2;
		return res;
	}
	//Module 4: compute ground truth
	public static double groundTruth(double t) {
		double term1= Math.exp(0.8*t)-Math.exp(-0.5*t);
		double term2= Math.exp(-0.5*t);
		double res= term1*4/1.3+2*term2;
		return res;
	}
	//Module 5: compute relative Error
	public static double computeErr(double x, double t) {
		double truth= groundTruth(t);
		double relErr= Math.abs((x-truth)/truth)*100;
		return relErr;
	}

}
