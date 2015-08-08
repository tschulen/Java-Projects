public class TextNode
{
private String text;
TextNode prev;
TextNode next;

public TextNode(String text)  { setText(text);	}
public void setText(String text) { this.text = text;	}
public String getText() { return text;	}
public String toString() { return text; }

}
