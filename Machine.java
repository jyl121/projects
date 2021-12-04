package enigma;

import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Jiyoon Lee
 */
public class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls. ALLROTORS contains all the
     *  available rotors. */
    int _numRotors;
    int _pawls;
    Rotor[] _rotors;
    private Permutation _plugboard;
    private Rotor[] _myRotors;
    public Machine(Alphabet alpha, int numRotors, int pawls,
            Rotor[] allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _rotors = allRotors;
        _plugboard = null;
        _myRotors = new Rotor[numRotors];
    }

    /** Return the number of rotor slots I have. */
    public int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    public int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    public void insertRotors(String[] rotors) {
        _myRotors = new Rotor[numRotors()];

        int index = 0;
        for (String rotorString : rotors) {
            for (Rotor rotor : _rotors) {
                if (rotor.name().toUpperCase().equals(rotorString.toUpperCase())) {
                    rotor.set(0);
                    _myRotors[index] = rotor;
                    index++;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    public void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i++) {
            char corresponding = setting.charAt(i - 1);
            int config = _alphabet.toInt(corresponding);
            _myRotors[i].set(config);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    public void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    public int convert(int c) {
    	advance();
    	if (_plugboard != null) {
    	    c = _plugboard.permute(c);
        }
    	for (int i = numRotors() - 1; i >= 0; i--) {
    	    c = _myRotors[i].convertForward(c);
        }
    	for (int i = 1; i < numRotors(); i++) {
    	    c = _myRotors[i].convertBackward(c);
        }
    	if (_plugboard != null) {
    	    c = _plugboard.invert(c);
        }
    	return c;
    }

    /** Optional helper method for convert() which rotates the necessary Rotors. */
    private void advance() {
    	Boolean[] shouldAdvance = new Boolean[numRotors()];
        Arrays.fill(shouldAdvance, false);
        shouldAdvance[numRotors() - 1] = true;
    	for (int i = numRotors() - 2; _myRotors[i].rotates(); i--) {
            Rotor right = _myRotors[i + 1];
            if (right.atNotch()) {
                shouldAdvance[i] = true;
                shouldAdvance[i + 1] = true;
            }
        }
    	for (int i = 0; i < numRotors(); i++) {
    	    if (shouldAdvance[i]) {
    	        _myRotors[i].advance();
            }
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    public String convert(String msg) {
        msg = msg.toUpperCase();
        char[] msgCharacters = msg.toCharArray();
        for (int i = 0; i < msgCharacters.length; i++) {
            char character = msgCharacters[i];
            if (_alphabet.contains(character)) {
                int converted = convert(_alphabet.toInt(character));
                msgCharacters[i] = _alphabet.toChar(converted);
            } else {
                msgCharacters[i] = ' ';
            }
        }
        String convertedMsg = new String(msgCharacters);
        return convertedMsg.replaceAll("\\s", "");
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    // To run this through command line, from the proj0 directory, run the following:
    // javac enigma/Machine.java enigma/Rotor.java enigma/FixedRotor.java enigma/Reflector.java enigma/MovingRotor.java enigma/Permutation.java enigma/Alphabet.java enigma/CharacterRange.java enigma/EnigmaException.java
    // java enigma/Machine
    public static void main(String[] args) {

        CharacterRange upper = new CharacterRange('A', 'Z');
        MovingRotor rotorI = new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", upper),
                "Q");
        MovingRotor rotorII = new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", upper),
                "E");
        MovingRotor rotorIII = new MovingRotor("III",
                new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", upper),
                "V");
        MovingRotor rotorIV = new MovingRotor("IV",
                new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", upper),
                "J");
        MovingRotor rotorV = new MovingRotor("V",
                new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", upper),
                "Z");
        FixedRotor rotorBeta = new FixedRotor("Beta",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", upper));
        FixedRotor rotorGamma = new FixedRotor("Gamma",
                new Permutation("(AFNIRLBSQWVXGUZDKMTPCOYJHE)", upper));
        Reflector rotorB = new Reflector("B",
                new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", upper));
        Reflector rotorC = new Reflector("C",
                new Permutation("(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) (PW) (QZ) (SX) (UY)", upper));

        Rotor[] allRotors = new Rotor[9];
        allRotors[0] = rotorI;
        allRotors[1] = rotorII;
        allRotors[2] = rotorIII;
        allRotors[3] = rotorIV;
        allRotors[4] = rotorV;
        allRotors[5] = rotorBeta;
        allRotors[6] = rotorGamma;
        allRotors[7] = rotorB;
        allRotors[8] = rotorC;

        Machine machine = new Machine(upper, 5, 3, allRotors);
        machine.insertRotors(new String[]{"B", "BETA", "III", "IV", "I"});
        machine.setRotors("AXLE");
        machine.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", upper));

        System.out.println(machine.numRotors() == 5);
        System.out.println(machine.numPawls() == 3);
        System.out.println(machine.convert(5) == 16);
        System.out.println(machine.convert(17) == 21);
        System.out.println(machine.convert("OMHISSHOULDERHIAWATHA").equals("PQSOKOILPUBKJZPISFXDW"));
        System.out.println(machine.convert("TOOK THE CAMERA OF ROSEWOOD").equals("BHCNSCXNUOAATZXSRCFYDGU"));
        System.out.println(machine.convert("Made of sliding folding rosewood").equals("FLPNXGXIXTYJUJRCAUGEUNCFMKUF"));
    }
}
