import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by adithya on 01/11/14.
 */
public class Fast {

    private static Point[] points;
    private static int numPoints;
    private static List<Point[]> collinearPoints;

    private static final int minCollinearCount = 4;

    public static void main(String[] args){

        String fileName = args[0];

        initPointPlotter();

        readInput(fileName);

        identifyCollinearPoints();

        printCollinearPoints();

        displayPointPlotter();
    }

    /**
     * Reads a list of points from a file
     * @param fileName  Name of the file to read list of points from
     */
    private static void readInput(String fileName) {

        In in = new In(fileName);

        // Read N
        int n = in.readInt();

        // Initialize points array
        points = new Point[n];

        numPoints = n;

        collinearPoints = new ArrayList<Point[]>();

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

    private static void identifyCollinearPoints(){

        // Sort the points array
        // This will ensure that lowest point will try to find higher points for collinearity
        Arrays.sort(points);

        Point[] unchangingPoints = new Point[numPoints];

        //copying points to an unchanging array.
        for (int x = 0; x < numPoints; x++)
            unchangingPoints[x] = points[x];

        for(int i = 0; i < numPoints - 1; i ++) {

            identifyCollinearPointsForIndex(unchangingPoints[i]);
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

    private static void identifyCollinearPointsForIndex(Point origin) {

        // Sort based on this point
        Arrays.sort(points, origin.SLOPE_ORDER);

        int startIndex = 1;
        int groupCount = 2;

        // Index 0 is self (NaN slope)
        // Start with index 1
        double expectedSlope = origin.slopeTo(points[1]);

        for(int i = 2; i < numPoints; i++){

            double slope = origin.slopeTo(points[i]);

            if(expectedSlope == slope){

                groupCount++;
            }
            else {

                if(groupCount >= minCollinearCount){

                    addPointsToOutput(origin, startIndex, groupCount);
                }

                // Reset
                expectedSlope = slope;
                groupCount = 2;
                startIndex = i;
            }

        }

        if(groupCount >= minCollinearCount){
            addPointsToOutput(origin, startIndex, groupCount);
        }
    }

    private static void addPointsToOutput(Point referencePoint, int startIndex, int groupCount) {

        boolean isGroupValid = true;

        Point[] collinearPointsInGroup = new Point[groupCount];

        collinearPointsInGroup[0] = referencePoint;

        for(int i = startIndex; i < startIndex + groupCount - 1; i++){

            if(referencePoint.compareTo(points[i]) > 0){

                isGroupValid = false;
                break;
            }

            collinearPointsInGroup[i - startIndex + 1] = points[i];
        }

        if(isGroupValid) {
            Arrays.sort(collinearPointsInGroup);
            collinearPoints.add(collinearPointsInGroup);
        }
    }

    private static void printCollinearPoints() {

        for(Point[] p : collinearPoints){

            int collinearPointsCount = p.length;

            String output = "";

            for(int i = 0; i < collinearPointsCount - 1; i++){

                output += p[i] + " -> ";
            }

            output += p[collinearPointsCount - 1];

            // StdOut
            StdOut.println(output);

            // StdDraw
            p[0].drawTo(p[collinearPointsCount-1]);
        }
    }
}