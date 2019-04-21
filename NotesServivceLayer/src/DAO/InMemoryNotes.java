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

    private Map<Long, ImageNote> allImageNotes = new HashMap<>();
    private Map<Long, TextNote> allTextNotes = new HashMap<>();
    private Map<Long, ToDoNote> allToDoNotes = new HashMap<>();


    @Override
    public List<ImageNote> getAllImageNotes() {
        return new ArrayList<>(allImageNotes.values());
    }

    @Override
    public List<TextNote> getAllTextNotes() {
        return new ArrayList<>(allTextNotes.values());
    }

    @Override
    public List<ToDoNote> getAllToDoNotes() {
        return new ArrayList<>(allToDoNotes.values());
    }

    @Override
    public Note getById(long id) {
        if (allTextNotes.get(id) != null) {
            return allTextNotes.get(id);
        } else {
            if (allImageNotes.get(id) != null) {
                return allImageNotes.get(id);
            }
        }

        return allToDoNotes.get(id);
    }

    @Override
    public Note create(String name, Date dateCreation, String stringBody) {
        TextNote note = new TextNote(name, dateCreation, idGenerator.incrementAndGet(), stringBody);
        allTextNotes.put(note.getId(), note);
        return note;
    }

    @Override
    public Note create(String name, Date dateCreation, Map<String,Boolean> list) {
        ToDoNote note = new ToDoNote(name, dateCreation, idGenerator.incrementAndGet(), list);
        allToDoNotes.put(note.getId(), note);
        return note;
    }

    @Override
    public Note create(String name, Date dateCreation, BufferedImage[] images) {
        ImageNote note = new ImageNote(name, dateCreation, idGenerator.incrementAndGet(), images);
        allImageNotes.put(note.getId(), note);
        return note;
    }

    @Override
    public void update(TextNote note) {
        allTextNotes.put(note.getId(), note);
    }

    @Override
    public void update(ImageNote note) {
        allImageNotes.put(note.getId(), note);
    }

    @Override
    public void update(ToDoNote note) {
        allToDoNotes.put(note.getId(), note);
    }

    @Override
    public boolean remove(long id) {
        Note removed = allImageNotes.remove(id);

        if (removed == null) {
            removed = allToDoNotes.remove(id);

            if (removed == null) {
                removed = allTextNotes.remove(id);
            }
        }

        return removed != null;
    }
}
