package NumJa;
import java.util.Random;
public class np {

    public static ndarray array(double[][] data) {
        return new ndarray(data);
    }





    public static ndarray softmax(ndarray a) {
    ndarray expA = exp(a);
    double sum = expA.sum();
    return multiply(expA, 1.0 / sum);
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


    //------ADD------

    public static ndarray add(ndarray a, ndarray b) {
        int[] shapeA = a.shape();
        int[] shapeB = b.shape();
        //TODO
        // if (shapeA[0] != shapeB[0] || shapeA[1] != shapeB[1]) {
        //     throw new NumJaException("Add Error: Shapes must match!");
        // }
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
        // if (shapeA[0] != shapeB[0] || shapeA[1] != shapeB[1]) {
        //     throw new NumJaException("Add Error: Shapes must match!");
        // }
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


    //------------------Matrix Multiplication---------------------
    public static ndarray matmul(ndarray a, ndarray b) {
        // 3x4 
        // 4x3

        int[] shapeA = a.shape();
        int[] shapeB = b.shape();
        // if (shapeA[0] != shapeB[1] || shapeA[1] != shapeB[0]) {
        //     throw new NumJaException("Matrix Multiplication error : Shapes doesnot match!");
        // }
        
        int t_row=shapeA[0];
        int t_col=shapeB[1];

        double[][] result_matrix= new double[t_row][t_col];


        // m x n @ n x p    0<=k<=n-1
        // 
        int n=shapeA[1];

        for(int k=0;k<n;k++){
            for(int i=0;i<t_row;i++){
                for(int j=0;j<t_col;j++){
                    result_matrix[i][j]+=a.get(i,k)*b.get(k,j);
                    // System.out.println(a.get(i,k)+" "+b.get(k,j));
                }
            }
        }

        ndarray res = new ndarray(result_matrix);
        return res;
    }

    public static ndarray dot(ndarray a, ndarray b) {
        return matmul(a, b); //same
    }



}


