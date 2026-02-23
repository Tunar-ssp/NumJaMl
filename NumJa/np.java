package NumJa;
import java.util.Random;
import java.io.*;
import NumJa.exceptions.*;
public class np {

    public static ndarray array(double[][] data) {
        return new ndarray(data);
    }


    public static ndarray softmax(ndarray a) {
        ndarray expA = exp(a);
        double sum = expA.sum();
        return multiply(expA, 1.0 / sum);
    }
    public static int argmax(ndarray a) {
        int r = a.shape()[0];
        int c = a.shape()[1];
        
        double maxVal = Double.NEGATIVE_INFINITY;
        int maxIndex = -1;

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                double current = a.get(i, j);
                if (current > maxVal) {
                    maxVal = current;
                    maxIndex = (i * c) + j;
                }
            }
        }
        return maxIndex;
    }

    public static ndarray exp(ndarray a) {
        int r = a.shape()[0];
        int c = a.shape()[1];
        double[][] result = new double[r][c];
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                result[i][j] = Math.exp(a.get(i, j));
            }
        }
        return new ndarray(result);
    }

    public static ndarray log(ndarray a) {
        int r = a.shape()[0];
        int c = a.shape()[1];
        double[][] result = new double[r][c];
        double epsilon = 1e-10;
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                result[i][j] = Math.log(a.get(i, j) + epsilon);
            }
        }
        return new ndarray(result);
    }
    public static ndarray maximum(ndarray a, ndarray b){
        int[] shapeA = a.shape();
        int[] shapeB = b.shape();
        if (shapeA[0] != shapeB[0] || shapeA[1] != shapeB[1]) {
            throw new InvalidShapeException("Maximum: shapes " + shapeA[0] + "x" + shapeA[1] + " and " + shapeB[0] + "x" + shapeB[1] + " must match");
        }
        int t_row = shapeA[0];
        int t_col = shapeA[1];
        
        double[][] arr = new double[t_row][t_col];
        for(int i = 0; i < t_row; i++){
            for(int j = 0; j < t_col; j++){
                arr[i][j] = Math.max(a.get(i, j), b.get(i, j));
            }
        }
        return new ndarray(arr);
    }
    
    public static ndarray maximum(double a, ndarray b){
        int t_row = b.shape()[0];
        int t_col = b.shape()[1];
        
        double[][] arr = new double[t_row][t_col];
        for(int i = 0; i < t_row; i++){
            for(int j = 0; j < t_col; j++){
                arr[i][j] = Math.max(a, b.get(i, j));
            }
        }
        return new ndarray(arr);
    }
    
    public static ndarray maximum(ndarray a, double b){
        return maximum(b, a);
    }

    public static ndarray power(ndarray a, double p) {
        int r = a.shape()[0];
        int c = a.shape()[1];
        double[][] result = new double[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                result[i][j] = Math.pow(a.get(i, j), p);
            }
        }
        return new ndarray(result);
    }

    public static double sum(ndarray a) {
        double total = 0;
        int r = a.shape()[0];
        int c = a.shape()[1];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                total += a.get(i, j);
            }
        }
        return total;
    }

    
    public static ndarray zeros(int r, int c) {
        return new ndarray(r, c); 
    }

    public static ndarray ones(int r, int c) {
        ndarray arr=new ndarray(r, c);
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                arr.set(i,j,1.0);
            }
        }
        return arr; 
    }

    
    public static ndarray random(int r, int c) {
        Random rand = new Random();
        ndarray arr=new ndarray(r, c);
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                arr.set(i,j,rand.nextDouble());
            }
        }
        return arr; 
    }

    public static ndarray arange(double start, double end, double step) {
        int length = (int) Math.ceil((end - start) / step);
        double[][] arr = new double[1][length];
        for(int i = 0; i < length; i++) {
            arr[0][i] = start + i * step;
        }
        return new ndarray(arr);
    }

    public static ndarray arange(double end) {
        return arange(0, end, 1);
    }

    public static ndarray linspace(double start, double end, int num) {
        double[][] arr = new double[1][num];
        if(num == 1) {
            arr[0][0] = start;
        } else {
            double step = (end - start) / (num - 1);
            for(int i = 0; i < num; i++) {
                arr[0][i] = start + i * step;
            }
        }
        return new ndarray(arr);
    }

    //------ADD------

    public static ndarray add(ndarray a, ndarray b) {
        int[] shapeA = a.shape();
        int[] shapeB = b.shape();
        if (shapeA[0] != shapeB[0] || shapeA[1] != shapeB[1]) {
            throw new InvalidShapeException("Add: shapes " + shapeA[0] + "x" + shapeA[1] + " and " + shapeB[0] + "x" + shapeB[1] + " must match");
        }
        int t_row=shapeA[0];
        int t_col=shapeA[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                
                val=a.get(i,j)+b.get(i,j);  
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray add(ndarray a, double b){

        int t_row=a.shape()[0];
        int t_col=a.shape()[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=a.get(i,j)+b;
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray add(double b,ndarray a){
        return add(a,b);
    }

    // public static ndarray  add(double a ,double b){
    // ??
        
    // }




    //------Multiply------

    public static ndarray multiply(ndarray a, ndarray b) {
        int[] shapeA = a.shape();
        int[] shapeB = b.shape();
        if (shapeA[0] != shapeB[0] || shapeA[1] != shapeB[1]) {
            throw new InvalidShapeException("Multiply: shapes " + shapeA[0] + "x" + shapeA[1] + " and " + shapeB[0] + "x" + shapeB[1] + " must match");
        }
        int t_row=shapeA[0];
        int t_col=shapeA[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                
                val=a.get(i,j)*b.get(i,j);  
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray multiply(ndarray a, double b){

        int t_row=a.shape()[0];
        int t_col=a.shape()[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=a.get(i,j)*b;
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray multiply(double b,ndarray a){
        return multiply(a,b);
    }

    //------Subtract------

    public static ndarray subtract(ndarray a, ndarray b) {
        int[] shapeA = a.shape();
        int[] shapeB = b.shape();
        int t_row=shapeA[0];
        int t_col=shapeA[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=a.get(i,j)-b.get(i,j);  
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray subtract(ndarray a, double b){
        int t_row=a.shape()[0];
        int t_col=a.shape()[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=a.get(i,j)-b;
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray subtract(double b,ndarray a){
        int t_row=a.shape()[0];
        int t_col=a.shape()[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=b-a.get(i,j);
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }

    //------Divide------

    public static ndarray divide(ndarray a, ndarray b) {
        int[] shapeA = a.shape();
        int[] shapeB = b.shape();
        int t_row=shapeA[0];
        int t_col=shapeA[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=a.get(i,j)/b.get(i,j);  
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray divide(ndarray a, double b){
        int t_row=a.shape()[0];
        int t_col=a.shape()[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=a.get(i,j)/b;
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray divide(double b,ndarray a){
        int t_row=a.shape()[0];
        int t_col=a.shape()[1];

        double[][] arr = new double[t_row][t_col];
        double val;
        for(int i=0;i<t_row;i++){
            for(int j=0;j<t_col;j++){
                val=b/a.get(i,j);
                arr[i][j]=val; 
            }
        }
        ndarray res=new ndarray(arr);
        return res;
    }
    public static ndarray mask(ndarray a, double threshold) {
        int r = a.shape()[0];
        int c = a.shape()[1];
        double[][] result = new double[r][c];
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                result[i][j] = (a.get(i, j) > threshold) ? 1.0 : 0.0;
            }
        }
        return new ndarray(result);
    }

    //------------------Matrix Multiplication---------------------
    public static ndarray matmul(ndarray a, ndarray b) {

        int[] shapeA = a.shape();
        int[] shapeB = b.shape();

        int rowsA = shapeA[0];
        int colsA = shapeA[1];
        int rowsB = shapeB[0];
        int colsB = shapeB[1];

        if (colsA != rowsB) {
            throw new DimensionMismatchException(
                "Matmul(): cannot multiply " +
                rowsA + "x" + colsA +
                " by " +
                rowsB + "x" + colsB
            );
        }

        double[] dataA = a.flattenedData();
        double[] dataB = b.flattenedData();
        double[] result = new double[rowsA * colsB];

        // Parallel over rows of A
        java.util.stream.IntStream.range(0, rowsA)
            .parallel()
            .forEach(i -> {
                for (int k = 0; k < colsA; k++) {           
                    double valA = dataA[i * colsA + k];     
                    int rowOffsetB = k * colsB; 
                    int rowOffsetResult = i * colsB;                

                    for (int j = 0; j < colsB; j++) { 
                        result[rowOffsetResult + j] += 
                            valA * dataB[rowOffsetB + j]; 
                    } 
                }  
            });  

       
        double[][] result2D = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) { 
            for (int j = 0; j < colsB; j++) { 
                result2D[i][j] = result[i * colsB + j]; 
            }
        }

        return new ndarray(result2D);
    }

    public static ndarray dot(ndarray a, ndarray b) {
        return matmul(a, b); //same
    }

    //-------------Saving and Loading------------
    public static void save(ndarray arr, String filename) throws IOException {
        if (!filename.endsWith(".njz")) filename += ".njz";

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            int[] shape = arr.shape();
            dos.writeInt(shape[0]); 
            dos.writeInt(shape[1]); 

            for (int i = 0; i < shape[0]; i++) {
                for (int j = 0; j < shape[1]; j++) {
                    dos.writeDouble(arr.get(i, j));
                }
            }
        }
    }

    public static ndarray load(String filename) throws IOException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            int rows = dis.readInt();
            int cols = dis.readInt();

            double[][] data = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    data[i][j] = dis.readDouble();
                }
            }
            return new ndarray(data);
        }
    }



}


