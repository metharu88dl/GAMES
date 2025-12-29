import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int rowCount = 21;
        int colCount = 19;   
        int tileSize = 32;
        int boardwidth = tileSize * colCount;
        int boardheight = tileSize * rowCount;
        JFrame window=new JFrame("PAC-MAN");
        window.setResizable(false);
        window.setSize(boardwidth,boardheight);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Pac_Man game=new Pac_Man();
        window.add(game);
        window.pack();
        game.requestFocus();
        window.setVisible(true);
    }
}
