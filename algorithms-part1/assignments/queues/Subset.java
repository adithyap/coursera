import java.util.Iterator;

/**
 * Created by adithya on 20/10/14.
 */
public class Subset {

    public static void main(String[] args){

//        // Debug use case
//        if(args.length == 0) {
//
//            // Debug use case
//            args = new String[1];
//            args[0] = "3";
//        }

        if(args.length == 1) {

            int sampleSize = Integer.parseInt(args[0]);
//            int sampleSize = 10;

            // Initialize RandomizedQueue
            RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

            String[] inputStrings = StdIn.readAllStrings();
            for(String input : inputStrings) {

                // Enqueue into randomizedQueue
                randomizedQueue.enqueue(input);
            }

//            randomizedQueue.enqueue("AA");
//            randomizedQueue.enqueue("AA");
//            randomizedQueue.enqueue("AA");
//            randomizedQueue.enqueue("AA");
//            randomizedQueue.enqueue("BB");
//            randomizedQueue.enqueue("BB");
//            randomizedQueue.enqueue("CC");
//            randomizedQueue.enqueue("CC");
//            randomizedQueue.enqueue("CC");
//            randomizedQueue.enqueue("DD");

            for(int i = 0; i < sampleSize; i++) {

                // Print random sampled elements
                String output = randomizedQueue.dequeue();

                StdOut.println(output);
            }
        }
    }
}
