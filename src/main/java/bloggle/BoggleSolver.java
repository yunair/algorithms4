package bloggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final int[][] dirs = {{1, 0}, {-1, 0}, {1, 1}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, -1}};
    private Node root = new Node();

    private static class Node {
        boolean isWord;
        Node[] next = new Node[26];
    }

    private void put(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            final int index = word.charAt(i) - 'A';
            if (node.next[index] == null) {
                node.next[index] = new Node();
            }
            node = node.next[index];
            if (i == word.length() - 1) {
                node.isWord = true;
            }
        }
    }

    private boolean isValidWord(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            node = node.next[word.charAt(i) - 'A'];
            if (node == null) {
                return false;
            }
            if (i == word.length() - 1 && node.isWord) {
                return true;
            }
        }
        return false;
    }


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            put(word);
        }
    }

    private int score(String word) {
        if (word.length() == 3 || word.length() == 4) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        } else if (word.length() >= 7) {
            return 11;
        } else {
            return 0;
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
//        two dice are adjacent if they are horizontal, vertical, or diagonal neighbors.
        Set<String> validWords = new HashSet<>();
        // 回朔记录
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                backtrack(board, i, j, root, visited, "", validWords);
            }
        }
        return validWords;
    }

    private String getWord(String word, BoggleBoard board, int i, int j) {
        char letter = board.getLetter(i, j);
        if (letter == 'Q') {
            return word + "QU";
        } else {
            return word + letter;
        }
    }


    private Node getNode(Node node, BoggleBoard board, int i, int j) {
        char letter = board.getLetter(i, j);
        if (letter == 'Q') {
            if (node.next['Q' - 'A'] == null) {
                return null;
            }
            return node.next['Q' - 'A'].next['U' - 'A'];
        } else {
            return node.next[letter - 'A'];
        }
    }

    private void backtrack(BoggleBoard board, int i, int j, Node node, boolean[][] visited, String word, Set<String> validWords) {
        String cur = getWord(word, board, i, j);
        Node next = getNode(node, board, i, j);
        if (next == null) {
            return;
        }

        if (cur.length() >= 3 && next.isWord) {
            validWords.add(cur);
        }

        visited[i][j] = true;
        for (int[] dir : dirs) {
            int i1 = dir[0] + i, j1 = dir[1] + j;
            if (i1 < 0 || i1 > board.rows() - 1 || j1 < 0 || j1 > board.cols() - 1 || visited[i1][j1]) {
                continue;
            }

            backtrack(board, i1, j1, next, visited, cur, validWords);
        }
        visited[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!isValidWord(word)) {
            return 0;
        }
        return score(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
