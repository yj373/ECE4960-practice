package A1;

public class Tests {
	public boolean integerOverflowTest(double last, double pres, int n) {
		return !(last==pres/n);
	}
	public boolean floatPointOverflowTest(double num) {
		return Double.isInfinite(num);
	}
	public boolean isPositiveZero(double x) {
		return 1/x>0;
	}
	public boolean isNegativeZero(double x) {
		return 1/x<0;
	}
}
