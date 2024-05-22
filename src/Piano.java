/**
 * CS 251 LAB 5 - Piano Simulator
 * Implement the methods to play with the sounds of the Piano Simulator.
 * Student name: Enrique Mendoza.
 */
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
public class Piano {
    // The energy decay factor (.996) models the slight dissipation in energy
    // as the waves makes a roundtrip through the wire.
    private double decayFactor;
    // Map to store the vibrating strings for each note.
    private Map<String, PianoString> stringMap;
    /**
     * Constructor to initialize the decay factor and an empty string map
     * @param decayFactor Decay Factor for the vibrating strings
     */
    public Piano(double decayFactor) {
        this.decayFactor = decayFactor;
        stringMap = new HashMap<>();
    }
    /**
     * Method to add a new string for a given note and queue size.
     * @param note string representation of a musical note.
     * @param queueSize size of the queue used to model the vibration of a
     *                  piano string.
     */
    public void addStringForNote(String note, int queueSize) {
        PianoString string = new PianoString(queueSize, decayFactor);
        stringMap.put(note, string);
    }
    /**
     * Method to strike a given note.
     * @param note string representation of a musical note.
     */
    public void strikeNote(String note) {
        PianoString string = stringMap.get(note);
        if (string != null) {
            string.strike();
        }
    }
    /**
     * Method to sample the overall sound by summing the sample of all strings.
     * @return the sum of the values returned.
     */
    public double sampleAll() {
        double sum = 0.0;
        for (PianoString string : stringMap.values()) {
            sum += string.sample();
        }
        return sum;
    }
    // Inner class representing a single vibrating string
    private class PianoString {
        private Queue<Double> queue;
        private double decayFactor;
        private Random random;
        /**
         * Constructor to initialize the string with a given queue size and
         * decay factor
         * @param queueSize size of the queue used to model the vibration of a
         *                  piano string.
         * @param decayFactor decay factor for the vibrating strings
         */
        public PianoString(int queueSize, double decayFactor) {
            this.decayFactor = decayFactor;
            queue = new LinkedList<>();
            random = new Random();
            for (int i = 0; i < queueSize; i++) {
                queue.offer(0.0);
            }
        }
        /**
         * Method to strike the string by filling the queue with random values
         */
        public void strike() {
            for (int i = 0; i < queue.size(); i++) {
                queue.poll();
                queue.offer(random.nextDouble() - 0.5);
            }
        }
        /**
         * Method to sample the string using the Karplus-Strong algorithm.
         * @return the value removed from the front of the queue.
         */
        public double sample() {
            double firstValue = queue.poll();
            double secondValue = queue.peek();
            double average = (firstValue + secondValue) / 2 * decayFactor;
            queue.offer(average);
            return firstValue;
        }
    }
}
