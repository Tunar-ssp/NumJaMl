import NumJa.np;
import NumJa.ndarray;
import NumJa.CoreJa;

public class Test{
    public static void main(String[] args) throws java.io.IOException {
        CoreJa model = new CoreJa(784, 256, 10);

        try {
            model.load("trained_mnist_model");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        model.exportToJSON("weights.json");
    }
}