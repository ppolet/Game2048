
package game2048;

//14 - описывающий эффективность хода
public class MoveEfficiency implements Comparable<MoveEfficiency>{
    private int numberOfEmptyTiles;
    private int score;
    private Move move;
    
    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move){
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }
    
    public Move getMove(){
        return move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        int result = 0;
        result = (this.numberOfEmptyTiles > o.numberOfEmptyTiles)? 1: -1;
        if(result == 0){
            result = (this.score > o.score)? 1: -1;
        }
        return result;
    }
    
}
