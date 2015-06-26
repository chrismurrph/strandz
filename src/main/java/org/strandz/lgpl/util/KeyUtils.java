/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.util;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * User: chris
 * Date: 2/09/2005
 * Time: 15:52:15
 */
public class KeyUtils
{

    public static void main(String[] args)
    {
        JTextField component = new JTextField();
//        String keyStr = "ctrl pressed C";
//        FindResult r = find(KeyStroke.getKeyStroke( keyStr), component);
//        Err.pr( keyStr + " - " + r);
//
//        keyStr = "ctrl released C";
//        r = find(KeyStroke.getKeyStroke( keyStr), component);
//        Err.pr( keyStr + " - " + r);
//
//        keyStr = "C";
//        r = find(KeyStroke.getKeyStroke( keyStr), component);
//        Err.pr( keyStr + " - " + r);
//
//        keyStr = "typed C";
//        r = find(KeyStroke.getKeyStroke( keyStr), component);
//        Err.pr( keyStr + " - " + r);
//
//        Character keyCh = new Character('\u0002');
//        r = find(KeyStroke.getKeyStroke( keyCh, 0), component);
//        Err.pr( keyCh + " - " + r);

//        Character keyCh = new Character('\t');
//        FindResult r = find(KeyStroke.getKeyStroke( keyCh, 0), component);
//        Err.pr( keyCh + " - " + r);

        int keyEvent = KeyEvent.VK_ENTER;
        FindResult r = find(KeyStroke.getKeyStroke(keyEvent, 0), component);
        Err.pr("VK_ENTER - " + r);
        keyEvent = KeyEvent.VK_TAB;
        r = find(KeyStroke.getKeyStroke(keyEvent, 0), component);
        Err.pr("VK_TAB - " + r);
    }

    /**
     * The source for this is
     * http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html
     *
     * @param control
     */
    public static void addEnterKey(JComponent control)
    {
        Set forwardKeys = control.getFocusTraversalKeys(
            KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set newForwardKeys = new HashSet(forwardKeys);
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        control.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
            newForwardKeys);
    }

    // The results of a find are returned in a FindResult object
    public static class FindResult
    {
        // Non-null if the keystroke is in an inputmap
        InputMap inputMap;

        // Non-null if the keystroke is in an keymap or default action
        Keymap keymap;

        // Non-null if the keystroke is in a default action
        // The keymap field holds the keymap containing the default action
        public Action defaultAction;

        // If true, the keystroke is in the component's inputMap or keymap
        // and not in one of the inputMap's or keymap's parent.
        boolean isLocal;

        String name;

        public String toString()
        {
            StringBuffer b = new StringBuffer();

            b.append("name: " + name + ", inputmap=" + inputMap + ",keymap=" + keymap
                + ",defaultAction=" + defaultAction + ",isLocal=" + isLocal);
            return b.toString();
        }
    }

    /**
     * The source for this is
     * http://javaalmanac.com/egs/javax.swing/FindKeyBind.html
     *
     * @param k
     * @param c
     */
    // Returns null if not found
    public static FindResult find(KeyStroke k, JComponent c)
    {
        FindResult result;

        result = find(k, c.getInputMap(JComponent.WHEN_FOCUSED));
        if(result != null)
        {
            return result;
        }
        result = find(k, c.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
        if(result != null)
        {
            return result;
        }
        result = find(k, c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW));
        if(result != null)
        {
            return result;
        }

        // Check keymaps
        if(c instanceof JTextComponent)
        {
            JTextComponent tc = (JTextComponent) c;
            result = new FindResult();

            // Check local keymap
            Keymap kmap = tc.getKeymap();
            if(kmap.isLocallyDefined(k))
            {
                result.keymap = kmap;
                result.isLocal = true;
                return result;
            }

            // Check parent keymaps
            kmap = kmap.getResolveParent();
            while(kmap != null)
            {
                if(kmap.isLocallyDefined(k))
                {
                    result.keymap = kmap;
                    return result;
                }
                kmap = kmap.getResolveParent();
            }

            // Look for default action
            if(k.getKeyEventType() == KeyEvent.KEY_TYPED)
            {
                // Check local keymap
                kmap = tc.getKeymap();
                if(kmap.getDefaultAction() != null)
                {
                    result.keymap = kmap;
                    result.defaultAction = kmap.getDefaultAction();
                    result.isLocal = true;
                    result.name = kmap.getName();
                    return result;
                }

                // Check parent keymaps
                kmap = kmap.getResolveParent();
                while(kmap != null)
                {
                    if(kmap.getDefaultAction() != null)
                    {
                        result.keymap = kmap;
                        result.defaultAction = kmap.getDefaultAction();
                        return result;
                    }
                    kmap = kmap.getResolveParent();
                }
            }
        }
        return null;
    }

