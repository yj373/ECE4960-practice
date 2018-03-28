package hackerPracticeWeek8;
import java.util.ArrayList;
import java.util.Arrays;

import homework_4.*;
public class Practice3_22 {

	public static void main(String[] args) {
		// The third hacker practice of the Note 6
		//Calculating pi using Monte Carlo
		int N=1;
		for(int i=1; i<=6; i++) {
			N=N*10;
			double pi= computePi(N);
			double err= computeError(pi);
			System.out.println("N= "+N+"\tpi= "+pi+"\terr= "+err);
		}
		//The forth hacker practice of the Note 6
		//generate v with distribution function of p(v)
		double[] arr= new double[1000];
		for(int i=0; i<1000; i++) {
			arr[i]= generateRandomV(0.2);
		}
		Arrays.sort(arr);
		double[] p= new double[1000];
		double[] f= new double[1000];
		for(int i=0; i<1000; i++) {
			if(arr[i]<10&&arr[i]>=0) {
				p[i]=pv(arr[i],0.2);
				f[i]=fv(arr[i], 0.2);
			}
		}
		System.out.println("p(v):************************************");
		for(int i=0; i<1000; i++) {
			System.out.println(p[i]);
		}
		System.out.println("F(v):*************************************");
		for(int i=0; i<1000; i++) {
			System.out.println(f[i]);
		}
		
	}
	public static double computePi(int N) {
		if(N==0) return 0;
		int count=0;
		for(int i=1; i<=N; i++) {
			double x= Math.random();
			double y= Math.random();
			if((x*x+y*y)<1) count++;
		}
		return (double)4*count/N;
	}
	public static double computeError(double p) {
		double pi= Math.PI;
		return Math.abs(p-pi);
	}
	
	public static double generateRandomV(double lambda) {
		double u= Math.random();
		return -Math.log(1-u)/lambda;
	}
	public static double pv(double v, double lambda) {
		if(v<0) return 0;
		return lambda*Math.pow(Math.E, -lambda*v); 
	}
	public static double fv(double v, double lambda) {
		return 1-Math.pow(Math.E, -lambda*v);
	}

}
