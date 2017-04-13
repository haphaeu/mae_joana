import static java.lang.Math.PI;
import static java.lang.Math.tan;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
//import static java.lang.Math.asinh;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

/**
 * Catenary Lib
 * ============
 * 
 * Solvers for catenary equations with touch down.
 * 
 * The functions below solve the catenary equation (with touch down), given any
 * 2 parameters. All functions return a tuple with all 5 catenary parameters:
 * TA, V, H, L, MBR
 * 
 * This is an upgrade to Python/PyQt4 of the old app written back in 2010 using MS VBA
 * 
 * Created on Wed 03 Sep 2016, in Python
 * Translated into Java 11 Apr 2017
 * 
 * @author: raf
 */

class CatenaryParameters {
	private double topAngle;
	private double verticalDistance;
	private double horizontalDistance;
	private double length;
	private double mbr;
	public CatenaryParameters(double ta, double vd, double hd, double l, double mbr) {
		topAngle = ta;
		verticalDistance = vd;
		horizontalDistance = hd;
		length = l;
		this.mbr = mbr;
	}
	public double[] getParameters () {
		double[] retval = {topAngle, verticalDistance, horizontalDistance, length, mbr};
		return retval;
	}
}
 
class CatenaryLib {

	public static CatenaryParameters CatenaryCalcTAV(double TA, double V) {
		//CatenaryCalcTAV(TA, V) -> TA, V, H, L, MBR
		// top angle w horizontal, in radians
		double theta = (90.0 - TA) * PI / 180.0;
		// minimum bend radius
		double MBR = V * cos(theta) / (1.0 - cos(theta));
		// curvilinear length
		double L = MBR * tan(theta);
		// horizontal distance
		double H = MBR * asinh(tan(theta));
		// return catenary parameters
		return new CatenaryParameters(TA, V, H, L, MBR);
	}

	public static CatenaryParameters CatenaryCalcTAH(double TA, double H) {
		//CatenaryCalcTAH(TA, H) -> TA, V, H, L, MBR
		// top angle w horizontal, on radians
		double theta = (90.0 - TA) * PI / 180.0;
		// minimum bend radius
		double MBR = H / asinh(tan(theta));
		// vertical distance
		double V = MBR / (cos(theta) / (1.0 - cos(theta)));
		// curvilinear length
		double L = MBR * tan(theta);
		return new CatenaryParameters(TA, V, H, L, MBR);
	}

	public static CatenaryParameters CatenaryCalcTAL(double TA, double L) {
		//CatenaryCalcTAL(TA, L) -> TA, V, H, L, MBR
		// top angle w horizontal, on radians
		double theta = (90.0 - TA) * PI / 180.0;
		// minimum bend radius
		double MBR = L / tan(theta);
		// vertical distance
		double V = MBR / (cos(theta) / (1.0 - cos(theta)));
		// horizontal distance
		double H = MBR * asinh(tan(theta));
		return new CatenaryParameters(TA, V, H, L, MBR);
	}

	public static CatenaryParameters CatenaryCalcTAMBR(double TA, double MBR) {
		//CatenaryCalcTAMBR(TA, MBR) -> TA, V, H, L, MBR
		// top angle w horizontal, on radians
		double theta = (90.0 - TA) * PI / 180.0;
		// vertical distance
		double V = MBR / (cos(theta) / (1.0 - cos(theta)));
		// curvilinear length
		double L = MBR * tan(theta);
		// horizontal distance
		double H = MBR * asinh(tan(theta));
		return new CatenaryParameters(TA, V, H, L, MBR);
	}

	public static CatenaryParameters CatenarySolveL_MBR(double L, double MBR) {
		//CatenarySolveL_MBR(L, MBR) -> TA, V, H, L, MBR
		double TAi = 0.0;
		double delta = 1.0;
		double tolerance = 0.00000000001; // esta tolerance precia ser muito menor
		TAi = TAi + delta;
		CatenaryParameters retval = CatenaryCalcTAL(TAi, L);
		double erro = retval.getParameters()[4] - MBR;
		double erroi;
		while (abs(erro) > tolerance) {
			TAi += delta;
			retval = CatenaryCalcTAL(TAi, L);
			erroi = retval.getParameters()[4] - MBR;
			if (erro * erroi < 0) {
				TAi -= delta;
				delta /= 2.0;
			} else {
				erro = erroi;
			}
		}
		return CatenaryCalcTAL(TAi, L); // poderia retornar retval aqui...
	}