    public static FindResult find(KeyStroke k, InputMap map)
    {
        // Check local inputmap
        KeyStroke[] keys = map.keys();
        for(int i = 0; keys != null && i < keys.length; i++)
        {
            if(k.equals(keys[i]))
            {
                FindResult result = new FindResult();
                result.inputMap = map;
                result.isLocal = true;
                return result;
            }
        }

        // Check parent inputmap
        map = map.getParent();
        while(map != null)
        {
            keys = map.keys();
            for(int i = 0; keys != null && i < keys.length; i++)
            {
                if(k.equals(keys[i]))
                {
                    FindResult result = new FindResult();
                    result.inputMap = map;
                    return result;
                }
            }
            map = map.getParent();
        }
        return null;
    }
    
    public static void debugComponent(JComponent control)
    {
        ActionMap actionMap = control.getActionMap();
        Print.prMap( actionMap);
    }
    
    private static DefaultTransferHandler defaultTransferHandler = new DefaultTransferHandler();

    /**
     * After XMLEncoding the handler has gone. Here we explicitly use 
     * JTextComponent.installDefaultTransferHandlerIfNecessary() to put one (back) in.
     * TODO - raise a Swing bug on this
     * TransferHandlerBugDemo - used to try to evidence this, but unfortunately it seems to work!
     * 
     * @param control
     */
    public static void installLostClipboardActions( JComponent control)
    {
//        ActionMap actionMap = control.getActionMap();
//        Action copyAction = actionMap.get( "copy"); 
//        Action cutAction = actionMap.get( "cut"); 
//        Action pasteAction = actionMap.get( "paste");
        if(control.getTransferHandler() == null)
        {
            installDefaultTransferHandlerIfNecessary( control);
            /*
            actionMap.put( "copy", null);
            actionMap.put( "cut", null);
            actionMap.put( "paste", null);
            //Not necessary
            control.setActionMap( actionMap);
            */
            /*
            control.getActionMap().remove(DefaultEditorKit.copyAction);
            control.getActionMap().remove(DefaultEditorKit.cutAction);
            control.getActionMap().remove(DefaultEditorKit.pasteAction);
            Assert.isTrue(control.getActionMap().get(DefaultEditorKit.copyAction) == null,
                          "Setting null action for copy failed for " +
                                  control.getClass().getName() + ", " + control.getName());
            Err.pr( "have removeUnhandledClipboardActions from " + control.getName());
            */
        }
        else
        {
            //Shows that the mDefs are ok
            //Err.pr( "No need to removeUnhandledClipboardActions from " + control.getName());
        }
    }    
    
    /**
     * If the current <code>TransferHandler</code> is null, this will
     * install a new one.
     */
    private static void installDefaultTransferHandlerIfNecessary( JComponent comp) 
    {
        if (comp.getTransferHandler() == null) 
        {
            comp.setTransferHandler(defaultTransferHandler);
        }
    }

    public static void debugComponent(JComponent control, KeyStroke keyStroke)
    {
        ActionMap actionMap = control.getActionMap();
        InputMap inputMap = control.getInputMap(JComponent.WHEN_FOCUSED);
        if(actionMap.allKeys() != null)
        {
            Print.prArray(actionMap.allKeys(), "Keep all these actions, but will add one of ours");
        }
        else
        {
            Err.pr("No actionMap on " + control.getClass().getName());
        }
        if(inputMap.allKeys() != null) //Is actually null so we will create one
        {
            Print.prArray(inputMap.allKeys(), "To replace ENTER key");
        }
        else
        {
            Err.pr("No inputMap on " + control.getClass().getName());
        }
        KeyUtils.FindResult r = KeyUtils.find(keyStroke, control);
        Err.pr("KEY: " + r);
    }

