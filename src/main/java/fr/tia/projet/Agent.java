package fr.tia.projet;

import java.util.Objects;

public class Agent {
    private final Character c;

    public Agent(Character c) {
        this.c = c;
    }

    public Character getC() {
        return c;
    }

    @Override
    public String toString() {
        return c.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(c, agent.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}

