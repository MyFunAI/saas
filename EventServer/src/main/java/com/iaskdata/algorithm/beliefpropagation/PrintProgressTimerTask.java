package com.iaskdata.algorithm.beliefpropagation;
import java.util.TimerTask;


/**
 * For printing the reading/writing process of a memory-mapped file.
 *
 */
public class PrintProgressTimerTask extends TimerTask {

	long sFromStart = -1;
	long start = -1;
	
	MemoryMappedFile file;
	long fileSize;	// bytes
	long processed; // bytes
	String filename;
	


	public PrintProgressTimerTask( MemoryMappedFile file ){
		this.file = file;
		fileSize = this.file.size();
		filename = this.file.getFilename();
	}
	
	
	@Override
	public void run() {
		if (start < 0){
			start = System.currentTimeMillis() / 1000;
		}
		
		sFromStart = System.currentTimeMillis() / 1000 - start;
		processed =  file.position();
		
		// if finished processing file
//		if ( processed >= fileSize ){
//			System.out.printf( "\r%s | %.1fMB | %.1fs | %.1fMB/s           %n", filename, fileSize/1048576.0, 1.0*sFromStart, processed/1048576.0/sFromStart );
//			this.cancel();			
		// else print progress
//		} else {
			double secondsleft = sFromStart * (1.0 * fileSize / processed - 1);
			System.out.printf( "\r%s | %.1fMB | %.1fs left | %.1fMB/s          ", filename, fileSize/1048576.0, secondsleft, processed/1048576.0/sFromStart );
//		}
	}
	
	public void end(){
		System.out.printf( "\r%s | %.1fMB | %.1fs | %.1fMB/s           %n", filename, fileSize/1048576.0, 1.0*sFromStart, processed/1048576.0/sFromStart );
		this.cancel();			
		
	}

	
}
