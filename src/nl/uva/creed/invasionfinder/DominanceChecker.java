package nl.uva.creed.invasionfinder;

public class DominanceChecker {
	
	
	public static boolean dominance(double aa, double ab, double ac, double ba,double bb, double bc, double ca, double cb, double cc) {
		return dominanceAB(aa,ab,ac,ba,bb,bc,ca,cb,cc) ||
		dominanceBA(aa,ab,ac,ba,bb,bc,ca,cb,cc) ||
		dominanceAC(aa,ab,ac,ba,bb,bc,ca,cb,cc) ||
		dominanceCA(aa,ab,ac,ba,bb,bc,ca,cb,cc) ||
		dominanceBC(aa,ab,ac,ba,bb,bc,ca,cb,cc) ||
		dominanceCB(aa,ab,ac,ba,bb,bc,ca,cb,cc);
	}
	
	public static boolean dominanceAB(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa >= ba && ab >= bb && ac >= bc) && oneStrictAB(aa,ab,ac,ba,bb,bc,ca,cb,cc);
	}
	
	public static boolean oneStrictAB(double aa, double ab, double ac, double ba,double bb, double bc, double ca, double cb, double cc) {
		return !(aa==ba && ab==bb && ac==bc);
	}

	public static boolean dominanceBA(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa <= ba && ab <= bb && ac <= bc) &&  oneStrictAB(aa,ab,ac,ba,bb,bc,ca,cb,cc);
	}
	
	public static boolean dominanceAC(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa >= ca && ab >= cb && ac >= cc) && oneStrictAC(aa,ab,ac,ba,bb,bc,ca,cb,cc);
	}
	
	public static boolean oneStrictAC(double aa, double ab, double ac, double ba,double bb, double bc, double ca, double cb, double cc) {
		return !(aa==ca && ab==cb && ac==cc);
	}
	
	public static boolean dominanceCA(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa <= ca && ab <= cb && ac <= cc) && oneStrictAC(aa,ab,ac,ba,bb,bc,ca,cb,cc);
	}
	
	public static boolean dominanceBC(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (ba >= ca && bb >= cb && bc >= cc) && oneStrictBC(aa,ab,ac,ba,bb,bc,ca,cb,cc);
	}
	
	public static boolean oneStrictBC(double aa, double ab, double ac, double ba,double bb, double bc, double ca, double cb, double cc) {
		return !(ba==ca && bb==cb && bc==cc);
	}
	
	public static boolean dominanceCB(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (ba <= ca && bb <= cb && bc <= cc) && oneStrictBC(aa,ab,ac,ba,bb,bc,ca,cb,cc);
	}
	

	public static boolean neutralCondition(double aa, double ab, double ac, double ba, double bb, double bc, double ca, double cb, double cc) {
		return (aa == ba && ba==ca 
				&& ab==bb && bb==cb 
				&& ac==bc && bc==cc);
	}


}
