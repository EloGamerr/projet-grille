package fr.tia.projet;

import java.util.Objects;

public class Cell {
    private final int id;
    private Character c;

    public Cell(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Character getC() {
        return c;
    }

    public void setC(Character c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Ligne : " + String.valueOf(id/5) + " Colonne : " + String.valueOf(id%5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell agent = (Cell) o;
        return id == agent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
