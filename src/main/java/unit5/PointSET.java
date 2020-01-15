package unit5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final Set<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNotNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNotNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNotNull(rect);
        final List<Point2D> ranges = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                ranges.add(point);
            }
        }
        return ranges;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNotNull(p);

        Point2D min = null;
        double minDis = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            final double dis = p.distanceSquaredTo(point);
            if (dis < minDis) {
                minDis = dis;
                min = point;
            }
        }
        return min;
    }

    private void checkNotNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET set = new PointSET();
        set.insert(new Point2D(1.0, 1.0));
        set.insert(new Point2D(0.0, 1.0));
        set.insert(new Point2D(1.0, 0.0));
        set.insert(new Point2D(0.0, 1.0));
        System.out.println(set.size());
    }
}