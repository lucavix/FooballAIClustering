package biz.opengate.calcetto.vix;
public class Player {
    String name;
    String role;
    Double score;

    public Player(String name, String role, Double score) {
        this.name = name;
        this.role = role;
        this.score = score;
    }

    public String toString() { 
        return name;
    }
}