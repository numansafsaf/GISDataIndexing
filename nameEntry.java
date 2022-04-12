import java.util.ArrayList;

public class nameEntry implements Hashable<nameEntry> {
	String key; // GIS feature name
	ArrayList<Long> locations; // file offsets of matching records

	/**
	 * Initialize a new nameEntry object with the given feature name and a single
	 * file offset.
	 */
	public nameEntry(String name, Long offset) {
		this.key = name;
		this.locations = new ArrayList<Long>();
		this.locations.add(offset);

	}

	/**
	 * Return feature name.
	 */
	public String key() {
		return key;
	}

	/**
	 * Return list of file offsets.
	 */
	public ArrayList<Long> locations() {
		return locations;
	}

	/**
	 * Append a file offset to the existing list.
	 */
	public boolean addLocation(Long offset) {
		return locations.add(offset);

	}

	/** Fowler/Noll/Vo hash function is mandatory for this assignment. **/
	public int Hash() {
		final int fnvPrime = 0x01000193; // Constant values for FNV
		final int fnvBasis = 0x811c9dc5; // hash algorithm
		int hashValue = fnvBasis;
		for (int i = 0; i < key.length(); i++) {
			hashValue ^= key.charAt(i);
			hashValue *= fnvPrime;
		}
		return Math.abs(hashValue);
	}

	/** Two nameEntry objects are considered equal iff they
	* hold the same feature name.
	*/
	public boolean equals(Object other) { 
		if (this.key().equals(((nameEntry) other).key())) {
			return true;
		}
		return false;
			}

	/**
	 * Return a String representation of the nameEntry object in the format needed
	 * for this assignment.
	 */
	public String toString() {
		return ("[" + this.key + ", " + this.locations.toString() + "]");
	}
}