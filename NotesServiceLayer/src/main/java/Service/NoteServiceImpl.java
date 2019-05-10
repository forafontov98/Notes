package Service;



import DAO.NotesDAO;
import Model.ImageNote;
import Model.Note;
import Model.TextNote;
import Model.ToDoNote;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NoteServiceImpl implements NoteService {

    private final NotesDAO dao;

    public NoteServiceImpl(NotesDAO dao) { this.dao = dao; }

    @Override
    public List<ImageNote> getAllImageNotes() {
        return dao.getAllImageNotes();
    }

    @Override
    public List<TextNote> getAllTextNotes() {
        return dao.getAllTextNotes();
    }

    @Override
    public List<ToDoNote> getAllToDoNotes() {
        return dao.getAllToDoNotes();
    }

    @Override
    public Note getById(long id) {
        return dao.getById(id);
    }

    @Override
    public Note create(String name, Date dateCreation, String stringBody) {
        return dao.create(name, dateCreation, stringBody);
    }

    @Override
    public Note create(String name, Date dateCreation, Map<String, Boolean> list) {
        return dao.create(name, dateCreation, list);
    }

    @Override
    public Note create(String name, Date dateCreation, BufferedImage images) {
        //получить место хранения фоторграфии и отправить

        return dao.createImgNote(name, dateCreation,  images, "Место хранения");
    }

    @Override
    public void update(Note note) {
        return;
        //dao.update(note);
    }

    @Override
    public boolean remove(Note note) {
        return dao.remove(note.getId());
    }

    @Override
    public boolean remove(long id) {
        return dao.remove(id);
    }
}
