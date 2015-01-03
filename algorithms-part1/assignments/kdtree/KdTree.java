public class KdTree
{
    //region "Constants"

    private static class Constants
    {
        public static final int HORIZONTAL = 0;
        public static final int VERTICAL = 1;

        public static final double X_MIN = 0.0;
        public static final double Y_MIN = 0.0;
        public static final double X_MAX = 1.0;
        public static final double Y_MAX = 1.0;
    }

    //endregion

    //region "Fields"

    private final _2DTree tree;

    //endregion

    //region "Initialization"

    public KdTree()
    {
        tree = new _2DTree();
    }

    //endregion

    //region "2-d tree public wrappers"

    public boolean isEmpty()
    {
        return tree.isEmpty();
    }

    public int size()
    {
        return tree.size();
    }

    public void insert(Point2D p)
    {
        tree.insert(p);
    }

    public boolean contains(Point2D p)
    {
        return tree.contains(p);
    }

    public void draw()
    {
        tree.draw();
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        return tree.range(rect);
    }

    public Point2D nearest(Point2D p)
    {
        return tree.nearest(p);
    }

    //endregion

    //region "2-d tree implementation"

    private class _2DTree
    {
        //region "Internal Data Structure"

        private class Node
        {
            // 2-d Tree
            private Point2D point;

            // Common members
            private int axis;
            private Node left;
            private Node right;

            public Node(Point2D point, int axis)
            {
                this.point = point;

                this.axis = axis;

                this.left = null;
                this.right = null;
            }
        }

        //endregion

        //region "Fields"

        private int size;
        private Node root;

        // Range search globals
        private Stack<Point2D> range_points;

        // Nearest neighbor search globals
        private Point2D nn_guess;
        private double nn_bestDistSquared;

        // endregion

        //region "Initialization"

        public _2DTree()
        {
            initialize();
        }

        private void initialize()
        {
            size = 0;
            root = null;
        }

        //endregion

        //region "Public functions"

        public boolean isEmpty()
        {
            return size == 0;
        }

        public int size()
        {
            return size;
        }

        public void insert(Point2D p)
        {
            // Input validation
            if(p == null)
                throw new NullPointerException("Argument p in insert is NULL");

            insertPoint(p);
        }

        public boolean contains(Point2D p)
        {
            if(p == null)
                throw new NullPointerException("Argument p in contains is NULL");

            return contains(root, p);
        }

        public void draw()
        {
            draw(root, Constants.X_MIN, Constants.Y_MIN, Constants.X_MAX, Constants.Y_MAX);
        }

        public Iterable<Point2D> range(RectHV rect)
        {
            if(rect == null)
                throw new NullPointerException("Argument rect in range is NULL");

            return findPointsInRange(rect, root);
        }

        public Point2D nearest(Point2D p)
        {
            if(p == null)
                throw new NullPointerException("Argument p in nearest is NULL");

            return getNearestPoint(p);
        }

        //endregion

        //region "Internal implementation function"

        //region "Insert Internals"

        private void insertPoint(Point2D p)
        {
            // Adding first element to tree
            if(root == null)
            {
                root = new Node(p, Constants.HORIZONTAL);
                ++size;
                return;
            }

            // Usual insert
            addToTree(root, p);
        }

        private void addToTree(Node node, Point2D point)
        {
            // Translate point based on axis
            int comparisonResult = getComparisonResult(node, point);

            // Duplicates should not be processed
            if(comparisonResult == 0)
                return;

            // Identify child node to add / make a recursive call into
            if(comparisonResult > 0)
            {
                if(node.left == null)
                {
                    node.left = new Node(point, getNextAxis(node.axis));
                    ++size;
                    return;
                }

                addToTree(node.left, point);
            }
            else
            {
                if(node.right == null)
                {
                    node.right = new Node(point, getNextAxis(node.axis));
                    ++size;
                    return;
                }

                addToTree(node.right, point);
            }
        }

        private int getComparisonResult(Node node, Point2D point)
        {
            int comparisonResult;

            if(node.axis == Constants.HORIZONTAL)
            {
                comparisonResult = Double.compare(node.point.x(), point.x());

                if(comparisonResult == 0)
                    comparisonResult = Double.compare(node.point.y(), point.y());
            }
            else
            {
                comparisonResult = Double.compare(node.point.y(), point.y());

                if(comparisonResult == 0)
                    comparisonResult = Double.compare(node.point.x(), point.x());
            }

            return comparisonResult;
        }

        private int getNextAxis(int currentAxis)
        {
            if(currentAxis == Constants.HORIZONTAL)
                return Constants.VERTICAL;

            return Constants.HORIZONTAL;
        }

        //endregion

        //region "Contains Internals"

        private boolean contains(Node node, Point2D p)
        {
            if(node == null)
                return false;

            if(node.point.equals(p))
                return true;

            // Determine search node and make a recursive call
            Node searchNode = getNearestChildNodeToPoint(node, p);

            return contains(searchNode, p);
        }

        //endregion

        //region "NN Internals"

        private Point2D getNearestPoint(Point2D p)
        {
            initNearestNeighborGlobals();

            findNearestPoint(root, p);

            return nn_guess;
        }

        private void initNearestNeighborGlobals()
        {
            nn_guess = null;
            nn_bestDistSquared = Double.MAX_VALUE;
        }

        private void findNearestPoint(Node node, Point2D point)
        {
            // Null check
            if (node == null)
                return;

            // Check and update best guess and best known distance

            if(nn_guess == null)
            {
                // Make self as best-guess
                nn_guess = node.point;
                nn_bestDistSquared = node.point.distanceSquaredTo(point);
            }
            else
            {
                // Determine best guess
                double distanceToSearchPoint = node.point.distanceSquaredTo(point);

                if(distanceToSearchPoint < nn_bestDistSquared)
                {
                    // Update guess and bestDist
                    nn_bestDistSquared = distanceToSearchPoint;
                    nn_guess = node.point;
                }
            }

            // Make calls to best sub-tree

            Node primarySearchSubTree = getNearestChildNodeToPoint(node, point);

            findNearestPoint(primarySearchSubTree, point);

            // Check and make a call if searching the other sub-tree is required

            if(node.axis == Constants.HORIZONTAL)
            {
                if( Math.abs(node.point.x() - point.x()) < Math.sqrt(nn_bestDistSquared))
                    findNearestPoint(primarySearchSubTree == node.left ? node.right : node.left, point);
            }
            else
            {
                if( Math.abs(node.point.y() - point.y()) < Math.sqrt(nn_bestDistSquared))
                    findNearestPoint(primarySearchSubTree == node.left ? node.right : node.left, point);
            }

        }

        //endregion

        //region "Draw Internals"

        private void draw(Node node, double xMin, double yMin, double xMax, double yMax)
        {
            // Handle null nodes
            if(node == null)
                return;

            // Actual draw
            drawPointAndLine(node, xMin, yMin, xMax, yMax);

            // Make recursive calls to child nodes
            if(node.axis == Constants.HORIZONTAL)
            {
                draw(node.left, xMin, yMin, node.point.x(), yMax);
                draw(node.right, node.point.x(), yMin, xMax, yMax);
            }
            else
            {
                draw(node.left, xMin, yMin, xMax, node.point.y());
                draw(node.right, xMin, node.point.y(), xMax, yMax);
            }
        }

        private void drawPointAndLine(Node node, double xMin, double yMin, double xMax, double yMax)
        {
            // Draw point
            StdDraw.setPenColor(java.awt.Color.BLACK);
            StdDraw.point(node.point.x(), node.point.y());

            // Draw line
            StdDraw.setPenColor(getPenColor(node.axis));

            if(node.axis == Constants.HORIZONTAL)
                StdDraw.line(node.point.x(), yMin, node.point.x(), yMax);
            else
                StdDraw.line(xMin, node.point.y(), xMax, node.point.y());
        }

        private java.awt.Color getPenColor(int axis)
        {
            if(axis == Constants.HORIZONTAL)
                return java.awt.Color.RED;

            return java.awt.Color.BLUE;
        }

        //endregion

        //region "Range Internals"

        private Iterable<Point2D> findPointsInRange(RectHV rect, Node root)
        {
            initRangeGlobals();

            findAndStorePointsInRange(root, rect, Constants.X_MIN, Constants.Y_MIN, Constants.X_MAX, Constants.Y_MAX);

            return range_points;
        }

        private void initRangeGlobals()
        {
            range_points = new Stack<Point2D>();
        }

        private void findAndStorePointsInRange(Node node, RectHV rect, double xMin, double yMin, double xMax, double yMax)
        {
            // Null check
            if(node == null)
                return;

            // Check rectangle intersection for node
            RectHV currentRect = new RectHV(xMin, yMin, xMax, yMax);

            // If there is no intersection, don't process this node and it's child nodes
            if(rect.intersects(currentRect) == false)
                return;

            // Add to stack if rectangle contains node
            if(rect.contains(node.point))
                range_points.push(node.point);

            // Call sub-trees
            if(node.axis == Constants.HORIZONTAL)
            {
                findAndStorePointsInRange(node.left, rect, xMin, yMin, node.point.x(), yMax);
                findAndStorePointsInRange(node.right, rect, node.point.x(), yMin, xMax, yMax);
            }
            else
            {
                findAndStorePointsInRange(node.left, rect, xMin, yMin, xMax, node.point.y());
                findAndStorePointsInRange(node.right, rect, xMin, node.point.y(), xMax, yMax);
            }
        }

        //endregion

        //region "Common"

        private Node getNearestChildNodeToPoint(Node node, Point2D point)
        {
            Node nearestChild;

            if(node.axis == Constants.HORIZONTAL)
            {
                if(point.x() == node.point.x())
                    nearestChild = (point.y() <  node.point.y()) ? node.left : node.right;
                else
                    nearestChild = (point.x() <  node.point.x()) ? node.left : node.right;
            }
            else
            {
                if(point.y() == node.point.y())
                    nearestChild = (point.x() <  node.point.x()) ? node.left : node.right;
                else
                    nearestChild = (point.y() <  node.point.y()) ? node.left : node.right;
            }

            return nearestChild;
        }

        //endregion

        //endregion
    }

    //endregion

    //region "Main"

    public static void main(String[] args)
    {
        // unit testing of the methods (optional)

        KdTree kdTree = new KdTree();

        // Insert nodes
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));


        // Draw test
        kdTree.draw();
        StdDraw.show(50);
    }

    //endregion
}
