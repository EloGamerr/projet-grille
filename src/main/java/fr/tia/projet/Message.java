package fr.tia.projet;

import java.util.Objects;

public class Message {
    private final Agent sender;
    private final Cell from;
    private final Cell to;

    public Message(Agent sender, Cell from, Cell to) {
        this.sender = sender;
        this.from = from;
        this.to = to;
    }

    public Agent getSender() {
        return sender;
    }

    public Cell getFrom() {
        return from;
    }

    public Cell getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sender, message.sender) && Objects.equals(from, message.from) && Objects.equals(to, message.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, from, to);
    }
}
