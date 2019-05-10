package DAO;

import Model.ImageNote;
import Model.Note;
import Model.TextNote;
import Model.ToDoNote;

import java.util.Date;

import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;

public interface NotesDAO {

    List<ImageNote> getAllImageNotes();

    List<TextNote> getAllTextNotes();

    List<ToDoNote> getAllToDoNotes();

    Note getById(long id);

    Note create(String name, Date dateCreation, String stringBody);

    Note create(String name, Date dateCreation, Map<String,Boolean> list);

    Note createImgNote(String name, Date dateCreation, BufferedImage images, String way);

    void update(TextNote note);

    void update(ImageNote note);

    void update(ToDoNote note);

    boolean remove(long id);
}
