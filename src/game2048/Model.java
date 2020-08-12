
package game2048;

//1.2 - будет содержать игровую логику и хранить игровое поле.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Model {
    private final int FIELD_WIDTH = 4;   //3 - определяющая ширину игрового поля
    private Tile[][] gameTiles;   //3.2
    int score;      // 5 - текущий счет 
    int maxTile;    // 5 - максимальный вес плитки на игровом поле
    Stack<Tile[][]> previousStates;  //11 - предыдущие состояния игрового поля
    Stack<Integer> previousScores;  //11 - предыдущие счета
    boolean isSaveNeeded = true;
    
    public Model(){
        resetGameTiles();
    }
    
    //8.1
    public Tile[][] getGameTiles(){
        return gameTiles;
    }
    
    protected void resetGameTiles(){
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for(int i = 0; i<gameTiles.length; i++)
            for(int j = 0; j<gameTiles.length; j++){
                gameTiles[i][j] = new Tile();       //3.3
            }
        score = 0;
        maxTile = 2;
        previousStates = new Stack<>();
        previousScores = new Stack<>();
        addTile();
        addTile();
    }
    
    //4 - который будет смотреть какие плитки пустуют и менять вес одной из них, выбранной случайным образом, на 2 или 4 (на 9 двоек должна приходиться 1 четверка)
    private void addTile(){
        ArrayList<Tile> emptyTiles = getEmptyTiles();
        if(!emptyTiles.isEmpty() && emptyTiles.size()>0){
            int index = (int)(Math.random() * emptyTiles.size());
            Tile emptyTile = emptyTiles.get(index);
            int newTileValue = (Math.random() < 0.9) ? 2 : 4;
            emptyTile.setValue(newTileValue);
        }
    }
    
    //4 - возвращающий список свободных плиток в массиве gameTiles
    private ArrayList<Tile> getEmptyTiles(){
        ArrayList<Tile> resultList = new ArrayList<>();
        for(int i = 0; i<gameTiles.length; i++)
            for(int j = 0; j<gameTiles.length; j++){
                if(gameTiles[i][j].isEmpty()) resultList.add(gameTiles[i][j]);
            }
        return resultList;
    }
    
    //5 - Сжатие плиток, таким образом, чтобы все пустые плитки были справа, т.е. ряд {4, 2, 0, 4} становится рядом {4, 2, 4, 0}
    // движение влево
    private boolean compressTiles(Tile[] tiles){
        boolean compress = false;
        int n = 0;
        for(int i=0; i<tiles.length; i++){
            if(!tiles[n].isEmpty()){
                n++;
                continue;
            }
            if(!tiles[i].isEmpty()){
                tiles[n] = tiles[i];
                tiles[i] = new Tile();
                n++;
                compress = true;
            }
        }
        return compress;
    }
    
    //5 - Слияние плиток одного номинала, т.е. ряд {4, 4, 2, 0} становится рядом {8, 2, 0, 0}. Обрати внимание, что ряд {4, 4, 4, 4} превратится в {8, 8, 0, 0}, а {4, 4, 4, 0} в {8, 4, 0, 0}.
    //сжатие будет всегда выполнено перед слиянием, таким образом в метод mergeTiles всегда передается массив плиток без пустых в середине.
    // движение влево
    private boolean mergeTiles(Tile[] tiles){
        boolean merge = false;
        for(int i=1; i<tiles.length; i++){
            if (tiles[i-1].getValue() == 0) continue;
            if (tiles[i-1].getValue() == tiles[i].getValue()){
                int tempValueTile = tiles[i-1].getValue();
                tiles[i-1].setValue(tempValueTile * 2);
                score += tempValueTile;                                 //Увеличиваем счет
                if(tempValueTile > maxTile) maxTile = tempValueTile;    //меняем максимальную плитку
                //сдвигаем весь массив влево
                for(int j=i+1; j<tiles.length; j++){
                    tiles[j-1].setValue(tiles[j].getValue());
                }
                tiles[tiles.length-1].setValue(0);  //значение самого правого элемента массива после сдвига = 0
                merge = true;
            }
        }
        return merge;
    }
    
    /////////////////////////////// для отладки ////////////////////////////////
    public void printGameTilesArray(){
        for(int i = 0; i<gameTiles.length; i++){
            for(int j = 0; j<gameTiles.length; j++){
                System.out.print(gameTiles[i][j].getValue());
            }
            System.out.println();
        }
        System.out.println();
    }
    
    //6.3
    public void left(){
        //12.2
        if(isSaveNeeded){
            saveState(gameTiles); //12.1 - сохраняем состояние для отмены
        }
        
        boolean isChange = false;
        for(int i=0; i<gameTiles.length; i++){
            if (compressTiles(gameTiles[i])){
                isChange = true;          //было изменение
            }
            if (mergeTiles(gameTiles[i])){
                isChange = true;          //было изменение
            }
        }
        if(isChange){
            addTile();      //добавляем плитку
            isSaveNeeded = true;
        } 
    }
    
    //7
    public void up(){
        saveState(gameTiles);   //12.1 - сохраняем состояние для отмены
        rotateArrayToRight();
        rotateArrayToRight();
        rotateArrayToRight();
        left();
        rotateArrayToRight();
    }
    
    public void down(){
        saveState(gameTiles);   //12.1 - сохраняем состояние для отмены
        rotateArrayToRight();
        left();
        rotateArrayToRight();
        rotateArrayToRight();
        rotateArrayToRight();
    }
    
    public void right(){
        saveState(gameTiles);   //12.1 - сохраняем состояние для отмены
        rotateArrayToRight();
        rotateArrayToRight();
        left();
        rotateArrayToRight();
        rotateArrayToRight();
    }
    
    //поворот 2-х мерного массива по часовой стрелке на 90 градусов
    private void rotateArrayToRight(){
        Tile tempCell;
        int n = gameTiles.length;
        for(int i=0; i<n/2; i++){
            for(int j=i; j<n-1-i; j++){
                tempCell = gameTiles[i][j];
                gameTiles[i][j] = gameTiles[n-j-1][i];
                gameTiles[n-j-1][i] = gameTiles[n-i-1][n-j-1];
                gameTiles[n-i-1][n-j-1] = gameTiles[j][n-i-1];
                gameTiles[j][n-i-1] = tempCell;
            }
        }
    }

    //8.2
    public boolean canMove(){
        //проверяем, если есть пустые клетки, то можно сделать ход
        if(!getEmptyTiles().isEmpty()) return true;
        
        //проверяем смежные клетки, если равны, то можно сделать ход с объединением
        for(int i = 0; i<gameTiles.length; i++){
            for(int j = 1; j<gameTiles.length; j++){
                if(gameTiles[i][j].value == gameTiles[i][j-1].value) return true;
                if(gameTiles[j][i].value == gameTiles[j-1][i].value) return true;
            }
        }
        return false;
    }
    
    //11 - будет сохранять текущее игровое состояние и счет в стеки
    private void saveState(Tile[][] tiles){
        Tile[][] gameTilesForStack = new Tile[tiles.length][tiles[0].length];
        for(int i = 0; i<gameTilesForStack.length; i++){
            for(int j = 0; j<gameTilesForStack[0].length; j++){
                gameTilesForStack[i][j] = new Tile(tiles[i][j].getValue());
            }
        }
        previousStates.push(gameTilesForStack);
        previousScores.push(score);
        isSaveNeeded = false;
    }
    
    //11 - будет устанавливать текущее игровое состояние равным последнему находящемуся в стеках с помощью метода pop
    public void rollback(){
        if(!previousStates.isEmpty() && !previousScores.isEmpty()){
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }
    
    //13 - который будет вызывать один из методов движения случайным образом
    public void randomMove(){
        int n = ((int) (Math.random() * 100)) % 4;
        switch(n){
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
        }
    }
    
    //15.1 - будет возвращать true, в случае, если вес плиток в массиве gameTiles отличается от веса плиток в верхнем массиве стека previousStates.
    public boolean hasBoardChanged(){
        return (score != previousScores.peek());
    }
    
    //15.2 - возвращает объект типа MoveEfficiency описывающий эффективность переданного хода
    public MoveEfficiency getMoveEfficiency(Move move){
        
        rollback();
        return null;
    }
}
