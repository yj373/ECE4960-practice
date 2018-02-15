package A1;

import java.io.BufferedWriter;
import java.math.BigDecimal;

public class Functions {
	public static void handleException(BufferedWriter output_1, BufferedWriter output_2, Exception e, String task) {
		try {
			output_1.write("The first-level report for "+task+" : ");
			output_2.write("The second-level report for "+task+" : ");
			output_1.write("An Exception is caught!\n");
			output_1.flush();
			output_2.write("An Exception is caught: "+e.getMessage()+"\n");
			output_2.write("\n");
			output_2.flush();
		}catch (Exception e_) {
			System.out.println(e_.getMessage());
		}
	}
	public void integerOverflow(BufferedWriter output_1, BufferedWriter output_2) {
		//using factorial to test integer overflow
		//two-level output
		try {
			int res=1;
			output_1.write("The first-level report for Integer overflow: ");
			output_2.write("The second-level report for Integer Overflow:\n");
			//try to generate integer overflow by computing factorial
			for(int i=2;i<=40;i++) {
				int last= res;
				res=res*i;
				//call the corresponding test method to test integer overflow
				Tests tester= new Tests();
				boolean isOverflow= tester.integerOverflowTest(last, res, i);
				//if integer overflow happens, break the loop and write the corresponding two-level reports
				if(isOverflow) {
					output_1.write("Overflow happens when compute"+i+"!"+"\n");
					output_2.write("compute"+i+"!\t");
					output_2.write("result:"+res+"\n");
					break;
				}
				output_2.write("compute"+i+"!\t");
				output_2.write("result:"+res+"\n");
			}
			output_2.write("\n");
			output_2.flush();
			output_1.flush();
			
		}catch(Exception e) {
			handleException(output_1, output_2, e, "integer overflow");
		}
		
	}
	
	public void integerDividedBy_0(BufferedWriter output_1, BufferedWriter output_2) {
		try {
			int res= 1/0;
			output_1.write("The first-level report for Integer divided by 0: ");
			output_2.write("The second-level report for Integer divided by 0: ");
			output_1.write("1/0="+res+"\n");
			output_1.flush();
			output_2.write("1/0="+res+"\n");
			output_2.write("\n");
			output_2.flush();
		}catch(Exception e) {
			handleException(output_1, output_2, e, "integer divided by zero");
			
		}
	}
	
	public void floatPointOverflow(BufferedWriter output_1, BufferedWriter output_2) {
		try {
			double res= 1.7e300;
			output_1.write("The first-level report for float-point overflow: ");
			output_2.write("The second-level report for float-point overflow:\n");
			//multiply res by 10 and iterate this step ten times to generate overflow
			for (int i=1; i<=10; i++) {
				res=res*10;
				int order=300+i;
				//create a tester to test float-point overflow
				//if float-point overflow happens, break the loop
				Tests tester= new Tests();
				boolean isFloatPointOverflow= tester.floatPointOverflowTest(res);
				if(isFloatPointOverflow) {
					output_1.write("overflow happens when: "+"Ground truth: 1.7e"+order+" fact: "+res+"\n");
					output_2.write("Ground truth: 1.7e"+order+"\tfact:"+res+"\n");
					break;
				}
				output_2.write("Ground truth: 1.7e"+order+"\tfact:"+res+"\n");
			}
			output_2.write("\n");
			output_1.flush();
			output_2.flush();
		}catch(Exception e) {
			handleException(output_1, output_2,e, "float-point overflow");
			
		}
	}
	
	public void infAndNinf(BufferedWriter output_1, BufferedWriter output_2) {
		try {
			output_1.write("The first-level report for INF and NINF: ");
			output_2.write("The second-level report for INF and NINF:\n");
			//generate INF and NINF
			double inf= 1.0/0.0;
			double nInf= -1.0/0.0;
			double reciInf= 1.0/inf;
			double reciNinf= 1.0/nInf;
			double sinInf= Math.sin(inf);
			double sinNinf= Math.sin(nInf);
			double expInf= Math.exp(inf);
			double expNinf= Math.exp(nInf);
			output_2.write("x="+inf+"\tsin(x)="+sinInf+"\t1/x="+reciInf+"\t"+"exp(x)="+expInf+"\n");
			output_2.write("x="+nInf+"\tsin(x)="+sinNinf+"\t1/x="+reciNinf+"\t"+"exp(x)="+expNinf+"\n");
			output_2.write("INF*INF="+inf*inf+"\tNINF*NINF="+nInf*nInf+"\tINF*NINF="+inf*nInf+"\n");
			output_2.write("INF/INF="+inf/inf+"\tNINF/NINF="+nInf/nInf+"\tINF/NINF="+inf/nInf+"\n");
			output_2.write("INF+INF="+(inf+inf)+"\tNINF+NINF="+(nInf+nInf)+"\tINF+NINF="+(inf+nInf)+"\n");
			output_2.write("INF-INF="+(inf-inf)+"\tNINF-NINF="+(nInf-nInf)+"\tINF-NINF="+(inf-nInf)+"\n");
			output_2.write("\n");
			output_1.write("No Exception found!\n");
			output_1.flush();
			output_2.flush();
		}catch(Exception e) {
			handleException(output_1, output_2, e, "INF and NINF");
		}
		
	}
	
