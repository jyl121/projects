package bearmaps.utils.ps;

import java.util.ArrayList;
import java.util.List;

public class KDTree implements PointSet {

    private KDTreeNode root;
    private List<Point> contents = new ArrayList<>();

    public KDTree(List<Point> points) {
        contents = points;
        root = new KDTreeNode(points.get(0), null, null);
        for (int i = 1; i < points.size(); i++) {
            insert(new KDTreeNode(points.get(i), null, null));
        }
    }

    public void insert(KDTreeNode n) {
        insertHelper(root, n, true);
    }

    public void insertHelper(KDTreeNode a, KDTreeNode n, boolean isOdd) {
        if (isOdd) {
            if (a.point.getX() >= n.point.getX()) {
                if (a.left == null) {
                    a.left = n;
                } else {
                    insertHelper(a.left, n, false);
                }
            } else {
                if (a.right == null) {
                    a.right = n;
                } else {
                    insertHelper(a.right, n, false);
                }
            }
        } else {
            if (a.point.getY() >= n.point.getY()) {
                if (a.left == null) {
                    a.left = n;
                } else {
                    insertHelper(a.left, n, true);
                }
            } else {
                if (a.right == null) {
                    a.right = n;
                } else {
                    insertHelper(a.right, n, true);
                }
            }
        }
    }

    public Point nearest(double x, double y) {
        Point p = new Point(x, y);
        return nearestHelper(root, p, root.point, true);
    }

    /**
    @source mgruben, https://github.com/mgruben/Kd-Trees/blob/master/KdTree.java
    */
    public Point nearestHelper(KDTreeNode n,
                               Point p, Point best, boolean isOdd) {
        if (n == null) {
            return best;
        }
        if (n.point.equals(p)) {
            return p;
        }
        if (Point.distance(n.point, p) < Point.distance(best, p)) {
            best = n.point;
        }
        double toPartitionLine = 0;
        Point test;
        if (isOdd) {
            toPartitionLine = p.getX() - n.point.getX();
            test = new Point(n.point.getX(), p.getY());
        } else {
            toPartitionLine = p.getY() - n.point.getY();
            test = new Point(p.getX(), n.point.getY());
        }
        if (toPartitionLine <= 0) {
            best = nearestHelper(n.left, p, best, !isOdd);
            if (Point.distance(p, best)
                    > Point.distance(test, p)) {
                best = nearestHelper(n.right, p, best, !isOdd);
            }
        } else {
            best = nearestHelper(n.right, p, best, !isOdd);
            if (Point.distance(p, best)
                    > Point.distance(test, p)) {
                best = nearestHelper(n.left, p, best, !isOdd);
            }
        }
        return best;
    }

    private class KDTreeNode {

        private Point point;
        private KDTreeNode left;
        private KDTreeNode right;

        KDTreeNode(Point p) {
            this.point = p;
        }

        KDTreeNode(Point p, KDTreeNode nLeft, KDTreeNode nRight) {
            this.point = p;
            this.left = nLeft;
            this.right = nRight;
        }

        Point point() {
            return point;
        }

        KDTreeNode left() {
            return left;
        }

        KDTreeNode right() {
            return right;
        }
    }

    public static void main(String[] args) {
        return;
    }
}
