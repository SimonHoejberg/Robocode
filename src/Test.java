public class Test 
{
	int vis;
    public void main( String[] args) throws Exception
    {        
    	boolean janus = true;
        int a = 5;
        int vis = 3;
        boolean donf = true;
        boolean donmutter = donf == janus;
        int c = (a + 7) * vis / 2;
        if (a == vis)
        	doThings(c);
    }
    
    public void doThings(int c) {
    	c++;
    	vis = c;
    }
}
