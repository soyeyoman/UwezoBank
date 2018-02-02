package Main.Models;

/**
 * Created by steve on 7/10/2017.
 */
public enum TypeOption {

    W("W"), S("S") , D("D");

    public String type;

    TypeOption(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
