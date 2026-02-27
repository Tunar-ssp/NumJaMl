import NumJa.np;
import NumJa.ndarray;
import NumJa.CoreJa;
import java.io.IOException;

public class accuracy {
    public static void main(String[] args) {
        try {
            CoreJa model = new CoreJa(784, 128, 10);
            model.load("trained_mnist_model");

            ndarray testData = np.loadCSV("data/mnist_train.csv", true);
            ndarray labels = testData.get(":, 0");
            ndarray images = testData.get(":, 1:");
            images = images.multiply(1.0 / 255.0);

            int testSamples = images.shape()[0];
            int correct = 0;

            for (int i = 0; i < testSamples; i++) {
                ndarray x = images.get(i + ":" + (i + 1) + ", :");
                int trueLabel = (int) labels.get(i, 0);

                ndarray prediction = model.forward(x);
                int predictedLabel = np.argmax(prediction);

                if (predictedLabel == trueLabel) {
                    correct++;
                }
            }

            double finalAccuracy = (correct * 100.0) / testSamples;
            System.out.printf("TEST ACCURACY: %.2f%%\n", finalAccuracy);

        } catch (IOException e) {
            System.err.println("Failed to load files: " + e.getMessage());
        }
    }
}