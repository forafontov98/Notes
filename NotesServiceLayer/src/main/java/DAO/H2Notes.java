package DAO;

import Model.ImageNote;
import Model.Note;
import Model.TextNote;
import Model.ToDoNote;
import exeption.NotesException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.sql.DataSource;
import javax.xml.soap.Node;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Date;

public class H2Notes implements NotesDAO {
    private DataSource dataSource;

    public H2Notes(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private ImageNote retriveImgNode(ResultSet resultSet) throws SQLException
    {
        return new ImageNote(
                resultSet.getString(2),
                resultSet.getDate(3),
                resultSet.getLong(1),
                decodeToImage(resultSet.getString(4))
                );
    }

    @Override
    public List<ImageNote> getAllImageNotes() {
        try (
                Connection connection = dataSource.getConnection();
                Statement  statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select NOTES.ID, NAME, CreatedAt, WAY from IMAGE_NOTE, NOTES where NOTES.ID = IMAGE_NOTE.NOTES_ID");
                ){

            if (resultSet == null) { throw new SQLException("Unable to load Image Notes"); }

            List <ImageNote> list = new ArrayList<>();

            while (resultSet.next())
                list.add(retriveImgNode(resultSet));

            return list;
        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    private TextNote retriveTextNode(ResultSet resultSet) throws SQLException
    {
        return new TextNote(
                resultSet.getString(2),
                resultSet.getDate(3),
                resultSet.getLong(1),
                resultSet.getString(4)
        );
    }

    @Override
    public List<TextNote> getAllTextNotes() {
        try (
                Connection connection = dataSource.getConnection();
                Statement  statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select NOTES.ID, NAME, CreatedAt, BODY from TEXT_NOTE, NOTES where NOTES.ID = TEXT_NOTE.NOTES_ID")
        ){

            if (resultSet == null) { throw new SQLException("Unable to load Text Notes"); }

            List <TextNote> list = new ArrayList<>();
            while (resultSet.next())
                list.add(retriveTextNode(resultSet));

            return list;
        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    private ToDoNote retriveToDoNode(ResultSet resultSet,  Map<String,Boolean> list) throws SQLException
    {
        return new ToDoNote(
                resultSet.getString(2),
                resultSet.getDate(3),
                resultSet.getLong(1),
                list
        );
    }
    //    public ToDoNote(String name, Date dateCreation, long id, Map<String,Boolean> list)
    @Override
    public List<ToDoNote> getAllToDoNotes() {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select NOTES.ID, NAME, CreatedAt from NOTES where Type = ?")
                ){
            statement.setInt(1, 3);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) throw  new NotesException("Enable to load ToDoNote");

            List<ToDoNote> allToDoTotes = new ArrayList<>();

            while (resultSet.next())
            {
                long id = resultSet.getLong(1);
                Map<String,Boolean> list = new HashMap<>();
                ResultSet newResultSet;

                //получение задач

                PreparedStatement newStat = connection.prepareStatement("select TASK_NAME, label from TODO_NOTE where NOTES_ID = ?");
                newStat.setLong(1, id);

                newResultSet = newStat.executeQuery();
                if (newResultSet == null) throw  new NotesException("Enable to load Tasks");
                while (newResultSet.next()) {
                    list.put(newResultSet.getString(1), newResultSet.getBoolean(2));
                }

                allToDoTotes.add(retriveToDoNode(resultSet, list));
            }

            return allToDoTotes;
        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    //    ImageNote(String name, Date dateCreation, long id, BufferedImage[] images)
    private ImageNote retriveImgNodeById(ResultSet resultSet, String imgCod) throws SQLException
    {
        return new ImageNote(
                resultSet.getString(2),
                resultSet.getDate(3),
                resultSet.getLong(1),
                decodeToImage(imgCod)
        );
    }
    //    public TextNote(String name, Date dateCreation, long id, String body)
    private TextNote retriveTextNodeById(ResultSet resultSet, String text) throws SQLException
    {
        return new TextNote(
                resultSet.getString(2),
                resultSet.getDate(3),
                resultSet.getLong(1),
                text
        );
    }

    @Override
    public Note getById(long id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select NOTES.ID, NAME, CreatedAt, Type from NOTES where ID = ?")
                ){
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery())
            {
                resultSet.next();
                ResultSet newResultSet;
                PreparedStatement newStat;

                int type = resultSet.getInt(4);
                switch (type)
                {
                    case 1:
                        newStat = connection.prepareStatement("select WAY from IMAGE_NOTE where NOTES_ID = ?");
                        newStat.setLong(1, id);
                        newResultSet = newStat.executeQuery();
                        if (newResultSet == null) throw  new NotesException("Enable to load image Note");
                        newResultSet.next();
                        return retriveImgNodeById(resultSet, newResultSet.getString(1));

                    case 2:
                        newStat = connection.prepareStatement("select BODY from TEXT_NOTE where NOTES_ID = ?");
                        newStat.setLong(1, id);
                        newResultSet = newStat.executeQuery();
                        if (newResultSet == null) throw  new NotesException("Enable to load Text Note");
                        newResultSet.next();
                        return retriveTextNodeById(resultSet, newResultSet.getString(1));

                    case 3:
                        newStat = connection.prepareStatement("select TASK_NAME, label from TODO_NOTE where NOTES_ID = ?");
                        newStat.setLong(1, id);
                        newResultSet = newStat.executeQuery();
                        if (newResultSet == null) throw  new NotesException("Enable to load ToDO Note");

                        Map<String,Boolean> list = new HashMap<>();
                        while (newResultSet.next())
                            list.put(newResultSet.getString(1), newResultSet.getBoolean(2));

                        return retriveToDoNode(resultSet, list);

                        default:
                            throw new SQLException("");
                }
            }
        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    private long Check(int created, Statement statement) throws SQLException
    {
        if (created != 1) throw new NotesException("Unable to Create");

        long id;
        try(ResultSet resultSet = statement.getGeneratedKeys())
        {
            if (resultSet.next())
            {
                id = resultSet.getLong(1);
            }
            else
            {
                throw new SQLException("no id");
            }
        }
        return id;
    }

    @Override
    public Note create(String name, Date dateCreation, String stringBody) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT into NOTES (NAME, Type) " +
                        "values (?, ?)", Statement.RETURN_GENERATED_KEYS)
                ){
            statement.setString(1, name);
            statement.setInt(2, 2);

            int created = statement.executeUpdate();
            long id = Check(created, statement);

            PreparedStatement newStatement = connection.prepareStatement("INSERT INTO TEXT_NOTE (BODY, NOTES_ID)" +
                    "values (?, ?) ");
            newStatement.setString(1, stringBody);
            newStatement.setLong(2, id);

            created = newStatement.executeUpdate();
            if (created != 1) throw new NotesException("Unable to Create");

            return new TextNote(name, dateCreation, id, stringBody);

        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    @Override
    public Note create(String name, Date dateCreation, Map<String, Boolean> list) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT into NOTES (NAME, Type) " +
                        "values (?, ?)", Statement.RETURN_GENERATED_KEYS)
        ){
            statement.setString(1, name);
            statement.setInt(2, 2);


            int created = statement.executeUpdate();
            long id = Check(created, statement);

            for (Map.Entry<String, Boolean> entry : list.entrySet())
            {
                PreparedStatement newStatement = connection.prepareStatement("INSERT INTO TODO_NOTE (TASK_NAME, label, NOTES_ID)" +
                        "values (?, ?, ?)");
                newStatement.setString(1, entry.getKey());
                newStatement.setBoolean(2, entry.getValue());
                statement.setLong(3, id);
            }

            return new ToDoNote(name, dateCreation, id, list);
        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    @Override
    public Note createImgNote(String name, Date dateCreation, BufferedImage images, String way) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT into NOTES (NAME, Type) " +
                        "values (?, ?)", Statement.RETURN_GENERATED_KEYS)
        ){
            statement.setString(1, name);
            statement.setInt(2, 2);

            int created = statement.executeUpdate();
            long id = Check(created, statement);

            /*2 часть*/
            PreparedStatement newStatement = connection.prepareStatement("INSERT INTO IMAGE_NOTE (WAY, NOTES_ID)" +
                    "values (?, ?) ");
            newStatement.setString(1, encodeToString(images, "png"));
            newStatement.setLong(2, id);

            created = newStatement.executeUpdate();
            if (created != 1) throw new NotesException("Unable to Create Img Note");

            return new ImageNote(name, dateCreation, id, images);

        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    @Override
    public void update(TextNote note) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE NOTES " +
                        "SET NAME = ? where ID = ?")
        )
        {
            statement.setString(1, note.getName());
            statement.setLong(2, note.getId());

            int updated = statement.executeUpdate();
            if (updated != 1) throw new SQLException("Enable to update TextNote");

            PreparedStatement newStatement = connection.prepareStatement("UPDATE TEXT_NOTE " +
                    "SET BODY = ? where NOTES_ID = ?");
            newStatement.setString(1,  note.getBody());
            newStatement.setLong(2, note.getId());

            updated = statement.executeUpdate();
            if (updated != 1) throw new SQLException("Enable to update");
        }
        catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    @Override
    public void update(ImageNote note) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE NOTES " +
                        "SET NAME = ? where ID = ?")
        )
        {
            statement.setString(1, note.getName());
            statement.setLong(2, note.getId());

            int updated = statement.executeUpdate();
            if (updated != 1) throw new SQLException("Enable to update ImageNote");

            PreparedStatement newStatement = connection.prepareStatement("UPDATE IMAGE_NOTE " +
                    "SET WAY = ? where NOTES_ID = ?");
            newStatement.setString(1, encodeToString(note.getImages(), "png"));
            newStatement.setLong(2, note.getId());

            updated = statement.executeUpdate();
            if (updated != 1) throw new SQLException("Enable to update");
        }
        catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    @Override
    public void update(ToDoNote note) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE NOTES " +
                        "SET NAME = ? where ID = ?")
        )
        {
            statement.setString(1, note.getName());
            statement.setLong(2, note.getId());

            int updated = statement.executeUpdate();
            if (updated != 1) throw new SQLException("Enable to update ToDoNote");

            PreparedStatement statementDel = connection.prepareStatement("DELETE  FROM TODO_NOTE " +
                    "WHERE NOTES_ID = ?");
            statementDel.setLong(1, note.getId());

            for (Map.Entry<String, Boolean> entry : note.getList().entrySet())
            {
                PreparedStatement newStatement = connection.prepareStatement("UPDATE TODO_NOTE " +
                        "SET TASK_NAME = ?, label = ? where NOTES_ID = ?");
                newStatement.setString(1, entry.getKey());
                newStatement.setBoolean(2, entry.getValue());
            }
        }
        catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    @Override
    public boolean remove(long id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE from NOTES WHERE ID = ?")
        )
            {
                statement.setLong(1, id);
                int deleted = statement.executeUpdate();
                if (deleted != 1) throw new SQLException("Enable to delete");

                return true;
        } catch (SQLException e) {
            throw new NotesException(e);
        }
    }

    private static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            imageString = encoder.encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    private static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;
        byte[] imageByte;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            imageByte = decoder.decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
