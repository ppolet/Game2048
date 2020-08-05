
package game2048;

//1.2 - будет содержать игровую логику и хранить игровое поле.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private final int FIELD_WIDTH = 4;   //3 - определяющая ширину игрового поля
    private Tile[][] gameTiles;   //3.2
    int score;      // 5 - текущий счет 
    int maxTile;    // 5 - максимальный вес плитки на игровом поле
    
    public Model(){
        resetGameTiles();
        addTile();
        addTile();
    }
    
    protected void resetGameTiles(){
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for(int i = 0; i<gameTiles.length; i++)
            for(int j = 0; j<gameTiles.length; j++){
                gameTiles[i][j] = new Tile();       //3.3
            }
    }
    
    //4 - который будет смотреть какие плитки пустуют и менять вес одной из них, выбранной случайным образом, на 2 или 4 (на 9 двоек должна приходиться 1 четверка)
    private void addTile(){
        List<Tile> emptyTiles = getEmptyTiles();
        if(!emptyTiles.isEmpty() && emptyTiles.size()>0){
            int index = (int)(Math.random() * emptyTiles.size());
            int newTileValue = (Math.random() < 0.9) ? 2 : 4;
            emptyTiles.set(index, new Tile(newTileValue));
        }
    }
    
    private List<Tile> getEmptyTiles(){
        List<Tile> resultList = new ArrayList<>();
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
        Tile[] tempTiles = new Tile[tiles.length];
        int n1 = 0;
        int n2 = tiles.length-1;
        for(int i=0; i<tiles.length; i++){
            if(!tiles[i].isEmpty()){
                if(n1 != i) compress = true;        //было сжатие
                tempTiles[n1] = tiles[i];   //добавляем в массив в начало (не 0)
                n1++;
            } else {
                tempTiles[n2] = tiles[i];   //добавляем в массив в конец (0)
                n2--;
            }
        }
        tiles = tempTiles;
        return compress;
    }
    
    //5 - Слияние плиток одного номинала, т.е. ряд {4, 4, 2, 0} становится рядом {8, 2, 0, 0}. Обрати внимание, что ряд {4, 4, 4, 4} превратится в {8, 8, 0, 0}, а {4, 4, 4, 0} в {8, 4, 0, 0}.
    //сжатие будет всегда выполнено перед слиянием, таким образом в метод mergeTiles всегда передается массив плиток без пустых в середине.
    // движение влево
    private boolean mergeTiles(Tile[] tiles){
        boolean merge = false;
        for(int i=1; i<tiles.length; i++){
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
    
    //6.3
    public void left(){
        for(int i=0; i<gameTiles.length; i++){
            if (compressTiles(gameTiles[i])){
                addTile();          //добавляем плитку, т.к. было сжатие
            }
            if (mergeTiles(gameTiles[i])){
                addTile();          //добавляем плитку, т.к. было слияние
            }
        }
    }
    
    public void up(){
        
    }
    
    public void down(){
        
    }
    
    public void right(){
        
    }
    
}
