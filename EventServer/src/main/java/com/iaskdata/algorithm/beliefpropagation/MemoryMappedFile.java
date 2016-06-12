package com.iaskdata.algorithm.beliefpropagation;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;


/**
 * This class is independent of the Belief Propagation algorithm
 * <p>
 * Allows a large file (more than a few tens of MB) to be memory-mapped, section by section, to support faster reads and writes.
 * Use this class, instead of the {@link MappedByteBuffer}, if you want the faster memory-mapped IO but your file is larger than the amount of available memory.
 * <p>
 * There are two file processing modes: read-only, read-write. In read-only mode, all "write" methods cannot be called, or an UnsupportedOperationException will be thrown.
 * <p>
 * In the current implementation, once an old section is unloaded from memory, it cannot be re-examined. Future implementations should support this.
 * Similarly, the read position is only allowed to increase, but not decrease (or reset to the beginning), which means you cannot re-process an old block of the file.
 *
 */
public class MemoryMappedFile {

    public enum Mode{READ_ONLY, READ_WRITE};
    private final Mode mode;


    // The empirical contiguous size of file section that can be mapped into memory (by Java) at a time.
    // This size "works" for most system. If your computer has more RAM, say 12GB, you can set this number much higher.
    public static final long INTERNAL_BUFFER_SIZE = 200000000;
    private ByteBuffer internalBuffer;
    private ByteOrder byteOrder;

    // After checkPoint number of bytes have been processed, we advance to the next block of the file
    private static final long BYTES_UNTIL_NEXT_READ = INTERNAL_BUFFER_SIZE - 350;

    // Here we assume no low-level read methods read no more than 200 bytes at a time; if any method reads more than that, buffer underflow will happen.
    private static final long BYTES_FOR_BACKTRACK = 200;

    // where we are at in the file
    private long apparentPosition;

    // the next read will occur at this position
    private long nextReadPosition;

    private FileChannel fileChannel;

    private String filename;
    private long fileLength = -1;

    private long originalFileSize = 0;