	public void nanObservation(BufferedWriter output_1, BufferedWriter output_2) {
		try {
			output_1.write("The first-level report for NAN: ");
			output_2.write("The second-level report for NAN:\n");
			double nan= Double.POSITIVE_INFINITY-Double.POSITIVE_INFINITY;
			if(Double.isNaN(nan)) {
				output_1.write("NAN is detected! ");
				output_2.write("NAN is detected!\n");
			}else {
				output_1.write("NAN is not detected!");
				output_2.write("NAN is not detected!\n");
			} 
			double reciNan= 1.0/nan;
			double sinNan= Math.sin(nan);
			double expNan= Math.exp(nan);
			output_2.write("x="+nan+"\tsin(x)="+sinNan+"\t1/x="+reciNan+"\t"+"exp(x)="+expNan+"\n");
			output_2.write("NAN*NAN="+nan*nan+"\tNAN/NAN="+nan/nan+"\tNAN+NAN="+(nan+nan)+"\tNAN-NAN="+(nan-nan)+"\n");
			output_2.write("\n");
			output_1.write("No Exception found!\n");
			output_1.flush();
			output_2.flush();
			
		}catch(Exception e) {
			handleException(output_1, output_2, e, "NAN observation");
		}
	}
	
	public void signedZeroObservation(BufferedWriter output_1, BufferedWriter output_2) {
		try {
			output_1.write("The first-level report for signed zero: ");
			output_2.write("The second-level report for signed zero:\n");
			double posZero= 1.0/(1.0/0.0);
			double negZero= 1.0/(-1.0/0.0);
			Tests tester= new Tests();
			boolean isPosZero= tester.isPositiveZero(posZero);
			boolean isNegZero= tester.isNegativeZero(negZero);
			//only if positive zero and negative zero are successfully created, we can do the following steps
			if(isPosZero&&isNegZero) {
				double logPosZero= Math.log(posZero);
				double logNegZero= Math.log(negZero);
				double funcPosZero= Math.sin(posZero)/posZero;
				double funcNegZero= Math.sin(negZero)/negZero;
				double funcAbsNEgZero= Math.sin(negZero)/Math.abs(negZero);
				output_2.write("x="+posZero+"\tlog(+0)="+logPosZero+"\tsin(+0)/(+0)="+funcPosZero+"\t"+"\n");
				output_2.write("x="+negZero+"\tlog(-0)="+logNegZero+"\tsin(-0)/(-0)="+funcNegZero+"\t"+"\tsin(-0)/(|-0|)="+funcAbsNEgZero+"\n");
				output_2.write("\n");
				output_1.write("Successfully create signed zero! No Exception found!\n");
			}else {
				output_1.write("Fail to create signed zero!\n");
				output_2.write("Fail to create signed zero!\n\n");
			}
			
			output_1.flush();
			output_2.flush();
		}catch(Exception e) {
			handleException(output_1, output_2, e, "signed zero");
		}
		
	}
	
	public void floatPointUnderflow(BufferedWriter output_1, BufferedWriter output_2) {
		try {
			output_1.write("The first-level report for float-point underflow:\n");
			output_2.write("The second-level report for float-point underflow:\n");
			double x= 1.2345678e-312;
			double y= 1.2345677e-312;
			double m=1e-314;
			boolean flag1=false, flag2=false, flag3=false;
			for(int i=0; i<10; i++) {
				x=x/10;
				y=y/10;
				m=m/10;
				double res= x-y;
				output_2.write("x="+x+"\ty="+y+"\tx-y="+res+"\t"+(x==y)+"\n");
				if(res==0.0&&!flag1) {
					output_1.write("x-y Underflow happens when: x="+x+"\ty="+y+"\n");
					flag1=true;
				}
				double res_2= x/y;
				output_2.write("x="+x+"\ty="+y+"\tx/y="+res_2+"\n");
				if(res_2==1.0&&!flag2) {
					output_1.write("x/y Underflow happens when: x="+x+"\ty="+y+"\n");
					flag2=true;
				}
				double res_3= Math.sin(1.23456789012345*m)/m;
				output_2.write("x="+m+"\tsin(1.23456789012345*x)/x="+res_3+"\n\n");
				if(Double.isNaN(res_3)&&!flag3) {
					output_1.write("sin(1.23456789012345*x)/x Underflow happens\n");
					flag3=true;
				}
			}
			output_1.flush();
			output_2.flush();
			
		}catch (Exception e) {
			handleException(output_1, output_2,e, "float-point underflow");
		}
	}
	
	public void calculatePi(BufferedWriter output_2) {
		try {
			BigDecimal res= new BigDecimal((6*Math.atan(0.125)+2*Math.atan(1.0/57.0)+Math.atan(1.0/239.0))*4);
			output_2.write("Compute pi"+res);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	
}
