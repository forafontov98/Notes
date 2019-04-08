package Model;

import Model.Enums.NotesType;

import java.util.Date;

public abstract class Note {

    /** Название заметки */
    protected String name;

    /** Дата создания заметки */
    protected Date dateCreation;

    /** id заметки */
    protected long id;

    Note(String name, Date dateCreation, long id) {
        this.name = name;
        this.dateCreation = dateCreation;
        this.id = id;
    }

    public String getName() { return name; }

    public Date getDateCreation() { return dateCreation; }

    public long getId() { return id; }
}
