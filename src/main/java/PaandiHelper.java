import java.util.ArrayList;

public class PaandiHelper {
    public static void main(String[] args) throws UnhandledLogicException, InvalidArgumentException {
        Board board = new Board();
        ArrayList<Integer> arr = new ArrayList<>();
        System.out.println("best index : " + Board.bestScoreRecursive(board, Board.PLAYER_A, arr));
        int index = Board.bestScoreRecursive(board, Board.PLAYER_A, arr);
        board.findScoreForThisStep(Board.PLAYER_A, index);
        board.printBoard();
        //System.out.println(arr.toString());

    }
}
