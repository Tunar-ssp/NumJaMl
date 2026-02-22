package NumJa;

public class NumJaArray {
    private int rows;
    private int columns;
    private double[][] data;

    public NumJaArray(int rows , int columns){
        this.rows = rows;
        this.columns= columns;
        this.data=new double[rows][columns];
    }
    public NumJaArray(double [][] data ){
        this.rows = data.length;
        this.columns= data[0].length;
        this.data= data;
    }


    //Here we override toString function  in String
    //Normally when we execute  System.out.println() it would call toString(from String class ) and execute it (print memory address at the end) 
    //But here we replace it with our own function 
    @Override
    public String toString(){
        String res="";
        for(int i=0;i<rows;i++){
            res +="[ ";
            for(int j=0;j<columns;j++){
                res+=data[i][j] + " ";
            }
            res +="]\n";
        }
        return res;
    }


    public int[] parsePart(String part, int maxSize){
        
        
        if(part==":"){
            return new int[]{0,maxSize};
        }


        if(part.contains(":")){
            String[] nums=part.split(":", -1);

            int start,end;

            if(nums[0].isEmpty()){
                start=0;
            }else{
                start=Integer.parseInt(nums[0]);
            }

            if(nums[1].isEmpty()){
                end=maxSize;
            }else{
                end=Integer.parseInt(nums[1]);
            }
            return new int[]{start,end};
        }

        //if there is no ":"
        // just one number like "3"
        // we would return {3,4}   (like [3,4))

        int index= Integer.parseInt(part);

        return new int[] {index,index+1};
    }



    public double [][] get(String slice){

        
        slice=slice.replace("\\s+", "");
        // this deletes any whitespace : \\s
        // +  : one or more 


        String[] parts= slice.split(",");

        if (parts.length !=2){
            throw new IllegalArgumentException("get() : It must have 2 parts");
        }
        String rowpart = parts[0];
        String columnpart=parts[1];

        int[] rowRange = parsePart(rowpart, rows);
        int[] colRange = parsePart(columnpart, columns);
        
        int rowStart = rowRange[0];
        int rowEnd = rowRange[1];
        int colStart = colRange[0];
        int colEnd = colRange[1];
        
        double[][] result = new double[rowEnd - rowStart][colEnd - colStart];
        
        try{
            for(int i = rowStart; i < rowEnd; i++){
                for(int j = colStart; j < colEnd; j++){
                    result[i - rowStart][j - colStart] = data[i][j];
                }
            }
            return result;
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught an error: " +e.getMessage());
            return null;
        }

    }
    


 
// set(i, j, val)

// transpose()

// reshape(r, c)

// max() 
// min() 
// mean() 
// sum()

// flatten()

// copy ()


}

