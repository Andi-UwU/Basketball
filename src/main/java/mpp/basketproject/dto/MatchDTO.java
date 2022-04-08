package mpp.basketproject.dto;

import mpp.basketproject.domain.Match;

public class MatchDTO {

    private Integer id;
    private String title;
    private String stage;
    private Double price;
    private String seatsAvailable;
    private String dateTime;
    private String status;

    public MatchDTO(Match match){
        this.id = match.getId();
        this.title = match.getTitle();
        this.stage = match.getStage().toString();
        this.price = match.getPrice();
        this.seatsAvailable = match.getSeatsAvailable().toString();
        this.dateTime = match.getDateFormatted();
        if (match.getSeatsAvailable() == 0) {
            status = "SOLD OUT";
        }
        else {
            status = "Available";
        }
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStage() {
        return stage;
    }

    public Double getPrice() {
        return price;
    }

    public String getSeatsAvailable() {
        return seatsAvailable;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getStatus(){
        return status;
    }
}
