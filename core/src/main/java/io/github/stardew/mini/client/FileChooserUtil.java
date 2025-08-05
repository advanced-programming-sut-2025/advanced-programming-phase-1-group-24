package io.github.stardew.mini.client;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooserUtil {


    public static String showFileChooserAndGetPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select an audio file");
        // فقط پسوندهای wav, mp3, ogg قابل انتخاب باشند
        chooser.setFileFilter(new FileNameExtensionFilter("Audio files", "wav", "mp3", "ogg"));

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }
}
