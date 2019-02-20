package com.hearatale.sightwords.ui.custom_view;

public enum  FixedAttribute {
    HEIGHT(0), WIDTH(1);
    final int id;

    FixedAttribute(int id) {
        this.id = id;
    }

    public static FixedAttribute fromId(int id) {
        for (FixedAttribute f : values()) {
            if (f.id == id) return f;
        }
        throw new IllegalArgumentException();
    }
}
