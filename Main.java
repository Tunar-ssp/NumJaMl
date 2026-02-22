import NumJa.NumJaArray;
import NumJa.NumJa;
public class Main {
    public static void main(String[] args) {
        NumJaArray a =  new NumJaArray(3,3);

        double[][] testData = {
                {1.1, 1.2, 1.3, 1.4},
                {2.1, 2.2, 2.3, 2.4},
                {3.1, 3.2, 3.3, 3.4}
            };
        NumJaArray arr = new NumJaArray(testData);
        
        // System.out.println(a); 
        // System.out.println(java.util.Arrays.toString(a.shape()));
        // System.out.println(a.max());
        // System.out.println(myArr.min());
        // System.out.println(java.util.Arrays.toString(myArr.shape()));
        // myArr=myArr.flatten();
        // System.out.println(myArr.mean());
        // System.out.println(myArr.sum());
        // arr.set(0, 0, 5.5);  
        // arr.get(2:3,)
        // System.out.println(arr.get("2:3, "));
        // System.out.println(myArr.transpose());


        // NumJaArray MyA=NumJa.random(5,5);
        // NumJaArray MyB=NumJa.random(5,5);
        // NumJaArray test=NumJa.add(MyA,MyB);
        // System.out.println(MyA);
        // System.out.println(MyB);
        NumJaArray test=NumJa.multiply(arr,3);
        System.out.println(test);
        

    }
} 