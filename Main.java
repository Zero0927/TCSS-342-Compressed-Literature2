//Siyuan Zhou
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 */

/**huffman coding.Encode
 * @author ***
 *
 */
public class Main {

	private static int fileLength=0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file="WarAndPeace.txt";
		String content=readTextContent(file);
		long startTime=System.currentTimeMillis();
		//Pass the String into the CodingTree in order to initiate Huffman 
		// encoding procedure and generate a map of codes.
		CodingTree tree=new CodingTree(content);
		try {
			//Output the codes to a text file.
			PrintStream save=new PrintStream("codes.txt");
			String[]words=tree.codes.keySet().toArray(new String[0]);
			save.print("[("+words[0]+", "+tree.codes.get(words[0])+")");
			for(int k=1;k<words.length;++k)
				save.print(", ("+words[k]+", "+tree.codes.get(words[k])+")");
			save.print("]");
			save.close();
			//Output the compressed message to a binary file.
			save=new PrintStream("compressed.txt");
			int saveLen=(tree.bits.length()+7)>>3;
			byte[]saveBit=new byte[saveLen];
			for(int k=0;k<saveLen;++k){
				byte b=0;
				for(int j=0;j<8;++j)
					if(8*k+j<tree.bits.length()&&tree.bits.charAt(8*k+j)=='1')
						b|=1<<j;
				saveBit[k]=b;
			}
			save.write(saveBit);
			save.close();
			//Display statistics. 
			//output the original size (in bytes)
			//the compressed size (in bytes)
			//the compression ratio (as a percentage) 
			// the Running time for compression.
			long endTime=System.currentTimeMillis();
			int originalSize=fileLength;
			int compressedSize=saveLen;
			double ratio=100.0*compressedSize/originalSize;
			long time=endTime-startTime;
			System.out.printf("\nUncompressed filesize:%d byte\n",originalSize);
			System.out.printf("Compressed file  size:%d byte\n",compressedSize);
			System.out.printf("compression ratio:%.0f%%\n",ratio);
			System.out.printf("Running Time:%d milliseconds\n",time);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Read the contents of a text file into a String
	private static String readTextContent(String file){
		try {
			StringBuilder sb=new StringBuilder();
			FileInputStream fis=new FileInputStream(file);
			byte[]buf=new byte[4096];
			int read;
			while((read=fis.read(buf))>0){
				sb.append(new String(buf,0,read,"utf-8"));
				fileLength+=read;
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
