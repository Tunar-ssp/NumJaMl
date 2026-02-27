import NumJa.CoreJa;
import NumJa.np;
import NumJa.ndarray;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {


    private static double crossEntropy(ndarray pred, ndarray target) {
        double loss = 0;
        for (int i = 0; i < 10; i++) {
            double p = Math.max(pred.get(0, i), 1e-9);
            loss -= target.get(0, i) * Math.log(p);
        }
        return loss;
    }

    private static ndarray ceGradient(ndarray pred, ndarray target) {
        return np.subtract(pred, target);
    }

    public static void main(String[] args) {
        String csvPath    = "data/mnist_train.csv";
        int hiddenSize    = 256;
        int epochs        = 8;
        double initLR     = 0.01;
        double lrDecay    = 0.75;

        CoreJa model = new CoreJa(784, hiddenSize, 10);

        try {
            double lr = initLR;

            for (int epoch = 1; epoch <= epochs; epoch++) {
                double totalLoss       = 0;
                int    correct         = 0;
                int    count           = 0;

                long epochStart = System.currentTimeMillis();

                try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("label") || line.contains("1x1")) continue;
                        String[] tok = line.split(",");
                        if (tok.length < 785) continue;

                        int label = (int) Double.parseDouble(tok[0]);
                        double[][] yData = new double[1][10];
                        yData[0][label] = 1.0;
                        ndarray yTrue = np.array(yData);

                        double[] px = new double[784];
                        for (int i = 0; i < 784; i++) {
                            px[i] = Double.parseDouble(tok[i + 1]) / 255.0;
                        }
                        ndarray x = np.array(new double[][]{ px });

                        ndarray yPred = model.forward(x);
                        if (np.argmax(yPred) == label) correct++;
                        totalLoss += crossEntropy(yPred, yTrue);

                        ndarray grad = ceGradient(yPred, yTrue);
                        model.backward(grad);
                        model.step(lr);
                        count++;

                        if (count % 10000 == 0) {
                            System.out.printf("  Epoch %d | %6d samples | Acc: %5.2f%% | Loss: %.4f | LR: %.5f%n",
                                epoch, count,
                                correct * 100.0 / count,
                                totalLoss / count,
                                lr);
                        }
                    }
                }

                long elapsed = System.currentTimeMillis() - epochStart;
                System.out.printf("%n[EPOCH %d] Acc: %.2f%% | Avg Loss: %.4f | Time: %ds | LR used: %.5f%n%n",
                    epoch,
                    correct * 100.0 / count,
                    totalLoss / count,
                    elapsed / 1000,
                    lr);

                lr *= lrDecay;
            }

            model.save("trained_mnist_model");

        } catch (IOException e) {
            System.err.println("CSV read error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






