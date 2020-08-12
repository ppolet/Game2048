
package game2048;

//1 - будет следить за нажатием клавиш во время игры.

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter{
    private final int WINNING_TILE = 2048;  //9 - вес плитки при достижении которого игра будет считаться выигранной.
    private Model model;
    private View view;
    
    //9
    public Controller(Model model){
        this.model = model;
        this.view = new View(this);
    }
    
    //8.4
    public Tile[][] getGameTiles(){
        return model.getGameTiles();
    }
    
    //8.5
    public int getScore(){
        return model.score;
    }
    
    //10
    public View getView(){
        return view;
    }
    
    //9 - который позволит вернуть игровое поле в начальное состояние
    // Необходимо обнулить счет, установить флаги isGameWon и isGameLost у представления в false и вызывать метод resetGameTiles у модели.
    public void resetGame(){
        model.score = 0;
        view.isGameLost = false;
        view.isGameWon = false;
        model.resetGameTiles();
    }

    //9
    /*
    1) Если была нажата клавиша ESC - вызови метод resetGame.
    2) Если метод canMove модели возвращает false - установи флаг isGameLost в true.
    3) Если оба флага isGameLost и isGameWon равны false - обработай варианты движения:
    а) для клавиши KeyEvent.VK_LEFT вызови метод left у модели;
    б) для клавиши KeyEvent.VK_RIGHT вызови метод right у модели;
    в) для клавиши KeyEvent.VK_UP вызови метод up у модели;
    г) для клавиши KeyEvent.VK_DOWN вызови метод down у модели.
    4) Если поле maxTile у модели стало равно WINNING_TILE, установи флаг isGameWon в true.
    5) В самом конце, вызови метод repaint у view.    
    Для получения кода нажатой клавиши используй метод getKeyCode класса KeyEvent.
    */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) resetGame();
        
        if(model.canMove() == false) view.isGameLost = true;
        
        if(!view.isGameLost && !view.isGameWon){
            switch(e.getKeyCode()){
                case (KeyEvent.VK_LEFT):
                    model.left();
                    break;
                case (KeyEvent.VK_RIGHT):
                    model.right();
                    break;
                case (KeyEvent.VK_UP):
                    model.up();
                    break;
                case (KeyEvent.VK_DOWN):
                    model.down();
                    break;
                case (KeyEvent.VK_Z):
                    model.rollback();   //12 - отмена хода 
                    break;
                case (KeyEvent.VK_R):
                    model.randomMove();   //13 - случайный ход 
                    break;
            }
        }
        
        if(model.maxTile == WINNING_TILE) view.isGameWon = true;
        
        view.repaint();
    }
    
    
    
}