    /**
     * Set the up-cycle key for a particular component
     * As they said in the book (pg 1108), due to a note you have
     * to press the key twice.
     *
     * @param comp component want to up-cycle from
     * @param key  the keyboard key that want to use to up-cycle
     */
    public static void setUpCycle(JComponent comp, int key)
    {
        /*
        java.util.Set upKeys = new java.util.HashSet(1);
        upKeys.add( AWTKeyStroke.getAWTKeyStroke( key, 0));
        comp.setFocusTraversalKeys( KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, upKeys);
        */
        //
        Set upKeys = comp.getFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS);
        Set newUpKeys = new HashSet(upKeys);
        newUpKeys.add(AWTKeyStroke.getAWTKeyStroke(key, 0));
        comp.setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS,
            newUpKeys);
    }
    
    public static void debugFocusTraversalPolicy(Container focusCycleRoot)
    {
        FocusTraversalPolicy policy = focusCycleRoot.getFocusTraversalPolicy();
        Err.pr("firstComponent: " + policy.getFirstComponent(focusCycleRoot));
        Err.pr("defaultComponent: " + policy.getDefaultComponent(focusCycleRoot));
        Err.pr("IS defaultComponent focus owner?: "
            + policy.getDefaultComponent(focusCycleRoot).isFocusOwner());
        Err.pr("lastComponent: " + policy.getLastComponent(focusCycleRoot));
    }

    public static void debugFocusState(Container comp)
    {
        Err.pr("......................BEGIN");
        Err.pr("isFocusOwner: " + comp.isFocusOwner());
        Err.pr("hasFocus: " + comp.hasFocus());
        //
        Err.pr("focusTraversalKeysEnabled: " + comp.getFocusTraversalKeysEnabled());
        Err.pr("UP: "
            + Utils.elementsOfSet(comp.getFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS)));
        Err.pr("DOWN: "
            + Utils.elementsOfSet(comp.getFocusTraversalKeys(KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS)));
        Err.pr("FORWARD: "
            + Utils.elementsOfSet(comp.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS)));
        Err.pr("BACKWARD: "
            + Utils.elementsOfSet(comp.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS)));
        debugFocusTraversalPolicy(comp);
        Err.pr("......................END");
    }

    public static void debugGlobalFocusState()
    {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Err.pr("---------------");

        // Window activeWindow = manager.getActiveWindow();
        // Err.pr( "         activeWindow: " + activeWindow);

        Container currentFocusCycleRoot = manager.getCurrentFocusCycleRoot();
        Err.pr("currentFocusCycleRoot: " + currentFocusCycleRoot.getClass().getName()
            + ", " + currentFocusCycleRoot);
        debugFocusState(currentFocusCycleRoot);

        // Window focusedWindow = manager.getFocusedWindow();
        // Err.pr( "        focusedWindow: " + focusedWindow);

        Component focusOwner = manager.getFocusOwner();
        Err.pr("           focusOwner: " + focusOwner.getClass().getName() + ", "
            + focusOwner);

        Component permFocusOwner = manager.getPermanentFocusOwner();
        Err.pr("       permFocusOwner: " + permFocusOwner);
        Err.pr("---------------");
    }
    
/*
        Set keys = control.getFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        AWTKeyStroke awtKeyStroke = (AWTKeyStroke)keys.toArray()[0];
        //Print.prSet( keys);
        KeyStroke keyStroke = KeyStroke.getKeyStroke( 
                        awtKeyStroke.getKeyChar(), awtKeyStroke.getModifiers());  
        //Err.pr( "action: " + control.getActionMap().get( keyStroke));
        //Err.pr( "action: " + control.getActionMap().get( );
        //KeyUtils.debugComponent( control, keyStroke);
*/
}
