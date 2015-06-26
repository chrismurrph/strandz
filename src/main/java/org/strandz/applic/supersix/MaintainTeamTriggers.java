package org.strandz.applic.supersix;

import org.strandz.core.interf.AlterSdzSetupI;
import org.strandz.applic.util.ValidationTriggers;
import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.NavigationTrigger;
import org.strandz.core.domain.event.NodeDefaultTrigger;
import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.domain.event.StateChangeEvent;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.OutNodeControllerEvent;
import org.strandz.core.interf.PostOperationPerformedTrigger;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.ActualNodeControllerI;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.domain.SuperSixDomainLookupEnum;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.Player;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.data.supersix.objects.Team;
import org.strandz.data.supersix.objects.TeamCompetitionSeason;
import org.strandz.lgpl.extent.InsteadOfAddRemoveTrigger;
import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;

import javax.swing.JOptionPane;
import java.util.Collection;
import java.util.List;


public class MaintainTeamTriggers
{
    public DataStore dataStore;
    public MaintainTeamDT dt;
    private DomainQueriesI queriesI;
    private AgeCalculator ageCalculator;
    private Division division;
    //private Team recentlyNavigatedToTeam;
    private SuperSixLookups superSixLookups;

    public MaintainTeamTriggers(
        DataStore dataStore,
        MaintainTeamDT dt,
        SdzBagI controller,
        AgeCalculator ageCalculator,
        Division division,
        SuperSixLookups superSixLookups
    )
    {
        this.dataStore = dataStore;
        this.dt = dt;
        this.queriesI = dataStore.getDomainQueries();
        this.ageCalculator = ageCalculator;
        this.division = division;
        this.superSixLookups = superSixLookups;
        dt.team.addDataFlowTrigger( new DataFlowT0());
        controller.getStrand().addTransactionTrigger( new FormTransactionT());
        controller.getStrand().setErrorHandler( new HandlerT());
        controller.getStrand().setEntityManagerTrigger(new EntityManagerT());
        dt.players.addNavigationTrigger(new TeamNavigationT());
        dt.strand.addPostControlActionPerformedTrigger(new PostOperationTrigger());
        dt.strand.addStateChangeTrigger(new ToFrozenTrigger());
        ValidationTriggers.DateValidationTrigger dvt = new ValidationTriggers.DateValidationTrigger(dt.dateOfBirthAttribute);
        dt.dateOfBirthAttribute.setItemValidationTrigger(dvt);
        dt.team.addNodeDefaultTrigger( new TeamNodeDefaultT());
        dt.playersCell.setInsteadOfAddRemoveTrigger( new InsteadOfAddRemovePlayerT());
        new AlterSdzSetup( controller).performAlterSdzSetup();
    }
    
    private class AlterSdzSetup implements AlterSdzSetupI
    {
        private SdzBagI sbI;
        
        AlterSdzSetup( SdzBagI sbI)
        {
            this.sbI = sbI;
        }
        
        public void performAlterSdzSetup()
        {
            ActualNodeControllerI actualController = ((SdzBag) sbI).getPhysicalController();
            actualController.deleteTool(OperationEnum.POST);
        }
    }

    static class HandlerT implements ValidationHandlerTrigger
    {
        public void handleError( ApplicationError e)
        {
            List msg = e.getMsg();
            if(msg != null)
            {
                new MessageDlg( msg, JOptionPane.ERROR_MESSAGE);
                Err.alarm( msg.get( 0).toString());
            }
            else
            {
                Print.prThrowable( e, "Auto-generated handler");
            }
        }
    }

    class EntityManagerT implements EntityManagerTriggerI
    {
        public SdzEntityManagerI fetchEM()
        {
            return ((EntityManagedDataStore)dataStore).getEM();
        }
    }

