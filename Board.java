import java.util.Arrays;
import java.util.ArrayList;
public class Board {
    private String[][] board;

    public Board() {};
    public Board(int size) {
        this.board = createBoard(size);
        initializeBoard(board);
    }

    public String[][] createBoard(int size) {
        String[][] board = new String[size][size];
        return board;
    }

    public void initializeBoard(String[][] board) {
        int center = board.length / 2 - 1;

        for(int row = 0; row < board.length; ++row) {
            for (int column = 0; column < board.length; ++column) {
                if ((row == center && column == center) || (row == center + 1 && column == center + 1)) {
                    board[row][column] = "W";
                } else if ((row == center && column == center + 1) || (row == center + 1 && column == center)) {
                    board[row][column] = "B";
                } else {
                    board[row][column] = "_";
                }
            }
        }
    }

    public void changePosition(Integer[] coordinates, String newPiece) {
        board[coordinates[0]][coordinates[1]] = newPiece;
    }

    public Integer[][] getLegalMoves(String color) {
        ArrayList<Integer[]> legalList = new ArrayList<Integer[]>();
        Searcher searcher = new Searcher(color, this);
        legalList = searcher.findLegalMoves();

        Integer[][] legalMoves = new Integer[legalList.size()][legalList.size()];
        legalMoves = legalList.toArray(legalMoves);
/*        for (Integer[] item : legalMoves) {
            System.out.println(Arrays.toString(item));
        }*/
        return legalMoves;
    }

    public Integer[][] getFlips(Integer[] coor) {
        String piece = board[coor[0]][coor[1]];
        ArrayList<Integer[]> flips = new ArrayList<Integer[]>();
        Searcher searcher = new Searcher(piece, this);
        flips = searcher.flipPieces(coor);

        Integer[][] validFlips = new Integer[flips.size()][flips.size()];
        validFlips = flips.toArray(validFlips);
        return validFlips;
    }

    public String[][] getBoardStructure() {
        return board;
    }

    public void displayBoard() {
        for(String[] row : board) {
            for(String position : row) {
                System.out.print(position);
            }
            System.out.println();
        }
    }
}

class Searcher {
    private String color;
    private String[][] board;

    Searcher(){};
    Searcher(String color, Board board) {
        this.color = color;
        this.board = board.getBoardStructure();
    }

    public ArrayList<Integer[]> findLegalMoves() {
        ArrayList<Integer[]> validMoves = new ArrayList<Integer[]>();

        for (int row = 0; row < board.length; ++row) {
            for (int col = 0; col < board.length; ++col) {
                int[] coor = {row, col};
                if (board[row][col] == color) {
                    validMoves.addAll(checkAxes(coor));
                }
            }
        }

        return validMoves;
    }

    public ArrayList<Integer[]> checkAxes(int[] coordinate) {
        
        ArrayList<Integer[]> cardinalMoves = checkCardinalAxis(coordinate);
        return cardinalMoves;
    }

    private ArrayList<Integer[]> checkVector(String piece, Integer[][] vector) {
        ArrayList<Integer[]> validMoves = new ArrayList<Integer[]>();

        for (int coorIndex = 0; coorIndex < vector.length; ++coorIndex) {
            Integer[] currentPos = vector[coorIndex];
            String currentPiece = board[currentPos[0]][currentPos[1]];

            if (currentPiece == piece) {
                break;
            }

            if (currentPiece == "_") {
                if (coorIndex != 0) {
                    validMoves.add(currentPos);
                }

                break;
            }
        }

        return validMoves;
    }

    private ArrayList<Integer[]> checkCardinalAxis(int[] coordinate) {
        int size = board.length;
        int rowIndex = coordinate[0];
        int columnIndex = coordinate[1];
        String piece = board[coordinate[0]][coordinate[1]];
        Integer[][] left = new Integer[coordinate[0]][2];
        Integer[][] right = new Integer[size - coordinate[0] - 1][2];
        Integer[][] upper = new Integer[coordinate[0]][2];
        Integer[][] lower = new Integer[size - coordinate[0] - 1][2];

        for (int i = 0; i < size; ++i) {
            Integer[] horizontalCoor = {rowIndex, i};
            Integer[] verticalCoor = {i, columnIndex};

            if (i < coordinate[0]) {
                left[left.length - i - 1] = horizontalCoor;
                upper[upper.length - i - 1] = verticalCoor;
            } else if (i > coordinate[0]) {
                right[i - left.length - 1] = horizontalCoor;
                lower[i - upper.length - 1] = verticalCoor;
            }
        }

        ArrayList<Integer[]> validMoves = new ArrayList<Integer[]>();
        ArrayList<Integer[]> leftValidMoves = checkVector(piece, left);
        ArrayList<Integer[]> rightValidMoves = checkVector(piece, right);
        ArrayList<Integer[]> upperValidMoves = checkVector(piece, upper);
        ArrayList<Integer[]> lowerValidMoves = checkVector(piece, lower);
       
        validMoves.addAll(leftValidMoves);
        validMoves.addAll(rightValidMoves);
        validMoves.addAll(upperValidMoves);
        validMoves.addAll(lowerValidMoves);

        return validMoves;
    }

    public ArrayList<Integer[]> flipPieces(Integer[] coordinate) {
        int size = board.length;
        int rowIndex = coordinate[0];
        int columnIndex = coordinate[1];
        String piece = board[coordinate[0]][coordinate[1]];
        Integer[][] left = new Integer[coordinate[0]][2];
        Integer[][] right = new Integer[size - coordinate[0] - 1][2];
        Integer[][] upper = new Integer[coordinate[0]][2];
        Integer[][] lower = new Integer[size - coordinate[0] - 1][2];

        for (int i = 0; i < size; ++i) {
            Integer[] horizontalCoor = {rowIndex, i};
            Integer[] verticalCoor = {i, columnIndex};

            if (i < coordinate[0]) {
                left[left.length - i - 1] = horizontalCoor;
                upper[upper.length - i - 1] = verticalCoor;
            } else if (i > coordinate[0]) {
                right[i - left.length - 1] = horizontalCoor;
                lower[i - upper.length - 1] = verticalCoor;
            }
        }

        ArrayList<Integer[]> validFlips = new ArrayList<Integer[]>();
        ArrayList<Integer[]> leftValidFlips = checkFlips(piece, left);
        ArrayList<Integer[]> rightValidFlips = checkFlips(piece, right);
        ArrayList<Integer[]> upperValidFlips = checkFlips(piece, upper);
        ArrayList<Integer[]> lowerValidFlips = checkFlips(piece, lower);

        validFlips.addAll(leftValidFlips);
        validFlips.addAll(rightValidFlips);
        validFlips.addAll(upperValidFlips);
        validFlips.addAll(lowerValidFlips);

        return validFlips;
    }

    private ArrayList<Integer[]> checkFlips(String piece, Integer[][] vector) {
        ArrayList<Integer[]> flipMoves = new ArrayList<Integer[]>();

        for (int coorIndex = 0; coorIndex < vector.length; ++coorIndex) {
            Integer[] currentPos = vector[coorIndex];
            String currentPiece = board[currentPos[0]][currentPos[1]];

            if (currentPiece == piece || currentPiece == "_") {
                break;
            } else {
                flipMoves.add(currentPos);
            }
        }

        return flipMoves;
    }
}
