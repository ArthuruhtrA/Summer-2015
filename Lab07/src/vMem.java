import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Ari Sanders on 19-Jul-15.
 */
public class vMem {
    private static int pageSize;
    private static int pageSize8;
    private static int swapSize;
    private static long[] mem;
    private static int page = 0;
    private static RandomAccessFile vmem;

    /**
     * Constructor for vMem
     * @param swapSize How big the swap file should be
     * @param ratio The ratio of pages to total space
     * @throws IOException IO errors are fatal here, so they are passed up to the caller
     */
    public vMem(int swapSize, int ratio) throws IOException{
        this.pageSize = swapSize / ratio;
        this.pageSize8 = pageSize * 8;
        this.swapSize = swapSize;
        mem = new long[pageSize];
        File file = new File("vMem");

        //Both of these functions can generate exceptions, but I have opted to pass them up to the caller, as they are fatal errors.
        vmem = new RandomAccessFile(file, "rw");
        vmem.writeByte(swapSize);
    }

    /**
     * Loads the page containing the requested value, storing the current page first if necessary
     * @param index An index on the page to load
     */
    private static void load(int index){
        if (index < 0 || index >= swapSize){
            throw new IndexOutOfBoundsException();
        }
        if (index >= page * pageSize && index < page * pageSize + pageSize){
            return;//Index is already in current page
        }
        //Write current page to swap
        try {
            vmem.seek(page * pageSize8);
//            for (long element : mem) {
//                /*if (element != vmem.readLong()) {
//                    vmem.seek(vmem.getFilePointer() - 8);
//                    vmem.writeLong(element);
//                }*/
//                vmem.writeLong(element);
//            }
            ByteBuffer bb = ByteBuffer.allocate(pageSize8);
            for (int i = 0; i < mem.length; i++){
                bb.putLong(mem[i]);
            }
            vmem.write(bb.array());
        }
        catch (IOException e){
            System.out.println("Error: Failed to write current page to swap.");
            //Don't know what else to do here yet
        }
        //Load requested page
        try {
            page = index / pageSize;
            vmem.seek(page * pageSize8);//This should find the correct page
            /*for (index = 0; index < pageSize; index++){
                mem[index] = vmem.readLong();
            }*/
            byte[] bytes = new byte[pageSize8];
            ByteBuffer bb;
            vmem.read(bytes);
            for (index = 0; index < pageSize; index++){
                bb = ByteBuffer.wrap(Arrays.copyOfRange(bytes, index * 8, index * 8 + 8));
                mem[index] = bb.getLong();
                //System.out.println("mem[" + index + "] = " + mem[index]);
            }
        }
        catch (IOException e){
            System.out.println("Error: Failed to load requested page.");
            e.printStackTrace();
        }
    }

    /**
     * Puts a value into the memory
     * @param index Where to put it
     * @param value What to put
     */
    public static void put(int index, long value){
        //System.out.println("Inserting " + value + " at " + index);
        load(index);
        mem[index - pageSize * page] = value;
    }

    /**
     * Gets the value at index
     * @param index Which value to get
     * @return Requested value
     */
    public static long get(int index){
        //System.out.println("Getting value at " + index);
        load(index);
        return mem[index - pageSize * page];
    }
}
