package ir.mrahimy.conceal.util;

import java.io.IOException;

import ir.mrahimy.conceal.data.Waver;

public class WavUtil {
    public static Waver fromWaveData(Wave.WavFile file) {

        final int BUF_SIZE = 5001;
        long[] buffer = new long[BUF_SIZE * file.getNumChannels()];

        int framesRead = 0;

        do {
            try {
                framesRead = file.readFrames(buffer, BUF_SIZE);
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
}
