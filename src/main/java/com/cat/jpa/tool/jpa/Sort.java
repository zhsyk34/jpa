package com.cat.jpa.tool.jpa;

import lombok.Getter;

@Getter
public class Sort {

    private final String column;
    private final Rule rule;

    private Sort(String column, Rule rule) {
        this.column = column;
        this.rule = rule;
    }

    public static Sort of(String column, Rule rule) {
        if (column == null || column.isEmpty()) {
            return null;
        }
        if (rule == null) {
            rule = Rule.ASC;
        }
        return new Sort(column, rule);
    }

}


