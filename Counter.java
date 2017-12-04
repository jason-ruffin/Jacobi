import java.util.concurrent.*;

public class Counter {
  int count = 0;

  public synchronized void Barrier(int NOTH){

try{
      count++;
      if(count == NOTH){
        notifyAll();
        count = 0;
      }else{
        wait();
      }
    }catch(InterruptedException e){

    }
    }
}
