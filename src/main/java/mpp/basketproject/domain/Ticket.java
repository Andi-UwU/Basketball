package mpp.basketproject.domain;

public class Ticket extends Entity<Integer>{

    private User seller;
    private String clientName;
    private Match match;
    private Integer numberOfSeats;

    public Ticket(User seller, String clientName, Match match, Integer numberOfSeats) {
        this.seller = seller;
        this.clientName = clientName;
        this.match = match;
        this.numberOfSeats = numberOfSeats;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "seller=" + seller +
                ", clientName='" + clientName + '\'' +
                ", match=" + match +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}
