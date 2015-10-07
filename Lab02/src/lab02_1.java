import java.util.*;
import java.io.*;

public class lab02_1 {
	/*
	 * Tasks:
	 * 1. main function to take input, ask for word to change, run two functions, then output result
	 * 2. function one: change bracket style
	 * 3. function two: change word
	 */

    public static void main(String[] args) throws RuntimeException {
		/*
		 * 1. Ask for file
		 * 2. Open file
		 * 3. Ask for word to change
		 * 4. Ask for word to change to
		 * 5. Pass through line changer
		 * 6. Pass through word changer
		 * 7. Output result
		 */

        Scanner in = new Scanner(System.in);
        File fileLocation = null;
        ArrayList<String> file = null;
        ArrayList<String> newFile = null;
        String wordFrom;
        String wordTo;
        int fails = 0;
        boolean canContinue = false;
        final int MAX_FAILS = 10;

        do {
            try {
                System.out.print("File location: ");
                fileLocation = new File(in.nextLine());

                file = file_parse(fileLocation);
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found. Please try again.");
            } catch (IOException e) {
                System.out.println("Error: " + e.toString() + "Trying again.");
                try {
                    file = file_parse(fileLocation);
                } catch (IOException ex) {
                    System.out.println("Error: File Parse failed again with a " + e.toString() + ", try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: Somehow, we got a " + e.toString() + ". Try again.");
            }
        } while (file == null);

        System.out.print("Which word would you like to change?: ");
        wordFrom = in.nextLine();

        System.out.print("Which word would you like to change to?: ");
        wordTo = in.nextLine();

        do {
            try {
                newFile = change_line(file);
                newFile = change_word(newFile, wordFrom, wordTo);
                canContinue = true;
            } catch (Exception e) {
                System.out.println("Error: Line ending change failed with a " + e.toString() + ". Trying again.");
                if (++fails >= MAX_FAILS) {
                    System.out.println("Fatal: Operation has failed too many times.");
                    throw new RuntimeException();
                }
            }
        } while (!canContinue);

        //Output array, then save to new file
        //Alternatively, rename old file, then save to original
        File oldLocation = new File(fileLocation.toString() + ".old");
        int i = 1;
        while (oldLocation.exists()) {
            oldLocation = new File(fileLocation.toString() + ".old" + i++);
        }
        fileLocation.renameTo(oldLocation);
        System.out.println("The old file has been saved as " + oldLocation.toString());
        i = 0;
        canContinue = false;
        do {
            try {
                //RandomAccessFile fileOut = new RandomAccessFile(fileLocation, "rw");
                PrintWriter fileOut = new PrintWriter(fileLocation);
                System.out.println("Your file now reads:\n");
                for (String line : newFile) {
                    System.out.println(line);
                    //fileOut.writeUTF(line + "\n");
                    fileOut.write(line + "\n");
                }
                fileOut.close();
                canContinue = true;
            } catch (Exception e) {
                if (i++ <= 10) {
                    System.out.println("Error: " + e.toString() + ". Trying again.");
                } else {
                    System.out.println("Error: complete failure. Reverting changes.");
                    oldLocation.renameTo(fileLocation);
                    break;
                }
            }
        } while (!canContinue);
    }

    public static ArrayList<String> file_parse(File fileLocation) throws IOException {
        RandomAccessFile fileInput = new RandomAccessFile(fileLocation, "r");
        ArrayList<String> file = new ArrayList<String>();

        String line;
        while ((line = fileInput.readLine()) != null) {//Not sure this will work
            file.add(line);
        }

        fileInput.close();

        return file;
    }

    public static Boolean odd_quotes(String line) {
        int total = 0;
        boolean escaped = false;
        for (char character : line.toCharArray()) {
            if (escaped){
                escaped = false;
            }
            else if (character == '\\'){
                escaped = true;
            }
            else if (character == '"') {
                total++;
            }
        }
        return total % 2 == 1;
    }

    public static ArrayList<String> change_line(ArrayList<String> file) {//I really hope this works
        String line;
        String[] lines;
        int i = 0;
        String temp;
        String whiteSpace;

        while (i < file.size()) {
            line = file.get(i);
            /* Problems with this version:
                1. Doesn't handle comments, especially not ones ending in {
                2. Doesn't tab { on newline
                3. Doesn't remove excess whitespace on old line
                4. Multiple { on same line
                5. { is first char
                Furthermore, something is going wrong with random chars at the beginning of many lines, no idea where that error is coming from
            if (line.endsWith("{")){
                file.set(i, line.substring(0, line.length()-1));//Remove { from end of line
                file.add(++i, "{");//Insert newline containing {
            }*/
            if (line.matches("^\\s*(\\/\\{|([^\\s\\/\\{]|\\/[^\\/\\{])(\\/?[^\\/\\{])*\\/?\\{).*$")) {//.contains("{")){
                whiteSpace = "";
                for (char character : line.toCharArray()) {//Copy initial whitespace
                    if (Character.isWhitespace(character)) {
                        whiteSpace += character;
                    } else {
                        break;
                    }
                }
                if (line.matches(".*\\{([^\\/]|\\/[^\\/])*\\{.*")) {//Multiple {s, need to be careful
                    lines = line.split("\\{");
                    int j = 0;
                    if (odd_quotes(lines[j])) {//Test for quotes
                        temp = lines[j] + "{";
                        while (!odd_quotes(lines[++j])) {
                            temp += lines[j] + "{";
                        }
                        temp += lines[j];
                        if (lines[j].contains("//")){
                            for (++j; j < lines.length; j++) {
                                temp += "{" + lines[j];
                            }
                        }
                        j++;
                        file.set(i, temp);
                    }
                    else {
                        file.set(i, lines[0]);
                    }
                    for (; j < lines.length; j++) {
                        if (odd_quotes(lines[j])) {//Test for quotes
                            temp = lines[j] + "{";
                            while (!odd_quotes(lines[++j])) {
                                temp += lines[j] + "{";
                            }
                            temp += lines[j];
                            file.add(++i, whiteSpace + "{" + temp);
                        }
                        else if (!lines[j].contains("//")) {//Not a comment
                            file.add(++i, whiteSpace + "{" + lines[j]);
                        }
                        else {//The rest is a comment, no need to make newlines
                            temp = "";
                            for (; j < lines.length - 1; j++) {
                                temp += lines[j] + "{";
                            }
                            temp += lines[j];
                            file.add(++i, whiteSpace + "{" + temp);
                            //break;
                        }
                    }
                }
                else {//Only one {, we can safely ignore comments
                    lines = line.split("\\{", 2);
                    if (odd_quotes(lines[0])) {//test for quotes
                        i++;
                        continue;
                    }
                    file.set(i, lines[0]);//gets rid of {+
                    if (lines.length < 2) {
                        file.add(++i, whiteSpace + "{");
                    } else {
                        file.add(++i, whiteSpace + "{" + lines[1]);//Moves {+ to new line
                    }
                }
            }
            else if (line.matches("^([^\\/]|\\/[^\\/])*\\\"[^\\\"]*\\/\\/[^\\\"]*\\\"([^\\/\\{]|\\/[^\\/\\{])*\\{.*$")){//One { following a // in quotes
                //First block checks to see if there are odd " surrounding the //
                lines = line.split("//");
                if (!odd_quotes(lines[0])){
                    break;
                }
                for (int j=1;j<lines.length;j++){
                    if (odd_quotes(lines[j])){
                        //Check for {, if present split on it, otherwise ignore
                        if (lines[j].contains("{")){

                        }

                    }
                    else if (lines[j].matches("\"")){
                        //Even quotes, could end this one and start another
                        //We need to check for { outside of any quotes
                    }
                }
            }
            i++;
        }
        return file;
    }

    public static ArrayList<String> change_word(ArrayList<String> file, String from, String to) {
        for (int i = 0; i < file.size(); i++) {
            //Find all instances of from, change to to
            file.set(i, file.get(i).replaceAll(from, to));
        }
        return file;
    }
}