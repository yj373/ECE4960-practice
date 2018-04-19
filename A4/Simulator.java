package A4;
import homework_4.*;
//Simulate the simple RC circuit with the parameters given in the task 4
//Simulate the amplifier circuit with the parameters given in the task 5
public class Simulator {
	
	private Vector xi;
	private double ti;
	private double interval; //step size
	private double duration;
	//Constructors
	public Simulator() {
		
	}
	public Simulator(double t, Vector x, double h, double d) {
		xi=x;
		ti=t;
		interval=h;
		duration=d;
	}
	//Getters and Setters
	public Vector getXi() {
		return xi;
	}
	public void setXi(Vector xi) {
		this.xi = xi;
	}
	public double getTi() {
		return ti;
	}
	public void setTi(double ti) {
		this.ti = ti;
	}
	public double getInterval() {
		return interval;
	}
	public void setInterval(double interval) {
		this.interval = interval;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	//Simulate
	public void simulate(String method) {
		System.out.println("RC circuit simulation with "+method+" :************************************************");
		ODE_solver solver= new ODE_solver(2);
		double t=0;
		System.out.println("t= "+t*1E9+"\tv1= "+xi.getValue()[0]+"\tv2= "+xi.getValue()[1]);
		//System.out.println(t*1E9+"\t"+xi.getValue()[0]+"\t"+xi.getValue()[1]);
		while(t<duration) {
			Vector x= solver.solve(ti, xi, interval, method);
			double[] x_v= x.getValue();
			t= t+interval;
			ti= ti+interval;
			xi=x;
			System.out.println("t= "+t*1E9+"\tv1= "+x_v[0]+"\tv2= "+x_v[1]);
			//System.out.println(t*1E9+"\t"+x_v[0]+"\t"+x_v[1]);

		}
		
		
	}
	

}
