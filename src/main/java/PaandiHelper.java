import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PaandiHelper {
    private static String PLAYER_A_VALUES = "PLAYER_A_VALUES";
    private static String PLAYER_B_VALUES = "PLAYER_B_VALUES";
    private static String PROCESSING_TIME = "PROCESSING_TIME";
    private static String STRATEGY = "STRATEGY";

    public static void main(String[] args) throws UnhandledLogicException, InvalidArgumentException {

        JSONObject argumentObject = new JSONObject(args[0]);
        JSONArray playerAArr = argumentObject.getJSONArray(PLAYER_A_VALUES);
        int[] aArr = new int[playerAArr.length()];
        JSONArray playerBArr = argumentObject.getJSONArray(PLAYER_B_VALUES);
        int[] bArr = new int[playerBArr.length()];

        for (int i = 0; i < playerAArr.length(); i++) {
            aArr[i] = playerAArr.getInt(i);
        }

        for (int i = 0; i < playerBArr.length(); i++) {
            bArr[i] = playerBArr.getInt(i);
        }

        int processingTime = argumentObject.getInt(PROCESSING_TIME);
        int strategy = argumentObject.getInt(STRATEGY);

        if(strategy == 0) {
            Board.setStrategy(Board.RIGHT_HOLE_CLOSE);
        }
        else if(strategy == 1){
            Board.setStrategy(Board.LEFT_HOLE_CLOSE);
        }
//        Board.setStrategy(Board.LEFT_HOLE_CLOSE);

        Board board = new Board(aArr, bArr);
//        board.playGame(Board.PLAYER_A,0);
        //Board.setToAchive(95);
        Board.setStartTime(new Date());
        Board.setProcessingMaxTimeInSeconds(processingTime);
        ArrayList<Integer> selectionChoices = new ArrayList<>();
        int score = Board.bestScoreRecursive(board, Board.PLAYER_A, selectionChoices);
        HashMap<Integer, ArrayList<Integer>> map = Board.getBestScoreWithLessChoicesMap();
        Gson gson = new Gson();
        String json = gson.toJson(map);
        System.out.println(json);

    }
}


/**
 "{PLAYER_A_VALUES:[7,7,7,7,7,7,7],PLAYER_B_VALUES:[7,7,7,7,7,7,7],PROCESSING_TIME:5}"
 */
