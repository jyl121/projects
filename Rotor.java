package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Jiyoon Lee
 */
public class Rotor {

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    private int _setting;

    /** A rotor named NAME whose permutation is given by PERM. */
    public Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
    }

    /** Return my name. */
    public String name() {
        return _name;
    }

    /** Return my alphabet. */
    public Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    public Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    public int size() {
        return _permutation.size();
    }

    /** Return true if and only if I have a ratchet and can move. */
    public boolean rotates() {
        return false;
    }

    /** Return true if and only if I reflect. */
    public boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    public int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    public void set(int posn) {
        _setting = _permutation.wrap(posn);
    }

    /** Set setting() to character CPOSN. */
    public void set(char cposn) {
        _setting = _permutation.alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    public int convertForward(int p) {
        int inContact = _permutation.wrap(p + _setting);
        int permuted = _permutation.permute(inContact);
        int outContact = _permutation.wrap(permuted - _setting);
        return outContact;
    }

    /** Return the conversion of C (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    public int convertBackward(int c) {
        int inContact = _permutation.wrap(c + _setting);
        int inverted = _permutation.invert(inContact);
        int outContact = _permutation.wrap(inverted - _setting);
        return outContact;
    }

    /** Returns true if and only if I am positioned to allow the rotor
     * to my left to advance. */
    public boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    public void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }


}
