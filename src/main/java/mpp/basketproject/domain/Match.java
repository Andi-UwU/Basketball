package mpp.basketproject.domain;

import java.time.LocalDateTime;

import static mpp.basketproject.utils.Constants.DATE_TIME_FORMATTER;

public class Match extends Entity<Integer> {

    private String team1;
    private String team2;
    private Stage stage;
    private Double price;
    private Integer seatsAvailable;
    private LocalDateTime dateTime;

    public Match(String team1, String team2, Stage stage, Double price, Integer seatsAvailable, LocalDateTime dateTime) {
        this.team1 = team1;
        this.team2 = team2;
        this.stage = stage;
        this.price = price;
        this.seatsAvailable = seatsAvailable;
        this.dateTime = dateTime;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle(){
        return team1 + " vs " + team2;
    }

    public String getDateFormatted(){
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public String getStageString(){
        return stage.toString();
    }

    @Override
    public String toString() {
        return "Match{" +
                team1 + " vs " +
                team2 + '\'' +
                ", stage=" + stage +
                ", price=" + price +
                ", seatsAvailable=" + seatsAvailable +
                ", dateTime=" + dateTime +
                '}';
    }
}
