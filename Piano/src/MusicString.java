/**
 * This interface represents a guitar string, piano wire, or other vibrating
 * musical instrument string.
 *
 * An implementation will contain a queue of displacement values ranging between
 * -0.5 and 0.5, where the length of the queue is related to the frequency of
 * the string and the sampling rate.
 * When the string is at rest (freshly constructed), the queue is full of zeros.
 * When the string is struck or plucked, the queue will be filled with random
 * values (white noise), between -0.5 and 0.5
 * When the string is sampled, the sample value at the front of the queue is
 * removed, and a new value averaging the first two values multiplied by a decay
 * factor is added to the end.
 */
public interface MusicString {

    /**
     * This method simulates striking the wire by replacing all of the values in
     * the queue with random values from the range -0.5 to 0.5.
     * The size of the queue should remain unchanged.
     */
    void strike();

    /**
     * This method returns the value currently stored at the front of the queue.
     * It also updates the contents of the queue so that a different sample is
     * obtained each time the method is called.
     * The update removes the value currently at the front and adds a new value
     * to the rear, which is the average of the two front values multiplied by a
     * decay factor.
     * @return Current sample value after updating queue.
     */
    double sample();
}
