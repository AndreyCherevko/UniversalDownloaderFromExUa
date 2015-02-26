package download.utility;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by Андрей on 10.12.2014.
 */
public class FileTypeGetter {
    private Set<String> image = new HashSet<>();
    private Set<String> video = new HashSet<>();
    private Set<String> music = new HashSet<>();
    private Set<String> archive = new HashSet<>();
    private static FileTypeGetter instance = null;
    private FileTypeGetter() {
    }
    public static FileTypeGetter getInstance(){
        if(instance==null) {
            instance = new FileTypeGetter();
            instance.initializeMaps();
        }
        return instance;
    }

    private void initializeMaps(){
        image.add("BMP".toLowerCase());
        image.add("GIF".toLowerCase());
        image.add("JPEG".toLowerCase());
        image.add("PNG".toLowerCase());
        image.add("JPG".toLowerCase());
        video.add("3gp".toLowerCase());
        video.add("FLV".toLowerCase());
        video.add("mp4".toLowerCase());
        video.add("mkv".toLowerCase());
        video.add("WMV".toLowerCase());
        video.add("avi".toLowerCase());
        music.add("AAC".toLowerCase());
        music.add("FLAC".toLowerCase());
        music.add("MP3 ".toLowerCase());
        music.add("WAV".toLowerCase());
        archive.add("zip".toLowerCase());
        archive.add("rar".toLowerCase());
        archive.add("7zip".toLowerCase());
        archive.add("jar".toLowerCase());
        archive.add("iso".toLowerCase());
    }
    public String getType(String file){
        int index = file.lastIndexOf(".");
        String type = file.substring(index+1).toLowerCase();
        if(image.contains(type))
            return "Image";
        if(video.contains(type))
            return "Video";
        if(archive.contains(type))
            return "Archive";
        if(music.contains(type))
            return "Music";
        return "Other";
    }
}
