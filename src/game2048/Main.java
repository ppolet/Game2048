
package game2048;

//1.4 - будет содержать только метод main и служить точкой входа в наше приложение.

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {
    
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        JFrame game = new JFrame();
        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(460, 550);
        game.setResizable(false);

        game.add(controller.getView());

        game.setLocationRelativeTo(null);
        game.setVisible(true);        
    }
    
}
