//Siyuan Zhou
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MyHashTable<K,V> {
	private int capacity;//max number of entries
	private int size;//number of entries inserted
	private Entry<K,V>[]entries;//container for key and value
	private List<Integer>probe;//probe time to insert a new key
	
	private static class Entry<K,V>{
		final K key;
		V value;
		public Entry(K key,V val){
			this.key=key;
			this.value=val;
		}

		@Override
		public String toString() {
			return "("+key+","+value+")";
		}
	}
	
	
	public static void main(String[]args){
		MyHashTable<String, Integer>table=new MyHashTable<String, Integer>(1<<10);
		table.put("word-1", 1);
		table.put("word-2", 1);
		table.put("word-3", 1);
		table.put("word-4", 1);
		if(1!=table.get("word-1"))
			System.out.println("Error! Get word-1");
		if(1!=table.get("word-2"))
			System.out.println("Error! Get word-2");
		if(1!=table.get("word-3"))
			System.out.println("Error! Get word-3");
		if(1!=table.get("word-4"))
			System.out.println("Error! Get word-4");
		if(null!=table.get("word-5"))
			System.out.println("Error! Get word-5");
		table.stats();
		for(int k=5;k<512;++k)
			table.put("word-"+k, k);
		table.stats();
	}
	//creates a hash table with capacity number of buckets 
	public MyHashTable(int capacity) {
		this.capacity=capacity;
		this.size=0;
		this.entries=new Entry[this.capacity];
		this.probe=new ArrayList<Integer>(100);
	}
	
	public int getSize(){
		return this.size;
	}
	
	public void getKeys(K[]arr){
		int s=0;
		for(int k=0;k<this.capacity;++k){
			if(this.entries[k]==null)
				continue;
			arr[s++]=this.entries[k].key;
			if(s==this.size)
				break;
		}
	}
	
	/***
	 update or add the newValue to the bucket hash(searchKey)
	 if hash(key) is full use linear probing to find the next available bucket
	 */
	public void put(K searchKey, V newValue){
		int index;
		index=search(searchKey);
		Entry<K,V>newEntry=new Entry<K, V>(searchKey, newValue);
		if(index<0){//the key not exist
			this.entries[-index-1]=newEntry;
			++this.size;
			int i0=hash(searchKey);
			int probeTime=(-index-1-i0+this.capacity)%this.capacity;
			//add probe time
			if(probeTime<this.probe.size())
				this.probe.set(probeTime, this.probe.get(probeTime)+1);
			else{
				//extend the probe list
				for(int k=this.probe.size();k<probeTime;++k)
					this.probe.add(0);
				this.probe.add(1);
			}
		}else{//find the key
			this.entries[index]=newEntry;
		}
	}
	/**
	return a value for the specified key from the bucket hash(searchKey)
	if hash(searchKey) doesn’t contain the value use linear probing to find the
	appropriate value
	 * */ 
	public V get(K searchKey){
		int index;
		if((index=search(searchKey))<0)
			return null;
		else{
			return this.entries[index].value;
		}
	}
	
	/** return true if there is a value stored for searchKey
	 * */
	public boolean containsKey(K searchKey){
		return search(searchKey)>=0;
	}
	
	/**
	 * displays the following stat block for the data in your hash table: </br>
	 * Hash Table Stats </br>
	 * ================ </br>
	 * Number of Entries: 22690 </br>
	 * Number of Buckets: 32768 </br>
	 * Histogram of Probes: </br>
	 * [14591, 3419, 1510, 859, 479, 337, 238, 169,</br>
	 * 166, 100, 90, 78, 53, 42, 51, 54, 29, 28, 18, 17, 21, 20, 17, 15, 12, 10,</br>
	 * 12, 11, 6, 4, 12, 4, 9, 13, 5, 4, 7, 0, 0, 3, 2, 3, 2, 3, 5, 1, 3, 2, 1,</br>
	 * 2, 6, 2, 1, 3, 1, 1, 2, 3, 3, 0, 2, 2, 1, 1, 1, 1, 2, 4, 3, 2, 1, 0, 2,</br>
	 * 1, 3, 0, 0, 2, 2, 1, 2, 3, 2, 1, 3, 0, 0, 0, 0, 0, 0, 3, 2, 0, 0, 0, 1,</br>
	 * 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 2, 0, 0, 1, 0, 0, 0, 1, 2, 0, 1, 0, 0,</br>
	 * 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,</br>
	 * 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 2, 0, 0, 1, 0, 1,</br>
	 * 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,</br>
	 * 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0,</br>
	 * 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0,</br>
	 * 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0,</br>
	 * 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,</br>
	 * 0, 1, 0, 1, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,</br>
	 * 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,</br>
	 * 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1,</br>
	 * 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0,</br>
	 * 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,</br>
	 * 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,</br>
	 * 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,</br>
	 * 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,</br>
	 * 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0,</br>
	 * 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,</br>
	 * 0, 0, 0, 1] </br>
	 * Fill Percentage: 69.244385% </br>
	 * Max Linear Prob: 533</br>
	 * Average Linear Prob: 3.434773
	 * */
	public void stats(){
		System.out.println("Hash Table Stats");
		System.out.println("================");
		System.out.println("Number of Entries:"+this.size);
		System.out.println("Number of Buckets:"+this.capacity);

		System.out.println("Histogram of Probes:"+this.probe);
		System.out.println("Fill Percentage: "+100.0*this.size/this.capacity+"%");
		System.out.println("Max Linear Prob: "+(this.probe.size()-1));
		double ave=0.0;
		for(int k:this.probe)
			ave+=k;
		if(this.size>0)
			ave/=this.size;
		System.out.println("Average Linear Prob: "+ave);
}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("[");
		int s=size;
		for(int k=0;k<this.capacity;++k){
			if(this.entries[k]==null)
				continue;
			sb.append(this.entries[k].toString());
			sb.append(',');
			--s;
			if(s==0)
				break;
		}
		return sb.toString();
	}
	
	//
	private int hash(K key){
		if(key==null)
			return 0;
		int h=key.hashCode();
		h^=(h>>11);
		if(h<0)
			h=-h;
		return h%this.capacity;
	}
	//
	private int search(K key){
		int index,i0;
		int hash0=hash(key);
		index=hash0;
		i0=(index-1+this.capacity)%this.capacity;
		while(i0!=index&&this.entries[index]!=null)
			if(hash(entries[index].key)==hash0&&entries[index].key.equals(key))
				return index;
			else
				index=(1+index)%this.capacity;//probe next entry
		return -1-index;
	}
}
