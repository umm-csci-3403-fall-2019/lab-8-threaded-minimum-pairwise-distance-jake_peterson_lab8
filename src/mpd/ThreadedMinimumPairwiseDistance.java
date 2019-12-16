package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    //result start at max for later comparisons
    public long result = Integer.MAX_VALUE;
    public int[] values;

    //update result. synchronized for threading
    public synchronized  void updateResult( long testResult){
        //if the current result is bigger than the new one being
        // tested, change the result to the result being tested.
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
    /*
    * The logic for the next four methods are essentially
    * the same, they're just setup to run different partitions.
    * botLeft, botRight, topLeft, and topRight are the methods
    * that are run in the threads. They each calculate the
    * min pairwise distance for different parts of the array.
    *
    **/
    public class botLeft implements Runnable{
        //start at max for later comparisons
        long thisResult = Integer.MAX_VALUE;
        //this stores the calculated min distance
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
             //update result with our calculated min distance
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
