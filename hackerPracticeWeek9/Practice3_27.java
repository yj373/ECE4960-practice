package hackerPracticeWeek9;

public class Practice3_27 {

	public static void main(String[] args) {
		// The first Hacker practice in Note 6
		// Compare the evaluation of x(t) by three method: Ground truth, Backward Euler, Trapezoid Euler
		System.out.println("The first hacker practice of the Note 6:********************************************************");
		double t=0;
		double trueX=1;
		double backX=1;
		double trapX=1;
		double delta_t=0.5;
		do{
			double backErr= Math.abs(backX-trueX);
			double trapErr= Math.abs(trapX-trueX);	
			System.out.println("delta_t= "+delta_t+"\tt= "+t+"\tbackward euler error: "+backErr+"\ttrapezoid euler error: "+trapErr);
			t=t+delta_t;
			trueX=groundTruth(t);
			double preBackX= backX;
			double preTrapX= trapX;
			backX= backwardEuler(delta_t, preBackX);
			trapX= trapezoidEuler(delta_t, preTrapX);
		}while(t<=20);
		
		t=0;
		trueX=1;
		backX=1;
		trapX=1;
		delta_t=0.1;
		do{
			double backErr= Math.abs(backX-trueX);
			double trapErr= Math.abs(trapX-trueX);	
			System.out.println("delta_t= "+delta_t+"\tt= "+t+"\tbackward euler error: "+backErr+"\ttrapezoid euler error: "+trapErr);
			t=t+delta_t;
			trueX=groundTruth(t);
			double preBackX= backX;
			double preTrapX= trapX;
			backX= backwardEuler(delta_t, preBackX);
			trapX= trapezoidEuler(delta_t, preTrapX);
		}while(t<=20);

	}
	public static double groundTruth(double t) {
		return Math.exp(-t);
	}
	public static double backwardEuler(double delta_t, double preX) {
		return preX/(1+delta_t);
	}
	public static double trapezoidEuler(double delta_t, double preX) {
		return (2-delta_t)*preX/(2+delta_t);
	}

}
