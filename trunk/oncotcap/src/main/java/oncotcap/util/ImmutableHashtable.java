package oncotcap.util;

import java.util.*;

public class ImmutableHashtable<K, V>
{
	private Hashtable<K, V> backingTable;
	public ImmutableHashtable(Hashtable<K, V> table)
	{
		backingTable = table;
	}

	public void clear(){ throw(new ImmutableError()); }
	public V put(K key, V value){ throw(new ImmutableError()); }
	public void putAll(Map<? extends K,? extends V> t){ throw(new ImmutableError()); }
	public V remove(Object key){ throw(new ImmutableError()); }
	public Object 	clone(){ return(new ImmutableHashtable((Hashtable) backingTable.clone()));}
	public boolean contains(Object value){ return(backingTable.contains(value));}
	public boolean containsKey(Object key){ return(backingTable.containsKey(key));}
	public boolean containsValue(Object value){return(backingTable.containsValue(value));}
	public Enumeration<V> elements(){return(backingTable.elements());}
	public Set<Map.Entry<K,V>> entrySet(){return(backingTable.entrySet());}
	public boolean equals(Object o){return(backingTable.equals(o));}
	public V get(Object key){return(backingTable.get(key));}
	public int hashCode(){return(backingTable.hashCode());}
	public boolean isEmpty(){return(backingTable.isEmpty());}
	public Enumeration<K> keys(){return(backingTable.keys());}
	public Set<K> keySet(){return(backingTable.keySet());}
	public int size(){return(backingTable.size());}
	public String toString(){return(backingTable.toString());}
	public Collection<V> values(){return(backingTable.values());}
	
}
