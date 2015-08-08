/**
 * This class can take a variable number of parameters on the command line. Program execution begins with the main() method. The class constructor is
 * not invoked unless an object of type 'Class1' created in the main() method.
 */
public class Class1 {
	/**
	 * The main entry point for the application.
	 * 
	 * @param args
	 *            Array of parameters passed to the application via the command line.
	 */
	public static void main(String[] args) {
		List list = new List();
		TextNode node;

		node = list.insert(null, "Stephen");
		list.insert(null, "Ray");
		list.insert(null, "Don");
		list.insert(null, "Alex");
		list.insert(node, "Roya");

		System.out.println();
		System.out.println("     " + list.toString());

		list.deleteNode(node);
		System.out.println("     " + list.toString());

//		list.printInReverse(list.getFirst());
		
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}
}
