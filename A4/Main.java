package A4;
import homework_4.Vector;
public class Main {

	public static void main(String[] args) {
		//Doing validation
//		Testor testor= new Testor();
//		testor.validate();
		
		//Change the objective functions in the class ODE_solver to simulate the circuit in Figure 3
		double[] v_v= {0,0};
		Vector v= new Vector(v_v);
		double ti= 0;
		double stepSize= 0.2E-9;
		double duration= 100E-9;
		Simulator simulator1= new Simulator(ti, v, stepSize, duration);
		//simulator1.simulate("Forward_Euler");
		//simulator1.simulate("RK4");
		//simulator1.simulate("RK34_adaptive");
		stepSize= 1E-9;
		simulator1.setInterval(stepSize);
		//simulator1.simulate("Forward_Euler");
		//simulator1.simulate("RK4");
		//simulator1.simulate("RK34_adaptive");
		
		//Change the objective functions in the class ODE_solver to simulate the circuit in Figure 5(a)
		double[] v_v2= {2.5, 2.5};
		Vector v2= new Vector(v_v2);
		ti=0;
		stepSize= 0.2E-9;
		duration= 100E-9;
		Simulator simulator2= new Simulator(ti, v2, stepSize, duration);
		//simulator2.simulate("Forward_Euler");
		//simulator2.simulate("RK4");
		//simulator2.simulate("RK34_adaptive");
		stepSize= 1E-9;
		simulator2.setInterval(stepSize);
		//simulator2.simulate("Forward_Euler");
		//simulator2.simulate("RK4");
		simulator2.simulate("RK34_adaptive");


	}

}
