import java.lang.Math;

public class Coordinates {
    public int x;
    public int y;

    public Coordinates(){
        x = 0;
        y = 0;
    }
    public Coordinates(int _x, int _y){
        x = _x;
        y = _y;
    }
    public boolean equals(Coordinates _c){
        if(this.x == _c.x && this.y == _c.y){
            return true;
        }
        return false;
    }
    public int manhattan(Coordinates that){
        return Math.abs(this.x - that.x) + Math.abs(this.y - that.y);
    }
    public double euclidian(Coordinates that){
        return Math.sqrt(Math.pow(((double)(this.x - that.x)), 2) + Math.pow(((double)(this.y - that.y)), 2));
    }

}