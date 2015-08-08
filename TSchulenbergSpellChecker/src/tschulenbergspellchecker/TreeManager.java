package tschulenbergspellchecker;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;

public class TreeManager {

	public TreeNode Current;
	public TreeNode Root;
	public String[] TextFile;
	public String[] Dictionary;
	public StringBuffer WordList = new StringBuffer();
	

	public TreeNode LoadTree(TreeNode curNode, String newText) {
		

		if (curNode == null) {
			TreeNode Nodes = new TreeNode();
			Nodes.Data = newText;
			return Nodes;
		} else {
			if (newText.compareTo(curNode.Data) < 0) {
				curNode.Left = LoadTree(curNode.Left, newText);
				return curNode;
			} else {
				curNode.Right = LoadTree(curNode.Right, newText);
				return curNode;
			}
		}
	}


	public void LoadTreeNodeArray(String fileString) {
		String content = fileString;
		//TreeNode Nodes = new TreeNode();


		Dictionary = content.split("\\s");
		int DLength = Dictionary.length;
		int MWord = DLength / 2; // gets placement of middle word
		//Nodes.setData(Dictionary[MWord]);// take middle word in dicitonary and
		// make it tree node
		Root = LoadTree(Root, Dictionary[MWord]);
		Collections.shuffle(Arrays.asList(Dictionary)); // randomizes dictionary
		// for better tree load

		for (int m = 0; m < Dictionary.length; m++) { // traverses through
			// dictionary to add all
			// words
			if (SpellChecker(Dictionary[m], Root) == false) { // if word doesnt
				// already
				// appear in
				// list
				//Nodes.setData(Dictionary[m]); // add word TreeNode
				LoadTree(Root, Dictionary[m]); // Load TreeNode into Tree
			}
		}
	}

	public void LoadTextFile(File file) {
		String Filecontent = null;

		try {
			Filecontent = FileUtils.readFileToString(file);

		} catch (IOException e) {

			e.printStackTrace();
		}
		TextFile = Filecontent.split("\\s+");

	}

	public boolean SpellChecker(String text, TreeNode startNode) {
		TreeNode curNode = startNode;
		int cmpResult;
		

		while (curNode != null) {
			cmpResult = text.compareTo(curNode.Data);
			if (cmpResult < 0)
				curNode = curNode.Left; // continues search in left subtree
			else if (cmpResult == 0)
				return true; // word found
			else
				curNode = curNode.Right; // continues search in right subtree
		}

		return false; // word not found
	}

	public String TakePreceding(String text, TreeNode startNode) {
		TreeNode curNode = startNode;
		int cmpResult;

		while (curNode != null) {
			cmpResult = text.compareTo(curNode.Data);
			if (cmpResult < 0) {
				// checks to see if preceding word is a leaf
				// in order to find the closest preceding word
				if (curNode.getLeft() != null) {
					if (curNode.getLeft().isLeaf() == true) {
						// if leaf curnode is set to leaf
						curNode = curNode.getLeft();
						// returns data of curnode
						return curNode.getData();
					} else {
						curNode = curNode.Left; // continues search in left
						// subtree
					}
				}else {
						return curNode.Data;
					}
				
					
				
			} else {
				// checks to see if right node is null
				// if null returns curnode
				if (curNode.Right != null) {
					// if not null checks to see if right node is leaf
					if (curNode.Right.isLeaf() == true) {
						// if leaf returns right node
						return curNode.Data;
						// if it isnt a leaf checks to see if the right
						// nodes right is null
					} else {
						if (curNode.Right.getRight() != null) {
							curNode = curNode.Right;
							// if not null continues search
							// down right subtree
						} else {
							// if no further right nodes return current data
							curNode = curNode.Right;
						}
					}
				} else {
					return curNode.Data;
				}
			}
		}
		return null;
	}

	public String TakeSuccedding(String text, TreeNode startNode) {
		TreeNode curNode = startNode;
		int cmpResult;
		int SecComp;

		while (curNode != null) { // current node isnt null
			cmpResult = text.compareTo(curNode.Data);
			if (cmpResult < 0){ // Comparison wants to go left
				if (curNode.getLeft() != null) { // if left isnt null
					//sets up a second comparison
					SecComp = text.compareTo(curNode.getLeft().Data);
					// if left child is a leaf
					if (curNode.getLeft().isLeaf() == true) {
						//return current since it is the Succeding word
							return curNode.Data;
						// if not a leaf
						} else{
						// if word is farther down left
							if (SecComp < 0){
								//down left is not equal to null
								if(curNode.getLeft().getLeft() != null){
									curNode = curNode.getLeft(); 
									}
								}else{
							// not a leaf wants to go right
							if (curNode.getLeft().getRight() != null){
								curNode = curNode.getLeft();
						
							}
						}
					}
					}
			}else { //wants to go right
				if (curNode.getRight() != null) {
				// checks to see if Succeding word is a leaf
				// in order to find the closest Succedding word
					SecComp = text.compareTo(curNode.getRight().Data);
					//if right node is a leaf
					if (curNode.getRight().isLeaf() == true) {
						//no succeding words if right node is a leaf
						String Message = "No Succeding Word Exists";
							return Message;	
					} else {
						// if not a leaf see if it wants to travel further down left
						//after traveling down one spot to the right
						if(SecComp < 0){
							curNode = curNode.getRight();
						
						} else{
							return curNode.Data;
						}
					}
				}
			}
		}
		return null;
	}
	public String inorderPrint(TreeNode startNode){
		TreeNode curNode = startNode;
		if(curNode.Left != null)
			inorderPrint(curNode.Left);
		WordList.append(curNode.Data).append(" ");
		if(curNode.Right != null)
			inorderPrint(curNode.Right);
		String s = WordList.toString();
		return s;
	
	}
}
