package it.unisa.application.utilities;

public class EmailValidator implements ValidatorStrategy {
    @Override
    public boolean validate(String campo) {
        if (campo == null) {
            return false;
        }
        return campo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") && !containsInvalidCharacters(campo);
    }

}


