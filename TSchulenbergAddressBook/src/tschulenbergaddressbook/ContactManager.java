package tschulenbergaddressbook;

import java.io.File;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ContactManager {
	public Contact current; // current selection of linked list
	public Contact top; // top of linked list
	int Count;

	public void main() {
	}

	public ContactManager() {
		top = null;
		current = null;
	}

	public String getCount(Contact contact) { //gets amount of contacts in list

		int answer;

		answer = 0;
		for (contact = top; contact != null; contact = contact.next)
			answer++;
		String GCount = Integer.toString(answer);
		
		return GCount;

	}

	public Contact Insert(Contact after, Contact contact) {
		// Allocate a new class node

		if (after != null) { // We are not inserting at the top of the list
			contact.prev = after;
			contact.next = after.next;
			after.next = contact;
			if (contact.next != null) // Verify there is a next node...
				contact.next.prev = contact;
		} // if (after)

		else { // We are inserting at the top of the list
			contact.next = top;
			if (top != null)
				top.prev = contact;
			top = contact;
			current = contact;
		} // else

		return contact;
	} // public TextNode insert

	public void load(File file) {
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder(); // reads xml from
															// file and then
															// converts the
															// elements into
															// strings
			Document doc = db.parse(file); // to be put into the contact class
			doc.getDocumentElement().normalize();
			System.out.println("Root element " + doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("contact");
			System.out.println("Information of all contacts");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s); // node containing contacts of
												// the S index

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode; // fstElmnt = elements
															// from the fstnode

					Contact contact = new Contact();
					
					//NodeList idElmntLst = fstElmnt.getElementsByTagName("id"); //creates a node 
					//Element idElmnt = (Element) idElmntLst.item(0);			// of all contacts
				//	NodeList id = idElmnt.getChildNodes();
					//System.out.println("id : " + ((Node) id.item(0)).getNodeValue());
					//String IDString = id.item(0).toString();
					//String[] IDSA = IDString.split(":"); //edits the string
					//String trimLN = IDSA[1];
					//String[]splitL = trimLN.split("]");
					//String TrimL = splitL[0].trim();
					//contact.setID(TrimL);
					
					
					NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("last"); // creates a node			
					Element lstNmElmnt = (Element) lstNmElmntLst.item(0);// list of lastname
					NodeList lstNm = lstNmElmnt.getChildNodes();         // in contacts
					System.out.println("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());
					String LstName = lstNm.item(0).toString();
					String[] ALN = LstName.split(":");
					String Trimalan = ALN[1];
					String[] BlahLN = Trimalan.split("]");
					String TrimedLN = BlahLN[0].trim();
					contact.setLastName(TrimedLN);
					
					NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("first"); // creates a node									
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);// list of firstname
					NodeList fstNm = fstNmElmnt.getChildNodes();// in contacts
					System.out.println("First Name : " + ((Node) fstNm.item(0)).getNodeValue());
					String FrstName = fstNm.item(0).toString();
					String[] AFN = FrstName.split(":");
					String TrimF = AFN[1];
					String[] TFN = TrimF.split("]");
					String TrimedFN = TFN[0].trim();
					contact.setFirstName(TrimedFN);
					
					NodeList phoneElmntLst = fstElmnt.getElementsByTagName("phone"); // creates a node
					Element phoneElmnt = (Element) phoneElmntLst.item(0);// list of phone #'s
					NodeList phone = phoneElmnt.getChildNodes();// in contacts
					System.out.println("phone : " + ((Node) phone.item(0)).getNodeValue());
					String Phonenum = phone.item(0).toString();
					String[] Fone = Phonenum.split(":");
					String Fphone = Fone[1];
					String[] Fphony = Fphone.split("]");
					String TrimedPhone = Fphony[0].trim();
					contact.setPhoneNumber(TrimedPhone);
					
					NodeList emailElmntLst = fstElmnt.getElementsByTagName("email"); // creates a node									
					Element emailElmnt = (Element) emailElmntLst.item(0);// list of emails in
					NodeList email = emailElmnt.getChildNodes();// contacts
					System.out.println("email : " + ((Node) email.item(0)).getNodeValue());
					String EMAIL = email.item(0).toString();
					String[] LAIM = EMAIL.split(":");
					String LAIME = LAIM[1];
					String[] LAIMES = LAIME.split("]");
					String TEmail = LAIMES[0].trim();
					contact.setEmail(TEmail);
					
					
					Insert(null, contact); // sends current contact class to
					Count++;						// insert method to load it into
											// linked list

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Contact getCurrent() { // gets current contact from linked list
		return current;
	}

	public Contact setCurrent(Contact contact) { // sets current contact
		return contact;
	}

	public Contact delete(Contact contact) { // deletes current contact
		Contact returnNode;

		if (contact == top) /* delete top node */
		{
			if (contact.next != null) /* not the only node in list */
			{
				returnNode = contact.next;
				current = contact.next;
				top = returnNode;
				returnNode.prev = null;
			} else /* only node in list */
			{
				returnNode = null;
				top = null;
			}
		} else /* not deleting top node */
		{
			if (contact.next != null) /* not the last node in the list */
			{
				returnNode = contact.next;
				current = contact.next;
				returnNode.prev = contact.prev;
				contact.prev.next = returnNode;
			} else /* deleting last node in list */
			{
				returnNode = contact.prev;
				current = contact.prev;
				returnNode.next = null;
			}
		}

		return (returnNode);
	}

	public Contact getPrevious(Contact contact) { // gets previous contact in
													// list
		contact = contact.prev;
		current = contact;
		return contact;
	}

	public Contact getNext(Contact contact) { // gets next contact in list
		contact = contact.next;
		current = contact;

		return contact;
	}

	public Contact getFirst() { // gets first contact in list
		current = top;
		return top;
	}

	public Contact getLast(Contact contact) { // gets last contact in list
		Contact Last = null;
		for (contact = top; contact != null; contact = contact.next) {
			Last = contact;
			//if (contact.next == null)	
				//return contact;
		}
		current = Last;
		return Last;
	}

	public void quit(Contact contact) { // saves contacts in linked list into a
										// document builder and then saves them
										// to a file
		try {
			
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = fact.newDocumentBuilder();
			
			Document doc = parser.newDocument();// creating a new DOM-document
		
			
			Node root = doc.createElement("contacts");
			doc.appendChild(root);
			for (contact = top; contact != null; contact = contact.next) { 

			Node stanza = doc.createElement("contact");
			root.appendChild(stanza);

			Node line = doc.createElement("id");
			stanza.appendChild(line);
			line.appendChild(doc.createTextNode(contact.ID));
			
			line = doc.createElement("last");
			stanza.appendChild(line);
			line.appendChild(doc.createTextNode(contact.lastName));
			
			line = doc.createElement("first");
			stanza.appendChild(line);
			line.appendChild(doc.createTextNode(contact.firstName));
			
			line = doc.createElement("phone");
			stanza.appendChild(line);
			line.appendChild(doc.createTextNode(contact.phoneNumber));
			
			line = doc.createElement("email");
			stanza.appendChild(line);
			line.appendChild(doc.createTextNode(contact.email));
		
			XMLSerializer serializer = new XMLSerializer();
			
			serializer.setOutputCharStream(new java.io.FileWriter("Test_Addresses_Output.xml"));
			//outputs xml document into "Test_Addresses_Output.xml"
			serializer.serialize(doc);
			}
			
			

		} catch (Exception ex) {
		}
		System.exit(1);
	}

	public Contact Search(Contact contact, String q) {
		
		contact = top;   //searches through linked list for contact with matching last name
		while ( contact != null )
			if (q.equals(contact.getLastName()))
					return contact;
			else
				contact = contact.next;
			
		return contact;
		
		
	}
}
