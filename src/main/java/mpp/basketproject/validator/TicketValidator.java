package mpp.basketproject.validator;

import mpp.basketproject.domain.Ticket;

public class TicketValidator implements Validator<Ticket>{
    @Override
    public void validate(Ticket entity) throws ValidationException {
        String errors = "";

        if (entity.getClientName().length() < 3)
            errors += "The client name must be at least 3 characters long!\n";
        if (entity.getNumberOfSeats() <= 0)
            errors += "The number of seats must be greater than 0!\n";
        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
