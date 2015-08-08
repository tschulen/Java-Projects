package tschulenbergxmlnesting;


import java.util.List;
import java.io.*;
import java.util.*;
import java.util.regex.*;

import javax.xml.parsers.*;

import org.jdom.*;
import org.jdom.Element;

import org.jdom.xpath.*;

import org.w3c.dom.Document;

public class NestingValidator {

	public StringStack current; // current selection of linked list
	public StringStack top; // top of linked list
	public int Count;
	private Document doc;
	String Pattern1 = "<>";
	String Pattern2 = "</>";
	List lstMatches = new ArrayList();
	String[] MatchesList;
	String Output;

	public NestingValidator() {
		top = null;
		current = null;
	}

	public void load(File file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// reads xml from
			// file and then
			// converts the
			// elements into
			// strings
			doc = db.parse(file); // to be used for matching
			doc.getDocumentElement().normalize();
		} catch (Exception e) {

		}
	}

	public void Match() throws JDOMException {
		StringStack Stack = new StringStack();

		String root = doc.getDocumentElement().getNodeName();
		Pattern p = Pattern.compile(Pattern1); // makes comparison pattern for first element	
		Pattern r = Pattern.compile(Pattern2); // makes comparison patter for second element
		java.util.List matches = XPath.selectNodes(doc, root); //makes a list of objects out of elements
		for (Object match : matches) { //transfers elements into an array of strings
			if (match instanceof Element) {
				lstMatches.add(((Element) match).getValue());
				// int t = 0;
				// Object[] array1 = lstMatches.toArray();
				MatchesList = (String[]) lstMatches.toArray(new String[0]);
				// t++;
				// insert(null, StringStack);
				// else
			}
		}
		for (int q = 0; q <= MatchesList.length; q++) { // cycles through array
														// of elements
			Matcher m = p.matcher(MatchesList[q]); // creates matcher for first
													// element

			if (m.lookingAt() == true) { // if first element matches pattern
				Stack.setComparison(MatchesList[q]); // set stack string
															// to first element
				insert(null, Stack); // pop on stack
				q++; // move to next element
			} else {
				Output = ("element" + MatchesList[q] + " is not nested properly on line" + q);
				break;
			}
			Matcher c = p.matcher(MatchesList[q]); // creates matcher for second
													// element
			if (c.lookingAt() == true) { // if second element matches pattern
				String Holder = MatchesList[q]; // puts in holder string for
												// compare editing
				String[] Holder2 = Holder.split("/"); // splits string to
														// compare with first
														// element
				String Holder3 = Holder2[1]; // assigns element to string to be
												// rededited for checking
				Holder3 = "<" + Holder3; // adds on < to make element match
											// first element ie <element>
				Holder3 = Holder3.trim(); // removes any white spaces
				if (Holder3.equals(top.Comparison)) { // compares element with
														// top element in stack
					delete(top); // deletes top of stack
				} else {
					int Line = q/2;
					Output = ("invalid nesting, elements do not match on line" + Line); // if invalid prints
															// error
					delete(top); // deletes top of stack
					break;
				}
			} else {
				int Line = q/2;
				Output = ("element" + MatchesList[q] + " is not nest properly on line" + Line);
				break;
			}
			Output = ("XML follows proper nesting");
			}
	}

	public StringStack insert(StringStack after, StringStack Stack) {

		if (after != null) {
			Stack.prev = after;
			Stack.next = after.next;
			if (Stack.next != null) {
				after.next = Stack;
			}
		} else {
			Stack.prev = null;
			Stack.next = top;
			if (top != null) {
				top.prev = Stack;
				top = Stack;
			}
		}
		return Stack;

	}

	public StringStack delete(StringStack Stack) { // deletes current Stack in
		// stacklist after
		// comparison
		StringStack returnNode = null;

		if (Stack == top) /* delete top node of Stacklist */
		{
			if (Stack.next != null) /*
												 * not the only node in
												 * Stacklist
												 */
			{
				returnNode = Stack.next;
				current = Stack.next;
				top = returnNode;
				returnNode.prev = null;
			} else /* only node in Stacklist */
			{
				returnNode = null;
				top = null;
			}
		}
		return returnNode;
	}
}