	public static CatenaryParameters CatenarySolveH_LMBR(double H, double param, String flag) {
		/*
		* CatenarySolveH_LMBR(H, param, flag) -> TA, V, H, L, MBR
        *
		* if flag == "L":
		* 	CatenarySolveH_LMBR(H, L, "L") -> TA, V, H, L, MBR
		* if flag == "MBR":
		* 	CatenarySolveH_LMBR(H, MBR, "MBR") -> TA, V, H, L, MBR
		*/
		double TAi = 0.0;
		double delta = 1.0;
		double tolerance = 0.0000001;
		TAi += delta;
		int idx;
		if (flag.toUpperCase().equals("L")) {
			idx = 3;
		} else if (flag.toUpperCase().equals("MBR")) {
			idx = 4;
		} else {
			System.out.println("Erro no flag da chamada de CatenarySolveH_LMBR.");
			idx = -1;
		}
		CatenaryParameters retval = CatenaryCalcTAH(TAi, H);
		double erro = retval.getParameters()[idx] - param;
		double erroi;
		while (abs(erro) > tolerance) {
			TAi += delta;
			retval = CatenaryCalcTAH(TAi, H);
			erroi = retval.getParameters()[idx] - param;
			if (erro * erroi < 0) {
				TAi -= delta;
				delta /= 2.0;
			} else {
				erro = erroi;
			}
		}
		return CatenaryCalcTAH(TAi, H);  // poderia retornar retval aqui ...
	}

	public static CatenaryParameters CatenarySolveV_HLMBR(double V, double param, String flag) {
		/*
		* CatenarySolveV_HLMBR(V, param, flag) -> TA, V, H, L, MBR
		* 	Solve catenary parameters given V and either H, L or MBR.
        * 
		* if flag == "H":
		* 	CatenarySolveV_HLMBR(V, H, "H") -> TA, V, H, L, MBR
		* if flag == "L":
		* 	CatenarySolveV_HLMBR(V, L, "L") -> TA, V, H, L, MBR
		* if flag == "MBR":
		* 	CatenarySolveV_HLMBR(V, MBR, "MBR") -> TA, V, H, L, MBR
		*/
		double TAi = 0.0;
		double delta = 1.0;
		double tolerance = 0.0000001;
		TAi += delta;
		int idx;
		if (flag.toUpperCase().equals("H")) {
			idx = 2;
		} else if (flag.toUpperCase().equals("L")) {
			idx = 3;
		} else if (flag.toUpperCase().equals("MBR")) {
			idx = 4;
		} else {
			System.out.println("Erro no flag da chamada de CatenarySolveH_LMBR.");
			idx = -1;
		}
		CatenaryParameters retval = CatenaryCalcTAV(TAi, V);
		double erro = retval.getParameters()[idx] - param;
		double erroi;
		while (abs(erro) > tolerance) {
			TAi += delta;
			retval = CatenaryCalcTAV(TAi, V);
			erroi = retval.getParameters()[idx] - param;
			if (erro * erroi < 0) {
				TAi -= delta;
				delta /= 2.0;
			} else {
				erro = erroi;
			}
		}
		return CatenaryCalcTAV(TAi, V);
	}
	
	public static double[] reactions(double TA, double L, double w) {
		/*
		`reactions(TA, L, w) -> (Fh, Fv, F)`
			Reaction forces due to catenary weight.

		Parameters
		----------
		TA : float
			top angle with vertical, in degrees.
		L : float
			length of suspended line, im meters.
		w : float
			unit weight of line, in kg/m
		Output
		------
		(Fh, Fv, F) : tuple of floats, in kN
			Horizontal, vertical and total reaction force at the top of the line
			due to self weight.
		*/
		double mg = 9.80665/1000.0;
		double Fv = w * L * mg;
		double Fh = Fv * tan(toRadians(TA));
		double F = Fv / cos(toRadians(TA));
		double[] retval = {Fh, Fv, F};
		return retval;
	}
	
	static double asinh(double x) {
		return log(x + sqrt(pow(x, 2.0) + 1.0));
	}
	
	static boolean unit_tests() {
		//Test all catenary functions for a known solution.
		double TA = 12.0;
		double V = 1800.0;
		double H = 1064.3904537756875;
		double L = 2222.8148817630927;
		double MBR = 472.47388849652003;
	    
		boolean[] all_array = {allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenaryCalcTAV(TA, V)),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenaryCalcTAH(TA, H)),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenaryCalcTAL(TA, L)),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenaryCalcTAMBR(TA, MBR)),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenarySolveH_LMBR(H, L, "L")),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenarySolveH_LMBR(H, MBR, "MBR")),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenarySolveV_HLMBR(V, H, "H")),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenarySolveV_HLMBR(V, L, "L")),
							   allclose(new CatenaryParameters(TA, V, H, L, MBR), CatenarySolveV_HLMBR(V, MBR, "MBR"))};
		if (all(all_array)) {
			return true;
		} else {
			return false;
		}
	}
	static boolean allclose(CatenaryParameters cp1, CatenaryParameters cp2) {
		final double EPS = 1e-6;
		double[] p1 = cp1.getParameters();
		double[] p2 = cp2.getParameters();
		for (int i=0; i < 5; i++) {
			System.out.println(String.format("%15.8f %15.8f", p1[i], p2[i]));
			if (abs(p1[i]-p2[i]) > EPS) {
				return false;
			}
		}
		return true;
	}
	static boolean all(boolean[] b) {
		for (boolean bi: b) {
			if (!bi) {
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) {
		System.out.println("Unit tests passed: " + unit_tests());
	}
}