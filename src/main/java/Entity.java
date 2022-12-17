/**
 * Guo Fan - assignment 7
 * CS 514
 * My repo is GuoFan1996
 */
import java.util.Date;

public class Entity {
    protected String name;
    protected static int counter = 0;
    protected String musicBrainzeID;
    protected Date dateCreated;
    protected int dbID;

    public Entity() {
        this.name="null";
        dateCreated = new Date();
    }

    public boolean equals(Entity otherEntity) {
        return musicBrainzeID == otherEntity.musicBrainzeID;
    }


    public Entity(String name) {
        this.name = name;
        dateCreated = new Date();
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Name: " + this.name;
    }
    public String toXML() {
        return "<entity><name>" + this.name + "</name><ID> " + this.musicBrainzeID + "</ID></entity>";
    }
}

