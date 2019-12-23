package unit4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Solver {
    private final MinPQ<Node> nodes = new MinPQ<>();
    //    private final ArrayList<Node> previousNodes = new ArrayList<>();
    private final Stack<Board> steps = new Stack<>();
    private Node success = null;

    private static class Node implements Comparable<Node> {
        private final Board board;
        private final int move;
        private final Node prev;
        private final int priority;

        public Node(Board board, int move, Node prev) {
            this.board = board;
            this.move = move;
            this.prev = prev;
//            this.priority = current.hamming();
            this.priority = board.manhattan();
        }

        public List<Node> neighbors() {
            final ArrayList<Node> neighbors = new ArrayList<>();
//            System.out.println("current: " + board);
            for (Board board : board.neighbors()) {
                if (prev != null && Objects.equals(board, prev.board)) {
                    continue;
                }
//                System.out.println("neighbors: " + board);
                neighbors.add(new Node(board, move + 1, this));
            }
            return neighbors;
        }

        private int cost() {
            return priority + move;
        }

        public int compareTo(Node y) {
            final int diff = cost() - y.cost();
            if (diff == 0) {
                return priority - y.priority;
            }
            return diff;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("argument is null");
        }

        nodes.insert(new Node(initial, 0, null));
        nodes.insert(new Node(initial.twin(), 0, null));

        while (!nodes.isEmpty()) {
            Node current = nodes.delMin();
            if (current.board.isGoal()) {
                success = current;
                break;
            }

            for (Node neighbor : current.neighbors()) {
                nodes.insert(neighbor);
            }
        }
        Node findStart = success;
        steps.push(findStart.board);
        while (findStart.prev != null) {
            findStart = findStart.prev;
            steps.push(findStart.board);
        }
        if (!Objects.equals(findStart.board, initial)) {
            success = null;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return success != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) {
            return success.move;
        }
        return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return steps;
        }
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
