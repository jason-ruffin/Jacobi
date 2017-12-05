#include <errno.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>

#define max(x, y) (((x) > (y)) ? (x) : (y))

typedef
struct arg_st{
  double **oldArray;
  double **newArray;
  int dimension;
  int NOTH;
  double *globalMax;
  int j;//which thread am i
} arg_t;


void* threadBody(void* ptr){
  arg_t* args = (arg_t*)(ptr);
  double EPSILON = 0.01;
  double maxDifference = 1.0;

  while(maxDifference >= EPSILON){
    maxDifference = 0.0;

    //ret = pthread_barrier_wait(&args->barrier);

    for(int i = args->j + 1; i < args->dimension - 1; i = i + args->NOTH){
      for(int k = 1; k < args->dimension - 1; k++){
        args->newArray[i][k]=
        (args->oldArray[i-1][k] +
        args->oldArray[i+1][k] +
        args->oldArray[i][k+1] +
        args->oldArray[i][k-1]) * 0.25;
      }
    }


    for(int i = args->j + 1; i < args->dimension - 1; i = i + args->NOTH){
      for(int k = 1; k < args->dimension - 1; k++){
        maxDifference = max(maxDifference, abs(args->oldArray[i][k] - args->newArray[i][k]));
        args->oldArray[i][k] = args->newArray[i][k];
      }
    }

    args->globalMax[args->j] = maxDifference;

    //ret = pthread_barrier_wait(&args->barrier);

    for(int i = 0; i < args->NOTH; i++){
      if(maxDifference < args->globalMax[i]){
        maxDifference = args->globalMax[i];
      }
    }

    printf("%lf \n", maxDifference);
}
  return NULL;
}


int main(int argc, const char* argv[]) {

  //open the input file, but if it can't be opened, exit and pringt errno
  FILE* input = fopen("./input.mtx", "r");


  if(fopen("./input.mtx", "r") == NULL) {
    printf("Unable to read the input file: %s\n", strerror(errno));
    exit(1);
  }

  int NOTH = 4;
  int dimension = 1024;
  double globalMax[NOTH];
  void* unused;

  //pthread_barrier_t barrier;
  //int ret = pthread_barrier_init(&barrier, NULL, 4);

  //create the arrays for the input data
  double *oldArray[dimension];
  for (int i = 0; i < dimension; i++)
    oldArray[i] = (double *)malloc(dimension * sizeof(double));

  double *newArray[dimension];
  for (int i = 0; i < dimension; i++)
    newArray[i] = (double *)malloc(dimension * sizeof(double));

    //load the data into the 2d arrays
    for (int i = 0; i < dimension; i++) {
        for (int j = 0; j < dimension; j++) {
            if (fscanf(input, "%lf", &oldArray[i][j]) != 1) {
                return (EXIT_FAILURE);
            }
            if (fscanf(input, "%lf", &newArray[i][j]) != 1) {
                return (EXIT_FAILURE);
            }
        }
    }
      pthread_t thd[NOTH];
      arg_t args[NOTH];
      //CO
      for (int j = 0; j < NOTH; j++) {
        args[j].oldArray = oldArray;
        args[j].newArray = newArray;
        args[j].dimension = dimension;
        args[j].NOTH = NOTH;
        args[j].globalMax = globalMax;

        if(pthread_create(&thd[j], NULL, &threadBody, (void*)&args[j])){
          perror("thread_create");
          return -1;
        }
      }


      //OC
      for(int j = 0; j < NOTH; j++){
        pthread_join(thd[j], &unused);
      }


    for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                printf("%lf ", newArray[i][j]);
            }
    }
}
