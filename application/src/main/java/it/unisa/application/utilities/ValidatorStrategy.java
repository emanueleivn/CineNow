package it.unisa.application.utilities;

public interface ValidatorStrategy {
    boolean validate(String campo);

    default boolean containsInvalidCharacters(String campo) {
        String invalidCharactersPattern = "[<>\"%;()&]";
        return campo != null && campo.matches(".*" + invalidCharactersPattern + ".*");
    }
}

