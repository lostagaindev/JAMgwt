package com.lostagain.JamGwt.JargScene.debugtools;

import java.util.ArrayList;
import java.util.Iterator;

import com.darkflame.client.semantic.SSSNode;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

/** used to display details on a semantic SSSNode object.
 * This includes, for example, its parent and child glass's.
 * ie, Apple might have "fruit" as a parent
 * Wood might have "flamable" as a parent 
 * 
 * @author Tom**/
public class NodeResultLabel extends Composite {

	Label DeducedFrom;
	Label EquivilentURIs;
	Label ParentsURI;
	Label DirectChildURI;
	
	/** used to display details on a semantic SSSNode object.
	 * This includes, for example, its parent and child glass's.
	 * ie, Apple might have "fruit" as a parent
	 * Wood might have "flamable" as a parent 
	 * 
	 * created from a disclosure panel that can open and show details - including other sub NodeResultLabels
	 * @author Tom**/
	public NodeResultLabel(SSSNode node) {

		// replace header in future with one that gives more information? (rather then just the getPLabel)
		DisclosurePanel disclosurePanel = new DisclosurePanel(node.getPLabel());
		disclosurePanel.setOpen(true);
		
		//set this composite to use the above created DisclosurePanel
		initWidget(disclosurePanel);
		//set the title (note title in this context is just the tool tip you get on mouse over)
		disclosurePanel.setTitle(node.getPURI()+"  ("+node.getAllPLabels()+")  ");
		//guess what we set here?		
		disclosurePanel.setWidth("100%");

		//panel to put the node details in
		VerticalPanel nodeDetails = new VerticalPanel();
		//put the above panel in the disclosure panel
		disclosurePanel.setContent(nodeDetails);
		nodeDetails.setSize("100%", "4cm"); //not sure why i decided to use cm here probably random tom

		//make a label from the Equivalent node uris.
		//that is, other nodes which claim to be identical to (ie, other names) for this one
		EquivilentURIs = new Label("Equivilent: "
				+ node.getEquivilentsAsString());
		nodeDetails.add(EquivilentURIs);

		//make a label for this nodes parents
		//as well as a panel to put them into
		ParentsURI = new Label("Parents: ");// +node.getDirectParentsAsString());
		HorizontalPanel parentNodes = new HorizontalPanel();

		parentNodes.add(ParentsURI);
		nodeDetails.add(parentNodes);
		
		//make a label for this nodes directchildren
		// as well as a panel to put them into
		//Note: we only get the direct children, not all the children of children
		HorizontalPanel childNodes = new HorizontalPanel();
		DirectChildURI= new Label("Direct Children: ");
		childNodes.add(DirectChildURI);
		childNodes.add(new Label(node.getDirectChildrenAsString()));
		
		nodeDetails.add(childNodes);
		
		// add parents as new NodeResultLabels like this one
		// Thats right! We heard you liked parent nodes, we 
		//we are putting your parent nodees in your parent nodes (etc)
		//if theres enough parents
		ArrayList<SSSNode> kdpc = node.getKnownDirectParentClasses();
		Iterator<SSSNode> kdpcit = kdpc.iterator();
		while (kdpcit.hasNext()) {

			SSSNode sssNode = (SSSNode) kdpcit.next();
			
			//skip self - dont want to add yourself as a parent to yourself.
			//time travel isnt good for the gene pool
			if (sssNode!=node){			
				parentNodes.add(new NodeResultLabel(sssNode));
			}
		}

		//information on what this node was deduced from
		//normally empty.
		//I think used for merged nodes
		DeducedFrom = new Label("DeducedFrom");
		nodeDetails.add(DeducedFrom);
		
		//close by default
		disclosurePanel.setOpen(false);
	}

}
