public class List {
	private TextNode top;

	public List() {
		top = null;
	}

	// ****************************************************************************
	// insert
	//
	// DESCRIPTION: inserts a node with the given text after the given node.
	//
	// PARAMETERS:
	// after IN: The new node will be inserted after this one.
	// This parameter is null if the new node is to be inserted
	// at the top of the list.
	// text IN: text for the new node.
	//
	// RETURNS:
	// Reference to the new node.
	// ****************************************************************************
	public TextNode insert(TextNode after, String text) {
		TextNode newNode = new TextNode(text); // Allocate a new text node

		if (after != null) { // We are not inserting at the top of the list
			newNode.prev = after;
			newNode.next = after.next;
			after.next = newNode;
			if (newNode.next != null) // Verify there is a next node...
				newNode.next.prev = newNode;
		} // if (after)

		else { // We are inserting at the top of the list
			newNode.next = top;
			if (top != null)
				top.prev = newNode;
			top = newNode;
		} // else

		return newNode;
	} // public TextNode insert

	
	
	public TextNode deleteNode(TextNode node2Del) {
		TextNode returnNode;
		if (node2Del == null)
			return null; /* safety check */
		if (node2Del == top) /* delete top node */
		{
			if (node2Del.next != null) /* not the only node in list */
			{
				returnNode = node2Del.next;
				top = returnNode;
				returnNode.prev = null;
			} else /* only node in list */
			{
				returnNode = null;
				top = null;
			}
		} else /* not deleting top node */
		{
			if (node2Del.next != null) /* not the last node in the list */
			{
				returnNode = node2Del.next;
				returnNode.prev = node2Del.prev;
				node2Del.prev.next = returnNode;
			} else /* deleting last node in list */
			{
				returnNode = node2Del.prev;
				returnNode.next = null;
			}
		}

		// Break the deleted node's connections to other nodes
		node2Del.prev = null;
		node2Del.next = null;

		return (returnNode);
	} /* deleteNode */

	public String toString() {
		TextNode pNode = top;
		StringBuffer sbuf = new StringBuffer();

		if (pNode != null) {
			sbuf.append(pNode.getText());
			pNode = pNode.next;
		}
		while (pNode != null) {
			sbuf.append(", ");
			sbuf.append(pNode.getText());
			pNode = pNode.next;
		}

		return sbuf.toString();
	}

	public void printInReverse(TextNode curNode) {
		if (curNode != null) {
		   printInReverse(curNode.next);
	      System.out.println(curNode.getText());
	      }
	}

	public TextNode getFirst() {
		return top;
	}

}
