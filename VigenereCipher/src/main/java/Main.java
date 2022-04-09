import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static ArrayList<Double> englishProbability = generateEnglishProbabilities();
    static String filteredText;
    static String encryptedText;
    static int length;
    static String randomKey;

    /**
     * Method to find user's option:
     * Decrypt without knowing the key.
     * Decrypt with a known key.
     */
/* CHOOSING OPTIONS */
    public static void chooseEncryptOrDecrypt(String option) {
        if(option.equals("1")) { //text is already encrypted
            encryptedText = filteredText;
            length = encryptedText.length();
            System.out.println("Length of text: " + length);

            chooseDecryptionOption();
        }
        else if(option.equals("2")) { //text needs encryption
            randomKey = getKey(); // 1-give key   or  2-generate random key
            if(randomKey.equals("Incorrect option"))
            {
                System.out.println("Incorrect option");
                System.exit(0);
            }
            System.out.println("KeyLength: " + randomKey.length() + '\n');

            encryptedText = encryptingText(filteredText,randomKey);
            System.out.println("Encrypted text: " + encryptedText);

            length = encryptedText.length();
            System.out.println("Length of text: " + length);

            chooseDecryptionOption();
        }
        else {
            System.out.println("Incorrect option");
            System.exit(0);
        }
    }

    public static void chooseDecryptionOption() {
        System.out.println("1 - to decrypt without the key");
        System.out.println("2 - to decrypt with a given key");
        System.out.print("Enter option: ");
        Scanner obj = new Scanner(System.in);
        String input = obj.nextLine();

        if(input.equals("1")) {
            ArrayList<Integer> frequency = generateFrequencyList(encryptedText);

            /*
            ArrayList<Double> probability = generateLettersProbability(frequency,length);
            double MR = measureOfRoughness(probability);
            System.out.println("MR: " + MR );
             */

            System.out.println("\nFinding Key length...");
            int keyLength = findKeyLength(encryptedText,0.015);
            System.out.println("Found KeyLength of: " + keyLength + '\n');

            //if(keyLength == randomKey.length()) {
            String foundKey = findKeyFromEncrypted(encryptedText,keyLength);
            System.out.println("Key found from encrypted text: " + foundKey);

            String decryptedText = decryptingText(encryptedText,foundKey);
            System.out.println("Decrypted text: " + decryptedText);
            //}
        }
        else if(input.equals("2")) {
            System.out.print("Enter your key: ");
            Scanner obj1 = new Scanner(System.in);
            String inputKey = obj1.nextLine();

            System.out.println("Decrypting text with " + "\"" + inputKey + "\":");
            String decryptedText = decryptingText(encryptedText,inputKey);
            System.out.println("Decrypted text: " + decryptedText);
        }
        else {
            System.out.println("Invalid Option!");
            System.exit(0);
        }
    }

    public static ArrayList<Double> generateEnglishProbabilities() {
        ArrayList<Double> probability = new ArrayList<>();
        probability.add(0, 8.55/100);   // A
        probability.add(1, 1.60/100);   // B
        probability.add(2, 3.16/100);   // C
        probability.add(3, 3.87/100);   // D
        probability.add(4, 12.10/100);  // E
        probability.add(5, 2.18/100);   // F
        probability.add(6, 2.09/100);   // G
        probability.add(7, 4.96/100);   // H
        probability.add(8, 7.33/100);   // I
        probability.add(9, 0.22/100);   // J
        probability.add(10, 0.0081);    // K
        probability.add(11, 4.21/100);  // L
        probability.add(12, 2.53/100);  // M
        probability.add(13, 7.17/100);  // N
        probability.add(14, 7.47/100);  // O
        probability.add(15, 2.07/100);  // P
        probability.add(16, 0.10/100);  // Q
        probability.add(17, 6.33/100);  // R
        probability.add(18, 6.73/100);  // S
        probability.add(19, 8.94/100);  // T
        probability.add(20, 2.68/100);  // U
        probability.add(21, 1.06/100);  // V
        probability.add(22, 1.83/100);  // W
        probability.add(23, 0.19/100);  // X
        probability.add(24, 1.72/100);  // Y
        probability.add(25, 0.11/100);  // Z

        return probability;
    }

    /**
     * Method for reading from a .txt file
     *
     * @param fileName      The name of the file from which the plain text is extracted.
     * @return              Returns the filtered text (only uppercase letters, without spaces or other characters).
     */
    public static String readFromFile(String fileName) {
        try {
            FileReader fr = new FileReader(fileName);
            int i;
            char character;

            StringBuilder build = new StringBuilder();

            while((i = fr.read()) != -1) {
                character = (char)i;

                if(character >= 'a' && character <= 'z') {
                    character = Character.toUpperCase(character);
                    build.append(character);
                }
                else if (character >= 'A' && character <= 'Z') {
                    build.append(character);
                }
            }
            fr.close();
            return build.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR!";
    }

/* GENERATE OR GIVE KEY */
    /**
     * This method randomly generates a char array which contains a random word with letters between A and Z.
     * The generated key is further used for encryption.
     *
     * @return     the random generated key with length between 3 and 10
     */
    public static String generateKey() {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        Random r = new Random();
        int randomLength;
        while((randomLength = r.nextInt(11)) < 3) {
            randomLength = r.nextInt(11);
        }

        StringBuilder randomKey = new StringBuilder();
        for (int i = 0; i < randomLength; i++) {
            int randomIndex = r.nextInt(25);
            randomKey.append(alphabet[randomIndex]);
        }
        return randomKey.toString();
    }

    /**
     * Method used for reading a certain key from stdin.
     * @return  the key read from the keyboard.
     */
    public static String giveKey() {
        Scanner obj = new Scanner(System.in);
        System.out.println("Enter a key with length between 3 and 10");
        System.out.print("Key: ");

        return obj.nextLine();
    }

    /**
     * Method used to decide whether the key is generated of read from stdin.
     * @return  the generated/read key.
     */
    public static String getKey() {
        System.out.println("1 - to enter a key");
        System.out.println("2 - to generate a random key");
        System.out.print("Enter option: ");
        Scanner obj = new Scanner(System.in);
        String input = obj.nextLine();

        if(input.equals("1")) {
            String randomKey = giveKey();
            System.out.print("Given key: ");
            System.out.println(randomKey);
            return randomKey;
        }
        else if(input.equals("2")) {
            String randomKey = generateKey();
            System.out.print("Generated key: ");
            System.out.println(randomKey);
            return randomKey;
        }
        else {
            return "Incorrect option";
        }
    }

/* ENCRYPTING TEXT */
    /**
     * Method used for encrypting a text.
     *
     * Each letter is transformed into the ASCII value by which the value of the letter 'A' is subtracted.
     * In this way we work with the values 0-25 to calculate how much the filtered text shifts and in which letter the current letter is transformed.
     * Example:     HELLO
     *              BC
     * [('H'-65) + ('B'-65)] % 26 = (7+1)%26 = 8.
     * 8 + 65 = 73 (='I')       H is shifted with 1 position (H -> I).
     *
     * @param filteredText      the filtered text
     * @param key               the given key used for encryption
     * @return                  the encrypted text
     */
    public static String encryptingText(String filteredText, String key) {
        StringBuilder text = new StringBuilder(filteredText);
        int r = 0;
        int textLength = text.length();
        int keyLength = key.length();

        for(int i = 0; i < textLength; i++) {
            int result = ((text.charAt(i) - 65) + (key.charAt(r) - 65)) % 26;
            result += 65;

            text.setCharAt(i,(char)result);
            r = (r + 1) % keyLength;
        }
        return text.toString();
    }

/* DECRYPTING TEXT */
    public static String decryptingText(String encryptedText, String key) {
        StringBuilder decryptedText = new StringBuilder(encryptedText);
        int keyLength = key.length();
        int textLength = encryptedText.length();
        int r = 0;

        for(int i = 0; i < textLength; i++) {
            int result = (encryptedText.charAt(i) - 65) - (key.charAt(r) - 65);

            if(result < 0) {
                result = 26 + result;
            }
            else {
                result = result % 26;
            }
            result = result + 65;
            decryptedText.setCharAt(i,(char)result);
            r = (r+1) % keyLength;
        }
        return decryptedText.toString();
    }

    public static ArrayList<Integer> emptyFrequency() {
        ArrayList<Integer> frequency = new ArrayList<>();
        for(int i = 0; i < 26; i++) {
            frequency.add(0);
        }
        return frequency;
    }

    /**
     * Method used for generating a frequency array for each letter ('A' -> 'Z') in the text.
     *
     * @param text     the used text
     * @return         the frequency array
     */
    public static ArrayList<Integer> generateFrequencyList(String text) {
        int length = text.length();

        ArrayList<Integer> frequency = emptyFrequency();
        for(int i = 0 ; i < length; i++) {
            int index = ((int)text.charAt(i) - 65);
            int oldValue = frequency.get(index);
            frequency.set(index,oldValue + 1);
        }
        return frequency;
    }

    public static ArrayList<Double> emptyProbability() {
        ArrayList<Double> probability = new ArrayList<>();
        for(int i = 0; i < 26; i++) {
            probability.add((double) 0);
        }
        return probability;
    }

    /**
     * Method used to calculate the probability that a letter will appear in the text.
     * Formula used: Prob(i) = freq(i) / textLength
     *
     * @param frequency     The frequency array
     * @param textLength    The text length
     * @return              The probability array
     */
    public static ArrayList<Double> generateLettersProbability(ArrayList<Integer> frequency, int textLength) {
        ArrayList<Double> probability = emptyProbability();
        int index = 0;
        for(Double prob : probability) {
            double result = (double)frequency.get(index) / textLength;
            probability.set(index,result);
            index++;
        }
        return probability;
    }

/* INDEX OF COINCIDENCE ALGORITHM */
    /**
     * Method used for calculating the Index of Coincidence.
     * Sum of [(freq(i) * (freq(i)-1)) / (textLength * (textLength-1))], where i is 'A', 'B', ... 'Z'.
     *
     * @param frequency     the frequency array
     * @param textLength    the text length
     * @return              the value of Index of Coincidence
     */
    public static double indexOfCoincidence(ArrayList<Integer> frequency, int textLength){
        double sum = 0.0;

        for(Integer freq : frequency) {
            double product1 = freq * (freq-1);
            double product2 = textLength * (textLength-1);
            sum = sum + (product1 / product2);
        }

        return sum;
    }

    /**
     * Method used to calculate the measure of roughness for the Index of coincidence of a given text.
     * Formula used: MR=[Sum[prob(i)^2] - 1/26], i = 'A',...,'Z'
     *
     * Not working in all cases!
     *
     * @param probability   The probability array of letters
     * @return              The value of measure of roughness
     */
    public static double measureOfRoughness(ArrayList<Double> probability) {
        double sumOfProbability = 0;

        for(Double prob : probability) {
            double power = prob * prob;
            sumOfProbability = sumOfProbability + power;
        }

        return sumOfProbability - (double)1/26;
    }

/* SUBSTRING GENERATION FROM INDEX TO INDEX */
    /**
     * Method used to generate the substring obtained from shiftingIndex to shiftingIndex positions starting with the specified index.
     *
     * @param text              The encrypted text.
     * @param shiftingIndex     The shifting value.
     * @param startingIndex     The starting index of the text. In general, the variable takes values between 0 and (shiftingIndex - 1).
     * @return                  The generated substring.
     */
    public static String generateShiftedString(String text, int shiftingIndex, int startingIndex) {
        StringBuilder subString = new StringBuilder();
        int length = text.length();

        for(int i = startingIndex; i < length; i += shiftingIndex) {
            subString.append(text.charAt(i));
        }

        return subString.toString();
    }

/* FINDING KEY LENGTH ALGORITHM USING INDEX OF COINCIDENCE*/
    public static int findKeyLength(String encryptedText, double measureOfRoughness) {

        double infLimit = 0.0650 - measureOfRoughness;
        double supLimit = 0.0650 + measureOfRoughness;

        System.out.println("Inf= " + infLimit + " ; Sup= " + supLimit);

        int keyLength = 2;

        do {
            boolean ok = true;
            keyLength++;
            //System.out.println("[Current Key length " + keyLength + "]");

            for(int index = 0; index < keyLength; index++) {
                String substring = generateShiftedString(encryptedText,keyLength, index);
                int lengthSubstring = substring.length();

                ArrayList<Integer> frequencySubstring = generateFrequencyList(substring);
                double iocSubstr = indexOfCoincidence(frequencySubstring,lengthSubstring);

                /*
                System.out.print("IOC(substring "+ (index + 1) + ") : " + iocSubstr);
                System.out.println();
                 */

                if(iocSubstr < infLimit || iocSubstr > supLimit) {
                    ok = false;
                    break;
                }
            }
            if(ok) {
                break;
            }
        }while(keyLength < 10);

        return keyLength;
    }

/* MUTUAL INDEX OF COINCIDENCE ALGORITHM */
    public static double mutualIndexOfCoincidence(String text) {
        double mIC = 0;
        int textLength = text.length();

        ArrayList<Integer> frequency = generateFrequencyList(text);

        for(int i = 0; i < 26; i++) {
            double fraction = (double)frequency.get(i) / textLength;
            mIC = mIC + (englishProbability.get(i) * fraction);
        }
        return mIC;
    }

    /**
     * Method used to shift text characters with s positions
     * @param shiftedText   Text
     * @param s             Value
     * @return              Shifted text
     */
    public static String changeShiftedText(String shiftedText, int s) {
        int textLength = shiftedText.length();
        StringBuilder newText = new StringBuilder(shiftedText);
        int result;

        for(int i = 0; i < textLength; i++) {
            result = ((shiftedText.charAt(i) - 65) + s) % 26;
            result += 65;

            newText.setCharAt(i,(char)result);
        }
        return newText.toString();
    }

/* FINDING KEY ALGORITHM USING KEY LENGTH AND MUTUAL INDEX OF COINCIDENCE */
    public static String findKeyFromEncrypted(String encryptedText, int keyLength) {
        StringBuilder foundKey = new StringBuilder();

       for(int j = 0; j < keyLength; j++) {
            int s = -1;
            boolean ok = false;

            int letter;
            do {
                s = s + 1;

                String shiftedText = generateShiftedString(encryptedText, keyLength, j);
                shiftedText = changeShiftedText(shiftedText, s);

                double mIC = mutualIndexOfCoincidence(shiftedText);

                letter = (26 - s) % 26;
                //System.out.println("Letter " + (char)(letter + 65) + " ->  MIC= " + mIC);

                if(mIC > 0.055 && mIC < 0.75) {
                    ok = true;
                }

            }while(!ok);
            foundKey.append((char)(letter + 65));
       }
       return foundKey.toString();
    }

    public static void main(String[] args) {
        System.out.print("Enter file name: ");
        Scanner obj = new Scanner(System.in);
        String fileName = obj.nextLine();

        System.out.println("[Reading text from " + fileName + "]\n");
        filteredText = readFromFile(fileName); // reading and filtering text at the same time

        if (filteredText.equals("Error!")) {
            System.out.println("File couldn't be opened");
            System.exit(0);
        }
        else {
            System.out.print("Filtered text: ");
            System.out.println(filteredText);

            System.out.println("If filtered text is already encrypted, enter 1 for decryption with a known key. Enter 2, otherwise. ");
            System.out.print("Enter option: ");
            obj = new Scanner(System.in);
            String option = obj.nextLine();

            chooseEncryptOrDecrypt(option);
        }
    }
}