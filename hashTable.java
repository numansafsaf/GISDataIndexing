import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implements a generic chained hash table, using an ArrayList of LinkedLists
 * for the physical table.
 *
 * The ArrayList has a default size of 256 slots, configurable via the class
 * constructor.
 *
 * The size of the ArrayList is doubled when the load factor exceeds the load
 * limit (defaulting to 0.7, but configurable via the class constructor).
 *
 * Elements inserted to the table must implement the Hashable interface:
 *
 * public int Hash();
 *
 * This allows the user to choose an appropriate hash function, rather than
 * being tied to a fixed hash function selected by the table designer.
 */
public class hashTable<T extends Hashable<T>> {
	private ArrayList<LinkedList<T>> table; // physical basis for the hash table
	private Integer numElements = 0; // number of elements in all the chains
	private Double loadLimit = 0.7; // table resize trigger
	private final Integer defaultTableSize = 256; // default number of table slots
	private int tableSize;
	int maxSlots = 0;

	/**
	 * Constructs an empty hash table with the following properties: Pre: - size is
	 * the user's desired number of lots; null for default - ldLimit is user's
	 * desired load factor limit for resizing the table; null for the default Post:
	 * - table is an ArrayList of size LinkedList objects, 256 slots if size == null
	 * - loadLimit is set to default (0.7) if ldLimit == null
	 */
	public hashTable(Integer size, Double ldLimit) {
		if (size == null) {
			table = new ArrayList<LinkedList<T>>(defaultTableSize);
			tableSize = defaultTableSize;
		} else {
			table = new ArrayList<LinkedList<T>>(size);
			tableSize = size;
		}
		if (ldLimit != null) {
			loadLimit = ldLimit;

		}
		for (int i = 0; i < tableSize; i++) {
			table.add(new LinkedList<T>());
		}

	}

	/**
	 * Inserts elem at the front of the elem's home slot, unless that slot already
	 * contains a matching element (according to the equals() method for the user's
	 * data type. Pre: - elem is a valid user data object Post: - elem is inserted
	 * unless it is a duplicate - if the resulting load factor exceeds the load
	 * limit, the table is rehashed with the size doubled Returns: true iff elem has
	 * been inserted
	 */
	public boolean insert(T elem) {
		int index = elem.Hash();
		index = index % tableSize;
		// table.get(index).add(elem);

		if (table.get(index).contains(elem)) {
			// duplicate
			 ((nameEntry) table.get(index).get(table.get(index).indexOf(elem))).locations().add(((nameEntry) elem).locations().get(0));
			return true;
		}

		else {
			table.get(index).add(elem);
		}
		numElements++;

		if (numElements > tableSize * loadLimit) {
			resize();
		}
		return true;
	}

	/**
	 * helper for insert to resize table
	 */
	private void resize() {
		tableSize *= 2;
		ArrayList<LinkedList<T>> temp = table;
		numElements = 0;
		table = new ArrayList<LinkedList<T>>(tableSize);
		for (int i = 0; i < tableSize; i++) {
			table.add(new LinkedList<T>());
		}

		
		for (LinkedList<T> linkedList : temp) {
			for (T elem : linkedList) {
				insert(elem);
			}
		}
		

	}

	/**
	 * Searches the table for an element that matches elem (according to the
	 * equals() method for the user's data type). Pre: - elem is a valid user data
	 * object Returns: reference to the matching element; null if no match is found
	 */
	public T find(T elem) {
		for (int i = 0; i < tableSize; i++) {
			if (table.get(i).contains(elem)) {
				return table.get(i).get(table.get(i).indexOf(elem));
			}

		}
		return null;
	}

	/**
	 * Removes a matching element from the table (according to the equals() method
	 * for the user's data type). Pre: - elem is a valid user data object Returns:
	 * reference to the matching element; null if no match is found
	 */
	public T remove(T elem) {
		T temp = null;
		for (int i = 0; i < tableSize; i++) {

			if (table.get(i).contains(elem)) {
				temp = table.get(i).get(table.get(i).indexOf(elem));
				table.get(i).remove(elem);
			}
		}
		return temp;
	}

	// Not necessary for this assignment
	/**
	 * Writes a formatted display of the hash table contents. Pre: - fw is open on
	 * an output file
	 */
	public void display(RandomAccessFile fw) throws IOException {
		getMaxSlots();
		fw.writeBytes("Number of elements: " + numElements + "\n");
		fw.writeBytes("Number of slots: " + table.size() + "\n");
		fw.writeBytes("Maximum elements in a slot: " + maxSlots + "\n");
		fw.writeBytes("Load limit: " + loadLimit + "\n");
		fw.writeBytes("\n");

		fw.writeBytes("Slot Contents\n");
		for (int idx = 0; idx < table.size(); idx++) {

			LinkedList<T> curr = table.get(idx);

			if (curr != null && !curr.isEmpty()) {

				fw.writeBytes(String.format("%5d: %s\n", idx, curr.toString()));
			}
		}
	}
	public void getMaxSlots() {
		maxSlots =  0;
		for (LinkedList<T> linkedList : table) {
			if (linkedList.size() > maxSlots) {
				maxSlots = linkedList.size();
			}
		}
	}
}