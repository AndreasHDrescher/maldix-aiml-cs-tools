package net.kurbel.cstools.converter;

import org.jsoup.helper.StringUtil;

import net.kurbel.cstools.nodes.CsElementNode;
import net.kurbel.cstools.nodes.CsMatchResponderNode;

public class ChatScriptNode2String 
{
	public static String toText(CsElementNode node) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("topic: ~" + node.getCategory().replace(" ", "_"));
		buffer.append(" [");
		buffer.append(node.getTopic());
		buffer.append("* ");
		buffer.append("] ");
		
		buffer.append("\n");
		
		for (CsMatchResponderNode responder : node.getResponderNodes())
		{
			buffer.append(responderNodeWithConcept(responder));
		}
		
		buffer.append("u: (" );
		buffer.append(node.getTopic());
		buffer.append("*) ^keep()\n");
		
		for (CsMatchResponderNode responder : node.getResponderNodes())
		{
			buffer.append(responderNodePlain(responder));
		}
		
		return buffer.toString();
	}
	
	
	public static String responderNodePlain(CsMatchResponderNode responderNode) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(responderNode.getSentence());
		buffer.append("]\n");
		return buffer.toString();
	}
	
	public static String responderNodeWithConcept(CsMatchResponderNode responderNode) {
		StringBuffer buffer = new StringBuffer();
		int size = responderNode.getConceptList().size();		
		
		if (size > 0) {
			
			addBegin(buffer, responderNode);
			
			if (size > 1)
				buffer.append("[");
			
			for (int i=0; i<size; i++) {			
				buffer.append(responderNode.getConceptList().get(i));
				buffer.append(" ");
			}
			
			if (size > 1)
				buffer.append("] ");
		
			addEnd(buffer, responderNode);
		}
		return buffer.toString();
	}
	
	private static void addBegin(StringBuffer buffer, CsMatchResponderNode responderNode)
	{
		buffer.append(responderNode.getResponderType());
		buffer.append(": ( << ");
		if (!StringUtil.isBlank(responderNode.getParentTopic())) {

			boolean enclosedInBrackets = responderNode.getParentTopic().indexOf(" ") > -1;
			
			if (enclosedInBrackets)
				buffer.append("\"");
			buffer.append(responderNode.getParentTopic());
			buffer.append("* ");
			if (enclosedInBrackets)
				buffer.append("\"");
		}
	}
	
	private static void addEnd(StringBuffer buffer, CsMatchResponderNode responderNode) 
	{
		buffer.append(" >> ) ");
		buffer.append(responderNode.getSentence());			
		buffer.append("\n");
	}
}
