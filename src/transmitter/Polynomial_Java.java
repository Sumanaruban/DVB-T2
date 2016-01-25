package transmitter;///FINAL bch ENCODER
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Polynomial_Java {

    private int[] coef;  // coefficients
    private int deg;     // degree of polynomial (0 for the zero polynomial)
    
    public ArrayList<FEC_Frame> bchEncode(ArrayList<FEC_Frame> frames) { 
        
    int framecount = frames.size();
    ArrayList<FEC_Frame> bchencodedframes = new ArrayList<FEC_Frame>();
    
    
    	for (int k = 0; k < framecount; k++) {
    		boolean[] outarray = new boolean[32400];    
    		Polynomial_Java zero = new Polynomial_Java(0, 0);
    		Polynomial_Java p = zero;
    		Polynomial_Java pp = zero;
    		
    		Polynomial_Java gtotal = new Polynomial_Java(1, 0);
    		Polynomial_Java[] arr = new Polynomial_Java[32208];
    		FEC_Frame current_frame = frames.get(k);
    		boolean[] bits = current_frame.FEC_frame;
    		System.arraycopy(bits, 0, outarray, 0, bits.length);
    		    		
    		
    		//-------------------------------------------------------------------------------
    		
    		for( int i=0; i<32208; i++ ){
    			//arr[i] = new Polynomial(input[i], i); OUT OF MEMORY
    			if (bits[i] == true) {
    				arr[i] = new Polynomial_Java(1, i);
				} else {
					arr[i] = new Polynomial_Java(0, i);
				}
    			
    		}
    		
    		for( int i=0; i<32208; i++ ){
    			p    = arr[i].plus(p); 
    		}
    		
    		
    		Polynomial_Java p192   = new Polynomial_Java(1, 192);
    		Polynomial_Java p2   = new Polynomial_Java(1, 2);
    		Polynomial_Java s    = p.times(p192); // multiply by x^192
    		//////////////////////////////////////////////////////////////////////////////
    		Polynomial_Java[] arrr = new Polynomial_Java[32400];
    		
    		for( int i=0; i<192; i++ ){
    			arrr[i] = new Polynomial_Java(1, i);
    		}
    		for( int i=0; i<192; i++ ){
    			pp    = arrr[i].plus(pp); 
    		}
    		System.gc();
    		//Polynomial ss    = s.divide(gtotal); // divide by 
    		System.out.println("p =        " + p);
    		System.out.println("pp =        " + pp);
    		///////////////////////////////////////////////////////////////////////////////// 
    		
    		Polynomial_Java[] g = new Polynomial_Java[17];//generator polynomial
    		for( int i=0; i<17; i++ ){
    			g[i] = new Polynomial_Java(1, i);
    		}
    		Polynomial_Java[] gg = new Polynomial_Java[13];
    		gg[1]= g[0].plus(g[2]).plus(g[3]).plus(g[5]).plus(g[16]);
    		gg[2]= g[0].plus(g[1]).plus(g[4]).plus(g[5]).plus(g[6]).plus(g[8]).plus(g[16]);
    		gg[3]= g[0].plus(g[2]).plus(g[3]).plus(g[4]).plus(g[5]).plus(g[7]).plus(g[8]).plus(g[9]).plus(g[10]).plus(g[11]).plus(g[16]);
    		gg[4]= g[0].plus(g[2]).plus(g[4]).plus(g[6]).plus(g[9]).plus(g[11]).plus(g[12]).plus(g[14]).plus(g[16]);
    		gg[5]= g[0].plus(g[2]).plus(g[3]).plus(g[5]).plus(g[8]).plus(g[9]).plus(g[10]).plus(g[11]).plus(g[12]).plus(g[16]);
    		gg[6]= g[0].plus(g[2]).plus(g[4]).plus(g[5]).plus(g[7]).plus(g[8]).plus(g[9]).plus(g[10]).plus(g[12]).plus(g[13]).plus(g[14]).plus(g[15]).plus(g[16]);
    		gg[7]= g[0].plus(g[2]).plus(g[5]).plus(g[6]).plus(g[8]).plus(g[9]).plus(g[10]).plus(g[11]).plus(g[13]).plus(g[15]).plus(g[16]);
    		gg[8]= g[0].plus(g[1]).plus(g[2]).plus(g[5]).plus(g[6]).plus(g[8]).plus(g[9]).plus(g[12]).plus(g[13]).plus(g[14]).plus(g[16]);
    		gg[9]= g[0].plus(g[5]).plus(g[7]).plus(g[9]).plus(g[10]).plus(g[11]).plus(g[16]);
    		gg[10]= g[0].plus(g[1]).plus(g[2]).plus(g[5]).plus(g[7]).plus(g[8]).plus(g[10]).plus(g[12]).plus(g[13]).plus(g[14]).plus(g[16]);
    		gg[11]= g[0].plus(g[2]).plus(g[3]).plus(g[5]).plus(g[9]).plus(g[11]).plus(g[12]).plus(g[13]).plus(g[16]);
    		gg[12]= g[0].plus(g[1]).plus(g[5]).plus(g[6]).plus(g[7]).plus(g[9]).plus(g[11]).plus(g[12]).plus(g[16]);
    		
    		// for( int i=0; i<17; i++ ){
    		//    p1    = g1[i].plus(p1); 
    		//}
    		for( int i=1; i<12; i++ ){
    			//  System.out.println("g(x) =        " + gg[i]); 
    		}
    		
    		for( int i=1; i<13; i++ ){
    			gtotal    = gg[i].times(gtotal); 
    		} 
    		System.gc();
    		System.out.println("generator polynomial =        " + gtotal); //total generator polynomial
    		System.out.println("s =        " + s);
    		
    		// Polynomial remainder[] = new Polynomial[192];
    		Polynomial_Java remainder;
    		remainder = p.divide(pp); // divide by 
    		System.out.println("remainder =        " + remainder  );
    		
    		///////////////////////////////////////////////////////////////////////////output here
    		
    		for( int i=32208; i<32400; i++ ){
    			
    			if(remainder.coef[i-32208] == 1)
    				outarray[i] = true;
    			else
    				outarray[i] = false; 
    			// System.out.println("output =        " + input[i]  );
    			
    		}
    		
    		bchencodedframes.add(new FEC_Frame(outarray));
    		//////////////////////////////////////////////////////////////////////////////////
    		for( int i=0; i<32400; i++ ){
    			System.out.println("output =        " + outarray[i]  );
    			System.out.println("output =        " + i  );
    		} 
    		
    		
    	}
    	    	
    	return bchencodedframes;
			
		}
    
    ////////////////////////////////////////////////////////////////////////////////////////////    
    //divide method here
    
    public Polynomial_Java divide(Polynomial_Java b) {
        Polynomial_Java a = this;
        if(a.deg <= b.deg)return null;
        
        Polynomial_Java d[] = new Polynomial_Java[500000];
        Polynomial_Java dd[] = new Polynomial_Java[500000];
        Polynomial_Java ddd[] = new Polynomial_Java[500000];
        
         int i=0;
         d[i] = new Polynomial_Java(1, a.deg - b.deg);
         dd[i]    = b.times(d[i]); 
         ddd[i]   = a.minus(dd[i]);
         
        while (0 < ddd[i].deg - b.deg){
        i++;
         d[i] = new Polynomial_Java(1, ddd[i-1].deg - b.deg);
        dd[i]    = b.times(d[i]); 
        ddd[i]   = ddd[i-1].minus(dd[i]); 
        }
        return ddd[i];
    }
    
////////////////////////////////////////////////////////////////////////////////////////////
    
    

    // a * x^b
    private Polynomial_Java(int a, int b) {
        coef = new int[b+1];
        coef[b] = a;
        deg = degree();
    }
    
    
    public Polynomial_Java(){
    	
    }

    // return the degree of this polynomial (0 for the zero polynomial)
    public int degree() {
        int d = 0;
        for (int i = 0; i < coef.length; i++)
            if (coef[i] != 0) d = i;
        return d;
    }

    // return c = a + b
    public Polynomial_Java plus(Polynomial_Java b) {
        Polynomial_Java a = this;
        Polynomial_Java c = new Polynomial_Java(0, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] += b.coef[i];
        c.deg = c.degree();
        System.gc();
        return c;
    } 
    
    // return (a * b)
    public Polynomial_Java times(Polynomial_Java b) {
        Polynomial_Java a = this;
        Polynomial_Java c = new Polynomial_Java(0, a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i+j] += (a.coef[i] * b.coef[j]);
        c.deg = c.degree();
        System.gc();
        return c;
    }
    

    // return (a - b)
    public Polynomial_Java minus(Polynomial_Java b) {
        Polynomial_Java a = this;
        Polynomial_Java c = new Polynomial_Java(0, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] -= b.coef[i];
        c.deg = c.degree();
        System.gc();
        return c;
    }

    
    // convert to string representation
    @Override
    public String toString() {
        if (deg ==  0) return "" + coef[0];
        if (deg ==  1) return coef[1] + "x + " + coef[0];
        String s = coef[deg] + "x^" + deg;
        for (int i = deg-1; i >= 0; i--) {
            if      (coef[i] == 0) continue;
            else if (coef[i]  > 0) s = s + " + " + ( coef[i]);
            else if (coef[i]  < 0) s = s + " - " + (-coef[i]);
            if      (i == 1) s = s + "x";
            else if (i >  1) s = s + "x^" + i;
        }
        return s;
    }
    
    public ArrayList<FEC_Frame> addrandom (ArrayList<FEC_Frame> frames){
    	 int framecount = frames.size();
    	 ArrayList<FEC_Frame> bchencodedframes = new ArrayList<FEC_Frame>();
		 boolean[] outarray = new boolean[32400];
    	 
    	 for (int i = 0; i < framecount; i++) {
    		 FEC_Frame current_frame = frames.get(i);
    		 boolean[] bits = current_frame.FEC_frame;
    	 	 System.arraycopy(bits, 0, outarray, 0, bits.length);
    	 	 
    	 	 bchencodedframes.add(new FEC_Frame(outarray));
		 }
    	 return bchencodedframes;
    	
    }
   
}