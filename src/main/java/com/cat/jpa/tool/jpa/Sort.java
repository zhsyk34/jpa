package com.cat.jpa.tool.jpa;

public final class Sort {

    private final static String DEFAULT_COLUMN = "id";

    private final String field;
    private final Rule rule;

    private Sort(String field, Rule rule) {
        this.field = field;
        this.rule = rule;
    }

    public static Sort of(String field, Rule rule) {
        if (field == null || field.isEmpty()) {
            field = DEFAULT_COLUMN;
        }
        if (rule == null) {
            rule = Rule.ASC;
        }
        return new Sort(field, rule);
    }

    public String field() {
        return field;
    }

    public Rule rule() {
        return rule;
    }

}


