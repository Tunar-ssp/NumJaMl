package NumJa;
import java.util.Random;
public class NumJa {
    public static NumJaArray zeros(int r, int c) {
        return new NumJaArray(r, c); 
    }

    public static NumJaArray ones(int r, int c) {
        NumJaArray arr=new NumJaArray(r, c);
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                arr.set(i,j,1.0);
            }
        }
        return arr; 
    }

    
    public static NumJaArray random(int r, int c) {
        Random rand = new Random();
        NumJaArray arr=new NumJaArray(r, c);
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                arr.set(i,j,rand.nextDouble());
            }
        }
        return arr; 
    }


    //------ADD------

    public static NumJaArray add(NumJaArray a, NumJaArray b) {
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
        NumJaArray res=new NumJaArray(arr);
        return res;
    }
    public static NumJaArray add(NumJaArray a, double b){

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
        NumJaArray res=new NumJaArray(arr);
        return res;
    }
    public static NumJaArray add(double b,NumJaArray a){
        return add(a,b);
    }

    // public static NumJaArray  add(double a ,double b){
    // ??
        
    // }




    //------Multiply------

    public static NumJaArray multiply(NumJaArray a, NumJaArray b) {
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
        NumJaArray res=new NumJaArray(arr);
        return res;
    }
    public static NumJaArray multiply(NumJaArray a, double b){

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
        NumJaArray res=new NumJaArray(arr);
        return res;
    }
    public static NumJaArray multiply(double b,NumJaArray a){
        return add(a,b);
    }










// matmul(A, B)


}


