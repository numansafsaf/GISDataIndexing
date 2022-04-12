import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class nameIndexer {
	
	private static  hashTable table;
	private static String data;
	private static int counter;
	
	
	
	
	public static void main(String[] args) throws IOException {
		RandomAccessFile writer = new RandomAccessFile(args[1], "rw");
		RandomAccessFile reader = new RandomAccessFile(args[0], "r");
		writer.writeBytes("Processing script file: Script01.txt\n");
		counter = 0;
		String command = reader.readLine();
		System.out.println("[[Lick Draft, [15219]]]".equals("[[Lick Draft, [15219]]]"));
		while(command!= null) {
		 System.out.println(command);
			if(command.startsWith(";")) {
				command = reader.readLine();
				continue;
			}
			
			if (command.contains("index")) {
				counter++;
				command = command.trim();
				data = command.substring(5).trim();
				index(data);
				writer.writeBytes("\nCmd 1: index " + data + "\n");
				writer.writeBytes("------------------------------\n");
				

	}
			else if(command.contains("what_is")) {
				counter++;
				writer.writeBytes("Cmd: " + String.valueOf(counter) +" "+  command + "\n"); 
				command = command.trim();
				//System.out.println(command.substring(8));
				what_is(command.substring(8).trim(), writer);
				

			}
			
	
			else if(command.contains("quit")) {
				counter++;
				writer.writeBytes("Cmd: " + String.valueOf(counter) + " "+command+"\n");
				writer.writeBytes("Found quit command... ending processing...");
				writer.writeBytes("\n------------------------------");

				writer.close();
				reader.close();
				return;
			}
			else if(command.contains("show")) {
				counter++;
				writer.writeBytes("Cmd: " + String.valueOf(counter) + " show hash\n");
				table.display(writer);
				writer.writeBytes("------------------------------\n");

				
			}
			
			
		command = reader.readLine();

		}
		
	}

	public static void index(String filename) throws IOException {
		// hashTable table = new

		 table = new hashTable(null, null);
		RandomAccessFile reader = new RandomAccessFile(filename, "r");
		String line = reader.readLine();
		long pointer = reader.getFilePointer();
		line = reader.readLine();
		//long pointer = reader.getFilePointer();
		while (line != null && line.length() > 0) {

			String[] splitLine = line.split("\\|");

			table.insert(new nameEntry(splitLine[1],pointer));
			pointer = reader.getFilePointer();
			line = reader.readLine();

		}
		reader.close();

	}

	public static void what_is(String feature, RandomAccessFile writer) throws IOException {
		nameEntry entry = new nameEntry(feature,(long) 0.0);
		nameEntry found = (nameEntry) table.find(entry);
		System.out.println(entry);
		RandomAccessFile reader = new RandomAccessFile(data, "r");
		if (found != null)  {
			for (int i = 0; i < found.locations().size();i++) {
				System.out.println(found);
				reader.seek(found.locations().get(i));
				System.out.println(found.locations().get(i));
				
				String line = reader.readLine();
				System.out.println(line);
				String[] newLine = line.split("\\|");
				System.out.println(newLine[8]);
				writer.writeBytes(Long.toString(found.locations.get(i)) + ": " + newLine[2] + " (");
				String longitude = newLine[8].replaceAll("[^0-9]", "");
				System.out.println("longitutde = "+longitude);
				if (longitude.substring(3, 4).equals("0")) {
					if (longitude.substring(5, 6).equals("0")) {
						writer.writeBytes(longitude.substring(0, 3) + "d " + longitude.substring(4, 5) + "m "
								+ longitude.substring(6, 7) + "s");
						System.out.println("yup");
					} else {
						writer.writeBytes(longitude.substring(0, 3) + "d " + longitude.substring(4, 5) + "m "
								+ longitude.substring(5, 7) + "s");
						System.out.println("yup2");
					}

				}

				else {
					if (longitude.substring(5, 6).equals("0")) {
						writer.writeBytes(longitude.substring(1, 3) + "d " + longitude.substring(3, 5) + "m "
								+ longitude.substring(6, 7) + "s");
						System.out.println("yup3");
					}
					else{writer.writeBytes(longitude.substring(1, 3) + "d " + longitude.substring(3, 5) + "m "
							+ longitude.substring(5, 7) + "s");
					System.out.println("yup4");
					}
				}

				if (newLine[8].contains("W")) {
					writer.writeBytes(" West, ");
				} else {
					writer.writeBytes(" East, ");
				}
				
				String latitude = newLine[7].replaceAll("[^0-9]", "");

				if (latitude.substring(2, 3).equals("0")) {
					if (latitude.substring(4, 5).equals("0")) {
						writer.writeBytes(latitude.substring(0, 2) + "d " + latitude.substring(3, 4) + "m "
								+ latitude.substring(5, 6) + "s");

					} else {
						writer.writeBytes(latitude.substring(0, 2) + "d " + latitude.substring(3, 4) + "m "
								+ latitude.substring(4, 6) + "s");
					}

				} else {
					if (latitude.substring(4, 5).equals("0")) {
						writer.writeBytes(latitude.substring(0, 2) + "d " + latitude.substring(2, 4) + "m "
								+ latitude.substring(5, 6) + "s");
					} else {
						writer.writeBytes(latitude.substring(0, 2) + "d " + latitude.substring(2, 4) + "m "
								+ latitude.substring(4, 6) + "s");
					}
				}

				if (newLine[7].contains("N")) {
					writer.writeBytes(" North)\n");
				} else {
					writer.writeBytes(" South)\n");
				}

			}
			}
		else {
			writer.writeBytes("No record matches " + feature +"\n");
		}
		writer.writeBytes("------------------------------\n");
		reader.close();
		}
	}
	

