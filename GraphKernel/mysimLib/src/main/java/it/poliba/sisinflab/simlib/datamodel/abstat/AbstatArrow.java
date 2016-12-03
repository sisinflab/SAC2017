package it.poliba.sisinflab.simlib.datamodel.abstat;

import it.poliba.sisinflab.simlib.datamodel.Arrow;

/**
 * Created by giorgio on 12/05/16.
 */
public class AbstatArrow extends Arrow{

    private int occurrence = 0;

    public AbstatArrow(AbstatNode property, String direction) {
        super(property, direction);
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }
}
