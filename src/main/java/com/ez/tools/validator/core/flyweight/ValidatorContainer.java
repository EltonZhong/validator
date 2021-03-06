package com.ez.tools.validator.core.flyweight;

import com.ez.tools.validator.annotations.*;
import com.ez.tools.validator.core.flyweight.impl.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ValidatorContainer {

    private Map<Class<? extends Annotation>, BasicValidator> map;
    private static ValidatorContainer instance = new ValidatorContainer();

    public static ValidatorContainer getInstance() {
        return instance;
    }

    public void validate(Object value, Annotation an, String message) {
        getByAnnotation(an)
                .validate(value)
                .whenFail(message)
                .with(an);
    }

    private ValidatorContainer() {
        constructValidatorsMap();
    }

    private void constructValidatorsMap() {
        map = new HashMap<>();
        map.put(VInt.class, new IntegerValidator<>());
        map.put(VString.class, new StringValidator());
        map.put(VNotNull.class, new NotNullValidator());
        map.put(VRecursive.class, new RecursiveValidator());
        map.put(VRule.class, new RuleValidator());
    }

    @SuppressWarnings("unchecked")
    private <V, T extends Annotation> BasicValidator<T, V> getByAnnotation(T annotation) {
        if (map.containsKey(annotation.annotationType())) {
            return map.get(annotation.annotationType());
        }
        throw new UnsupportedOperationException(
                String.format("Can not validate with annotation type %s", annotation.getClass()));
    }
}
