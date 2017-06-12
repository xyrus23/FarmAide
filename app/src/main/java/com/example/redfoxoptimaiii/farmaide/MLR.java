package com.example.redfoxoptimaiii.farmaide;

import com.example.redfoxoptimaiii.farmaide.Jama.Matrix;

/**
 * Created by REDFOX™ OptimaIII on 5/12/2017.
 */

public class MLR {
    Matrix X, Y, answer, input;
    double predictedOutput, r_squared, adj_r_sqrd;


    public MLR(double[][] dataX, double[][] dataY, double[][] in) {
        X = new Matrix(dataX);
        Y = new Matrix(dataY);
        input = new Matrix(in);

        answer = normalEquation(X, Y);
        predictedOutput = predict(input, answer);
        r_squared = (ssTotal(Y) - ssResidual(answer, X, Y)) / ssTotal(Y);
        adj_r_sqrd = (msTotal(X, Y) - msResidual(answer, X, Y)) / msTotal(X, Y);
    }

    public MLR(double[][] dataX) {
        double[][] dataY = new double[dataX.length][1];
        for (int i = 0; i < dataX.length; i += 1) {
            dataY[i][0] = dataX[i][0];
            dataX[i][0] = 1;
        }

        X = new Matrix(dataX);
        Y = new Matrix(dataY);
        answer = normalEquation(X, Y);
        r_squared = (ssTotal(Y) - ssResidual(answer, X, Y)) / ssTotal(Y);
        adj_r_sqrd = (msTotal(X, Y) - msResidual(answer, X, Y)) / msTotal(X, Y);
    }

    private double predict(Matrix in, Matrix answer) {
        return in.times(answer).getArray()[0][0];
    }

    public double getPredictedOutput() {
        return predictedOutput;
    }

    public double getR_squared() {
        return r_squared;
    }

    public double getAdj_r_sqrd() {
        return adj_r_sqrd;
    }

    public double ssTotal(Matrix Y) {
        // summation of (actual y - mean y)^2
        double meanY = 0.00000;
        double ssTotal = 0.00000;

        for (int i = 0; i < Y.getRowDimension(); i++)
            meanY += Y.get(i, 0);

        meanY = meanY / Y.getRowDimension();

        for (int i = 0; i < Y.getRowDimension(); i++)
            ssTotal += Math.pow((Y.get(i, 0) - meanY), 2);

        return ssTotal;
    }

    public double ssResidual(Matrix answer, Matrix X, Matrix Y) {
        // summation of (actual y - calculated y)^2
        double ssResidual = 0.00000;
        Matrix calculatedY = X.times(answer);

        for (int i = 0; i < Y.getRowDimension(); i++)
            ssResidual += Math.pow((Y.get(i, 0) - calculatedY.get(i, 0)), 2);

        return ssResidual;
    }

    public double msTotal(Matrix X, Matrix Y) {
        // msTotal = ssTotal / dfTotal
        // dfTotal = sample size - 1

        double dfTotal = X.getRowDimension() - 1;

        return ssTotal(Y) / dfTotal;
    }

    public double msResidual(Matrix answer, Matrix X, Matrix Y) {
        // msResidual = square root of the error variance
        // msResidual = ssResidual / n - k - 1 where n = sample size and k = no of predictor variables

        double dfResidual = X.getRowDimension() - (X.getColumnDimension() - 1) - 1;

        return ssResidual(answer, X, Y) / dfResidual;
    }

    public Matrix normalEquation(Matrix x, Matrix y) {
        Matrix op1 = x.transpose();
        Matrix op2 = (op1.times(x)).inverse();
        Matrix op3 = op2.times(op1).times(y);
        return op3;
    }
}