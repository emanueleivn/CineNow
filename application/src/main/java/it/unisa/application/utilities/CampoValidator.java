package it.unisa.application.utilities;

public class CampoValidator implements ValidatorStrategy {
    @Override
    public boolean validate(String campo) {
        if (campo == null || campo.trim().isEmpty()) {
            return false;
        }
        return !containsInvalidCharacters(campo);
    }
}


