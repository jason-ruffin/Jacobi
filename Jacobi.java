import java.lang.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Jacobi{
    public static void main(String[] args){
        int dimension = 2048;
        double maxDifference = 1000.0;
        double EPSILON = 0.0001;

        double[][] oldArray = new double[dimension][dimension];
        double[][] newArray = new double[dimension][dimension];

        try{
          String file = "/home/ruffinj/Jacobi/input.mtx";
          Path path = Paths.get(file);
          Scanner input = new Scanner(path);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                oldArray[i][j] = input.nextDouble();
                newArray[i][j] = oldArray[i][j];
            }
        }
      }catch(Exception e){}

        while(maxDifference >= EPSILON){
            maxDifference = 0.0;
            for (int i = 1; i < dimension - 1; i++){
                for (int j = 1; j < dimension - 1; j++){
                    newArray[i][j] = (oldArray[i-1][j] + oldArray[i+1][j] + oldArray[i][j+1] + oldArray[i][j-1]) * 0.25;
                    maxDifference = Math.max(maxDifference, Math.abs(oldArray[i][j] - newArray[i][j]));
                }
            }

            for (int i = 1; i < dimension - 1; i++){
                for (int j = 1; j < dimension - 1; j++){
                    oldArray[i][j] = newArray[i][j];
                }
            }
        }


        for (double[] row : newArray)
        {
            System.out.println(Arrays.toString(row));
        }

        System.out.println("The Maximum difference was:" + maxDifference);
    }
}
