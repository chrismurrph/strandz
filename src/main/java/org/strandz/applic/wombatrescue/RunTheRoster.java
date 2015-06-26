package org.strandz.applic.wombatrescue;

import org.strandz.applic.util.LoginHelper;
import org.strandz.core.applichousing.MenuTabApplication;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.data.util.UserDetailsFactory;
import org.strandz.data.util.UserDetailsProviderI;
import org.strandz.data.wombatrescue.business.LookupsProvider;
import org.strandz.data.wombatrescue.business.ParticularRosterFactory;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.business.RostererSessionFactory;
import org.strandz.data.wombatrescue.business.RostererSessionI;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;


/**
 * at v.604 but simpler, will do for generation
 */
public class RunTheRoster
{
    private DataStoreFactory dataStoreFactory;
    private MenuTabApplication app;
    private DataStore dataStore;
    
    private static final String DISPLAY_ROSTER = "Display Roster";
    
    private static void createAndShowGUI()
    {
        new RunTheRoster();
    }
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
    
    public RunTheRoster()
    {
        JFrame frame = new JFrame();
        MessageDlg.setFrame( frame);
        Err.setBatch( false);
        dataStoreFactory = new WombatDataStoreFactory();
        dataStoreFactory.addConnection( WombatConnectionEnum.DEMO_WOMBAT_MEMORY);
        dataStore = dataStoreFactory.getDataStore();
        app = new MenuTabApplication( dataStore);
        app.addTitle( DISPLAY_ROSTER, 'd');
        app.addItem( incumbantItem( app, dataStore));
        app.addItem( latestItem( app));
        app.setDataStore( dataStore);
        JPanel panel = app.getEnclosingPanel();
        MessageDlg.setDlgParent( panel);
        app.display();
        DisplayUtils.display(
            panel, true, DisplayUtils.DEFAULT_HEIGHT, DisplayUtils.DEFAULT_WIDTH, frame);
    }
    
    private VisibleStrandAction latestItem( MenuTabApplication application)
    {
        VisibleStrandAction result;
        TheRosterStrand visibleStrand = new TheRosterStrand( application, rosterWorkersStrand(), true);
        ParticularRosterI particularRoster = ParticularRosterFactory.newParticularRoster( RosteringConstants.CURRENT_STR);
        RostererSessionI rostererSession = RostererSessionFactory.newRostererSession( 
                RostererSessionFactory.WITH_SERVICE);
        rostererSession.init( dataStore);
        particularRoster.init(rostererSession, dataStore);
        visibleStrand.setBusinessObjects( particularRoster, rostererSession);
        result = new VisibleStrandAction( "Latest Style", DISPLAY_ROSTER);
        result.setAdorned( false);
        result.setVisibleStrand( visibleStrand);
        return result;
    }

    /**
     * Tried to make this the same as if was in the real application 
     */
    private RosterWorkersStrand rosterWorkersStrand()
    {
        dataStore.setLookupsProvider( new LookupsProvider( dataStore));
        String title = WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription();
        RosterWorkersStrand result = new RosterWorkersStrand( title, app);
        VisibleStrandAction vsa = new VisibleStrandAction( title, "Display Roster")            
        {
            public void actionPerformed(ActionEvent evt)
            {
                super.actionPerformed( evt);
            }
        };
        //vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
        //vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
        //    WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription());
        vsa.setVisibleStrand( result);
        app.addItem( vsa);
        result.sdzInit();
        result.preForm();
        return result;
    }
    
    private VisibleStrandAction incumbantItem( MenuTabApplication application, DataStore dataStore)
    {
        VisibleStrandAction result;
        RosterSessionUtils.setMemoryProperty( "thickClient", "true");
        RosterSessionUtils.setMemoryProperty( "live", "false");
        //Only necessary if had set to thin client
        //setCredentials();
        RostererSessionI rostererSession = RostererSessionFactory.newRostererSession( 
                RostererSessionFactory.WITH_SERVICE);
        rostererSession.init( dataStore);
        ParticularRosterI currentRosterBO = ParticularRosterFactory.newParticularRoster( "RunTheRoster");
        currentRosterBO.init( rostererSession, dataStore);
        currentRosterBO.setAtMonth(RosteringConstants.CURRENT);
        PrintRosterStrand prs = new PrintRosterStrand( application);
        prs.setBusinessObjects( currentRosterBO, rostererSession);
        result = new VisibleStrandAction( "Incumbent Style", DISPLAY_ROSTER);
        result.setVisibleStrand( prs);
        return result;
    }
    
    private boolean setCredentials()
    {
        boolean isLive = RosterSessionUtils.getProperty( "live").equals( "true");
        String username = null;
        String password = null;
        if(SdzNote.USER_DETAILS.isVisible() && !isLive)
        {
            //If not live then automatically fill the login screen:
            UserDetailsProviderI userDetails = UserDetailsFactory.newUserDetails( UserDetailsFactory.LIVE_TERESA_USER_DETAILS);
            Object details[] = userDetails.get( 0);
            username = (String)details[0];
            password = (String)details[1];
        }
        LoginHelper.Params params = LoginHelper.newParams( WombatConnectionEnum.DEMO_WOMBAT_MEMORY, dataStoreFactory, 
                   "Rosterer Login (for incumbant style)", //TeresaSpringConstant.CLIENT_CONTEXT_CONFIG_LOCATION, 
                   username, password, false);
        LoginHelper loginHelper = new LoginHelper( params);
        return loginHelper.login();
    }
}