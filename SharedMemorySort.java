import java.util.*;
import java.io.*;
public class SharedMemorySort{

        static int TemFileCount = 0;//total temp files created for the project
        static int ThreadCount = 4;//number of threads for the process to run
        static long FileSize, ChunkSize;//filesize and chuck size for the project
        static long SizeofMemory = 80000000;//memory size given in the assignment
        static String FileName, OutFile;//Input and output file name
        static long StartTime, EndTime; //StartTime and end time for the process to run
        static double TimeTaken;
        public static void main(String[] args)
        {
                if(args.length == 2) {
                        FileName = args[0];
                        OutFile = args[1];
                }
                else {
                        try {
                                throw new Exception("No Arguments");
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }

                StartTime = System.currentTimeMillis();
                Chunks.chunks();
                EndTime = System.currentTimeMillis();
                TimeTaken = (double)((EndTime - StartTime)/1000);

                System.out.println("Total time taken: "+ (TimeTaken) + " seconds");
                //System.out.println("Throughput: "+ ((SharedMemorySort.FileSize)*2));
                System.out.println("Throughput: "+ (((SharedMemorySort.FileSize)*2)/(TimeTaken*1000000)));
        }
}



class Merge extends Thread
{
	@Override
	public void run() {
		int TempFiles = SharedMemorySort.TemFileCount;
		int i = 0;
		int minimum;
		BufferedReader [] br = new BufferedReader[TempFiles];
		long counter = SharedMemorySort.FileSize/100;
		String min = "";
		
		try {
			TreeMap<String, Integer> mappedTree = new TreeMap<String, Integer>();
			BufferedWriter bw = new BufferedWriter(new FileWriter(SharedMemorySort.OutFile));
			
			for(int var = 0; var < TempFiles; ++var) {
				br[var] = new BufferedReader(new FileReader("temp" + SharedMemorySort.FileName + var));
				mappedTree.put(br[var].readLine(), var);
			}

			while(i++ < counter) {
				min = (String)mappedTree.firstKey();
				minimum = (int)mappedTree.get(min);

				bw.write(min + "\r\n");
				mappedTree.remove(min);
				
				if((min = br[minimum].readLine()) != null)
					mappedTree.put(min, minimum);
			}
			bw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class WriteToFile{
	static void writeToFile(ArrayList<String> arr, int FileCount) {
		arr = new MergeSort().SortData(arr);
		try {
			BufferedWriter bufferwriter = new BufferedWriter(new FileWriter(new File("temp" + SharedMemorySort.FileName + FileCount)));
			for(String val: arr) {
				bufferwriter.write(val);
				bufferwriter.newLine();
			}
			bufferwriter.flush();
			bufferwriter.close();
		}
		catch (IOException e)  {
			e.printStackTrace();
		}
	}
}


class Chunks{
	static void chunks() {
		File file = new File(SharedMemorySort.FileName);
		long chunkSize;
		SharedMemorySort.FileSize = file.length();
		SharedMemorySort.ChunkSize = SizeofChunk(SharedMemorySort.FileSize);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String sentence = br.readLine();
			ArrayList<String> arr = null;
			while(sentence != null)
			{
				arr = new ArrayList<String>();
				chunkSize = 0;
				
				while(sentence != null && ((chunkSize + sentence.length()) <= SharedMemorySort.ChunkSize)) {
					arr.add(sentence);
					chunkSize = chunkSize + (sentence.length() + 2);
					sentence = br.readLine();
				}
				
				WriteToFile.writeToFile(arr, SharedMemorySort.TemFileCount);
				SharedMemorySort.TemFileCount = SharedMemorySort.TemFileCount + 1;
				arr = null;
			}
			
			System.out.println("No. of temporary files that are created: " + SharedMemorySort.TemFileCount);
			Thread [] thread = new Thread[SharedMemorySort.ThreadCount];
			
			for(int i = 0; i < SharedMemorySort.ThreadCount; ++i) {
				thread[i] = new Thread(new Merge());
				thread[i].start();
			}
			
			for(int i = 0; i < SharedMemorySort.ThreadCount; ++i) {
				try {
					thread[i].join();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static long SizeofChunk(long fileSize)
	{
		System.gc();
		System.out.println("Number of Threads "+SharedMemorySort.ThreadCount);
		System.out.println("Total Number of chunks " + (fileSize / SharedMemorySort.SizeofMemory) + " Size of Chunk " + SharedMemorySort.SizeofMemory);
		
		return SharedMemorySort.SizeofMemory;
	}
}



class MergeSort extends Thread
{
	public MergeSort() {
		
	}
	
	@Override
	public void run() {

	}

	public ArrayList<String> SortData(ArrayList<String> arr)
	{
		TreeSet<String> sortedTree = new TreeSet<String>();
		ArrayList<String> sortedList = new ArrayList<String>();
		
		for(String val: arr) {
			if(val.length() >= 11)
				sortedTree.add(val);
		}
		
		sortedList.addAll(sortedTree);
		return sortedList;
	}
}

