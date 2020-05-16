public class Hole {
    private static int startGemsCountPerHole = 7;

    private int gemsCount;
    private boolean isActive;
    public Hole(int gemsCount){
        this.gemsCount = gemsCount;
        this.isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Hole(){
        this.gemsCount = startGemsCountPerHole;
    }

    public static int getStartGemsCountPerHole() {
        return startGemsCountPerHole;
    }

    public static void setStartGemsCountPerHole(int startGemsCountPerHole) {
        Hole.startGemsCountPerHole = startGemsCountPerHole;
    }

    public int getGemsCount() {
        return gemsCount;
    }

    public void setGemsCount(int gemsCount) {
        this.gemsCount = gemsCount;
    }

    public void addOneGem(){
        this.gemsCount++;
    }

    public int popAll(){
        int popped = this.gemsCount;
        this.gemsCount = 0;
        return popped;
    }

    public void addAll(int gems){
        this.gemsCount += gems;
    }
}