    class DataFlowT0 implements DataFlowTrigger
    {
        public void dataFlowPerformed( DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx();
                setLOVs();
                CompetitionSeason competitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();        
                Collection c = SuperSixManagerUtils.getGlobalCurrentSuperSixManager().getCurrentTeams( 
                        competitionSeason.getSeasonYear(), competitionSeason.getCompetition(), division);
                List enterQryAttribs = dt.team.getEnterQueryAttributes();
                c = InterfUtils.refineFromMatchingValues(
                    c, enterQryAttribs
                );
                dt.teamCell.setData( c);
            }
        }
    }

    class FormTransactionT implements CloseTransactionTrigger
    {
        public void perform( TransactionEvent evt)
        {
            if(evt.getID() == TransactionEvent.PRE_CLOSE)
            {
                dataStore.commitTx();
            }
        }
    }

    private void setLOVs()
    {
        List divisions = superSixLookups.get( SuperSixDomainLookupEnum.DIVISION);
                // queriesI.executeRetList(SuperSixDomainQueryEnum.DIVISION_CLOSED);
        dt.divisionLookupCell.setLOV(divisions);
    }

    //An alternative to all these triggers is to use calculated field ROJLabel 
    private class ToFrozenTrigger implements StateChangeTrigger
    {
        public void stateChangePerformed(StateChangeEvent e)
        {
            StateEnum current = e.getCurrentState();
            if(current == StateEnum.FROZEN && dt.strand.getCurrentNode() == dt.players)
            {
                ageCalculator.calculateAge( "Players Node has been put in Frozen state");
            }
        }
    }

    //An alternative to all these triggers is to use calculated field ROJLabel 
    private class PostOperationTrigger
        implements PostOperationPerformedTrigger
    {
        public void postOperationPerformed(OutNodeControllerEvent evt)
        {
            if(evt.getNode() == dt.players
                && evt.getID() == OperationEnum.GOTO_NODE
                && evt.getNode().getState() != StateEnum.FROZEN
                    )
            {
                ageCalculator.calculateAge( "Going to Team Players");
            }
            if(evt.getNode() == dt.team
                && evt.getID() == OperationEnum.GOTO_NODE
                && evt.getNode().getState() != StateEnum.FROZEN)
            {
                //Doing internally
                //recentlyNavigatedToTeam = getCurrentTeam();
            }
        }
    }

    //An alternative to all these triggers is to use calculated field ROJLabel 
    private class TeamNavigationT implements NavigationTrigger
    {
        public void navigated(OperationEvent evt)
        {
            ageCalculator.calculateAge( "Navigating thru records on Team Node");
            //Doing internally
            //recentlyNavigatedToTeam = getCurrentTeam();
        }
    }
    
    class TeamNodeDefaultT implements NodeDefaultTrigger
    {
        public void nodeChange(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_INSERT)
            {
                dt.divisionLookupCell.setDefaultElement( division);
            }
        }
    }
    
    private Team getCurrentTeam()
    {
        Team result = null;
        VisibleExtent ve = dt.teamCell.getDataRecords();
        int row = dt.team.getRow();
        //Err.pr( "row using to getCurrentTeam(): " + row);
        if(row != -1) //when stateId == StateEnum.FROZEN/UNKNOWN row will be -1
        {
            result = (Team)ve.get(row);
        }
        else
        {
            Err.error( "Unable to obtain the current Team");
        }
        return result;
    }
    
    class InsteadOfAddRemovePlayerT implements InsteadOfAddRemoveTrigger
    {
        public void add(Object collection, Object obj, int index)
        {
            Player player = (Player)obj;
            TeamCompetitionSeason team = (TeamCompetitionSeason)dt.teamCell.getLastNavigatedToDO();
            if(collection != team.getPlayers())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( team.getPlayers(), "team.getPlayers()");
                Err.error( "collection != team.getPlayers()");
            }
            team.addPlayer( player, index);
        }

        public boolean remove(Object collection, Object obj)
        {
            Player player = (Player)obj;
            TeamCompetitionSeason team = (TeamCompetitionSeason)dt.teamCell.getLastNavigatedToDO();
            if(collection != team.getPlayers())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( team.getPlayers(), "team.getPlayers()");
                Err.pr( "Current Team: " + team);
                Err.error( "collection != team.getPlayers()");
            }
            return team.removePlayer( player);
        }
    }
}