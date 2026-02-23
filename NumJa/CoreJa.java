package NumJa;

import NumJa.np;
import NumJa.ndarray;

public class CoreJa {
    private int input_layers;
    private int hidden_layers;
    private int output_layers;
    public CoreJa(int inputSize, int hiddenSize, int outputSize){
        this.input_layers=inputSize;
        this.output_layers=outputSize;
        this.hidden_layers=hiddenSize;
        initializeWeights();
        
    }

    public ndarray W1, b1, W2, b2;

    private void initializeWeights(){
        // ndarray W1,W2,b1,b2; 

        this.W1 = np.random(input_layers, hidden_layers).multiply(0.01);
        this.b1 = np.zeros(1, hidden_layers); 

        this.W2 = np.random(hidden_layers, output_layers).multiply(0.01); 
        this.b2 = np.zeros(1, output_layers); 


    }

    public ndarray relu(ndarray a) {
        return np.maximum(0,a);
    }
    
    public ndarray sigmoid(ndarray a) { 
        // formula: 1 / (1 + exp(-a))   
        ndarray negA = a.multiply(-1.0);  
        ndarray expNegA = np.exp(negA);   
        ndarray denom = expNegA.add(1.0);  
        return np.divide(1.0, denom);  
    }

    public ndarray forward(ndarray input) {
        ndarray z1 = np.dot(input, this.W1).add(this.b1);
        ndarray a1 = relu(z1);
        ndarray z2 = np.dot(a1, this.W2).add(this.b2);
           
        return z2; 
    }

    public double calculateLoss(ndarray prediction, ndarray target) {

        ndarray diff = prediction.subtract(target);
        ndarray sqDiff = np.power(diff, 2);
        return np.sum(sqDiff) / prediction.size();
    }
    private ndarray a1, input_stored; 
    private ndarray dW1, db1, dW2, db2;

public void backward(ndarray output_error) {
        // output_error is (prediction - target)
        
        // dW2 = a1.T * error
        this.dW2 = np.dot(this.a1.transpose(), output_error);
        this.db2 = output_error; 
        // hidden_error = (error * W2.T) * relu_derivative(z1)
        ndarray hidden_error = np.dot(output_error, this.W2.transpose());
        ndarray d_relu = np.mask(this.a1, 0); 
        hidden_error = np.multiply(hidden_error, d_relu);
        this.dW1 = np.dot(this.input_stored.transpose(), hidden_error);
        this.db1 = hidden_error;
    }

    public void step(double learningRate) {
        // W = W - (lr * gradient)
        this.W1 = this.W1.subtract(this.dW1.multiply(learningRate));
        this.b1 = this.b1.subtract(this.db1.multiply(learningRate));
        this.W2 = this.W2.subtract(this.dW2.multiply(learningRate));
        this.b2 = this.b2.subtract(this.db2.multiply(learningRate));
    }

    public int predict(ndarray output) {
        return np.argmax(output);
    }



}