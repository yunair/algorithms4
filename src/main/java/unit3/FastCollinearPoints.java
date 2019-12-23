package unit3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegmentList;
    private final ArrayList<Point> parsedPoints;
    private final Point[] points;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] pointsIn) {
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
        parsedPoints = new ArrayList<>();
//        System.out.println(Arrays.deepToString(points));
        final Point[] otherPoints = new Point[points.length - 1];

        for (int i = 0; i < points.length; i++) {
            final Point currentPoint = points[i];
            parsedPoints.add(currentPoint);
            for (int j = 0; j < points.length; j++) {
                // assign otherPoints array without current point
                if (i < j) {
                    otherPoints[j - 1] = points[j];
                }
                if (i > j) {
                    otherPoints[j] = points[j];
                }
            }
            Arrays.sort(otherPoints, currentPoint.slopeOrder());
//            System.out.println(Arrays.deepToString(otherPoints));
            for (int j = 0; j < otherPoints.length - 2; j++) {
                final Point startPoint = otherPoints[j];
                final Point thirdPoint = otherPoints[j + 1];
                final Point fourthPoint = otherPoints[j + 2];

                final Double p12 = currentPoint.slopeTo(startPoint);
                final Double p13 = currentPoint.slopeTo(thirdPoint);
                final Double p14 = currentPoint.slopeTo(fourthPoint);
                if (p12.equals(p14) && p12.equals(p13)) {
                    if (j + 3 >= otherPoints.length) {
                        if (this.parsed(startPoint, thirdPoint, fourthPoint)) {
                            break;
                        }
                        lineSegmentList.add(new LineSegment(currentPoint, fourthPoint));
                        break;
                    }
                    boolean hasSeg = false;
                    final int initK = j + 3;
                    // try point 5 and then
                    for (int k = initK; k < otherPoints.length; k++) {
                        final Point kthPoint = otherPoints[k];
                        final double p1k = currentPoint.slopeTo(kthPoint);
                        if (p12 != p1k) {
                            j = k - 1;
                            hasSeg = true;
                            if (this.parsed(startPoint, thirdPoint, fourthPoint)) {
                                break;
                            }
                            lineSegmentList.add(new LineSegment(currentPoint, otherPoints[k - 1]));
                            break;
                        }
                    }
                    if (!hasSeg) {
                        if (this.parsed(startPoint, thirdPoint, fourthPoint)) {
                            break;
                        }
                        j = otherPoints.length - 1;
                        lineSegmentList.add(new LineSegment(currentPoint, otherPoints[j]));
                    }
                }
            }
        }
    }

    private boolean parsed(Point p1, Point p2, Point p3) {
        for (Point p : parsedPoints) {
            if (p == p1 || p == p2 || p == p3) {
                return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegmentList.size();
    }

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
