package NumJa;
import NumJa.exceptions.*;

public class ndarray{
    private int rows;
    private int columns;
    private double[][] data;

    ndarray(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    ndarray(double[][] data) {
        this.rows = data.length;
        this.columns = data[0].length;
        this.data = data;
    }


    private int[] parsePart(String part, int maxSize){
        if(part.equals(":")){
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

    public int[] shape(){
        return new int[] {rows,columns};
    }
  
    //I am gonna use this for Matrix multiplication
    //They cannot be reached from outside
    double[][] raw() {
        return data;
    }
    double[] flattenedData() {
        double[] flat = new double[rows * columns];
        int index = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                flat[index++] = data[i][j];
            }
        }

    return flat;
    }





    //---------------------------Get----------------------
    public double get(int r,int c){
        return data[r][c];
    }



    public ndarray get(String slice){
        slice = slice.replaceAll("\\s", "");

        String[] parts= slice.split(",");

        if (parts.length < 1 || parts.length > 2){
            throw new InvalidShapeException("Slice must have 1 or 2 parts, got " + parts.length);
        }
        String rowpart = parts[0];
        String columnpart=(parts.length == 2 && !parts[1].isEmpty()) ? parts[1] : ":";

        int[] rowRange = parsePart(rowpart, rows);
        int[] colRange = parsePart(columnpart, columns);
        
        int rowStart = rowRange[0];
        int rowEnd = rowRange[1];
        int colStart = colRange[0];
        int colEnd = colRange[1];
        
        if (rowStart < 0 || rowEnd > rows || colStart < 0 || colEnd > columns) {
            throw new OutOfBoundsException("Slice out of bounds: rows [" + rowStart + ":" + rowEnd + "] cols [" + colStart + ":" + colEnd + "] for shape " + rows + "x" + columns);
        }
        
        double[][] result = new double[rowEnd - rowStart][colEnd - colStart];
        
        for(int i = rowStart; i < rowEnd; i++){
            for(int j = colStart; j < colEnd; j++){
                result[i - rowStart][j - colStart] = data[i][j];
            }
        }
        return new ndarray(result);
    }
    public void set(int i, int j, double val) {
        if (i < 0 || i >= rows || j < 0 || j >= columns) {
            throw new OutOfBoundsException("Index (" + i + ", " + j + ") out of bounds for array of shape (" + rows + ", " + columns + ")");
        }
        this.data[i][j] = val;
    }



    public double max(){

        if (rows==0 || columns == 0) {
        throw new IllegalStateException("max(): Array is empty");
        }else{
        double res=data[0][0];
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(res<data[i][j]){res=data[i][j];}
            }
            }
        return res;
        }
        
    }
    public double min(){

        if (rows==0 || columns == 0) {
        throw new IllegalStateException("max(): Array is empty");
        }else{
        double res=data[0][0];
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(res>data[i][j]){res=data[i][j];}
            }
        }
        return res;
        }
        
    }
    public double sum(){
        
        //TODO:exceptions 
        //if 


        double total=0;
        
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                total+=data[i][j];
            }
        
        }
        return total;
    }

    public double mean(){
        int totalElements=rows*columns;

        double result=sum()/totalElements;
        
        return result; 

    } 










    //I could use void and make it more faster but it would cost me some ease of use in the future
    //And in small project like that it doesnot matter to much so: 
    public ndarray transpose(){
        if (rows==0 || columns == 0) {throw new IllegalStateException("transpose(): Array is empty");}

        double[][] transposed= new double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                transposed[j][i] = data[i][j];
            }
    }
        return new ndarray(transposed);


    } 
    
    public ndarray reshape(int target_row,int target_column){
        int totalElements=rows*columns;
        int target_element=target_row*target_column;

        if (totalElements!=target_element){
            throw new ReshapeException(
                "Cannot reshape NumJa array: "+totalElements+
                " elements do not match up with new shape: (" +target_row+","+target_column+")"
            );
        }

        double[][] reshaped_array = new double[target_row][target_column];
        int index=0;
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
            reshaped_array[index/target_column][index%target_column]=data[i][j];
            index++;
            }
        }

        return new ndarray(reshaped_array);
    }
    public ndarray flatten() {
        return reshape(1, rows * columns);
    }
    public ndarray copy() {
    double[][] newData = new double[rows][columns];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            newData[i][j] = this.data[i][j];
        }
    }
    return new ndarray(newData);
}


}

