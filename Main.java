import NumJa.np;
import NumJa.ndarray;
public class Main {
    public static void main(String[] args) {
        // ndarray a =  np.array(3,3);

        double[][] testData = {
                {1.1, 1.2, 1.3, 1.4},
                {2.1, 2.2, 2.3, 2.4},
                {3.1, 3.2, 3.3, 3.4}
        };
        ndarray a =  np.array(testData);
        ndarray b = np.random(3,4);
        
        
        System.out.println(np.add(a,b));
        
        

    }
} 