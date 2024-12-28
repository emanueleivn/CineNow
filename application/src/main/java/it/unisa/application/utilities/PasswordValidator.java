package it.unisa.application.utilities;

public class PasswordValidator implements ValidatorStrategy {
    @Override
    public boolean validate(String campo) {
        return campo != null
                && campo.length() >= 8
                && campo.matches(".*[a-z].*")
                && campo.matches(".*[A-Z].*")
                && campo.matches(".*[!@#$%^&*()_+\\-={}\\[\\]:;,.<>?/].*")
                && !containsInvalidCharacters(campo);
    }
}


