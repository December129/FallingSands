/*
Shriya Prabhudev
Victor Luo
1/17/25
 */
import java.awt.*;
import java.util.*;
import java.util.Arrays;

public class SandLab
{
    public static void main(String[] args)
    {
        SandLab lab = new SandLab(120, 80);
        lab.run();
    }

    //add constants for particle types here
    //add constants for particle types here
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;
    public static final int FIRE = 4;
    public static final int DYNAMITE = 5;
    public static final int MAGMA = 6;
    public static final int CLOUD = 7;
    public static final int OIL = 8;
    public static final int ROCK = 9;

    //do not add any more fields
    private int[][] grid;
    private SandDisplay display;

    public SandLab(int numRows, int numCols)
    {
        String[] names;
        names = new String[9];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal"; //for the metal button
        names[SAND] = "Sand"; // for the sand button
        names[WATER] = "Water";// for the water button
        names[FIRE] = "Fire";
        names[DYNAMITE] = "Dynamite";
        names[MAGMA] = "Magma";
        names[CLOUD] = "Clouds";
        names[OIL] = "Oil";

        display = new SandDisplay("Falling Sand", numRows, numCols, names);
        grid = new int[numRows][numCols];
    }

    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool)
    {
        grid[row][col] = tool; // sets the value of each tool into the grid place that is clicked
    }

    //copies each element of grid into the display
    public void updateDisplay()
    {
        //changes the color of the grid depending on what tool/number is assigned to that square
        for(int row = 0; row < grid.length; row++) {
            for(int col = 0; col<grid[row].length; col++) {
                if(grid[row][col] == METAL) {
                    display.setColor(row, col, Color.GRAY);
                }
                else if(grid[row][col]==SAND) {
                    display.setColor(row, col, Color.YELLOW);
                }
                else if(grid[row][col]==WATER) {
                    display.setColor(row, col, Color.BLUE);
                }
                else if(grid[row][col]==FIRE) {
                    display.setColor(row, col, Color.RED);
                }
                else if(grid[row][col]==DYNAMITE) {
                    display.setColor(row, col, Color.PINK);
                }
                else if(grid[row][col]==MAGMA) {
                    display.setColor(row, col, new Color(130, 10, 0));
                }
                else if(grid[row][col]==ROCK) {
                    display.setColor(row, col, new Color(100, 75, 0));
                }
                else if(grid[row][col]==CLOUD) {
                    display.setColor(row, col, Color.WHITE);
                }
                else if(grid[row][col]==OIL){
                    display.setColor(row, col, Color.ORANGE);
                }
                else{
                    display.setColor(row, col, Color.BLACK);
                }
            }
        }
    }

    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step()
    {
        int rowLen = grid.length;
        int colLen = grid[0].length;
        int randomRow = (int)(Math.random()*rowLen);
        int randomCol = (int)(Math.random()*colLen);
        //for the assigning of squares to sand so that it drops down
        int row = grid.length;
        int col = grid[0].length;
        int i = (int) (row * Math.random());
        int j = (int) (col * Math.random());
        int[][] move = new int[][]{{0, 0, 1, -1, 0}, {1, -1, 0, 0, 0}};
        int type = grid[i][j];
        if (type == SAND) { //ADDED PYRAMID SAND IMPLEMENTATION
            try {
                //drops the sand to left and right to create the pyramid
                if (grid[i + 1][j] == EMPTY) {
                    grid[i + 1][j] = SAND;
                    grid[i][j] = EMPTY;
                    return;
                } else if (grid[i + 1][j] == WATER) {
                    grid[i + 1][j] = SAND;
                    grid[i][j] = WATER;
                    return;
                }
            } catch (Exception e) {;}

            try{
                if(grid[i+1][j+1] == EMPTY){
                    grid[i+1][j+1] = SAND;
                    grid[i][j] = EMPTY;
                    return;
                }
            } catch (Exception e){;}

            try{
                if(grid[i+1][j-1] == EMPTY){
                    grid[i+1][j-1] = SAND;
                    grid[i][j] = EMPTY;
                    return;
                }
            } catch (Exception e){;}
        }
        //for the assigning of squares to water so that it flows
        if(grid[randomRow][randomCol] == WATER) {
            //chooses the random direction that the water will flow
            int random = (int)(Math.random()*3)+1;
            //for the water to go left
            if(random == 1 && randomCol != 0 && (grid[randomRow][randomCol-1]==EMPTY || grid[randomRow][randomCol-1]==MAGMA)){
                if(grid[randomRow][randomCol-1] == MAGMA) {
                    grid[randomRow][randomCol-1] = ROCK;
                    grid[randomRow][randomCol] = ROCK;
                }
                else{
                    grid[randomRow][randomCol-1] = WATER;
                    grid[randomRow][randomCol] = EMPTY;
                }
            }
            //for the water to go down
            else if(random == 2 && randomRow != rowLen-1 && (grid[randomRow+1][randomCol]==EMPTY || grid[randomRow+1][randomCol]==MAGMA)){
                if(grid[randomRow+1][randomCol]==MAGMA) {
                    grid[randomRow+1][randomCol] = ROCK;
                    grid[randomRow][randomCol] = ROCK;
                }
                else{
                    grid[randomRow+1][randomCol] = WATER;
                    grid[randomRow][randomCol] = EMPTY;
                }
            }
            //for the water to go right
            else if(random == 3 && randomCol != colLen-1 && (grid[randomRow][randomCol+1]==EMPTY || grid[randomRow][randomCol+1]==MAGMA)){
                if(grid[randomRow][randomCol+1]==MAGMA) {
                    grid[randomRow][randomCol+1] = ROCK;
                    grid[randomRow][randomCol] = ROCK;
                }
                else{
                    grid[randomRow][randomCol+1] = WATER;
                    grid[randomRow][randomCol] = EMPTY;
                }
            }
        }
        else if (type == FIRE) {  //FIRE IMPLEMENTATION

            int dcnt = 0;
            for(int m = 0;m<4;m++){
                for(int n = 0;n<4;n++){
                    int newI = i+move[0][m];
                    int newJ = j+move[0][n];
                    try{
                        //if the fire is touching dynamite it switches places
                        if(grid[newI][newJ] == DYNAMITE){
                            grid[newI][newJ] = FIRE;
                            dcnt++;
                        }
                    } catch(Exception e){;}
                }
            }
            if(dcnt>2){
                //places around the dynamite area creates this spark effect
                for(int m = 0;m<4;m++){
                    for(int n = 0;n<4;n++){
                        for(int d = 1;d<(int)(dcnt*1.5);d++){
                            int newI = i+d*move[0][m];
                            int newJ = j+d*move[0][n];
                            try{
                                grid[newI][newJ] = FIRE;
                            } catch(Exception e){;}
                        }
                    }
                }
            }
            try{
                if(grid[i+1][j] == OIL){
                    grid[i-1][j] = FIRE;
                    return;
                }
            } catch(Exception e){;}

            int newI = i+move[0][Math.min((int)(4*Math.random()), 3)];
            int newJ = j+move[0][Math.min((int)(4*Math.random()), 3)];
            try{
                switch(grid[newI][newJ]){
                    case METAL: break; //ADD SIMILAR CASE MAGMA,
                    case ROCK: break;
                    case OIL: break;
                    case MAGMA: if(newJ>j){
                        try{
                            if(grid[i][j+1]==EMPTY){grid[i][j+1] = FIRE;}
                        } catch(Exception e){;}
                        try{
                            if(grid[i][j-1]==EMPTY){grid[i][j-1] = FIRE;}
                        } catch(Exception e){;}
                    } break;
                    case WATER: grid[i][j] = CLOUD; break; //REPLACE W/ STEAM when fire touches water, creates cloud
                    case EMPTY:
                        if(Math.random()<0.2){
                            grid[newI][newJ] = FIRE;
                            grid[i][j] = EMPTY;
                        } else if(Math.random()<0.2){
                            grid[i][j] = EMPTY;
                        }
                        break;
                    case DYNAMITE:
                        grid[newI][newJ] = FIRE;
                        break;
                    case SAND:
                        if(Math.random() < 0.1){ grid[newI][newJ] = FIRE;} break;
                    default: if(Math.random() < 0.5){ grid[newI][newJ] = FIRE;} break;
                }
            } catch(Exception e){
                ;
            }
        } else if (type == DYNAMITE){ //DYNAMITE IMPLEMENTATION
            int newI = i+move[0][Math.min((int)(6*Math.random()), 4)];
            int newJ = j+move[0][Math.min((int)(6*Math.random()), 4)];
            try{
                switch(grid[newI][newJ]){
                    case FIRE: grid[i][j] = FIRE; break;
                    default: break;
                }
            } catch(Exception e){
                ;
            }
        }
        if(grid[randomRow][randomCol] == MAGMA) {
            //chooses the random direction that the magma will flow
            int random = (int)(Math.random()*3)+1;
            //creates rock when magma touches water
            //for the magma to go left
            if(random == 1 && randomCol != 0 && (grid[randomRow][randomCol-1]==EMPTY || grid[randomRow][randomCol-1]==WATER)){
                if(grid[randomRow][randomCol-1] == WATER) {
                    grid[randomRow][randomCol-1] = ROCK;
                    grid[randomRow][randomCol] = ROCK;
                }
                else{
                    grid[randomRow][randomCol-1] = MAGMA;
                    grid[randomRow][randomCol] = EMPTY;
                }
            }
            //for the magma to go down
            else if(random == 2 && randomRow != rowLen-1 && (grid[randomRow+1][randomCol]==EMPTY || grid[randomRow+1][randomCol]==WATER)){
                if(grid[randomRow+1][randomCol]==WATER) {
                    grid[randomRow+1][randomCol] = ROCK;
                    grid[randomRow][randomCol] = ROCK;
                }
                else{
                    grid[randomRow+1][randomCol] = MAGMA;
                    grid[randomRow][randomCol] = EMPTY;
                }
            }
            //for the magma to go right
            else if(random == 3 && randomCol != colLen-1 && (grid[randomRow][randomCol+1]==EMPTY || grid[randomRow][randomCol+1]==WATER)){
                if(grid[randomRow][randomCol+1]==WATER) {
                    grid[randomRow][randomCol+1] = ROCK;
                    grid[randomRow][randomCol] = ROCK;
                }
                else{
                    grid[randomRow][randomCol+1] = MAGMA;
                    grid[randomRow][randomCol] = EMPTY;
                }
            }
        }
        //keeps the clouds in the top third
        int topThird = rowLen/3;
        if(grid[randomRow][randomCol] == CLOUD && randomRow > topThird-1) {
            int random = (int) (Math.random() * 3) + 1;
            //for the clouds to go left
            if (random == 1 && randomCol != 0 && (grid[randomRow][randomCol - 1] == EMPTY || grid[randomRow][randomCol - 1] == WATER)) {
                int temp = grid[randomRow][randomCol - 1];
                grid[randomRow][randomCol - 1] = CLOUD;
                grid[randomRow][randomCol] = temp;
            }
            //for the clouds to go up and group together
            else if (random == 2 && grid[randomRow - 1][randomCol] == EMPTY || grid[randomRow - 1][randomCol] == WATER) {
                int temp = grid[randomRow - 1][randomCol];
                grid[randomRow - 1][randomCol] = CLOUD;
                grid[randomRow][randomCol] = temp;
            }
            //for the clouds to go right
            else if (random == 3 && randomCol != colLen - 1 && (grid[randomRow][randomCol + 1] == EMPTY || grid[randomRow][randomCol + 1] == WATER)) {
                int temp = grid[randomRow][randomCol+1];
                grid[randomRow][randomCol + 1] = CLOUD;
                grid[randomRow][randomCol] = temp;
            }

        }
        else if(grid[randomRow][randomCol] == CLOUD && randomRow < topThird) {
            int random = (int)(Math.random()*4)+1;
            if(random == 1 && randomCol != 0 && grid[randomRow+1][randomCol-1]==EMPTY){
                grid[randomRow+1][randomCol-1] = CLOUD;
                grid[randomRow][randomCol] = EMPTY;
            }
            else if(random == 2 && grid[randomRow+1][randomCol]==EMPTY){
                grid[randomRow+1][randomCol] = CLOUD;
                grid[randomRow][randomCol] = EMPTY;
            }
            //for the water to go right
            else if(random == 3 && randomCol != colLen-1 && grid[randomRow+1][randomCol+1]==EMPTY){
                grid[randomRow+1][randomCol+1] = CLOUD;
                grid[randomRow][randomCol] = EMPTY;
            }
            else if(random == 4 && randomRow!=0 && grid[randomRow-1][randomCol]==EMPTY && grid[randomRow+1][randomCol]==CLOUD &&
                    (randomCol>=colLen-2 || grid[randomRow][randomCol+2]==CLOUD) && (randomCol<=1 || grid[randomRow][randomCol-2]==CLOUD)){
                grid[randomRow-1][randomCol] = CLOUD;
                grid[randomRow+1][randomCol] = EMPTY;
            }
        }
        //drops and rains water once at a time at a slow speed
        if(grid[randomRow][randomCol] == CLOUD && randomRow == topThird) {
            if(grid[randomRow+1][randomCol]==EMPTY){
                if(Math.random()<0.0005){grid[randomRow+1][randomCol] = WATER; if(Math.random()<0.5){grid[randomRow][randomCol] = EMPTY;}}
            }
        }
        if(grid[randomRow][randomCol] == OIL) {
            //chooses the random direction that the oil will flow
            int random = (int)(Math.random()*3)+1;
            //for the oil to go left
            if(random == 1 && randomCol != 0 && (grid[randomRow][randomCol-1]==0)){
                grid[randomRow][randomCol-1] = OIL;
                grid[randomRow][randomCol] = 0;
            }
            //for the oil to go down
            else if(random == 2 && randomRow != rowLen-1 && (grid[randomRow+1][randomCol]==0)){
                grid[randomRow+1][randomCol] = OIL;
                grid[randomRow][randomCol] = 0;
            }
            //for the oil to go right
            else if(random == 3 && randomCol != colLen-1 && (grid[randomRow][randomCol+1]==0)){
                grid[randomRow][randomCol+1] = OIL;
                grid[randomRow][randomCol] = 0;
            }
        }
    }

    //do not modify
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}