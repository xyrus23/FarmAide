package com.example.redfoxoptimaiii.farmaide;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 4/18/2017.
 */

public class Optimizer {
    private int row, col; // row and column
    private float[][] tableau; // simplex tableau
    private int pivotColumn;
    private float[] solution;
    private boolean optimized;

    public Optimizer(int row, int col, float[][] tableau) {
        this.row = row;
        this.col = col;
        this.tableau = new float[col][row + col - 1];
        float[][] slack = new float[row][row];

        for (int i = 0; i < row; i += 1)
            for (int j = 0; j < row; j += 1)
                if (i == j && i != row - 1) slack[i][j] = 1;

        for (int i = 0; i < this.row; i += 1) {
            int k = 0;
            for (int j = 0; j < this.col + row - 1; j += 1) {
                if (j < this.col - 1) this.tableau[i][j] = tableau[i][j];
                else if (j == this.col + row - 2) this.tableau[i][j] = tableau[i][j - row + 1];
                else if (j >= this.col - 1 && k < row - 1) {
                    this.tableau[i][j] = slack[i][k];
                    k += 1;
                }
            }
        }
        this.col += row - 1;

        optimize(this.row, this.col, this.tableau);
    }

    public void optimize(int row, int col, float[][] tableau) {
        int ite = 0;
        while (isOptimized(tableau) && ite <= 20) {
            pivotColumn = getPivotColumn(tableau);
            ArrayList<Float> tr = getTestRatios(tableau, pivotColumn);
            int pivotElement = getPivotElement(tr);

            //normalize pivot row
            float pivElem = tableau[pivotElement][pivotColumn];
            for (int i = 0; i < col; i++)
                tableau[pivotElement][i] = tableau[pivotElement][i] / pivElem;

            //clear pivot column
            for (int i = 0; i < row; i++) {
                if (i != pivotElement) {
                    float pe = tableau[i][pivotColumn];
                    for (int j = 0; j < col; j++) {
                        tableau[i][j] = tableau[i][j] - (tableau[pivotElement][j] * pe);
                    }
                }
            }
            print(tableau);
            ite += 1;
        }
        optimized = isOptimized(tableau);
        solution = getSolution(tableau);
    }

