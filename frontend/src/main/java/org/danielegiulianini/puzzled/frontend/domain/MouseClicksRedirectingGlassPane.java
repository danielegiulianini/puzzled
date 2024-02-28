package org.danielegiulianini.puzzled.frontend.domain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 *  Simple implementation of a transparent glass pane that redirects the mouse-clicks events to
 *  the deepest underlying GUI component.
 *  (needed in this project because mouseMotion events handlers for client-pointers (to be 
 *  delivered to all the other clients in the same room) should be 
 *  otherwise attached to any tile button and that approach gives some rendering problems anyway.
 */
public class MouseClicksRedirectingGlassPane extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private Container contentPane;

	public MouseClicksRedirectingGlassPane(Container contentPane)	{
		this.contentPane = contentPane;

		SwingUtilities.invokeLater(() ->{
			setLayout(new BorderLayout());

			//redirecting mouse events to underlying GUI's layers
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					redispatchMouseEvent(e, false);
				}
			});
		});
	}


	//Make the glass pane work (it is invisible)
	public void activate(boolean activated) {
		SwingUtilities.invokeLater(()-> setVisible(activated));
	}

	//A basic implementation of re-dispatching events.
	private void redispatchMouseEvent(MouseEvent e,	boolean repaint) {
		Point glassPanePoint = e.getPoint();
		//Container container = contentPane;
		
		Point containerPoint = SwingUtilities.convertPoint(
				this,
				glassPanePoint,
				contentPane);
		
		if (containerPoint.y < 0) { 
			//we're not in the content pane
			//The mouse event is over non-system window 
			//decorations, such as the ones provided by
			//the Java look and feel. Could handle specially.
		} else {
			//The mouse event is over the content pane.
			//Find out exactly which component it's over.  
			Component component = 
					SwingUtilities.getDeepestComponentAt(
							contentPane,
							containerPoint.x,
							containerPoint.y);

			//System.out.println("the component target of the event is:"+ component);

			if ((component != null) /*&& (component.equals(button))*/) {
				//Forward events over the clicked component.
				Point componentPoint = SwingUtilities.convertPoint(
						this,
						glassPanePoint,
						component);

				component.dispatchEvent(new MouseEvent(component,
						e.getID(),
						e.getWhen(),
						e.getModifiers(),
						componentPoint.x,
						componentPoint.y,
						e.getClickCount(),
						e.isPopupTrigger()));
			}
			//Update the glass pane if requested.
			if (repaint) {
				this.repaint();
			}
		}
	}
}