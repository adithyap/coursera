public class PointSET
{
    private SET<Point2D> setOfPoints;

    public PointSET()
    {
        // construct an empty set of points

        initializePointList();
    }

    private void initializePointList()
    {
        setOfPoints = new SET<Point2D>();
    }

    public boolean isEmpty()
    {
        return setOfPoints.isEmpty();
    }

    public int size()
    {
        return setOfPoints.size();
    }

    public void insert(Point2D p)
    {
        if(p == null)
            throw new NullPointerException("Argument p in insert is NULL");

        setOfPoints.add(p);
    }

    public boolean contains(Point2D p)
    {
        if(p == null)
            throw new NullPointerException("Argument p in contains is NULL");

        return setOfPoints.contains(p);
    }

    public void draw()
    {
        // draw all points to standard draw
        for(Point2D p : setOfPoints)
        {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        if(rect == null)
            throw new NullPointerException("Argument rect in range is NULL");

        // all points that are inside the rectangle

        Stack<Point2D> pointsInRectangle = new Stack<Point2D>();

        // Brute force - iterate over all points and add the ones that are contained in the rectangle
        for(Point2D p : setOfPoints)
            if (rect.contains(p))
                pointsInRectangle.push(p);

        return pointsInRectangle;
    }

    public Point2D nearest(Point2D p)
    {
        if(p == null)
            throw new NullPointerException("Argument p in nearest is NULL");

        double minDist = Double.MAX_VALUE;
        Point2D nearestPoint = null;

        for(Point2D point : setOfPoints)
        {
            double distance = point.distanceTo(p);

            if (distance < minDist)
            {
                minDist = distance;
                nearestPoint = point;
            }
        }

        return nearestPoint;
    }

//    public static void main(String[] args)
//    {
//        // unit testing of the methods (optional)
//    }
}
