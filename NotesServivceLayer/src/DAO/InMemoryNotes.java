package DAO;

import Model.ImageNote;
import Model.Note;
import Model.TextNote;
import Model.ToDoNote;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryNotes implements NotesDAO {

    /** Singleton */
    private InMemoryNotes() {}
    private InMemoryNotes instance;

    public InMemoryNotes shared() {
        if (instance == null) instance = new InMemoryNotes();

        return instance;
    }


    private AtomicLong idGenerator = new AtomicLong();
    private Map<Long, Note> allNotes = new HashMap<>();


    @Override
    public List<Note> getAll() {
        return new ArrayList<>(allNotes.values());
    }

    @Override
    public Note getById(long id) {
        return allNotes.get(id);
    }

    @Override
    public Note create(String name, Date dateCreation, String stringBody) {
        Note note = new TextNote(name, dateCreation, idGenerator.incrementAndGet(), stringBody);
        allNotes.put(note.getId(), note);
        return note;
    }

    @Override
    public Note create(String name, Date dateCreation, Map<String,Boolean> list) {
        Note note = new ToDoNote(name, dateCreation, idGenerator.incrementAndGet(), list);
        allNotes.put(note.getId(), note);
        return note;
    }

    @Override
    public Note create(String name, Date dateCreation, BufferedImage[] images) {
        Note note = new ImageNote(name, dateCreation, idGenerator.incrementAndGet(), images);
        allNotes.put(note.getId(), note);
        return note;
    }

    @Override
    public void update(Note note) {
        allNotes.put(note.getId(), note);
    }

    @Override
    public boolean remove(long id) {
        Note removed = allNotes.remove(id);
        return removed != null;
    }
}
