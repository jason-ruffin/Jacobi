import java.util.concurrent.*;

public class Barrier{

public synchronized void barrier(){
  int count = 0;
  if(count == Jacobi1.NOTH){
    notifyAll();
    count = 0;
  }else{
    wait();
    count++;
  }
}




}
