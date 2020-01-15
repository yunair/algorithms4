package unit5;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KdTree {
    private static final boolean X_AXIS = true;

    private static class Node {
        private Point2D p;
        private boolean axis;
        private Node left;
        private Node right;
        private RectHV rect;

        public Node(Point2D p, boolean axis, RectHV rect) {
            this.p = p;
            this.axis = axis;
            this.rect = rect;
        }

        public Point2D getP() {
            return p;
        }

        public void setP(Point2D p) {
            this.p = p;
        }

        public boolean isAxis() {
            return axis;
        }

        public void setAxis(boolean axis) {
            this.axis = axis;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public RectHV getRect() {
            return rect;
        }

        public void setRect(RectHV rect) {
            this.rect = rect;
        }
    }

    private Node head;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
        head = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNotNull(p);
        if (head == null) {
            head = new Node(p, X_AXIS, new RectHV(0, 0, 1, 1));
        } else {
            Node node = head;
            int equal;
            while ((equal = checkEqual(node, p)) != 0) {
                if (equal < 0) {
                    if (node.right == null) {
                        final RectHV rightRect;
                        if (node.axis == X_AXIS) {
                            rightRect = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
                        } else {
                            rightRect = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
                        }
                        node.right = new Node(p, !node.axis, rightRect);
                        break;
                    }
                    node = node.right;
                } else {

                    if (node.left == null) {
                        final RectHV leftRect;
                        if (node.axis == X_AXIS) {
                            leftRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
                        } else {
                            leftRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
                        }
                        node.left = new Node(p, !node.axis, leftRect);
                        break;
                    }
                    node = node.left;
                }
            }
            if (equal == 0) {
                size--;
                node.p = p;
            }
        }
        size++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNotNull(p);
        Node node = head;
        if (head == null) {
            return false;
        }

        int equal;
        while ((equal = checkEqual(node, p)) != 0) {
            if (equal < 0) {
                if (node.right == null) {
                    return false;
                }
                node = node.right;
            } else {
                if (node.left == null) {
                    return false;
                }
                node = node.left;
            }
        }
        return true;
    }

    private int checkEqual(Node node, Point2D point) {
        if (node.axis == X_AXIS) {
            if (Double.compare(node.p.x(), point.x()) != 0) {
                return Double.compare(node.p.x(), point.x());
            }
            if (Objects.equals(point.y(), node.p.y())) {
                // 完全相同才返回0
                return 0;
            } else {
                // 左子树<本身的值，右子树>=本身值
                return -2;
            }
        } else {
            if (Double.compare(node.p.y(), point.y()) != 0) {
                return Double.compare(node.p.y(), point.y());
            }
            if (Objects.equals(point.x(), node.p.x())) {
                // 完全相同才返回0
                return 0;
            } else {
                // 左子树<本身的值，右子树>=本身值
                return -2;
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(head);
    }

    private void draw(Node node) {
        if (node == null) {
            return;
        }
        RectHV rectHV = node.rect;
        StdDraw.setPenColor(StdDraw.BLACK);
        node.p.draw();
        if (node.axis == X_AXIS) {
            StdDraw.setPenColor(StdDraw.RED);
            Point2D p = new Point2D(node.p.x(), rectHV.ymin());
            p.drawTo(new Point2D(node.p.x(), rectHV.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            Point2D p = new Point2D(rectHV.xmin(), node.p.y());
            p.drawTo(new Point2D(rectHV.xmax(), node.p.y()));
        }

        if (node.left != null) {
            draw(node.left);
        }
        if (node.right != null) {
            draw(node.right);
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNotNull(rect);
        final List<Point2D> ranges = new ArrayList<>();

        range(rect, head, ranges);

        return ranges;
    }

    private void range(RectHV rect, Node node, List<Point2D> ranges) {
        if (node == null) {
            return;
        }
        if (!rect.intersects(node.rect)) {
            return;
        }

        // no need to explore that node (or its subtrees).
        if (rect.contains(node.p)) {
            ranges.add(node.p);
        }
        range(rect, node.left, ranges);
        range(rect, node.right, ranges);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNotNull(p);
        if (isEmpty()) {
            return null;
        }
        return findNearest(head, p, head.p);
    }

    private Point2D findNearest(Node node, Point2D p, Point2D min) {
        if (node == null) {
            return min;
        }

        if (node.p.equals(p)) {
            return p;
        }

        double dis = p.distanceSquaredTo(node.p);
        double currentMin = p.distanceSquaredTo(min);
        if (dis < currentMin) {
            min = node.p;
        }
        int equal = checkEqual(node, p);
        if (equal < 0) {
            min = findNearest(node.right, p, min);
            if (node.left != null && p.distanceSquaredTo(min) > node.left.rect.distanceSquaredTo(p)) {
                min = findNearest(node.left, p, min);
            }
        } else {

            min = findNearest(node.left, p, min);
            if (node.right != null && p.distanceSquaredTo(min) > node.right.rect.distanceSquaredTo(p)) {
                min = findNearest(node.right, p, min);
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
        System.out.println(args[0]);
        String filename = args[0];
        In in = new In(filename);
        KdTree tree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
        System.out.println(tree.nearest(new Point2D(0.447, 0.413)));
    }
}