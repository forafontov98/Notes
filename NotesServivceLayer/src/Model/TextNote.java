package Model;

import Model.Enums.NotesType;

import java.util.Date;

public class TextNote extends Note {

    /** Текстовое тело заметки */
    private String body;

    public TextNote(String name, Date dateCreation, long id, String body) {
        super(name, dateCreation, id);

        this.body = body;
    }


}
