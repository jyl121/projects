package enigma;

import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jiyoon Lee
 */
public class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    private String[] _permutation;

    public Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;

        //get rid of all the parenthesis
        cycles = cycles.replaceAll("[)(]", " ");

        //counting how many cycles
        Scanner scanner = new Scanner(cycles);
        scanner.useDelimiter("\\s+");
        int count = 0;
        while (scanner.hasNext(".+")) {
            scanner.next();
            count++;
        }
        _permutation = new String[count];

        //storing the cycles into the array
        scanner = new Scanner(cycles);
        scanner.useDelimiter("\\s+");
        int index = 0;
        while (scanner.hasNext()) {
            String cycle = scanner.next();
            _permutation[index] = cycle;
            index++;
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    public int size() {
        return _alphabet.size();
    }

    /** Return the index result of applying this permutation to the character
     *  at index P in ALPHABET. */
    public int permute(int p) {
    	// NOTE: it might be beneficial to have one permute() method always call the other
        char character = _alphabet.toChar(wrap(p));
        for (String cycle : _permutation) {
            if (cycle.indexOf(character) != -1) {
                int i = cycle.indexOf(character);
                char permutedCharacter;
                if (i == cycle.length() - 1) {
                    permutedCharacter = cycle.charAt(0);
                } else {
                    permutedCharacter = cycle.charAt(i + 1);
                }
                return _alphabet.toInt(permutedCharacter);
            }
        }
        return wrap(p);
    }


    /** Return the index result of applying the inverse of this permutation
     *  to the character at index C in ALPHABET. */
    public int invert(int c) {
        char character = _alphabet.toChar(wrap(c));
        for (String cycle : _permutation) {
            if (cycle.indexOf(character) != -1) {
                int i = cycle.indexOf(character);
                char permutedCharacter;
                if (i == 0) {
                    permutedCharacter = cycle.charAt(cycle.length() - 1);
                } else {
                    permutedCharacter = cycle.charAt(i - 1);
                }
                return _alphabet.toInt(permutedCharacter);
            }
        }
        return wrap(c);
    }

    /** Return the character result of applying this permutation to the index
     * of character P in ALPHABET. */
    public char permute(char p) {
    	return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the character result of applying the inverse of this permutation
	 * to the index of character P in ALPHABET. */
    public char invert(char c) {
    	return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    public Alphabet alphabet() {
        return _alphabet;
    }



    // Some starter code for unit tests. Feel free to change these up!
    // To run this through command line, from the proj0 directory, run the following:
    // javac enigma/Permutation.java enigma/Alphabet.java enigma/CharacterRange.java enigma/EnigmaException.java
    // java enigma/Permutation
    public static void main(String[] args) {
        Permutation perm = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", new CharacterRange('A', 'Z'));
        System.out.println(perm.size() == 26);
        System.out.println(perm.permute('A') == 'B');
        System.out.println(perm.invert('B') == 'A');
        System.out.println(perm.permute(0) == 1);
        System.out.println(perm.invert(1) == 0);
    }
}
