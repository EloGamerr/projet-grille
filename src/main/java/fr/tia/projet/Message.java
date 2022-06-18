package fr.tia.projet;

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
}
