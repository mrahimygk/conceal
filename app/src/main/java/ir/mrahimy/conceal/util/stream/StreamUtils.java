package ir.mrahimy.conceal.util.stream;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
    public static byte[] convertStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[10240];
        int i = Integer.MAX_VALUE;
        while ((i = is.read(buff, 0, buff.length)) > 0) {
            baos.write(buff, 0, i);
        }

        return baos.toByteArray(); // be sure to close InputStream in calling function
    }


    public static byte[] getByteArray(InputStream is, DataInputStream dis) throws IOException {
        byte[] music = new byte[is.available()];
        int i = 0; // Read the file into the "music" array
        while (dis.available() > 0) {
            // dis.read(music[i]); // This assignment does not reverse the order
            music[i] = dis.readByte();
            i++;
        }

        return music;
    }
}
