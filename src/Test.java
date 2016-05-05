public class Test 
{
	int vis;
    public void main( String[] args) throws Exception
    {        
        int a = 5;
        int vis = 3;
        boolean donf = true;
        int c = (a + 7) * vis / 2;
        doThings(c);
    }
    
    public void doThings(int c) {
    	c++;
    	vis = c;
    }
}
