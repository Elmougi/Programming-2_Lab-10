package Checkers;

public class RowChecker extends Checker
{
    public RowChecker (int id,int[]rowValues)
    {
        super(id,rowValues);
    }

    @Override
    
    public void runCheck(){
        detectDuplicates();
    }
}