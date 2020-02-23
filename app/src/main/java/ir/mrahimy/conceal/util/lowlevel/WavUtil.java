package ir.mrahimy.conceal.util.lowlevel;

import java.io.IOException;

import ir.mrahimy.conceal.data.Waver;

public class WavUtil {
    public static Waver fromWaveData(Wave.WavFile file) {

        final int BUFFER_SIZE = (int) file.getNumFrames();
        final int CHANNEL_COUNT = file.getNumChannels();
        long[] buffer = new long[BUFFER_SIZE * CHANNEL_COUNT];

        int framesRead = 0;
        int offset = 0;

        do {
            try {
                framesRead = file.readFrames(buffer, offset, BUFFER_SIZE);
                offset += framesRead;
            } catch (Wave.WavFileException | IOException e) {
                e.printStackTrace();
            }
        } while (framesRead != 0);

        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Waver(buffer,
                file.getSampleRate(),
                file.getNumChannels(),
                file.getNumFrames(),
                file.getValidBits());
    }

    public static void writeAllFrames(Wave.WavFile file, Waver waver) {

        final int BUFFER_SIZE = 1024;

        int framesWritten = 0;
        int offset = 0;

        do {
            try {
                framesWritten = file.writeFrames(waver.getData(), offset, BUFFER_SIZE);
                offset += framesWritten;
            } catch (Wave.WavFileException | IOException e) {
                e.printStackTrace();
            }
        } while (framesWritten != 0);

        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
