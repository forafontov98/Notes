package Model;

import Model.Enums.NotesType;

import java.util.Date;
import java.util.Map;

public class ToDoNote extends Note {

    /** Словарь значений списка. Key - название задачи, Boolean - помечена ли задача */
    private Map<String,Boolean> list;

    public ToDoNote(String name, Date dateCreation, long id, Map<String,Boolean> list) {
        super(name, dateCreation, id);

        this.list = list;
    }

    @Override
    public String toString() {
        return "ToDoNote{" +
                "list=" + list +
                ", name='" + name + '\'' +
                ", dateCreation=" + dateCreation +
                ", id=" + id +
                '}';
    }

    public Map<String, Boolean> getList() {
        return list;
    }
}
