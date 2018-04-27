package hackerPracticeWeek13;
import homework_4.*;
import A3.FullMatrixSolver;
public class Practice4_26 {

	public static void main(String[] args) {
		// The forth hacker practice of Note 7
		// 1-D Hyperbolic PDE
		System.out.println("Zero Dirichlet boundary conditions**********************************");
		System.out.println("Upwinding forward Euler**********************************");
		double delta_x = 1;
		double delta_t = 1;
		double v = 1;
		double[] u_v = {0, 10, 0, 0, 0};
		Vector u = new Vector(u_v);
		upwindingForwardEuler(u, delta_x, delta_t, v);
		System.out.println("Upwinding backward Euler**********************************");
		u.setValue(u_v);
		upwindingBackwardEuler(u, delta_x, delta_t, v);
		System.out.println("Downwinding forwardward Euler**********************************");
		u.setValue(u_v);
		downwindingForwardEuler(u, delta_x, delta_t, v);
		System.out.println("Downwinding forwardward Euler**********************************");
		u.setValue(u_v);
		downwindingBackwardEuler(u, delta_x, delta_t, v);
		System.out.println("Upwinding forward Euler (zero-slope Neumann condition)**********************************");
		u.setValue(u_v);
		upwindingForwardEuler2(u, delta_x, delta_t, v);
	}
	public static void upwindingForwardEuler(Vector u, double delta_x, double delta_t, double v) {
		FullMatrixSolver fs = new FullMatrixSolver();
		double term1 = v/delta_x;
		double term2 = 1.0/delta_t;
		double term3 = term1+term2;
		double[][] A_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			A_v[i][i] = term2;
		}
		FullMatrix A = new FullMatrix(A_v);
		double[][] B_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			B_v[i][i] = term3;
			if(i!=0) {
				B_v[i][i-1] = -term1;
			}
		}
		FullMatrix B = new FullMatrix(B_v);
		Vector b = B.productAx(u);
		u = fs.solve(A, b);
		for(int i=2; i<=5;i++) {
			b = B.productAx(u);
			u = fs.solve(A, b);
			double[] cur_u = u.getValue();
			System.out.println("t = "+i+"\tu1 = "+cur_u[0]+"\tu2 = "+cur_u[1]+"\tu3 = "+cur_u[2]+"\tu4 = "+cur_u[3]+"\tu5 = "+cur_u[4]);
		}
	}
	public static void upwindingBackwardEuler(Vector u, double delta_x, double delta_t, double v) {
		FullMatrixSolver fs = new FullMatrixSolver();
		double term1 = v/delta_x;
		double term2 = 1.0/delta_t;
		double term3 = -term1+term2;
		double[][] A_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			A_v[i][i] = term3;
			if(i!=0) {
				A_v[i][i-1] = term1;
			}
		}
		FullMatrix A = new FullMatrix(A_v);
		double[][] B_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			B_v[i][i] = term2;
		}
		FullMatrix B = new FullMatrix(B_v);
		Vector b = B.productAx(u);
		u = fs.solve(A, b);
		for(int i=2; i<=5;i++) {
			b = B.productAx(u);
			u = fs.solve(A, b);
			double[] cur_u = u.getValue();
			System.out.println("t = "+i+"\tu1 = "+cur_u[0]+"\tu2 = "+cur_u[1]+"\tu3 = "+cur_u[2]+"\tu4 = "+cur_u[3]+"\tu5 = "+cur_u[4]);
		}
	}
	public static void downwindingForwardEuler(Vector u, double delta_x, double delta_t, double v) {
		FullMatrixSolver fs = new FullMatrixSolver();
		double term1 = v/delta_x;
		double term2 = 1.0/delta_t;
		double term3 = -term1+term2;
		double[][] A_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			A_v[i][i] = term2;
		}
		FullMatrix A = new FullMatrix(A_v);
		double[][] B_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			B_v[i][i] = term3;
			if(i!=u.getLength()-1) {
				B_v[i][i+1] = term1;
			}
		}
		FullMatrix B = new FullMatrix(B_v);
		Vector b = B.productAx(u);
		u = fs.solve(A, b);
		for(int i=2; i<=5;i++) {
			b = B.productAx(u);
			u = fs.solve(A, b);
			double[] cur_u = u.getValue();
			System.out.println("t = "+i+"\tu1 = "+cur_u[0]+"\tu2 = "+cur_u[1]+"\tu3 = "+cur_u[2]+"\tu4 = "+cur_u[3]+"\tu5 = "+cur_u[4]);
		}
	}
	public static void downwindingBackwardEuler(Vector u, double delta_x, double delta_t, double v) {
		FullMatrixSolver fs = new FullMatrixSolver();
		double term1 = v/delta_x;
		double term2 = 1.0/delta_t;
		double term3 = -term1+term2;
		double[][] A_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			A_v[i][i] = term3;
			if(i!=u.getLength()-1) {
				A_v[i][i+1] = -term1;
			}
		}
		FullMatrix A = new FullMatrix(A_v);
		double[][] B_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			B_v[i][i] = term2;
		}
		FullMatrix B = new FullMatrix(B_v);
		Vector b = B.productAx(u);
		u = fs.solve(A, b);
		for(int i=2; i<=5;i++) {
			b = B.productAx(u);
			u = fs.solve(A, b);
			double[] cur_u = u.getValue();
			System.out.println("t = "+i+"\tu1 = "+cur_u[0]+"\tu2 = "+cur_u[1]+"\tu3 = "+cur_u[2]+"\tu4 = "+cur_u[3]+"\tu5 = "+cur_u[4]);
		}
	}
	public static void upwindingForwardEuler2(Vector u, double delta_x, double delta_t, double v) {
		FullMatrixSolver fs = new FullMatrixSolver();
		double term1 = v/delta_x;
		double term2 = 1.0/delta_t;
		double term3 = term1+term2;
		double[][] A_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			A_v[i][i] = term2;
		}
		FullMatrix A = new FullMatrix(A_v);
		double[][] B_v = new double[u.getLength()][u.getLength()];
		for (int i=0; i<u.getLength(); i++) {
			if(i==0) {
				B_v[i][i] = term2;
			}else {
				B_v[i][i] = term3;
			}
			if(i!=0) {
				B_v[i][i-1] = -term1;
			}
		}
		FullMatrix B = new FullMatrix(B_v);
		Vector b = B.productAx(u);
		u = fs.solve(A, b);
		for(int i=2; i<=10;i++) {
			b = B.productAx(u);
			u = fs.solve(A, b);
			double[] cur_u = u.getValue();
			System.out.println("t = "+i+"\tu1 = "+cur_u[0]+"\tu2 = "+cur_u[1]+"\tu3 = "+cur_u[2]+"\tu4 = "+cur_u[3]+"\tu5 = "+cur_u[4]);
		}
	}

}
