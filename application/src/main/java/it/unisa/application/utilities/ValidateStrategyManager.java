package it.unisa.application.utilities;

import java.util.HashMap;
import java.util.Map;

public class ValidateStrategyManager {
    private Map<String, ValidatorStrategy> validators;

    public ValidateStrategyManager() {
        this.validators = new HashMap<>();
    }

    public void addValidator(String campo, ValidatorStrategy validator) {
        validators.put(campo, validator);
    }

    public boolean validate(Map<String, String> inputs) {
        for (Map.Entry<String, ValidatorStrategy> entry : validators.entrySet()) {
            String campo = entry.getKey();
            ValidatorStrategy validator = entry.getValue();
            String input = inputs.get(campo);
            if (input == null || !validator.validate(input)) {
                return false;
            }
        }
        return true;
    }
}

