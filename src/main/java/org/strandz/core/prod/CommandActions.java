/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.core.prod;

import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.lgpl.util.CapabilityAction;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.OperationAction;
import org.strandz.lgpl.util.Print;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CommandActions
{
    private Map cmds = new HashMap();
    /**
     * capabilities includes cmds, because cmds are really capabilities
     */
    private Map capabilities = new HashMap();

    CommandActions
        (CommandActions state)
    {
        this.cmds.putAll(state.cmds);
        this.capabilities.putAll(state.capabilities);
    }

    public CommandActions()
    {
        initOperation(OperationEnum.EXECUTE_QUERY, new ExecuteQuery());
        initOperation(OperationEnum.EXECUTE_SEARCH, new ExecuteSearch());
        initOperation(OperationEnum.INSERT_AFTER_PLACE, new ExecuteInsert());
        initOperation(OperationEnum.INSERT_AT_PLACE, new ExecuteInsertPrior());
        initOperation(OperationEnum.REMOVE, new ExecuteRemove());
        initOperation(OperationEnum.POST, new ExecutePost());
        initOperation(OperationEnum.REFRESH, new ExecuteRefresh());
        initOperation(OperationEnum.COMMIT_ONLY, new ExecuteCommitOnly());
        initOperation(OperationEnum.COMMIT_RELOAD, new ExecuteCommitReload());
        initOperation(OperationEnum.PREVIOUS, new ExecutePrevious());
        initOperation(OperationEnum.NEXT, new ExecuteNext());
        initOperation(OperationEnum.SET_ROW, new ExecuteSetRow());
        initOperation(OperationEnum.GOTO_NODE, new ExecuteGoToNode());
        initCapability(OperationEnum.ENTER_QUERY, new ExecuteEnterQuery());
        initCapability(CapabilityEnum.CASCADE_DELETE, new CascadeDelete());
        initCapability(CapabilityEnum.FOCUS_NODE, new FocusCausesNodeChange());
        initCapability(CapabilityEnum.BLANK_RECORD, new BlankRecord());
        initCapability(CapabilityEnum.UPDATE, new Update());
        initCapability(CapabilityEnum.IGNORED_CHILD, new IgnoredChild());
        initCapability(CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT,
            new EditInsertsBeforeCommit());
        initCapability(CapabilityEnum.GOTO_NODE_IGNORE, new GoToNodeIgnore());
        initCapability(CapabilityEnum.SET_VALUE, new SetValue());
        initCapability(CapabilityEnum.MANUALLY_INITIATED, new ManuallyInitiated());
    }

    private void initOperation(OperationEnum enumId, OperationAction oa)
    {
        cmds.put(enumId, oa);
        capabilities.put(enumId, oa);
    }

    private void initCapability(CapabilityEnum enumId, CapabilityAction oa)
    {
        capabilities.put(enumId, oa);
    }

    public void setNodeDefaults()
    {
        // Err.pr( "In setNodeDefaults()");
        for(int i = 0; i <= CapabilityEnum.ALL_KNOWN_CAPABILITIES.length - 1; i++)
        {
            CapabilityAction cap = get(CapabilityEnum.ALL_KNOWN_CAPABILITIES[i]);
            cap.setNodeAllowed(cap.getDefaultAllowed());
        }
    }

    public OperationAction get(OperationEnum enumId)
    {
        OperationAction result = (OperationAction) cmds.get(enumId);
        if(result == null)
        {
            Err.error("Could not find a OperationAction for OperationEnum " + enumId);
        }
        return result;
    }

    public CapabilityAction get(CapabilityEnum enumId)
    {
        CapabilityAction result = (CapabilityAction) capabilities.get(enumId);
        if(result == null)
        {
            Print.prMap(capabilities);
            Err.error("Could not find a CapabilityAction for CapabilityEnum " + enumId);
        }
        return result;
    }

    /**
     * When have a new block we assign to the runtime allowed
     * (blockAllowed) value, using the design time allowed
     * value (nodeAllowed).
     */
    void notifyBlockConstructed()
    {
    }

    /**
     * We have assumed that the intention was for this to be done to
     * all known capabilities
     */
    public void setDynamicAllowed()
    {
        // Err.pr( "In setDynamicAllowed()");
        for(int i = 0; i <= CapabilityEnum.ALL_KNOWN_CAPABILITIES.length - 1; i++)
        {
            CapabilityAction cap = get(CapabilityEnum.ALL_KNOWN_CAPABILITIES[i]);
            cap.setBlockAllowed(cap.isNodeAllowed());
        }
        /*
        setDynamicAllowed( node.isUpdate(), CapabilityEnum.UPDATE);
        setDynamicAllowed( node.isEnterQuery(), OperationEnum.ENTER_QUERY);
        setDynamicAllowed( node.isExecuteQuery(), OperationEnum.EXECUTE_QUERY);
        setDynamicAllowed( node.isExecuteSearch(), OperationEnum.EXECUTE_SEARCH);
        setDynamicAllowed( node.isInsert(), OperationEnum.INSERT);
        setDynamicAllowed( node.isRemove(), OperationEnum.REMOVE);
        setDynamicAllowed( node.isPost(), OperationEnum.POST);
        setDynamicAllowed( node.isCommitOnly(), OperationEnum.COMMIT_ONLY);
        setDynamicAllowed( node.isCommitReload(), OperationEnum.COMMIT_RELOAD);
        setDynamicAllowed( node.isPrevious(), OperationEnum.PREVIOUS);
        setDynamicAllowed( node.isNext(), OperationEnum.NEXT);
        setDynamicAllowed( node.isSetRow(), OperationEnum.SET_ROW);
        setDynamicAllowed( node.isEditInsertsBeforeCommit(), CapabilityEnum.EDIT_INSERTS_BEFORE_COMMIT);
        setDynamicAllowed( node.isBlankRecord(), CapabilityEnum.BLANK_RECORD);
        setDynamicAllowed( node.isCascadeDelete(), CapabilityEnum.CASCADE_DELETE);
        setDynamicAllowed( node.isFocusCausesNodeChange(), CapabilityEnum.FOCUS_NODE);
        */
    }

    private static class ExecuteEnterQuery extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteQuery extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteSearch extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteInsert extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteInsertPrior extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteRemove extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecutePost extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }

    
    private static class ExecuteRefresh extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }
    

    private static class ExecuteCommitOnly extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return false;
        }
    }


    private static class ExecuteCommitReload extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecutePrevious extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteNext extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteSetRow extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class ExecuteGoToNode extends OperationAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class CascadeDelete extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class FocusCausesNodeChange extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class BlankRecord extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return false;
        }
    }


    private static class Update extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }

        public void setBlockAllowed(boolean blockAllowed)
        {
            super.setBlockAllowed(blockAllowed);
            // Err.pr( "BlockAllowed been set to " + blockAllowed);
        }

        public void setNodeAllowed(boolean nodeAllowed)
        {
            super.setNodeAllowed(nodeAllowed);
            // Err.pr( "NodeAllowed been set to " + nodeAllowed);
        }
    }


    private static class Enter extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class IgnoredChild extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return false;
        }
    }


    private static class EditInsertsBeforeCommit extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return true;
        }
    }


    private static class GoToNodeIgnore extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return false;
        }
    }


    private static class SetValue extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return false;
        }
    }

    private static class ManuallyInitiated extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return false;
        }
    }

    private static class AllDetails extends CapabilityAction
    {
        public boolean getDefaultAllowed()
        {
            return false;
        }
    }
}
