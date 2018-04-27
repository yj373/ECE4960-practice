package hackerPracticeWeek12;
import java.util.ArrayList;
import A2.Iterative_solver;
import homework_4.*;
public class Prctice4_19 {

	public static void main(String[] args) {
		// The second hacker practice of note 7
		//2-D elliptical PDE
		double[] psi_v= {0,0,0,0,0,0,0,0,0};
		Vector psi= new Vector(psi_v);
		double ni= 1.5E10;
		double psi0=1.0;
		double ND= 1E15/ni;
		double NA= 1E17/ni;
		
		double LD= 2.4E-3;
		double h= 1E-5/LD;
		
		double norm= computeNorm(psi, psi0, h, ND, NA);
		Vector deltaPsi= new Vector();
		double norm_deltaPsi= 1;
		do {
			deltaPsi= computeDeltaPsi(psi, psi0, h, ND, NA);
			norm_deltaPsi= deltaPsi.computeSecondNorm();
			for(int i=0; i<psi.getLength(); i++) {
				System.out.print("psi"+(i+1)+"= "+psi.getValue()[i]+"\t");
			}
			System.out.print("\n");
			psi=psi.add(deltaPsi);
		}while(norm>Math.pow(10, -7)&&norm_deltaPsi>Math.pow(10, -7));
		

	}
	public static Vector fi(Vector psi, double psi0, double h, double ND, double NA) {
		double[] psi_v= psi.getValue();
		ArrayList<Double> f_v= new ArrayList<Double>();
		f_v.add(psi_v[1]+psi_v[0]*(-4)+psi_v[3]+h*h*(Math.exp(-psi_v[0])-Math.exp(psi_v[0])+ND)+2*psi0); 
		f_v.add(psi_v[0]+psi_v[1]*(-4)+psi_v[2]+psi_v[4]+h*h*(Math.exp(-psi_v[1])-Math.exp(psi_v[1])+ND));
		f_v.add(psi_v[1]+psi_v[2]*(-4)+psi_v[5]+h*h*(Math.exp(-psi_v[2])-Math.exp(psi_v[2])-NA));
		f_v.add(psi_v[0]+psi_v[3]*(-4)+psi_v[4]+psi_v[6]+h*h*(Math.exp(-psi_v[3])-Math.exp(psi_v[3])+ND));
		f_v.add(psi_v[1]+psi_v[3]+psi_v[4]*(-4)+psi_v[5]+psi_v[7]+h*h*(Math.exp(-psi_v[4])-Math.exp(psi_v[4])-NA));
		f_v.add(psi_v[2]+psi_v[5]*(-4)+psi_v[4]+psi_v[8]+h*h*(Math.exp(-psi_v[5])-Math.exp(psi_v[5])-NA));
		f_v.add(psi_v[3]+psi_v[6]*(-4)+psi_v[7]+h*h*(Math.exp(-psi_v[6])-Math.exp(psi_v[6])-NA));
		f_v.add(psi_v[4]+psi_v[6]+psi_v[7]*(-4)+psi_v[8]+h*h*(Math.exp(-psi_v[7])-Math.exp(psi_v[7])-NA));
		f_v.add(psi_v[5]+psi_v[7]+psi_v[8]*(-4)+h*h*(Math.exp(-psi_v[8])-Math.exp(psi_v[8])-NA));
		return new Vector(f_v);
	} 
	public static SparseMatrix Jaco(Vector psi, double psi0, double h) {
		ArrayList<Double> v= new ArrayList<Double>();
		ArrayList<Integer> rp= new ArrayList<Integer>();
		ArrayList<Integer> ci= new ArrayList<Integer>();
		double[] psi_v= psi.getValue();
		v.add(-4-h*h*(Math.exp(-psi_v[0])+Math.exp(psi_v[0])));
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[1])+Math.exp(psi_v[1])));
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[2])+Math.exp(psi_v[2])));
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[3])+Math.exp(psi_v[3])));
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[4])+Math.exp(psi_v[4])));
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[5])+Math.exp(psi_v[5])));
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[6])+Math.exp(psi_v[6])));
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[7])+Math.exp(psi_v[7])));
		v.add(1.0);
		v.add(1.0);
		v.add(1.0);
		v.add(-4-h*h*(Math.exp(-psi_v[8])+Math.exp(psi_v[8])));
		ci.add(0);
		ci.add(1);
		ci.add(3);
		ci.add(0);
		ci.add(1);
		ci.add(2);
		ci.add(4);
		ci.add(1);
		ci.add(2);
		ci.add(5);
		ci.add(0);
		ci.add(3);
		ci.add(4);
		ci.add(6);
		ci.add(1);
		ci.add(3);
		ci.add(4);
		ci.add(5);
		ci.add(7);
		ci.add(2);
		ci.add(4);
		ci.add(5);
		ci.add(8);
		ci.add(3);
		ci.add(6);
		ci.add(7);
		ci.add(4);
		ci.add(6);
		ci.add(7);
		ci.add(8);
		ci.add(5);
		ci.add(7);
		ci.add(8);
		rp.add(0);
		rp.add(3);
		rp.add(7);
		rp.add(10);
		rp.add(14);
		rp.add(19);
		rp.add(23);
		rp.add(26);
		rp.add(30);
		rp.add(33);
		return new SparseMatrix(v, rp, ci);
	}
	public static Vector computeDeltaPsi(Vector psi, double psi0, double h, double ND, double NA) {
		Vector f= fi(psi, psi0, h, ND, NA);
		SparseMatrix j= Jaco(psi, psi0, h);
		Iterative_solver solver= new Iterative_solver(j, f);
		return solver.iterate();
	}
	public static double computeNorm(Vector psi, double psi0, double h, double ND, double NA) {
		Vector v= fi(psi, psi0, h, ND, NA);
		return v.computeSecondNorm();
	}

}
