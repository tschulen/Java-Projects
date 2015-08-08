package tschulenbergspellchecker;

public class TreeNode {
	
public String Data;
public TreeNode Right;
public TreeNode Left;

public String getData() {
	return Data;
}
public void setData(String data) {
	Data = data;
}
public TreeNode getRight() {
	return Right;
}
public void setRight(TreeNode right) {
	Right = right;
}
public TreeNode getLeft() {
	return Left;
}
public void setLeft(TreeNode left) {
	Left = left;
}
public boolean isLeaf(){
	if(Left == null && Right == null){
			return true;
		}else 
			return false;
}

}
