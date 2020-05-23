import java.util.ArrayList;

public class PaandiHelper {
    public static void main(String[] args) throws UnhandledLogicException, InvalidArgumentException {
        Board board = new Board(new int[]{1,4,4,10,7,1}, new int[]{3,7,1,7,8,2,2});
//        board.playGame(Board.PLAYER_A,0);
        Board.setToAchive(52);
        ArrayList<Integer> arr = new ArrayList<>();
        System.out.println("best index : " + Board.bestScoreRecursive(board, Board.PLAYER_A, arr));
        board.printBoard();
        int index = Board.bestScoreRecursive(board, Board.PLAYER_A, arr);
        board.findScoreForThisStep(Board.PLAYER_A, index);
        board.printBoard();
        System.out.println(arr.toString());

    }
}
