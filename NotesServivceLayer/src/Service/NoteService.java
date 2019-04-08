package Service;

import Model.Note;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NoteService {

    List<Note> getAll();

    Note getById(long id);

    Note create(String name, Date dateCreation, String stringBody);

    Note create(String name, Date dateCreation, Map<String,Boolean> list);

    Note create(String name, Date dateCreation, BufferedImage[] images);

    void update(Note note);

    boolean remove(Note note);

    boolean remove(long id);

}
