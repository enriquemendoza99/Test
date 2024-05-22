import java.util.Arrays;

public class PianoTest {

    /**
     * Repeatedly play samples from a piano for given duration
     * @param piano The piano to sample
     * @param duration Duration in seconds to sample
     */
    public static void sampleForDuration(Piano piano, double duration) {
        double samples = SimpleSound.SAMPLE_RATE*duration;
        for(int i = 0; i < samples; i++) {
            SimpleSound.playSample(piano.sampleAll());
        }
    }

    /**
     * Play sequence of notes on the piano for a half second between strikes
     * @param piano The piano to play on
     * @param notes The notes to play
     */
    public static void playNotes(Piano piano, Iterable<String> notes) {
        playNotes(piano, 0.5, notes);
    }

    /**
     * Play sequence of notes on the piano for same duration each
     * @param piano The piano to play on
     * @param duration Duration in seconds
     * @param notes The notes to play
     */
    public static void playNotes(Piano piano, double duration, Iterable<String> notes) {
        for(String note : notes) {
            piano.strikeNote(note);
            sampleForDuration(piano, duration);
        }
    }

    /** Simple record to contain the note and its duration */
    public record NoteAndDuration(String note, double duration) {}

    /**
     * Play a tune on the piano
     * @param piano Piano to play on
     * @param tune Sequence of notes with their durations
     */
    public static void playTune(Piano piano, NoteAndDuration... tune) {
        playTune(piano, Arrays.asList(tune));
    }

    /**
     * Play a tune on the piano
     * @param piano Piano to play on
     * @param tune Sequence of notes with their durations
     */
    public static void playTune(Piano piano, Iterable<NoteAndDuration> tune) {
        for(NoteAndDuration nd : tune) {
            if(nd.note() != null) {
                piano.strikeNote(nd.note());
            }
            sampleForDuration(piano, nd.duration());
        }
    }

    public static void main(String[] args) {

        double decay = 0.996;
        if(args.length > 0) {
            decay = Double.parseDouble(args[0]);
        }

        Piano p = new Piano(decay);
        String[] notes = { "C", "C#", "D", "D#", "E",
                "F", "F#", "G", "G#", "A", "A#", "B",
                "C+", "C#+", "D+"};

        // queue length is SAMPLE_RATE / frequency
        // frequency is 2^(offsetFromA4/12) * 440
        for(int i = 0; i < notes.length; i++) {
            double freq = Math.pow(2, (i-21)/12.0) * 440;
            int queueSize =(int)(SimpleSound.SAMPLE_RATE / freq);
            //System.out.println(notes[i] + " " + freq + " " + queueSize);
            p.addStringForNote(notes[i], queueSize);
        }

        // play all the notes
        playNotes(p, Arrays.asList(notes));

        // take a second without striking to fade out sounds
        sampleForDuration(p, 1);

        // Let's play some chords to make sure multiple strikes work
        p.strikeNote("C");
        p.strikeNote("E");
        p.strikeNote("G");
        sampleForDuration(p, 1);

        p.strikeNote("C");
        p.strikeNote("D#");
        p.strikeNote("G");
        sampleForDuration(p, 1);

        p.strikeNote("C");
        p.strikeNote("E");
        p.strikeNote("G#");
        sampleForDuration(p, 1);

        p.strikeNote("C");
        p.strikeNote("D#");
        p.strikeNote("F#");
        sampleForDuration(p, 1);


        // "Do Re Mi" from the sound of music
        playTune(p,
                new NoteAndDuration("C", 0.75),
                new NoteAndDuration("D", 0.25),
                new NoteAndDuration("E", 0.75),
                new NoteAndDuration("C", 0.25),
                new NoteAndDuration("E", 0.5),
                new NoteAndDuration("C", 0.5),
                new NoteAndDuration("E", 1.0),

                new NoteAndDuration("D", 0.75),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("F", 0.25),
                new NoteAndDuration("F", 0.25),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("D", 0.25),
                new NoteAndDuration("F", 2.0),

                new NoteAndDuration("E", 0.75),
                new NoteAndDuration("F", 0.25),
                new NoteAndDuration("G", 0.75),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("G", 0.5),
                new NoteAndDuration("E", 0.5),
                new NoteAndDuration("G", 1.0),

                new NoteAndDuration("F", 0.75),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("F", 0.25),
                new NoteAndDuration("A", 2.0),

                new NoteAndDuration("G", 0.75),
                new NoteAndDuration("C", 0.25),
                new NoteAndDuration("D", 0.25),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("F", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 2.0),

                new NoteAndDuration("A", 0.75),
                new NoteAndDuration("D", 0.25),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("F", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("B", 2.0),

                new NoteAndDuration("B", 0.75),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("F", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("B", 0.25),
                new NoteAndDuration("C+", 1.5),

                new NoteAndDuration("C+", 0.25),
                new NoteAndDuration("B", 0.25),
                new NoteAndDuration("A", 0.5),
                new NoteAndDuration("F", 0.5),
                new NoteAndDuration("B", 0.5),
                new NoteAndDuration("G", 0.5),
                new NoteAndDuration("C+", 2.0));


        // "Do Re Mi" shifted up this time
        playTune(p,
                new NoteAndDuration("D", 0.75),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("F#", 0.75),
                new NoteAndDuration("D", 0.25),
                new NoteAndDuration("F#", 0.5),
                new NoteAndDuration("D", 0.5),
                new NoteAndDuration("F#", 1.0),

                new NoteAndDuration("E", 0.75),
                new NoteAndDuration("F#", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("F#", 0.25),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("G", 2.0),

                new NoteAndDuration("F#", 0.75),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 0.75),
                new NoteAndDuration("F#", 0.25),
                new NoteAndDuration("A", 0.5),
                new NoteAndDuration("F#", 0.5),
                new NoteAndDuration("A", 1.0),

                new NoteAndDuration("G", 0.75),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("B", 0.25),
                new NoteAndDuration("B", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("B", 2.0),

                new NoteAndDuration("A", 0.75),
                new NoteAndDuration("D", 0.25),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("F#", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("B", 2.0),

                new NoteAndDuration("B", 0.75),
                new NoteAndDuration("E", 0.25),
                new NoteAndDuration("F#", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("B", 0.25),
                new NoteAndDuration("C#+", 2.0),

                new NoteAndDuration("C#+", 0.75),
                new NoteAndDuration("F#", 0.25),
                new NoteAndDuration("G", 0.25),
                new NoteAndDuration("A", 0.25),
                new NoteAndDuration("B", 0.25),
                new NoteAndDuration("C#+", 0.25),
                new NoteAndDuration("D+", 1.5),

                new NoteAndDuration("D+", 0.25),
                new NoteAndDuration("C#+", 0.25),
                new NoteAndDuration("B", 0.5),
                new NoteAndDuration("G", 0.5),
                new NoteAndDuration("C#+", 0.5),
                new NoteAndDuration("A", 0.5),
                new NoteAndDuration("D+", 2.0));

        // Drain buffer when done to make sure all the samples get to
        // the sound card
        SimpleSound.drainBuffer();
    }
}
