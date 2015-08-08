package tschulenbergsortscomparison;

import java.io.*;

public class TextFile {

	public static String read(String fileName) throws IOException {
		File file = new File(fileName);
		int size = (int) file.length();
		int chars_read = 0;
		FileReader in = new FileReader(file);
		char[] data = new char[size];
		chars_read = in.read(data, 0, size);
		in.close();
		return new String(data, 0, chars_read);
	}

	public static void write(String filename, String text) throws IOException {
		File file = new File(filename);
		FileWriter out = new FileWriter(file);
		out.write(text);
		out.close();
	}
}
