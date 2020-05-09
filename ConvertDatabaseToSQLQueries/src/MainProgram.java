import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class MainProgram {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private MainProgram() {
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code SEPARATORS}) or "separator string" (maximal length string of
     * characters in {@code SEPARATORS}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            string with all the separators
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection entries(SEPARATORS) = {}
     * then
     *   entries(nextWordOrSeparator) intersection entries(SEPARATORS) = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection entries(SEPARATORS) /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of entries(SEPARATORS)  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of entries(SEPARATORS))
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            String separators) {
        assert text != null : "Violation of: text is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        int counter = position;

        if (separators.indexOf(text.charAt(counter)) != -1) {
            while (counter < text.length()
                    && separators.indexOf(text.charAt(counter)) != -1) {
                counter++;
            }
        } else {
            while (counter < text.length()
                    && (separators.indexOf(text.charAt(counter)) == -1)) {
                counter++;
            }
        }
        return text.substring(position, counter);
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        out.println("Enter input file source: ");
        String nextLine = in.nextLine();
        SimpleReader inputFile = new SimpleReader1L(nextLine);
        out.println("Enter output file source: ");
        String outputFileName = in.nextLine();
        out.println("Enter the table name: ");
        String tableName = in.nextLine();
        SimpleWriter outputFile = new SimpleWriter1L(outputFileName);

        String separators = "\t";
        while (!inputFile.atEOS()) {
            String sqlQuery = "INSERT INTO " + tableName + " Values (";
            int position = 0;
            String line = inputFile.nextLine();
            while (position < line.length()) {
                String nextWordSeparator = nextWordOrSeparator(line, position,
                        separators);
                position += nextWordSeparator.length();
                if (!(separators.indexOf(nextWordSeparator.charAt(0)) != -1)) {
                    if (position < line.length()) {
                        sqlQuery += "'";
                        sqlQuery += nextWordSeparator + "', ";
                    } else {
                        sqlQuery += "'";
                        sqlQuery += nextWordSeparator + "');";
                    }
                }
            }
            outputFile.println(sqlQuery);
        }
        out.println("Program done!");
        in.close();
        out.close();
    }
}
