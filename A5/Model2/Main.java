package A5;
import homework_4.Vector;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[] i_v= {0.0, 0.0};
		Vector x= new Vector(i_v);
		double ti= 0.0;
		double stepSize=100E-6;
		double duration= 200E-3;
		double freq = 20E3;
		Simulator simulator1= new Simulator(ti, x, stepSize, duration);
		simulator1.simulate(ti, freq);
	}

}
