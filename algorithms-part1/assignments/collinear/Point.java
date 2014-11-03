/**
 * Created by adithya on 01/11/14.
 */
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
        @Override
        public int compare(Point point1, Point point2) {

            if(point1 == null)
                throw new NullPointerException("Argument point1 is null");

            if(point2 == null)
                throw new NullPointerException("Argument point2 is null");

            return Double.compare(slopeTo(point1), slopeTo(point2));
        }
    };

    private final int x;
    private final int y;

    // create the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {

        if(that == null)
            throw new NullPointerException("Argument that is null in slopeTo");

//        if(this.x == that.x){
//            if(that.y < this.y)
//                return Double.POSITIVE_INFINITY;
//            else if(that.y > this.y)
//                return  Double.NEGATIVE_INFINITY;
//            else
//                return Double.NaN;
//        }
//
//        return ((double)(that.y - this.y) / (double)(that.x - this.x));

        double xDiff = (double) (that.x - this.x);
        double yDiff = (double) (that.y - this.y);

        if (xDiff == 0 && yDiff == 0) {
            return Double.NEGATIVE_INFINITY;
        }

        if (xDiff == 0) {
            return Double.POSITIVE_INFINITY;
        }
        if (yDiff == 0) {
            return 0.0;
        }

        else {
            double slope =  yDiff / xDiff;
            return slope;
        }

    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {

        if(that == null)
            throw new NullPointerException("Argument that is null in compareTo");

//        int yDiff = (that.y - this.y);
//        int xDiff = (that.x - this.x);

        int yDiff = (this.y - that.y);
        int xDiff = (this.x - that.x);

        if(yDiff == 0)
            return xDiff;

        return yDiff;
    }

    // return string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // unit test
//    public static void main(String[] args) {
//
//
//    }
}