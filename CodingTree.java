//Siyuan Zhou
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CodingTree implements Comparable<CodingTree> {

	private HuffmanTreeNode node;
	private CodingTree leftChild;
	private CodingTree rightChild;
	
	//a map of words in the message to binary codes (Strings of ‘0’ or '1') created by tree.
	public Map<String, String> codes;
	
	//the message encoded using the Huffman codes.
	public String bits="";
	
	/**a constructor that takes the text of a message to be compressed. 
	 * The constructor is responsible for calling all private methods 
	 * that carry out the Huffman coding algorithm
	 * */
	public  CodingTree(String message){
		String[]words=this.separate(message);
		MyHashTable<String,Integer> freq=computeFrequency(words);
		freq.stats();
		buildTree(freq);
		codes=new HashMap<String, String>();
		buildCodesMap("", codes);
		StringBuilder sb=new StringBuilder();
		//char[]chars=message.toCharArray();
		for(String s:words)
			sb.append(codes.get(s));
		bits=sb.toString();
	}
	private CodingTree(HuffmanTreeNode node) {
		this.node=node;
		leftChild=rightChild=null;
	}
	private CodingTree(CodingTree left,CodingTree right) {
		this.node=new HuffmanTreeNode(left.node, right.node);
		leftChild=left;
		rightChild=right;
	}
	private boolean isWordChar(char c){
		return (Character.isLetter(c))||(Character.isDigit(c))||(c=='\'')||(c=='-');
	}
	//separate message to words
	private String[]separate(String message){
		List<String>words=new ArrayList<String>();
		String tmp="";
		for(char c:message.toCharArray()){
			if(!isWordChar(c)){//separator of a word
				if(tmp.length()>0)
					words.add(tmp);
				tmp=""+c;
				if(tmp.length()>0)
					words.add(tmp);
				tmp="";
			}else{
				tmp+=c;
			}
		}
		if(tmp.length()>0)
			words.add(tmp);
		return words.toArray(new String[0]);
	}
	//compute frequency of each word in words
	private MyHashTable<String,Integer> computeFrequency(String[]words){
		MyHashTable<String,Integer>table=new MyHashTable<String,Integer>(1<<15);
		for(String s:words){
			Integer count=table.get(s);
			if(count!=null)
				table.put(s, 1+count);
			else
				table.put(s, 1);
		}
		return table;
	}
	//use a min-heap to build huffman tree
	private void buildTree(MyHashTable<String,Integer>freq){
		MyPriorityQueue<CodingTree>heap=new MyPriorityQueue<CodingTree>(freq.getSize());
		//build a original min heap
		String[]words=new String[freq.getSize()];
		freq.getKeys(words);
		for(String word:words){
			CodingTree.HuffmanTreeNode node=
					new CodingTree.HuffmanTreeNode(word, freq.get(word));
			heap.insert(new CodingTree(node));
		}
		//build tree
		while(heap.getSize()>1){
			CodingTree min=heap.popMin();//pop the min tree
			CodingTree min2=heap.popMin();//pop second min tree
			//make a new tree with two trees
			heap.insert(new CodingTree(min, min2));
		}
		CodingTree tree= heap.popMin();
		this.node=tree.node;
		this.leftChild=tree.leftChild;
		this.rightChild=tree.rightChild;
	}
	//
	private void buildCodesMap(String prefix,Map<String, String> codes){
		//leaf node. the key is valid and should be encoded
		if(node.isValid()){
			//store the key and its code, length of bit of the code
			if(codes.containsKey(node.getKey()))
				System.out.println("Error: "+node.getKey());
			codes.put(node.getKey(),prefix);
			//System.out.printf("word=%s, codes=%s\n",node.getKey(),prefix);
		}else{
			//code for left child tree
			leftChild.buildCodesMap(prefix+"0",codes);
			// code for right child tree
			rightChild.buildCodesMap(prefix+"1",codes);
		}
	}
	//颅 this method will	take the output of Huffman鈥檚 encoding and produce the original text.
	private static String decode(String bits,Map<String,String>codes){
		StringBuilder sb=new StringBuilder();
		Map<String,String>decodes=new HashMap<String, String>();
		for(String c:codes.keySet())
			decodes.put(codes.get(codes), c);
		String tmp="";
		for(int k=0;k<bits.length();++k){
			tmp+=bits.charAt(k);
			//find a sequence of bits that can be decoded to a character
			if(decodes.containsKey(tmp)){
				sb.append(decodes.get(tmp));
				tmp="";
			}
		}
		return sb.toString();
	}

	@Override
	public int compareTo(CodingTree o) {
		return node.getFrequency()-o.node.getFrequency();
	}
	
	
	static class HuffmanTreeNode {
		public static final String INVALID_KEY=null;
		private String key;
		private int frequency;
		
		public HuffmanTreeNode(String key, int frequency) {
			super();
			this.key = key;
			this.frequency = frequency;
		}
		//join two nodes and new node's frequency is the sum of two nodes'frequency
		public HuffmanTreeNode(HuffmanTreeNode a, HuffmanTreeNode b) {
			super();
			this.key = INVALID_KEY;
			this.frequency = a.frequency+b.frequency;
		}
		public boolean isValid(){
			return key!=INVALID_KEY;
		}
		public String getKey() {
			return key;
		}

		public int getFrequency() {
			return frequency;
		}

		@Override
		public String toString() {
			return isValid()?String.format("[%s,%4d]", key,frequency):
				String.format("[null,%4d]", frequency);
		}
	}}
