package mpp.basketproject.validator;

import mpp.basketproject.domain.User;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User entity) throws ValidationException {

        String errors = "";

        if (entity.getUsername().length() < 3)
            errors += "The username must be at least 3 characters long!\n";
        if (!entity.getUsername().matches("[a-zA-Z0-9]+"))
            errors += "The username must contain only letters and numbers!\n";
        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
