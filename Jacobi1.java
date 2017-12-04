import java.lang.*;
import java.util.*;
import java.io.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.*;

public class Jacobi1{
    public static void main(String[] args) throws InterruptedException{
        if(args.length != 1){
          System.out.println("Usage Jacobi1 number of threads.");
          return;
        }
        int NOTH = Integer.parseInt(args[0]);
        int dimension = 2048;
        double EPSILON = 0.0001;
        int height = (dimension - 2)/NOTH;
        double[][] oldArray = new double[dimension][dimension];
        double[][] newArray = new double[dimension][dimension];

				Counter counter = new Counter();

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
        		long start = System.nanoTime();
        		//CO
            Thread[] thds = new Thread[NOTH];
            for (int j = 0; j < NOTH; ++j) {
                final int J = j;
            //this creates an array of threads that implement the runnable function
                thds[j] = new Thread ( new Runnable () {
                public void run() {
                double maxDifference = 1.0;

                while(maxDifference >= EPSILON){
									maxDifference = 0.0;
									//counter.Barrier(NOTH);
                    for (int i = 1; i < dimension-1; i++){
                        for (int k = (height*J)+1; k < (height*(J+1))+1; k++){
                            newArray[i][k] = (oldArray[i-1][k] + oldArray[i+1][k] + oldArray[i][k+1] + oldArray[i][k-1]) * 0.25;
                            maxDifference = Math.max(maxDifference, Math.abs(oldArray[i][k] - newArray[i][k]));
                        }
												//System.out.println(maxDifference);
                    }
										counter.Barrier(NOTH);

                    for (int i = 1; i < dimension-1; i++){
                        for (int k = 1; k < (height*(J+1))+1; k++){
                            oldArray[i][k] = newArray[i][k];
                        }
                    }
										counter.Barrier(NOTH);
                }

                }
              });
              //start all the threads
              thds[j].start();
            }

        //OC
        for (int j = 0; j < NOTH; ++j){
            //join all the threads
            thds[j].join();
        }

				long end = System.nanoTime();


        for (double[] row : newArray)
        {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("time =  " + (end - start));

    }
}

//8 threads:
//6 threads:
//4 threads:
//2 threads:
//1 thread:
