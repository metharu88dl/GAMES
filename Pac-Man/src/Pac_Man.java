import java.util.HashSet;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Pac_Man extends JPanel implements ActionListener, KeyListener {
    class Block{
        int x,y,height,width;
        Image image;

        int startX,startY;
        char direction='U'; // U=up, D=down, L=left, R=right
        int velocityX=0,velocityY=0;

        Block(Image image,int x,int y,int width,int height){
            this.image=image;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
            this.startX=x;
            this.startY=y;
        }
        void updateDirection(char direction){
            char prevDirection=this.direction;
            this.direction=direction;
            updateVelocity();
            //this added code-snippet is used to ensure if-only-opening-->move pacman..
            this.x+=this.velocityX;
            this.y+=this.velocityY;
            for(Block wall:walls){
                if(collision(this,wall)){
                    this.x-=this.velocityX;
                    this.y-=this.velocityY;
                    this.direction=prevDirection;
                    updateVelocity();
                }
            }
        }
        void updateVelocity(){
            if(this.direction=='U'){
                this.velocityX=0;
                this.velocityY= -tileSize/4;
            }
            else if(this.direction=='D'){
                this.velocityX=0;
                this.velocityY= tileSize/4;
            }
            else if(this.direction=='L'){
                this.velocityX= -tileSize/4;
                this.velocityY=0;
            }
            else if(this.direction=='R'){
                this.velocityX= tileSize/4;
                this.velocityY=0;
            }
        }
        void reset(){
            this.x=this.startX;
            this.y=this.startY;
        }
    }
    int rowCount = 21;
    int colCount = 19;
    int tileSize = 32;
    int boardwidth = tileSize * colCount;   
    int boardheight = tileSize * rowCount;

    HashSet<Block> walls;
    HashSet<Block> food;
    HashSet<Block> ghosts;
    Block pacman;
    Timer gameLoop;

    char[] directions={'U','D','L','R'}; //direction for ghosts..
    Random random=new Random();
    int score=0;
    int lives=3;
    boolean gameOver=false;

    // Using 2-D array to store the map of the Base-layout...
        private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "X       bpo       X",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };
    
    Image wall;
    Image blueGhostImage;
    Image powerFoodImage;
    Image cherryImage;  
    Image RedGhostImage;
    Image OrangeGhostImage;
    Image pinkGhostImage;
    Image pacmanLeftImage;
    Image pacmanRightImage;
    Image pacmanUpImage;
    Image pacmanDownImage;
    Image cherry2Image;
    Image scaredGhostImage;

    Pac_Man(){
        setPreferredSize(new Dimension(boardwidth,boardheight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //import images, and load them..
        wall=new ImageIcon(getClass().getResource("wall.png")).getImage();
        blueGhostImage=new ImageIcon(getClass().getResource("blueGhost.png")).getImage();
        RedGhostImage=new ImageIcon(getClass().getResource("RedGhost.png")).getImage();
        OrangeGhostImage=new ImageIcon(getClass().getResource("OrangeGhost.png")).getImage();
        pinkGhostImage=new ImageIcon(getClass().getResource("pinkGhost.png")).getImage();

        pacmanLeftImage=new ImageIcon(getClass().getResource("pacmanLeft.png")).getImage();
        pacmanRightImage=new ImageIcon(getClass().getResource("pacmanRight.png")).getImage();
        pacmanUpImage=new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        pacmanDownImage=new ImageIcon(getClass().getResource("pacmanDown.png")).getImage();
        cherryImage=new ImageIcon(getClass().getResource("cherry.png")).getImage();
        cherry2Image=new ImageIcon(getClass().getResource("cherry2.png")).getImage();
        powerFoodImage=new ImageIcon(getClass().getResource("powerFood.png")).getImage();
        scaredGhostImage=new ImageIcon(getClass().getResource("scaredGhost.png")).getImage();
        
        loadMap();
        for(Block ghost:ghosts){
            char dir=directions[random.nextInt(directions.length)];
            ghost.updateDirection(dir);
        }

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }
    public void loadMap(){
        walls=new HashSet<>();
        food=new HashSet<>();
        ghosts=new HashSet<>();
        for(int row=0;row<rowCount;row++){
            for(int col=0;col<colCount;col++){
                char tile=tileMap[row].charAt(col);
                int x=col*tileSize;
                int y=row*tileSize;
                if(tile=='X'){ // Wall
                    Block wallBlock=new Block(wall,x,y,tileSize,tileSize);
                    walls.add(wallBlock);
                }
                else if(tile=='o'){ // Orange Ghost
                    Block orangeGhostBlock=new Block(OrangeGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(orangeGhostBlock);
                }
                else if(tile=='r'){ // Red Ghost
                    Block redGhostBlock=new Block(RedGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(redGhostBlock);
                }
                else if(tile=='b'){ // Blue Ghost
                    Block blueGhostBlock=new Block(blueGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(blueGhostBlock);
                }
                else if(tile=='p'){ // Pink Ghost
                    Block pinkGhostBlock=new Block(pinkGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(pinkGhostBlock);
                }
                else if(tile==' '){ //Food
                    Block foodBlock=new Block(null, x+14, y+14, 4, 4);
                    food.add(foodBlock);
                }
                else if(tile=='P'){ // Pac man
                    pacman=new Block(pacmanRightImage,x,y,tileSize,tileSize);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, this);
        for(Block ghost:ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, this);
        }
        for(Block wall:walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, this);
        }
        for(Block foodBlock:food){
            g.setColor(Color.WHITE);
            g.fillOval(foodBlock.x, foodBlock.y, foodBlock.width, foodBlock.height);
        }
        //score
        g.setFont(new Font("Arial", Font.BOLD, 18));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else{
            g.drawString("x"+String.valueOf(lives)+" Score:  "+String.valueOf(score),tileSize/2,tileSize/2);
        }
    }

    public void move(){
        pacman.x+=pacman.velocityX;
        pacman.y+=pacman.velocityY;
        //check wall collisions..
        for(Block wall:walls){
            if(collision(pacman,wall)){
                //move back
                pacman.x-=pacman.velocityX;
                pacman.y-=pacman.velocityY;
                break;
            }
        }
        //check ghost collisions..
        for(Block ghost:ghosts){
            if(collision(ghost,pacman)){
                lives-=1;
                if(lives==0){
                    gameOver=true;
                    return;
                }
                resetPositions();
            }
            if(ghost.y==tileSize*9 && ghost.direction !='U' && ghost.direction!='D'){
                char ran=directions[random.nextInt(2)]; // randomly chose U or D.
                ghost.updateDirection(ran);
            }
            ghost.x+=ghost.velocityX;
            ghost.y+=ghost.velocityY;
            for(Block wall : walls){
                if(collision(ghost, wall)){
                    //move back
                    ghost.x-=ghost.velocityX;
                    ghost.y-=ghost.velocityY;
                    //change direction randomly
                    char dir=directions[random.nextInt(directions.length)];
                    ghost.updateDirection(dir);
                    break;
                }
            }
            //if opening and boundary of the Applet-panel, teleport to other side of panel..
            if(ghost.x<0){
                ghost.x=boardwidth-ghost.width;
            }
            else if(ghost.x>boardwidth-ghost.width){
                ghost.x=0;
            }
            //check food collisions
            Block foodEaten=null;
            for(Block foo : food){
                if(collision(pacman,foo)){
                    foodEaten=foo;
                    score+=10; 
                }
            } 
            food.remove(foodEaten);
            if (food.isEmpty()) {
                loadMap();
                resetPositions();
            }
        }
    }

    public boolean collision(Block a, Block b){
        return a.x<b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }
    public void resetPositions(){
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;
        for(Block ghost:ghosts){
            ghost.reset();
            char dir=directions[random.nextInt(directions.length)];
            ghost.updateDirection(dir);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        //if gameOver and ENTER key pressed reset-game..
        if(gameOver && e.getKeyCode()==KeyEvent.VK_ENTER){
            loadMap();
            resetPositions();
            lives=3;
            score=0;
            gameOver=false;
            gameLoop.start();
        }
        System.out.println("Key Released: " + e.getKeyCode());
        if(e.getKeyCode()==KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }

        //updating the pacman images....
        if(pacman.direction=='U'){
            pacman.image=pacmanUpImage;
        }
        else if(pacman.direction=='D'){
            pacman.image=pacmanDownImage;
        }
        else if(pacman.direction=='L'){
            pacman.image=pacmanLeftImage;
        }
        else if(pacman.direction=='R'){
            pacman.image=pacmanRightImage;
        }
    }
    //Approximation
            int epsilon=5;

}
