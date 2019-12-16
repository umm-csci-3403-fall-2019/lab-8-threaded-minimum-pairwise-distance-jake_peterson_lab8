package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    //result start at max for later comparisons
    public long result = Integer.MAX_VALUE;
    public int[] values;

    //update result
    public synchronized  void updateResult( long testResult){
        if(result>testResult){
            result = testResult;
        }
    }


    @Override
    public long minimumPairwiseDistance(int[] values) {
        //throw new UnsupportedOperationException();
        //
        this.values = values;
        //create array of threads
        Thread[] threads = new Thread[4];
        //create threads
        threads[0] = new Thread(new topLeft());
        threads[1] = new Thread(new topRight());
        threads[2] = new Thread(new botLeft());
        threads[3] = new Thread(new botRight());
        
        //start threads
        for (int i = 0; i<4; i++) {
            threads[i].start();
        }
        //join threads when they finish
        for(int i=0; i<4; i++){

            try{
            threads[i].join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    public class botLeft implements Runnable{
        long thisResult = Integer.MAX_VALUE;
        long value = 0;
        public void run(){
             int length = values.length;

             for(int i=0; i<length/2; i++){
                 for(int j=0; j<i; j++){
                     value = Math.abs(values[i] - values[j]);

                     if(value<thisResult){
                         thisResult = value;
                     }
                 }
             }
             updateResult(thisResult);
        }
    }
    public class topRight implements Runnable{
        long thisResult = Integer.MAX_VALUE;
        long value = 0;
        public void run(){
            int length = values.length;

            for(int i=length/2; i<length; i++){
                for(int j=length/2; j<i; j++){
                    value = Math.abs(values[i] - values[j]);

                    if(value<thisResult){
                        thisResult = value;
                    }
                }
            }
            updateResult(thisResult);
        }
    }
    public class topLeft implements Runnable{
        long thisResult = Integer.MAX_VALUE;
        long value = 0;
        public void run(){
            int length = values.length;

            for(int i=0; i< length - (length/2); i++){
                for(int j=length/2; j <= i+(length/2); j++){
                    value = Math.abs(values[i] - values[j]);

                    if(value<thisResult){
                        thisResult = value;
                    }
                }
            }
            updateResult(thisResult);
        }
    }
    public class botRight implements Runnable{
        long thisResult = Integer.MAX_VALUE;
        long value = 0;
        public void run(){
            int length = values.length;

            for(int i=length/2; i<length; i++){
                for(int j=0; j+(length/2)<i; j++){
                    value = Math.abs(values[i] - values[j]);

                    if(value<thisResult){
                        thisResult = value;
                    }
                }
            }
            updateResult(thisResult);
        }
    }
}
