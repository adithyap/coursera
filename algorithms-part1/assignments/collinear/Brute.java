import java.util.Arrays;
import java.util.Collections;

/**
 * Created by adithya on 01/11/14.
 */
public class Brute {

    private static Point[] points;

    public static void main(String[] args){

        String fileName = args[0];

        readInput(fileName);

        initPointPlotter();

        printCollinearStrings();

        displayPointPlotter();
    }

    private static void readInput(String fileName) {

        In in = new In(fileName);

        // Read N
        int n = in.readInt();

        // Initialize points array
        points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();

            Point p = new Point(x, y);

            // Plot the point
            p.draw();

            // Add to local array
            points[i] = p;
        }
    }

    private static void printCollinearStrings() {

        // Check for 4 collinear points by comparing slopes
        // N^4

        Arrays.sort(points);

        int numPoints = points.length;

        for(int i = 0; i < numPoints; i++){

            for(int j = i + 1; j < numPoints; j++){

                for(int k = j + 1; k < numPoints; k++){

                    for(int l = k + 1; l < numPoints; l++){

                        // Compare slopes of j, k, l indices to i

                        double JslopeToI = points[j].slopeTo(points[i]);
                        double KslopeToI = points[k].slopeTo(points[i]);
                        double LslopeToI = points[l].slopeTo(points[i]);

                        if( (JslopeToI == KslopeToI) && (KslopeToI == LslopeToI) ){

                            // These 4 are collinear, print them out
                            String message = points[i] + " -> " + points[j] + " -> " + points[k] + " -> " + points[l];

                            StdOut.println(message);

                            // Plot line
                            points[i].drawTo(points[l]);
                        }
                    }
                }
            }
        }
    }

    private static void initPointPlotter(){

        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger

    }

    private static void displayPointPlotter(){

        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
    }
}
