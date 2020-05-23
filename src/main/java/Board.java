import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private static int childHoleCountPerSideA = 7;
    private static int childHoleCountPerSideB = 7;
    public static String PLAYER_A = "PLAYER_A";
    public static String PLAYER_B = "PLAYER_B";
    private static int toAchive = 10;

    private MotherHole playerAMotherHole;
    private MotherHole playerBMotherHole;
    private ArrayList<ChildHole> playerAChildHoles;
    private ArrayList<ChildHole> playerBChildHoles;

    public Board() {
        playerAChildHoles = new ArrayList<>();
        playerBChildHoles = new ArrayList<>();
        addChildHoles(playerAChildHoles);
        addChildHoles(playerBChildHoles);
        playerAMotherHole = new MotherHole();
        playerBMotherHole = new MotherHole();
    }

    public static int getToAchive() {
        return toAchive;
    }

    public static void setToAchive(int toAchive) {
        Board.toAchive = toAchive;
    }

    public Board(int[] arrA, int[] arrB){
        setChildHoleCountPerSideA(arrA.length);
        setChildHoleCountPerSideB(arrB.length);
        playerAChildHoles = new ArrayList<>();
        playerBChildHoles = new ArrayList<>();
        addChildHoles(playerAChildHoles, arrA);
        addChildHoles(playerBChildHoles, arrB);
        playerAMotherHole = new MotherHole();
        playerBMotherHole = new MotherHole();
    }

    public static int getChildHoleCountPerSideA() {
        return childHoleCountPerSideA;
    }

    public static void setChildHoleCountPerSideA(int childHoleCountPerSideA) {
        Board.childHoleCountPerSideA = childHoleCountPerSideA;
    }

    public static int getChildHoleCountPerSideB() {
        return childHoleCountPerSideB;
    }

    public static void setChildHoleCountPerSideB(int childHoleCountPerSideB) {
        Board.childHoleCountPerSideB = childHoleCountPerSideB;
    }

    public ScoreStatus findScoreForThisStep(String player, int index) throws InvalidArgumentException, UnhandledLogicException {
        if (getMyChildHoles(player).get(index).getGemsCount() == 0) {
            ScoreStatus scoreStatus = new ScoreStatus();
            scoreStatus.setValid(false);
            return scoreStatus;
        }
        int gemsInHand = getMyChildHoles(player).get(index).popAll();
        String side = player;
        while (true) {
//            System.out.println(player + " " + side + " " + (index + 1) + " " + gemsInHand + " " + (totalGem() + gemsInHand));
            Status status = playGems(player, side, index + 1, gemsInHand);
            if (status.isEndTurn()) {
                ScoreStatus scoreStatus = new ScoreStatus();
                scoreStatus.setChance(false);
                scoreStatus.setScore(getMyMotherHole(player).getGemsCount());
                return scoreStatus;
            }
            if (status.isAddToMother()) {
                getMyMotherHole(player).addAll(status.getGemInHand());
                ScoreStatus scoreStatus = new ScoreStatus();
                scoreStatus.setChance(false);
                scoreStatus.setScore(getMyMotherHole(player).getGemsCount());
                return scoreStatus;
            }
            if (status.isChance()) {
                ScoreStatus scoreStatus = new ScoreStatus();
                scoreStatus.setChance(true);
                scoreStatus.setScore(getMyMotherHole(player).getGemsCount());
//                printBoard();
                return scoreStatus;
            }
            if (status.isGameContinue()) {
                index = status.getNextIndex();
                gemsInHand = status.getGemInHand();
                side = status.getSide();
                continue;
            }

        }
    }

    public static class ScoreStatus{
        int score;
        boolean chance;
        boolean isValid = true;
        public int getScore() {
            return score;
        }

        public boolean isValid() {
            return isValid;
        }

        public void setValid(boolean valid) {
            isValid = valid;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public boolean isChance() {
            return chance;
        }

        public void setChance(boolean chance) {
            this.chance = chance;
        }
    }

    public void playGame(String player, int index) throws InvalidArgumentException, UnhandledLogicException {
        Scanner scanner = new Scanner(System.in);
        int gemsInHand = getMyChildHoles(player).get(index).popAll();
        String side = player;
        while (true) {
            System.out.println(player + " " + side + " " + (index + 1) + " " + gemsInHand + " " + (totalGem() + gemsInHand));
            Status status = playGems(player, side, index + 1, gemsInHand);
            printBoard();
            if (status.isEndTurn()) {
                player = getOppositeSide(player);
                System.out.println(player + ", which hole you want to play ? : ");
                index = scanner.nextInt();
                gemsInHand = getMyChildHoles(player).get(index).popAll();
                side = player;
                continue;
            }
            if (status.isAddToMother()) {
                getMyMotherHole(player).addAll(status.getGemInHand());
                player = getOppositeSide(player);
                System.out.println(player + ", which hole you want to play ? : ");
                index = scanner.nextInt();
                gemsInHand = getMyChildHoles(player).get(index).popAll();
                side = player;
                continue;
            }
            if (status.isChance()) {
                System.out.println(player + ", which hole you want to play ? : ");
                index = scanner.nextInt();
                gemsInHand = getMyChildHoles(player).get(index).popAll();
                side = player;
                continue;
                //throw new UnhandledLogicException("is chance");
            }
            if (status.isGameContinue()) {
                index = status.getNextIndex();
                gemsInHand = status.getGemInHand();
                side = status.getSide();
                continue;
            }
        }
    }

    private Status playGems(String player, String side, int index, int gemInHand) throws InvalidArgumentException, UnhandledLogicException {
        if (gemInHand == 0) {
            throw new InvalidArgumentException("gem in hand is zero but player tried to start with that value");
        }
        String prevSide = side;
        int prevIndex = index;
        if (index == getChildHoleCountPerSide(side)) {
            if (side.equals(player)) {
                getMyMotherHole(player).addOneGem();
                gemInHand--;
            }
            index = 0;
            side = getOppositeSide(side);
        }
        while (gemInHand > 0) {
            prevSide = side;
            prevIndex = index;
            if (index == getChildHoleCountPerSide(side)) {
                if (side.equals(player)) {
                    getMyMotherHole(player).addOneGem();
                    gemInHand--;
                }
                index = 0;
                side = getOppositeSide(side);
                continue;
            }
            ArrayList<ChildHole> childHoles = getMyChildHoles(side);
            ChildHole childHole = childHoles.get(index);
            childHole.addOneGem();
            gemInHand--;
            index++;
        }

        if (prevIndex == index) {
            throw new UnhandledLogicException("no moves made");
        }

        if (prevIndex == getChildHoleCountPerSide(prevSide) && prevSide.equals(player)) {
            Status status = new Status(side, prevIndex);
            status.setChance(true);
            return status;
        } else if (prevIndex == getChildHoleCountPerSide(prevSide)) {
            throw new UnhandledLogicException("Logic problem 1");
        } else if (prevSide.equals(player)) {
            if (!prevSide.equals(side)) {
                throw new UnhandledLogicException("Logic problem 2");
            }

            if (getMyChildHoles(side).get(prevIndex).getGemsCount() == 1) {
                String oppositeSide = getOppositeSide(side);
                int oppositeIndex = getOppositeIndex(prevIndex, oppositeSide);
                if (getMyChildHoles(oppositeSide).get(oppositeIndex).getGemsCount() > 0) {
                    int gemsFromOppsiteSide = getMyChildHoles(oppositeSide).get(oppositeIndex).popAll();
                    int gemsFromOurSide = getMyChildHoles(side).get(prevIndex).popAll();
                    Status status = new Status(side, prevIndex);
                    status.setGemInHand(gemsFromOppsiteSide + gemsFromOurSide);
                    status.setAddToMother(true);
                    return status;
                } else {
                    Status status = new Status(side, prevIndex);
                    status.setEndTurn(true);
                    return status;
                }
            } else if (getMyChildHoles(side).get(prevIndex).getGemsCount() > 1) {
                Status status = new Status(side, prevIndex);
                status.setGemInHand(getMyChildHoles(side).get(prevIndex).popAll());
                status.setGameContinue(true);
                return status;
            } else {
                throw new UnhandledLogicException("Logic problem 3");
            }


        } else {
            if (getMyChildHoles(side).get(prevIndex).getGemsCount() == 1) {
                Status status = new Status(side, prevIndex);
                status.setEndTurn(true);
                return status;
            } else if (getMyChildHoles(side).get(prevIndex).getGemsCount() > 1) {
                Status status = new Status(side, prevIndex);
                status.setGemInHand(getMyChildHoles(side).get(prevIndex).popAll());
                status.setGameContinue(true);
                return status;
            } else {
                throw new UnhandledLogicException("Logic problem 3");
            }

        }

    }

    private int getChildHoleCountPerSide(String side) throws InvalidArgumentException {
        if (PLAYER_A.equals(side)) {
            return childHoleCountPerSideA;
        } else if (PLAYER_B.equals(side)) {
            return childHoleCountPerSideB;
        }
        throw new InvalidArgumentException("side should be either PLAYER _A or _B");
    }

    private int getOppositeIndex(int index, String side) throws InvalidArgumentException {
        if (index > getChildHoleCountPerSide(side) - 1) {
            return -1;
        } else {
            return getChildHoleCountPerSide(side) - 1 - index;
        }
    }

    private String getOppositeSide(String side) throws InvalidArgumentException {
        if (PLAYER_A.equals(side)) {
            return PLAYER_B;
        } else if (PLAYER_B.equals(side)) {
            return PLAYER_A;
        }
        throw new InvalidArgumentException("side should be either PLAYER _A or _B");
    }

    private class Status {
        private int gemInHand = 0;
        private boolean isEndTurn = false;
        private boolean addToMother = false;
        private boolean chance = false;
        private boolean gameContinue = true;
        private String side;
        private int nextIndex;

        Status(String side, int nextIndex) {
            this.side = side;
            this.nextIndex = nextIndex;
        }

        public int getNextIndex() {
            return nextIndex;
        }

        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public boolean isGameContinue() {
            return gameContinue;
        }

        public void setGameContinue(boolean gameContinue) {
            this.gameContinue = gameContinue;
        }

        public boolean isChance() {
            return chance;
        }

        public void setChance(boolean chance) {
            this.chance = chance;
        }

        public int getGemInHand() {
            return gemInHand;
        }

        public void setGemInHand(int gemInHand) {
            this.gemInHand = gemInHand;
        }

        public boolean isEndTurn() {
            return isEndTurn;
        }

        public void setEndTurn(boolean endTurn) {
            isEndTurn = endTurn;
        }

        public boolean isAddToMother() {
            return addToMother;
        }

        public void setAddToMother(boolean addToMother) {
            this.addToMother = addToMother;
        }

    }

    private ArrayList<ChildHole> getMyChildHoles(String player) throws InvalidArgumentException {
        if (PLAYER_A.equals(player)) {
            return playerAChildHoles;
        } else if (PLAYER_B.equals(player)) {
            return playerBChildHoles;
        }
        throw new InvalidArgumentException("Player should be either PLAYER_A or PLAYER_B");
    }

    private MotherHole getMyMotherHole(String player) throws InvalidArgumentException {
        if (PLAYER_A.equals(player)) {
            return playerAMotherHole;
        } else if (PLAYER_B.equals(player)) {
            return playerBMotherHole;
        }
        throw new InvalidArgumentException("Player should be either PLAYER_A or PLAYER_B");
    }


    private void addChildHoles(ArrayList<ChildHole> childHoles) {
        for (int i = 0; i < 7; i++) {
            childHoles.add(new ChildHole());
        }
    }

    private void addChildHoles(ArrayList<ChildHole> childHoles, int[] arr){
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            childHoles.add(new ChildHole(arr[i]));
        }
    }

    public void printBoard() throws InvalidArgumentException {
        for (int i = 0; i < Math.max(childHoleCountPerSideA, childHoleCountPerSideB) + 4; i++) {
            System.out.print("= ");
        }
        System.out.println();
        System.out.print(" ");
        for (int i = childHoleCountPerSideA - 1; i >= 0; i--) {
            System.out.print(getMyChildHoles(PLAYER_A).get(i).getGemsCount() + " ");
        }
        System.out.println();
        System.out.print(getMyMotherHole(PLAYER_A).getGemsCount());
        for (int i = 0; i < Math.max(childHoleCountPerSideA, childHoleCountPerSideB); i++) {
            System.out.print("  ");
        }
        System.out.print(getMyMotherHole(PLAYER_B).getGemsCount() + "\n");

        System.out.print(" ");
        for (int i = 0; i < childHoleCountPerSideB; i++) {
            System.out.print(getMyChildHoles(PLAYER_B).get(i).getGemsCount() + " ");
        }
        System.out.println();
        for (int i = 0; i < Math.max(childHoleCountPerSideA, childHoleCountPerSideB) + 4; i++) {
            System.out.print("= ");
        }
        System.out.println("\n\n");
    }

    public int totalGem() {
        int total = 0;
        for (ChildHole childHole : playerBChildHoles) {
            total += childHole.getGemsCount();
        }
        for (ChildHole childHole : playerAChildHoles) {
            total += childHole.getGemsCount();
        }
        total += playerAMotherHole.getGemsCount();
        total += playerBMotherHole.getGemsCount();
        return total;
    }

