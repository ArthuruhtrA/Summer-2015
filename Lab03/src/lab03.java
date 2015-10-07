import java.io.File;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ari Sanders on 29-Jun-15.
 */
public class lab03 {
    static MTQueue scheduler;
    final static int MAX_RECORDS = 20;
    final static int MAX_RECLEN = 73;
    static boolean failed = true;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        File fileLocation;
        Updater updater = new Updater(null);
        boolean canContinue = false;
        String[] playerData = new String[5];
        int tempInt;

        System.out.println("Welcome to Sports Database.");
        scheduler = new MTQueue();
        do {
            try {
                System.out.print("File location:\n>");
                fileLocation = new File(in.nextLine());
                updater = new Updater(fileLocation);
                updater.start();
                if (failed){
                    TimeUnit.MILLISECONDS.sleep(100);
                    if (failed) {
                        TimeUnit.SECONDS.sleep(1);
                        if (failed) {
                            throw new IOException();//I figure after over a second, it should be up. Else it failed.
                        }
                    }
                }
                canContinue = true;
            }
            catch (IOException e){
                System.out.println("Error: Cannot access file. Please try again.");
            }
            catch (Exception e) {
                System.out.println("Error: Somehow, we got a " + e.toString() + ". Try again.");
            }
        } while (!canContinue);

        while(true){
            System.out.print("Please enter one of the following commands:\n(N)ew\n(E)nd\n>");
            switch (in.nextLine().toLowerCase().charAt(0)){
                case 'n':
                    //make a new record
                    canContinue = false;
                    while (!canContinue) {
                        System.out.print("Player ID (1-" + MAX_RECORDS + "): ");
                        playerData[0] = in.nextLine();
                        try {
                            tempInt = Integer.parseInt(playerData[0]);
                            if (tempInt > 0 && tempInt <= MAX_RECORDS) {
                                canContinue = true;
                            } else {
                                throw new NumberFormatException();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input. Must be an integer from 1 to " + MAX_RECORDS + ".");
                            continue;
                        }
                    }
                    System.out.print("Player name: ");
                    playerData[1] = in.nextLine();
                    if (playerData[1].length() > 26) {
                        System.out.println("Warning: Name will be truncated to 26 characters.");
                        playerData[1] = playerData[1].substring(0, 26);
                        System.out.println("Name will be: " + playerData[1]);
                    }
                    System.out.print("Team name: ");
                    playerData[2] = in.nextLine();
                    if (playerData[2].length() > 26) {
                        System.out.println("Warning: Name will be truncated to 26 characters.");
                        playerData[2] = playerData[2].substring(0, 26);
                        System.out.println("Name will be: " + playerData[2]);
                    }
                    canContinue = false;
                    while (!canContinue) {
                        System.out.print("Skill level (0-99): ");
                        playerData[3] = in.nextLine();
                        try {
                            tempInt = Integer.parseInt(playerData[3]);
                            if (tempInt >= 0 && tempInt < 100) {
                                canContinue = true;
                            } else {
                                throw new NumberFormatException();
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input. Must be an integer from 0 to 99.");
                        }
                    }
                    canContinue = false;
                    while (!canContinue) {
                        System.out.print("Draft date (ex: 25Jun2014): ");
                        playerData[4] = in.nextLine();
                        if (playerData[4].length() == 9) {
                            canContinue = true;
                        } else {
                            System.out.println("Invalid input. Must be in the format DDMMMYYYY.");
                        }
                    }
                    scheduler.MTPut(format("5%26%26%5%9", playerData));
                    System.out.println("Update has been scheduled.");
                    break;
                case 'e':
                    updater.interrupt();
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    public static String format(String format, String[] args) {
        //This function intentionally poorly written
        String[] formatArray = format.split("%");
        String out = "";
        int difference;
        for (int i = 0; i < formatArray.length; i++) {
            //Pad args[i] to the length specified in formatArray[i]
            difference = Integer.parseInt(formatArray[i]) - args[i].length();
            if (difference > 0) {
                out += args[i];
                for (int j = 0; j < difference; j++) {
                    out += ' ';
                }
            } else if (difference < 0) {//Should never get here, but can't hurt to do this anyway
                out += args[i].substring(0, Integer.parseInt(formatArray[i]) - 1);
            } else {
                out += args[i];
            }
        }
        return out;
    }

    static class Updater extends Thread {
        private File fileLocation;

        public Updater(File file) {
            this.fileLocation = file;
        }

        @Override
        public void run() {
            boolean newFile;
            RandomAccessFile file;

            try {
                newFile = !fileLocation.exists();
                file = new RandomAccessFile(fileLocation, "rw");
                if (newFile) {
                    for (int i = 0; i < MAX_RECORDS * MAX_RECLEN; i++) {
                        file.writeByte(0);
                    }
                }
                failed = false;
            }
            catch (IOException e){
                failed = true;
                return;
            }

            String player;
            while (!isInterrupted()){
                //Do work
                player = scheduler.MTGet();
                int location;
                while (player != null) {
                    location = MAX_RECLEN * (Integer.parseInt(player.substring(0, 5).trim()) - 1);
                    try {
                        file.seek(location);
                        file.writeUTF(player);
                    }
                    catch (IOException e) {
                        System.out.println("Error: File write failed. Recommend restarting program.");
                    }
                    try {
                        file.seek(location);
                        player = file.readUTF();
                        System.out.println("\nWrote player to file:");
                        System.out.println("Player ID: " + player.substring(0, 5).trim());//5
                        System.out.println("Player name: " + player.substring(5, 31).trim());//26
                        System.out.println("Team name: " + player.substring(31, 57).trim());//26
                        System.out.println("Skill level: " + player.substring(57, 62).trim());//5
                        System.out.println("Draft date: " + player.substring(62, 64).trim()
                                + " " + player.substring(64, 67)
                                + " " + player.substring(67, 71).trim());//9
                        System.out.print("\n>");
                    }
                    catch (IOException e){
                        System.out.println("Error: Failed to read player data.");
                    }
                    player = scheduler.MTGet();
                }
                try {
                    sleep(4000);//People don't input data instantaneously
                }
                catch (InterruptedException e){
                    break;
                }
            }

            //Cleanup
            try {
                file.close();
            }
            catch (Exception e) {
                System.out.println("Error: Failed to close file.");
                try {
                    file.close();
                } catch (Exception ex) {
                    System.out.println("Error: Failed to close file again.");
                    throw new IOError(ex);
                }
            }
        }
    }
}
