package com.marufeb.models.abstraction;

/**
 * Makes something collectable
 */
public interface Collectable {
    /**
     * Performs the PICK operation.
     * @return TRUE if the object has been picked up otherwise FALSE.
     */
    boolean collect(Backpack reference);
}
