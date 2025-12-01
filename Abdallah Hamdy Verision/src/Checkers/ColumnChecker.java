package Checkers;

public class ColumnChecker extends Checker
{
    public ColumnChecker (int id,int[]columnValues)
    {
        super(id,columnValues);
    }

    @Override
    
    public void runCheck(){
        detectDuplicates();
    }
}