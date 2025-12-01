package Checkers;

public class BoxChecker extends Checker
{
    public BoxChecker (int id,int[]boxValues)
    {
        super(id,boxValues);
    }

    @Override
    
    public void runCheck(){
        detectDuplicates();
    }
}