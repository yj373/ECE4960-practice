package A5;

import A5.ODE_solver;
import homework_4.Vector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
	public void simulate(double t, double freq) {
		//System.out.println("RC circuit simulation with "+method+" :************************************************");
		ODE_solver solver= new ODE_solver(2);
		File f = new File("src/A5/cmos_output.txt");
		//System.out.println("t= "+t*1E9+"\ti= "+xi.getValue()[0]+"\tv= "+xi.getValue()[1]);
		try {
			BufferedWriter output= new BufferedWriter(new FileWriter(f));
			output.write(t*1E6+"\t"+xi.getValue()[0]+"\t"+xi.getValue()[1]+"\n");
			System.out.println("t= "+t*1E9+"\ti= "+xi.getValue()[0]+"\tv= "+xi.getValue()[1]);
			while(t<duration) {
				Vector x= solver.solve(ti, freq, xi, interval);
				double[] x_v= x.getValue();
				t= t+interval;
				ti= ti+interval;
				xi=x;
				output.write(t*1E6+"\t"+x_v[0]+"\t"+x_v[1]+"\n");
				System.out.println(t*1E6+"\t"+x_v[0]+"\t"+x_v[1]+"\n");
				if(Double.isNaN(x_v[1])) break;
			}
			output.close();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		
	}
	
}
