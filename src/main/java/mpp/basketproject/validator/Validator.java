package mpp.basketproject.validator;

public interface Validator <Type> {
    void validate(Type entity) throws ValidationException;
}
