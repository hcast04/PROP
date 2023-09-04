package dominio.auxiliarclasses;

/**
 * Representa una clase auxiliar que es un par de ints y sus funciones (creadora, obtener valor 1 y 2, introducir valor 1 y 2 y comparar a otro pair
 */
public class Pair  {
    private int x;
    private int y;

    public Pair(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Pair(){}

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isEqual(Pair p) {
        return p.getX() == this.x && p.getY() == this.y;
    }
}


