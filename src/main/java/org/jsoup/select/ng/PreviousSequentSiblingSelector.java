package org.jsoup.select.ng;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Evaluator;

public class PreviousSequentSiblingSelector extends Evaluator {
	Evaluator sel;
	
	

	public PreviousSequentSiblingSelector(Evaluator sel) {
		this.sel = sel;
	}



	@Override
	public boolean matches(Element element) {
		Element prev = element.previousElementSibling();
		
		while(prev != null) {
			if(sel.matches(prev))
				return true;
			
			prev = prev.previousElementSibling();
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return String.format(":prev*%s", sel);
	}


}
