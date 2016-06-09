package game;




public class Figure {
    public int posX;
    public int posY;
    public int[][] matrix;

    public Figure(int posX, int posY, int[][] matrix) {
        this.posX = posX;
        this.posY = posY;
        this.matrix = matrix;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void rotate(){
        if (matrix[0][0] == 1 && matrix[0][1] == 1 && matrix[1][0] ==1 && matrix[1][1] == 1) { return; }
        int[][] matrix2 = new int[3][3];
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if(i == 0 && j == 0){ matrix2[0][2] = matrix[i][j]; }
                if(i == 0 && j == 1){ matrix2[1][2] = matrix[i][j]; }
                if(i == 0 && j == 2){ matrix2[2][2] = matrix[i][j]; }
                if(i == 1 && j == 0){ matrix2[0][1] = matrix[i][j]; }
                if(i == 1 && j == 1){ matrix2[1][1] = matrix[i][j]; }
                if(i == 1 && j == 2){ matrix2[2][1] = matrix[i][j]; }
                if(i == 2 && j == 0){ matrix2[0][0] = matrix[i][j]; }
                if(i == 2 && j == 1){ matrix2[1][0] = matrix[i][j]; }
                if(i == 2 && j == 2){ matrix2[2][0] = matrix[i][j]; }
            }
        }
        matrix = matrix2;
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Integer value = Tetris.getValue(posX + j, posY + i);
                if (value == null && posX < 2){
                    right();
                }
                if (value == null && posX > Tetris.WIDTH - 3){
                    left();
                }
            }
        }

    }

    public void down(){
        posY++;
    }

    public void up(){
        posY--;
    }

    public void left(){
        posX--;
        if (!isCurrentPositionAvailable())
            posX++;
    }

    public void right(){
        posX++;
        if(!isCurrentPositionAvailable())
            posX--;
    }

    public void downMaximum(){
        while (isCurrentPositionAvailable()){
            posY++;
        }
        posY--;
    }

    public boolean isCurrentPositionAvailable(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (matrix[i][j] == 1){
                    if (posY + i >= Tetris.HEIGHT)
                        return false;

                    Integer value = Tetris.getValue(posX + j, posY + i);
                    if (value == null || value == 1){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void landed(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (matrix[i][j] == 1){
                    Tetris.setValue(posX + j, posY + i, 1);
                }
            }
        }
    }
}
