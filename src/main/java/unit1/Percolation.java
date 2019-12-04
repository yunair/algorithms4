package unit1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf1;
    private int openSiteCount;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n should be larger than 0");
        grid = new boolean[n][n];
        // 带head的
        uf = new WeightedQuickUnionUF(n * n + 1);
        // 带head和tail的
        uf1 = new WeightedQuickUnionUF(n * n + 2);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        assertValid(row, col);
        if (isOpen(row, col)) return;
        final int n = grid.length;
        grid[row - 1][col - 1] = true;
        final int index = (row - 1) * n + col;
        if (row == 1) {
            uf.union(0, index);
            uf1.union(0, index);
        }
        if (row == n) {
            uf1.union(n * n + 1, index);
        }
        if (row - 1 >= 1) {
            if (isOpen(row - 1, col)) {
                uf.union(index - n, index);
                uf1.union(index - n, index);
            }
        }
        if (row + 1 <= n) {
            if (isOpen(row + 1, col)) {
                uf.union(index + n, index);
                uf1.union(index + n, index);
            }
        }
        if (col - 1 >= 1) {
            if (isOpen(row, col - 1)) {
                uf.union(index - 1, index);
                uf1.union(index - 1, index);
            }
        }
        if (col + 1 <= n) {
            if (isOpen(row, col + 1)) {
                uf.union(index + 1, index);
                uf1.union(index + 1, index);
            }
        }
        openSiteCount++;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        assertValid(row, col);
        return grid[row - 1][col - 1] == true;
    }

    // A full site is an open site
    // that can be connected to an open site in the top row
    // via a chain of neighboring (left, right, up, down) open sites.
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        assertValid(row, col);
        final int n = grid.length;
        // 不能用uf1，可能从当前连到尾部，再连回头部
        return uf.connected(0, (row - 1) * n + col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        final int n = grid.length;
        return uf1.connected(0, n * n + 1);
    }

    private void assertValid(int row, int col) {
        final int n = grid.length;
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("index out of range");
    }
}
