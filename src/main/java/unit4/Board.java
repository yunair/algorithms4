package unit4;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = clone(tiles);
        dimension = this.tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] tile : tiles) {
            for (int i : tile) {
                sb.append(i + " ");
            }
            sb.append("\n");
        }
        return this.tiles.length + "\n" + sb.substring(0, sb.length() - 1);

    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int value = 0;
        for (int i = 0; i < dimension; i++) {
            final int[] tileRow = tiles[i];
            for (int j = 0; j < dimension; j++) {
                if (tileRow[j] == 0) {
                    continue;
                }
                if (getAimValue(i, j) != tileRow[j]) {
                    value++;
                }
            }
        }
        return value;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dis = 0;
        for (int i = 0; i < dimension; i++) {
            final int[] tileRow = tiles[i];
            for (int j = 0; j < dimension; j++) {
                final int value = tileRow[j];
                if (value == 0) {
                    continue;
                }

                final int aimValue = getAimValue(i, j);
                if (aimValue != value) {
                    final int xIndex = (value - 1) / dimension;
                    final int yIndex = (value - 1) % dimension;
                    dis += (Math.abs(xIndex - i) + Math.abs(yIndex - j));
//                    System.out.printf("i: %d\tj: %d\tvalue: %d\tdis: %d\n", i, j, tileRow[j], (Math.abs(xIndex - i) + Math.abs(yIndex - j)));
                }
            }
        }
        return dis;
    }

    private int getAimValue(int i, int j) {
        if (i == dimension - 1 && j == dimension - 1) {
            return 0;
        }
        return i * dimension + j + 1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension; i++) {
            final int[] tileRow = tiles[i];
            for (int j = 0; j < dimension; j++) {
                if (getAimValue(i, j) != tileRow[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board other = (Board) y;

        if (other.dimension != this.dimension) {
            return false;
        }
        for (int i = 0; i < dimension; i++) {
            int[] tileRow = tiles[i];
            int[] otherTileRow = other.tiles[i];
            for (int j = 0; j < dimension; j++) {
                if (tileRow[j] != otherTileRow[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int zeroXIndex = 0;
        int zeroYIndex = 0;
        for (int i = 0; i < dimension; i++) {
            int[] tileRow = tiles[i];
            for (int j = 0; j < dimension; j++) {
                if (tileRow[j] == 0) {
                    zeroXIndex = i;
                    zeroYIndex = j;
                }
            }
        }
        ArrayList<Board> neighbors = new ArrayList<>();

        if (zeroXIndex - 1 >= 0) {
            final Board twinBoard = new Board(clone(tiles));
            twinBoard.tiles[zeroXIndex][zeroYIndex] = twinBoard.tiles[zeroXIndex - 1][zeroYIndex];
            twinBoard.tiles[zeroXIndex - 1][zeroYIndex] = 0;
            neighbors.add(twinBoard);
        }
        if (zeroYIndex - 1 >= 0) {
            final Board twinBoard = new Board(clone(tiles));
            twinBoard.tiles[zeroXIndex][zeroYIndex] = twinBoard.tiles[zeroXIndex][zeroYIndex - 1];
            twinBoard.tiles[zeroXIndex][zeroYIndex - 1] = 0;
            neighbors.add(twinBoard);
        }

        if (zeroXIndex + 1 < dimension) {
            final Board twinBoard = new Board(clone(tiles));
            twinBoard.tiles[zeroXIndex][zeroYIndex] = twinBoard.tiles[zeroXIndex + 1][zeroYIndex];
            twinBoard.tiles[zeroXIndex + 1][zeroYIndex] = 0;
            neighbors.add(twinBoard);
        }

        if (zeroYIndex + 1 < dimension) {
            final Board twinBoard = new Board(clone(tiles));
            twinBoard.tiles[zeroXIndex][zeroYIndex] = twinBoard.tiles[zeroXIndex][zeroYIndex + 1];
            twinBoard.tiles[zeroXIndex][zeroYIndex + 1] = 0;
            neighbors.add(twinBoard);
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        final Board twinBoard = new Board(clone(tiles));

        if (twinBoard.tiles[0][0] == 0) {
            final int temp = twinBoard.tiles[0][1];
            twinBoard.tiles[0][1] = twinBoard.tiles[1][0];
            twinBoard.tiles[1][0] = temp;
        } else if (twinBoard.tiles[0][1] == 0) {
            final int temp = twinBoard.tiles[0][0];
            twinBoard.tiles[0][0] = twinBoard.tiles[1][0];
            twinBoard.tiles[1][0] = temp;
        } else {
            final int temp = twinBoard.tiles[0][0];
            twinBoard.tiles[0][0] = twinBoard.tiles[0][1];
            twinBoard.tiles[0][1] = temp;
        }

        return twinBoard;
    }

    private int[][] clone(int[][] arr) {
        int[][] clone = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            clone[i] = arr[i].clone();
        }
        return clone;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println(initial);
        Board same = new Board(tiles);
        System.out.println("equals ? " + initial.equals(same));
        System.out.println("twin: " + initial.twin());
        System.out.println("isGoal: " + initial.isGoal());
        System.out.println("hamming: " + initial.hamming());
        System.out.println("manhattan: " + initial.manhattan());

        final int[][] goalTiles = new int[n][n];
        for (int i = 0; i < goalTiles.length; i++) {
            for (int j = 0; j < goalTiles.length; j++) {
                goalTiles[i][j] = i * n + j + 1;
            }
        }
        goalTiles[n - 1][n - 1] = 0;
        Board goal = new Board(goalTiles);
        System.out.println("isGoal: " + goal.isGoal());
        System.out.println("hamming: " + goal.hamming());
        System.out.println("manhattan: " + goal.manhattan());
    }
}