    public boolean isOptimized(float[][] tableau) {
        boolean check = false;
        for (int i = 0; i < this.col; i++)
            if (tableau[this.row - 1][i] < 0) check = true;
        return check;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public int getPivotColumn(float[][] tableau) {
        int pivotColumn = 1;
        for (int i = 0; i < this.col - 1; i++)
            if (tableau[this.row - 1][i] < tableau[this.row - 1][pivotColumn])
                pivotColumn = i;
        return pivotColumn;
    }

    public ArrayList<Float> getTestRatios(float[][] tableau, int pivotColumn) {
        ArrayList<Float> tr = new ArrayList<>();
        for (int i = 0; i < this.row - 1; i++) {
            if (tableau[i][pivotColumn] > 0)
                tr.add(tableau[i][this.col - 1] / tableau[i][pivotColumn]);
            else tr.add(0f);
        }
        return tr;
    }

    public int getPivotElement(ArrayList<Float> tr) {
        int pivotElement = 0;
        for (int i = 0; i < tr.size(); i++) {
            if (tr.get(i) != 0) {
                pivotElement = i;
                break;
            }
        }
        for (int i = 0; i < tr.size(); i++) {
            if (tr.get(i) != 0)
                if (tr.get(i) < tr.get(pivotElement))
                    pivotElement = i;
        }
        return pivotElement;
    }

    public void print(float[][] tableau) {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                String value = String.format("%.2f", tableau[i][j]);
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public float[] getSolution(float[][] tableau) {
        int j = 0;
        float[] sol = new float[this.col - (this.col - this.row)];
        System.out.println("Solution:");
        for (int i = this.col - this.row; i < this.col; i += 1) {
            sol[j] = tableau[this.row - 1][i];
            j += 1;
        }
        return sol;
    }

    public float[] getSolution() {
        return solution;
    }
//    private int row, col; // row and column
//    private float[][] tableau; // simplex tableau
//    private int pivotColumn;
//    public ArrayList<Float> solution = new ArrayList<>();
//    public float[] sol;
//    public boolean isOptimize;
//    public static String s="";
//
//    public Optimizer(int row, int col, float[][] tableau){
//        this.row = row;
//        this.col = col;
//        this.tableau = new float[col][row+col-1];
//        float[][] slack = new float[row][row];
//
//        for(int i=0;i<row;i+=1){
//            for(int j=0;j<row;j+=1){
//                if(i==j && i!=row-1) slack[i][j] = 1;
//                System.out.print(slack[i][j]+"\t");
//            } System.out.println();
//        }
//
//        print(tableau);
//
//        for(int i=0;i<this.row;i+=1){
//            int k=0;
//            for(int j=0;j<this.col+row-1;j+=1){
//                if(j<this.col-1) this.tableau[i][j] = tableau[i][j];
//                else if(j==this.col+row-2) this.tableau[i][j] = tableau[i][j-row+1];
//                else if(j>=this.col-1 && k<row-1){
//                    this.tableau[i][j] = slack[i][k];
//                    k+=1;
//                }
//            }
//        }
//        this.col+=row-1;
//        print(this.tableau);
//
//        optimize(this.row, this.col, this.tableau);
//    }
//
//    public void optimize(int row, int col, float[][] tableau){
//        int ite = 0;
//        while(isOptimized(tableau) && ite <= 30){
//            pivotColumn = getPivotColumn(tableau);
//            ArrayList<Float> tr = getTestRatios(tableau, pivotColumn);
//            int pivotElement=getPivotElement(tr);
//
//            //normalize pivot row
//            float pivElem = tableau[pivotElement][pivotColumn];
//            for(int i=0;i<col;i++)
//                tableau[pivotElement][i] = tableau[pivotElement][i]/pivElem;
//
//            //clear pivot column
//            for(int i=0;i<row;i++){
//                if(i != pivotElement){
//                    float pe = tableau[i][pivotColumn];
//                    for(int j=0;j<col;j++){
//                        tableau[i][j] = tableau[i][j] - (tableau[pivotElement][j]*pe);
//                    }
//                }
//            }
//            print(tableau);
//            ite+=1;
//        }
//        isOptimize = isOptimized(tableau);
//        sol = getSolution(tableau);
//    }
//
//    public boolean isOptimized(float[][] tableau){
//        boolean check = false;
//        for(int i=0;i<this.col;i++)
//            if(tableau[this.row-1][i] < 0) check = true;
//        return check;
//    }
//
//    public boolean isOptimized(){
//        return isOptimize;
//    }
//
//    public int getPivotColumn(float[][] tableau){
//        int pivotColumn=1;
//        for(int i=0;i<this.col-1;i++)
//            if(tableau[this.row-1][i] < tableau[this.row-1][pivotColumn])
//                pivotColumn = i;
//        return pivotColumn;
//    }
//
//    public ArrayList<Float> getTestRatios(float[][] tableau, int pivotColumn){
//        ArrayList<Float> tr = new ArrayList<Float>();
//        for(int i=0;i<this.row-1;i++) {
//            if(tableau[i][pivotColumn] > 0) tr.add(tableau[i][this.col-1]/tableau[i][pivotColumn]);
//            else tr.add(0f);
//        }
//        return tr;
//    }
//
//    public int getPivotElement(ArrayList<Float> tr){
//        int pivotElement = 0;
//        for(int i=0;i<tr.size();i++){
//            if(tr.get(i) != 0){
//                pivotElement = i;
//                break;
//            }
//        }
//        for(int i=0;i<tr.size();i++){
//            if(tr.get(i) != 0)
//                if(tr.get(i) < tr.get(pivotElement))
//                    pivotElement = i;
//        }
//        return pivotElement;
//    }
//
//    public void print(float[][] tableau){
//        for(int i=0;i<this.row;i++){
//            for(int j=0;j<this.col;j++){
//                String value = String.format("%.2f", tableau[i][j]);
//                System.out.print(value + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
//
//    public float[] getSolution(float[][] tableau){
//        int j=0;
//        float[] sol = new float[this.col-(this.col-this.row)];
//        for(int i=this.col-this.row;i<this.col;i+=1) {
//            solution.add(tableau[this.row - 1][i]);
//            sol[j] = tableau[this.row-1][i];
//            j+=1;
//            s+=tableau[this.row-1][i]+"\t";
//        }
//        return sol;
//    }
//
//    public float[] getSolution(){
//        return sol;
//    }
}
