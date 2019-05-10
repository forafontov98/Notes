import DAO.H2Notes;
import Model.ImageNote;
import Model.TextNote;
import Model.ToDoNote;
import Service.NoteService;
import Service.NoteServiceImpl;
import exeption.NotesException;

import javax.sql.DataSource;
import java.io.Console;
import java.io.IOException;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
        DataSource dataSource = ConnectionManager.createDataSource();
        NoteServiceImpl service = new NoteServiceImpl(
                new H2Notes(dataSource));
        try {

            for (ImageNote note : service.getAllImageNotes())
            {
                System.out.println(note);
            }

            System.out.println();

            for (TextNote note : service.getAllTextNotes())
            {
                System.out.println(note);
            }

            System.out.println();

            for (ToDoNote note : service.getAllToDoNotes())
            {
                System.out.println(note);
            }

            System.out.println();

            System.out.println(service.getById(1));
            System.out.println();
            System.out.println(service.getById(2));
            System.out.println();
            System.out.println(service.getById(3));
            System.out.println();

            Date data = new Date();

            System.out.println(service.create("sdfasdf", data, "sdafsdfasd"));

            for (ImageNote note : service.getAllImageNotes())
            {
                System.out.println(note);
            }

            System.out.println();

            for (TextNote note : service.getAllTextNotes())
            {
                System.out.println(note);
            }

            System.out.println();

            for (ToDoNote note : service.getAllToDoNotes())
            {
                System.out.println(note);
            }



//
        } catch (NotesException e) {
            System.out.println(e);
        }
    }
}
