package unit3;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;
    private final ArrayList<LineSegment> lineSegmentList;

    /**
     * Throw an IllegalArgumentException if the argument to the constructor is null,
     * if any point in the array is null, or if the argument to the constructor contains a repeated point.
     *
     * @param pointsIn
     */
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] pointsIn) {
        if (pointsIn == null) {
            throw new IllegalArgumentException("points is null");
        }
        for (Point point : pointsIn) {
            if (point == null) {
                throw new IllegalArgumentException("point is null");
            }
        }
        this.points = pointsIn.clone();
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("exist repeated point");
            }
        }

        lineSegmentList = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            final Point p1 = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                final Point p2 = points[j];
                final Double orderP12 = p1.slopeTo(p2);
                for (int k = j + 1; k < points.length - 1; k++) {
                    final Point p3 = points[k];
                    final Double orderP13 = p1.slopeTo(p3);
                    if (!orderP12.equals(orderP13)) {
                        continue;
                    }
                    for (int m = k + 1; m < points.length; m++) {
                        final Point p4 = points[m];
                        final Double orderP14 = p1.slopeTo(p4);
                        if (orderP12.equals(orderP14)) {
                            lineSegmentList.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegmentList.size();
    }

    /**
     * should include each line segment containing 4 points exactly once.
     * If 4 points appear on a line segment in the order p→q→r→s,
     * then you should include either the line segment p→s or s→p (but not both)
     * and you should not include subsegments such as p→r or q→r.
     * For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.
     */
    // the line segments
    public LineSegment[] segments() {
        final LineSegment[] segments = new LineSegment[lineSegmentList.size()];
        for (int i = 0; i < lineSegmentList.size(); i++) {
            segments[i] = lineSegmentList.get(i);
        }
        return segments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}