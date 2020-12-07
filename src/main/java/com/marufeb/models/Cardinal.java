package com.marufeb.models;

import java.io.Serializable;

/**
 * Enum representing cardinal points. Allows you to link the image path also.
 */
public enum Cardinal implements Serializable {
    N(0), S(1), E(2), W(3);

    int I;

    Cardinal(int i) {
        I = i;
    }

    /**
     * @return The opposite cardinal
     */
    public Cardinal opposite() {
        switch (this) {
            case N: return S;
            case E: return W;
            case S: return N;
            case W: return E;
        }
        return null;
    }

    /**
     * @param left Chose between E (false) or W (true)
     * @return The final {@link Cardinal}
     */
    public Cardinal side(boolean left) {
        if (left) {
            return W;
        } else {
            return E;
        }
    }

    /**
     * Rotates the {@link Cardinal} to a particular angle
     * @param angle The angle of the rotation (from 0 to +infinity)
     * @return The final {@link Cardinal}
     */
    public Cardinal rotate(int angle) {
        angle = (angle/90) % 4;
        switch (angle) {
            case 0 : return this;
            case 1 : return side(false);
            case 2 : return opposite();
            case 3 : return side(true);
            default: return null;
        }
    }

}
