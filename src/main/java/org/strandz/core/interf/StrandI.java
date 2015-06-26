package org.strandz.core.interf;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.prod.OperationsProcessor;
import org.strandz.lgpl.util.Publisher;

import java.util.List;

/**
 * User: Chris
 * Date: 18/04/2009
 * Time: 11:16:49 PM
 */
public interface StrandI
{
    List<Node> getNodes();
    boolean SET_ROW(int row);
    Node getCurrentNode();
    Node getNodeByName(String name);
    void goNode( Node target);
    void setNodeController(NodeController c);
    void setRequiresRefresh();
    void setWhenLastVisibleFalseControllerMemento(
        NodeController.ControllerMemento lastControllerMemento);
    boolean execute(OperationEnum op);
    void goNode(Node target, int row);
    Publisher removeAllPreControlActionPerformedTriggers();
    Publisher removeAllPostControlActionPerformedTriggers();
    void addPreControlActionPerformedTrigger(PreOperationPerformedTrigger cal);
    void addPostControlActionPerformedTrigger(PostOperationPerformedTrigger cal);
    void setNewCapabilities(Node localNode, boolean firstTime);
    void generateNodeChangeEvent(Node newNode);
    NodeController getNodeController();
    void addNodeChangeListener(NodeChangeListener listener);
    OperationsProcessor getOPor();
    boolean isTopLevel( Node node);
    void consumeNodesIntoRT( Node newNode, String reason, Node nodeToSetAsCurrent, boolean dontReplaceNewNodeBlock);
    void setCurrentState(StrandState currentState);    
    boolean isAlreadyBeenCustomized();
    void setAlreadyBeenCustomized(boolean alreadyBeenCustomized);
    List retrieveValidateBeanMsg();
}
