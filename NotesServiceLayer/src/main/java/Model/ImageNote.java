package Model;

import Model.Enums.NotesType;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Date;

public class ImageNote extends Note {

    /** Изображения заметки */
    private BufferedImage images;

    public ImageNote(String name, Date dateCreation, long id, BufferedImage images) {
        super(name, dateCreation, id);
        this.images = images;
    }

    public BufferedImage getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "ImageNote{" +
                "images=" + images.toString() +
                ", name='" + name + '\'' +
                ", dateCreation=" + dateCreation +
                ", id=" + id +
                '}';
    }
}