//    public static int findBestIndexForBestScore(Board board, String player) throws InvalidArgumentException, UnhandledLogicException {
//        Gson gson = new Gson();
//        int holeCount = board.getChildHoleCountPerSide(player);
//        int maxScore = 0;
//        int index = 0;
//        for (int i = 0; i < holeCount; i++) {
//            Board boardCopy = gson.fromJson(gson.toJson(board), Board.class);
//            ScoreStatus scoreStatus = boardCopy.findScoreForThisStep(player,i);
//            int score;
//            if(!scoreStatus.isValid){
//                continue;
//            }
//            else if(scoreStatus.isChance()){
//                score = bestScoreRecursive(board, player);
//            }
//            else{
//                score = scoreStatus.getScore();
//            }
//            if(maxScore < score) {
//                maxScore = score;
//                index = i;
//            }
//        }
//        return index;
//    }

    public static class HoleOrder{

    }

    public static int bestScoreRecursive(Board board, String player, ArrayList<Integer> holeOrder) throws InvalidArgumentException, UnhandledLogicException {
        Gson gson = new Gson();
        int holeCount = board.getChildHoleCountPerSide(player);
        int maxScore = 0;
        int index = 0;
        ArrayList<Integer> bestHoleOrder = null;
        for (int i = 0; i < holeCount; i++) {
            Board boardCopy = gson.fromJson(gson.toJson(board), Board.class);
            ScoreStatus scoreStatus = boardCopy.findScoreForThisStep(player,i);
            ArrayList<Integer> copyArr = new ArrayList<>();
            copyArr.addAll(holeOrder);
            copyArr.add(i);
            int score;
            if(!scoreStatus.isValid){
                continue;
            }
            else if(scoreStatus.isChance()){
                score = bestScoreRecursive(boardCopy, player, copyArr);
            }
            else{
                score = scoreStatus.getScore();
            }
            if(maxScore < score) {
                index = i;
                maxScore = score;
                bestHoleOrder = copyArr;
                if(maxScore > toAchive) {
                    System.out.println("max: " + maxScore + "arr : " + bestHoleOrder);
                }
            }
        }
        if(bestHoleOrder != null){
            holeOrder.clear();
            holeOrder.addAll(bestHoleOrder);
        }
        return maxScore;
    }
}
