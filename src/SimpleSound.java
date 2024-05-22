import javax.sound.sampled.*;

/**
 * Small class to wrap the Java sampled sound library.
 *
 * To play sounds, call the playSample method SAMPLE_RATE times per
 * second with values between -1.0 and 1.0
 *
 * Based on code from
 * https://introcs.cs.princeton.edu/java/stdlib/StdAudio.java
 */
public class SimpleSound {
    /** The sample rate: 44,100 Hz for CD quality audio */
    public static final int SAMPLE_RATE = 44100;

    /**
     * Send sample value to the sound card.
     * @param sample value between -1.0 and 1.0
     */
    public static void playSample(double sample) {
        getSoundPlayer().play(sample);
    }

    /**
     * Some systems need to force the samples to go to the sound card.
     */
    public static void drainBuffer() {
        getSoundPlayer().drain();
    }
    
    /** Singleton sound player object */
    private static SimpleSound soundPlayer;

    private static SimpleSound getSoundPlayer() {
        if(soundPlayer == null) {
            soundPlayer = new SimpleSound();
        }
        return soundPlayer;
    }

    private static final int SAMPLE_BUFFER_SIZE = 4096;

    private SourceDataLine line;
    private static byte[] buffer;         // our internal buffer
    private static int bufferSize = 0;    // number of samples currently in internal buffer

    private SimpleSound() {
        try {
            // 44,100 Hz, 16-bit audio, mono, signed PCM, little endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE,
                    16, 1,true , false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * 2);

            // the internal buffer is a fraction of the actual buffer
            // size, this choice is arbitrary it gets divided because
            // we can't expect the buffered data to line up exactly
            // with when the sound card decides to push out its
            // samples.
            buffer = new byte[SAMPLE_BUFFER_SIZE * 2 / 3];
        }
        catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

        // no sound gets made before this call
        line.start();
    }

    /**
     * Queues a sample to the sound card
     * @param sample value between -1.0 and 1.0
     */
    private void play(double sample) {
        if (Double.isNaN(sample)) throw new IllegalArgumentException("sample is NaN");

        // clip if outside [-1, +1]
        if (sample < -1.0) sample = -1.0;
        if (sample > +1.0) sample = +1.0;

        // convert to bytes
        short s = (short) (-Short.MIN_VALUE * sample);
        if (sample == 1.0) s = Short.MAX_VALUE;   // special case since 32768 not a short
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8);   // little endian

        // send to sound card if buffer is full
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    /**
     * Sends any queued samples to the sound card.
     */
    private void drain() {
        if (bufferSize > 0) {
            line.write(buffer, 0, bufferSize);
            bufferSize = 0;
        }
        line.drain();
    }


    private void close() {
        drain();
        line.close();
    }

    /**
     * Test/demo the SimpleSound functionality.
     */
    public static void main(String[] args) {
        // 440 Hz for 1 sec (sampling a sin wave)
        double freq = 440.0;
        for (int i = 0; i < SAMPLE_RATE; i++) {
            SimpleSound.playSample(0.5 * Math.sin(2*Math.PI * freq * i / SAMPLE_RATE));
        }


        // white noise is just random values
        for(int i = 0; i < SAMPLE_RATE; i++) {
            SimpleSound.playSample(Math.random() - 0.5);
        }
    }
}
