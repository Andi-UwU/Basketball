package mpp.basketproject.validator;

import mpp.basketproject.domain.Match;

import java.time.LocalDateTime;

public class MatchValidator implements Validator<Match>{
    @Override
    public void validate(Match entity) throws ValidationException {
        String errors = "";

        if (entity.getTeam1().length() < 2 || entity.getTeam1().length() < 2)
            errors += "The team name must be at least 2 characters long!\n";
        if (entity.getStage() == null)
            errors += "The stage is not set!\n";
        if (entity.getPrice() <= 0)
            errors += "Price must be greater than 0!\n";
        if (entity.getSeatsAvailable() < 0)
            errors += "The Seats Available must be greater or equal to 0!\n";
        if (entity.getDateTime().isBefore(LocalDateTime.now()))
            errors += "The date time must describe a future date!\n";
        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