    /**
     * Maps a file into memory, using the given mode, and the default {@link ByteOrder#BIG_ENDIAN} byte order.
     * @param filename the file to be memory-mapped
     * @param mode {@link #READ_ONLY}, or {@link #READ_WRITE}
     */
    public MemoryMappedFile( String filename, Mode mode){
        this(filename, mode, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Maps a file into memory, using the given mode and byte order.
     * @param filename the file to be mapped to memory
     * @param mode {@link Mode} of accessing the memory-mapped file
     * @param byteOrder {@link ByteOrder#BIG_ENDIAN} or {@link ByteOrder#LITTLE_ENDIAN}
     */
    public MemoryMappedFile(String filename, Mode mode, ByteOrder byteOrder){
        this.filename = filename;
        this.mode = mode;
        this.byteOrder = byteOrder;

        open();
    }

    /**
     * Gets file size, in bytes
     * @return
     */
    public long size(){
        if (fileLength < 0){
            fileLength = new File(filename).length();
        }
        return fileLength;
    }

    public String getFilename(){
        return filename;
    }

    /**
     * Creates a channel from the file, through which Java reads and writes to the file.
     * Also starts the memory-mapping process starting at the beginning of the file.
     */
    private final void open(){

        nextReadPosition = BYTES_UNTIL_NEXT_READ;
        apparentPosition = 0;

        try {

            if ( mode == Mode.READ_ONLY) {

                fileChannel =  new RandomAccessFile(filename, "r").getChannel();
                this.originalFileSize = fileChannel.size();
                long smallerSize = (fileChannel.size() > INTERNAL_BUFFER_SIZE)? INTERNAL_BUFFER_SIZE : fileChannel.size();
                internalBuffer = fileChannel.map( FileChannel.MapMode.READ_ONLY, 0, smallerSize);

            } else if ( mode == Mode.READ_WRITE ) {	//this.mode == READ_WRITE_KEEP_FILESIZE ||

                fileChannel = new RandomAccessFile(filename, "rw").getChannel();

                this.originalFileSize = this.fileChannel.size();
                internalBuffer = fileChannel.map( FileChannel.MapMode.READ_WRITE, 0, INTERNAL_BUFFER_SIZE);

//		    	internalBuffer = ByteBuffer.allocate( (int) this.fileChannel.size() );
//		    	fileChannel.read(internalBuffer);
//		    	internalBuffer.position(0);

            } else {
                System.out.println( "Mode not supported in BigMappedByteBuffer" );
                System.exit(-1);
            }

            // !!! set byte order
            internalBuffer.order( this.byteOrder );

//			fileChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Always Trim for READ_WRITE mode.
     * <p>
     * Unmaps the file from memory and optionally trim the file at the current read/write marker position.
     * Trimming only works if the file was opened for read and write, which can cause the file to increase in size.
     * If the file was opened for read only, setting <code>trimFile</code> to <code>true</code> has no effect.
     * @param trimFile <code>true</code> will trim the file, if the file was opened for read and write.
     */
    public final void close( boolean trimFile ){

        unmapMappedByteBuffer();

        // Truncate file (for read_write mode only, since only when writing that the file may temporarily be resized to larger than it should)
        if ( mode == Mode.READ_WRITE) {	// mode == READ_WRITE_KEEP_FILESIZE ||

            try {
                fileChannel.close();	// close previously unclosed channel
                fileChannel = new RandomAccessFile(filename, "rw").getChannel();

//				if (trimFile) {
                fileChannel.truncate(apparentPosition);
//				}

                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private final void unmapMappedByteBuffer(){
        // unmap -- clear the mappedByteBuffer first because it "locks" the file to make its size unchangeable
        try {
//        	if (internalBuffer.isDirect()) {
            clean(internalBuffer);
//			}

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @SuppressWarnings("unchecked")
    private static final void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    final Method getCleanerMethod = buffer.getClass().getMethod( "cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    // XXX configure Eclipse's preferences to ignore or only warn about accessing the restricted Cleaner class,
                    // instead of flagging the following two lines as errors.
                    final sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }



//    public final long getOriginalFileSize(){
//		return this.originalFileSize;
//    }
//
//    public final long getCurrentFileSize(){
//    	long currentFileSize = -1;
//    	try {
//			currentFileSize = this.fileChannel.size();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return currentFileSize;
//    }


    /**
     * Sets the position of the memory-mapped file's read marker.
     * <b>Warning: this does NOT change the internal buffer's position</b>
     * @param position
     */
    private final void position(long position){

        this.apparentPosition = position;

        // if the new position is outside the bound of this internal buffer
        if (position >= nextReadPosition) {

            try{
                unmapMappedByteBuffer();

                if ( this.mode == Mode.READ_ONLY) {

                    if ( this.originalFileSize - position >= INTERNAL_BUFFER_SIZE ) {
                        internalBuffer = fileChannel.map( FileChannel.MapMode.READ_ONLY, position, INTERNAL_BUFFER_SIZE );
                    } else {
                        internalBuffer = fileChannel.map( FileChannel.MapMode.READ_ONLY, position, this.originalFileSize - position );
                    }
//	    	    	System.out.println("remapped readonly");

//				} else if ( this.mode == READ_WRITE_KEEP_FILESIZE ) {
//			    	internalBuffer = fileChannel.map( FileChannel.MapMode.READ_WRITE, position - BYTES_FOR_BACKTRACK, INTERNAL_BUFFER_SIZE );
//			    	internalBuffer.position( new Long(BYTES_FOR_BACKTRACK).intValue() );
////	    	    	System.out.println("remapped read write (keep filesize)");

                } else if ( this.mode == Mode.READ_WRITE ) {
//			    	internalBuffer = fileChannel.map( FileChannel.MapMode.READ_WRITE, position - BYTES_FOR_BACKTRACK, (this.originalFileSize - position >= INTERNAL_BUFFER_SIZE)? (INTERNAL_BUFFER_SIZE+BYTES_FOR_BACKTRACK) : (this.originalFileSize - position+BYTES_FOR_BACKTRACK)  );

                    internalBuffer = fileChannel.map( FileChannel.MapMode.READ_WRITE, position - BYTES_FOR_BACKTRACK, INTERNAL_BUFFER_SIZE );

                    internalBuffer.position(new Long(BYTES_FOR_BACKTRACK).intValue());
//	    	    	System.out.println("remapped read write (resize file)");

                } else {
                    System.out.println("NOT remapped!");
                }

                // !!! set byte order
                internalBuffer.order( this.byteOrder );


            } catch (IOException e) {
                e.printStackTrace();
            }

            nextReadPosition = position + BYTES_UNTIL_NEXT_READ;
        }
    }

    /**
     * Detect if the intended end position is outside of the internal buffer's limit.
     * If so, re-map the internal buffer from the file's current position.
     * <p>
     * WARNING! This method does NOT change the apparent reading position of the file.
     * @param currentPosition
     * @param intendedEndPosition
     */
    private final void allocateIfNecessary(long currentPosition, long intendedEndPosition){

        this.apparentPosition = currentPosition;

        // if the new position is outside the bound of this internal buffer
        if (intendedEndPosition >= nextReadPosition) {

            try{
                unmapMappedByteBuffer();

                if ( mode == Mode.READ_ONLY) {

                    if ( this.originalFileSize - apparentPosition >= INTERNAL_BUFFER_SIZE ) {
                        internalBuffer = fileChannel.map( FileChannel.MapMode.READ_ONLY, apparentPosition, INTERNAL_BUFFER_SIZE );
                    } else {
                        internalBuffer = fileChannel.map( FileChannel.MapMode.READ_ONLY, apparentPosition, this.originalFileSize - apparentPosition );
                    }
                } else if ( mode == Mode.READ_WRITE ) {
                    internalBuffer = fileChannel.map( FileChannel.MapMode.READ_WRITE, apparentPosition - BYTES_FOR_BACKTRACK, INTERNAL_BUFFER_SIZE );
                    internalBuffer.position(new Long(BYTES_FOR_BACKTRACK).intValue());
                } else {
                    System.out.println("NOT remapped!");
                }

                // !!! set byte order
                internalBuffer.order( this.byteOrder );


            } catch (IOException e) {
                e.printStackTrace();
            }

            nextReadPosition = apparentPosition + BYTES_UNTIL_NEXT_READ;
        }

    }

    /**
     * Gets the position of the memory-mapped file's read marker.
     * @return
     */
    public final long position(){
        return this.apparentPosition;
    }

    /**
     * Advances the specified number of bytes.
     * @param numBytes number of bytes to advance
     */
    public final void advance(int numBytes){
//    	if (numBytes > BYTES_FOR_BACKTRACK) throw new IllegalArgumentException("numBytes cannot be greater than " + BYTES_FOR_BACKTRACK );

        long intendedEndPosition = apparentPosition + numBytes;
        allocateIfNecessary(apparentPosition, intendedEndPosition);

        this.internalBuffer.position( this.internalBuffer.position() + numBytes );	// advance internal butter position
        position( intendedEndPosition);	// advance apparent position
    }

//    public final long getUint64AsLong(){
//    	byte[] b = new byte[8];
//    	this.internalBuffer.get(b);
//
//    	long result = DataTypeConversion.LittleEndian.toLongFromUint64(b);
//
////    	long result = this.internalBuffer.getLong();
//
//    	position( this.apparentPosition + 8 );
//    	return result;
//    }
//
//
//    public final int getUint32AsInt(){
//    	byte[] b = new byte[4];
//    	this.internalBuffer.get(b);
//
//    	int result = DataTypeConversion.LittleEndian.toIntFromUint32(b);
//
//    	position( this.apparentPosition + 4 );
//    	return result;
//    }

    public final void get(byte[] buffer){
        this.internalBuffer.get( buffer );
        position( this.apparentPosition + buffer.length );
    }

    public final int getInt(){

        int result = this.internalBuffer.getInt();
        position( this.apparentPosition + 4 );
        return result;
    }

    public final void getIntArray(int[] buffer){
        getIntArray(buffer, buffer.length);
    }

    public final void getIntArray(int[] buffer, int length){
        long intendedEndPosition = apparentPosition + 4 * length;
        allocateIfNecessary(apparentPosition, intendedEndPosition);
//System.out.println("buffer length: " + buffer.length);
//System.out.println("length: " +  length);
        internalBuffer.asIntBuffer().get(buffer, 0, length);

        // need to increment internalBuffer's position since it's independent of the intBuffer's
        internalBuffer.position( internalBuffer.position() + 4 * length );

        apparentPosition = intendedEndPosition;
    }

    public final void getFloatArray(float[] buffer){
        getFloatArray(buffer, buffer.length);
    }

    public final void getFloatArray(float[] buffer, int length){
        long intendedEndPosition = apparentPosition + 4 * length;
        allocateIfNecessary(apparentPosition, intendedEndPosition);

        internalBuffer.asFloatBuffer().get(buffer, 0, length);

        // need to increment internalBuffer's position since it's independent of the floatBuffer's
        internalBuffer.position( internalBuffer.position() + 4 * length );
        apparentPosition = intendedEndPosition;
    }

    public final void getFloatArrayThenRewind(float[] buffer){
        getFloatArrayThenRewind(buffer, buffer.length);
    }

    public final void getFloatArrayThenRewind(float[] buffer, int length){
        long intendedEndPosition = apparentPosition + 4 * length;
        allocateIfNecessary(apparentPosition, intendedEndPosition);
        internalBuffer.asFloatBuffer().get(buffer, 0, length);
    }

    /**
     * Write the all entries of the array.
     * @param buffer
     */
    public final void putFloatArray(float[] buffer){
        putFloatArray(buffer, buffer.length);
    }

    /**
     * Write the first length entries of the array.
     * @param buffer
     * @param length
     */
    public final void putFloatArray(float[] buffer, int length){
        long intendedEndPosition = apparentPosition + 4 * length;
        allocateIfNecessary(apparentPosition, intendedEndPosition);

        internalBuffer.asFloatBuffer().put(buffer, 0, length);
        // need to increment internalBuffer's position since it's independent of the FloatBuffer's
        internalBuffer.position( internalBuffer.position() + 4 * length );
        apparentPosition = apparentPosition + 4 * length;
    }
    /**
     * Write the all entries of the array.
     * @param buffer
     */
    public final void putIntArray(int[] buffer){
        putIntArray(buffer, buffer.length);
    }
    /**
     * Write the first length entries of the array.
     * @param buffer
     * @param length
     */
    public final void putIntArray(int[] buffer, int length){
        long intendedEndPosition = apparentPosition + 4 * length;
        allocateIfNecessary(apparentPosition, intendedEndPosition);

        internalBuffer.asIntBuffer().put(buffer, 0, length);
        // need to increment internalBuffer's position since it's independent of the FloatBuffer's
        internalBuffer.position( internalBuffer.position() + 4 * length );
        apparentPosition = apparentPosition + 4 * length;
    }

    public final void putInt(int input){
        this.internalBuffer.putInt(input);
        position( this.apparentPosition + 4 );
    }

    public final int getIntThenRewind(){
        int result = this.internalBuffer.getInt( this.internalBuffer.position() );
        return result;
    }

    public final float[] getFloatArray(int length){
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = getFloat();
        }
        return array;
    }



//	public final void putFloatArray(float[] array){
//		for (int i = 0; i < array.length; i++) {
//			putFloat(array[i]);
//		}
//	}


    public final void putFloatArrayThenRewind( float[] array ){
        int currentPosition = internalBuffer.position();
        for (int i = 0; i < array.length; i++) {
            internalBuffer.putFloat( currentPosition + (4 * i), array[i]);
        }
    }

    /**
     * Absolute put method for writing a float array.
     * Writes an array of floats (of size that is multiple of 4), in the current byte order, into this buffer at current location + positionOffset.
     * @param positionOffset offset, in terms of bytes
     * @param array the float array
     */
    public final void putFloatArray(int positionOffset, float[] array){
        int currentPosition = internalBuffer.position();
        for (int i = 0; i < array.length; i++) {
            try {
                internalBuffer.putFloat( currentPosition + positionOffset + (4 * i), array[i]);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Current position: " + currentPosition);
                System.out.println("To put at position: " + (currentPosition + positionOffset + (4 * i)) );
                System.out.println("Current limit: " + internalBuffer.limit());
            }

        }
    }



    public final float getFloat(){
        float result = this.internalBuffer.getFloat();
        position( this.apparentPosition + 4 );
        return result;
    }

    public final void putFloat(float input){
        this.internalBuffer.putFloat(input);
        position( this.apparentPosition + 4 );
    }

    public final void putFloatThenRewind(float input){
        internalBuffer.putFloat( internalBuffer.position(), input);
    }

    public final float getFloatThenRewind(){
        float result = this.internalBuffer.getFloat( this.internalBuffer.position() );
        return result;
    }

    public final double getDouble(){
        double result = this.internalBuffer.getDouble();
        position( this.apparentPosition + 8 );
        return result;
    }


    public final void putDouble(double input){
        this.internalBuffer.putDouble(input);
        position( this.apparentPosition + 8 );
    }


    public final long getLong(){
        long result = this.internalBuffer.getLong();
        position( this.apparentPosition + 8 );
        return result;
    }


    public final void putLong(long input){
        this.internalBuffer.putLong(input);
        position( this.apparentPosition + 8 );
    }


//	/**
//	 * Moves both the internal byteBuffer's (<100MB) marker position
//	 * and the BigMappedByteBuffer's overall marker position forward for a number of bytes.
//	 * @param numBytes the number of bytes
//	 */
//    public final void advance(int numBytes){
//    	this.internalBuffer.position( this.internalBuffer.position() + numBytes );
//    	position( this.position + numBytes );
//    }


    public final byte getByte() {
        byte result = this.internalBuffer.get();
        position( this.apparentPosition + 1 );
        return result;
    }

    public final void putByte(byte input){
        this.internalBuffer.put(input);
        position( this.apparentPosition + 1 );
    }


    public final short getShort(){
        short result = this.internalBuffer.getShort();
        position(this.apparentPosition + 2);
        return result;
    }

    public final void putShort(short input){
        this.internalBuffer.putShort(input);
        position(this.apparentPosition + 2);
    }

    /**
     * TODO: Not tested
     * @return
     */
    public final boolean getBoolean() {
        boolean result = (this.internalBuffer.get() == 1) ? true : false;
        position( this.apparentPosition + 1 );
        return result;
    }

    /**
     * TODO: Not tested
     * @param input
     */
    public final void putBoolean(boolean input){
        this.internalBuffer.put( (byte) ((input)? 1 : 0) );
        position( this.apparentPosition + 1 );
    }

    public final byte[] getByteArray(int length){
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = getByte();
        }
        return array;
    }

    public final void putByteArray(byte[] array){
        for (int i = 0; i < array.length; i++) {
            putByte(array[i]);
        }
    }

}


