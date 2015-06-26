package org.strandz.applic.supersix;

import org.strandz.core.domain.DataFlowTrigger;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.DataFlowEvent;
import org.strandz.core.domain.event.NavigationTrigger;
import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.domain.event.TransactionEvent;
import org.strandz.core.domain.event.NodeDefaultTrigger;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.EntityManagerTriggerI;
import org.strandz.core.interf.InterfUtils;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.ValidationHandlerTrigger;
import org.strandz.core.interf.PostOperationPerformedTrigger;
import org.strandz.core.interf.OutNodeControllerEvent;
import org.strandz.core.interf.PreOperationPerformedTrigger;
import org.strandz.core.interf.AlterSdzSetupI;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.data.supersix.objects.Game;
import org.strandz.data.supersix.objects.Meet;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.domain.SuperSixDomainLookupEnum;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.extent.InsteadOfAddRemoveTrigger;
import org.strandz.lgpl.extent.VisibleExtent;
import org.strandz.lgpl.note.SuperSixNote;
import org.strandz.applic.util.ValidationTriggers;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class MaintainSeasonMeetMatchTriggers
{
    public DataStore dataStore;
    public MaintainSeasonDT dt;
    private DomainQueriesI queriesI;
    private Meet recentlyNavigatedToScreenMeet;
    //private Meet recentlyNavigatedToMeet;
    private TeamCopier teamCopier;
    private DivisionTeamLookup divisionTeamLookup;
    private SuperSixLookups superSixLookups;

    public MaintainSeasonMeetMatchTriggers(
        DataStore dataStore,
        MaintainSeasonDT dt,
        SdzBagI controller,
        TeamCopier teamCopier,
        DivisionTeamLookup divisionTeamLookup,
        SuperSixLookups superSixLookups
    )
    {
        this.dataStore = dataStore;
        queriesI = dataStore.getDomainQueries();
        this.dt = dt;
        this.superSixLookups = superSixLookups;
        this.teamCopier = teamCopier;
        this.divisionTeamLookup = divisionTeamLookup;
        dt.seasonNode.addDataFlowTrigger( new DataFlowT0());
        controller.getStrand().addTransactionTrigger( new FormTransactionT());
        controller.getStrand().setErrorHandler( new HandlerT());
        controller.getStrand().setEntityManagerTrigger(new EntityManagerT());
        dt.meetsListDetailNode.addNavigationTrigger(new MeetsNavigationT());
        dt.gamesListDetailNode.addNavigationTrigger(new GamesNavigationT());
        String msg = "Scores must be entered as numbers";
        dt.matchteamOneGoalsAttribute.setItemValidationTrigger( new ValidationTriggers.WholeNumbersOnlyValidationTrigger( dt.matchteamOneGoalsAttribute, msg));
        dt.matchteamTwoGoalsAttribute.setItemValidationTrigger( new ValidationTriggers.WholeNumbersOnlyValidationTrigger( dt.matchteamTwoGoalsAttribute, msg));
        dt.meetsCell.setInsteadOfAddRemoveTrigger( new InsteadOfAddRemoveMeetT());
        dt.gamesCell.setInsteadOfAddRemoveTrigger( new InsteadOfAddRemoveGameT());
        ValidationTriggers.DateValidationTrigger dvt =
                new ValidationTriggers.DateValidationTrigger(dt.meetdateAttribute);
        dt.meetdateAttribute.setItemValidationTrigger(dvt);
        dt.meetsListDetailNode.addNodeDefaultTrigger( new MeetNodeDefaultT());
        dt.gamesListDetailNode.addNodeDefaultTrigger( new GameNodeDefaultT());
        dt.strand.addPreControlActionPerformedTrigger(new PreOperationTrigger());
        dt.strand.addPostControlActionPerformedTrigger(new PostOperationTrigger());
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
            SuperSixUtils.alterSdzSetupForOneRow( sbI);
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
        
        private Division getDivisionOfFirstMeet( CompetitionSeason competitionSeason)
        {
            Division result;
            List<Meet> meets =  competitionSeason.getMeets();
            Meet firstMeet = meets.get(0);
            Game firstGame = firstMeet.getGames().get(0);
            result = firstGame.getDivision();
            Err.pr(SuperSixNote.TEAMS_NOT_SHOWING, "Bug solved by finding out division that will go to: " + result);
            return result;
        }
        
        public void dataFlowPerformed( DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_QUERY)
            {
                dataStore.rollbackTx();
                dataStore.startTx();
                CompetitionSeason competitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();
                setLOVs( getDivisionOfFirstMeet( competitionSeason));
                List seasons = new ArrayList();
                seasons.add(competitionSeason);
                List enterQryAttribs = dt.seasonNode.getEnterQueryAttributes();
                seasons = InterfUtils.refineFromMatchingValues(
                    seasons, enterQryAttribs
                );
                dt.seasonCell.setData( seasons);
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

    /**
     * Note that this is not the end of the story for LOVs of teams done for each
     * Game panel. Each Game belongs (potentially) to a different division. Thus
     * triggers other than PRE-QUERY are used. Each of these triggers use
     * divisionTeamLookup, which provides a cache of teams that need to be looked
     * up. 
     * 
     * @param division
     */
    private void setLOVs( Division division)
    {
        //List dayInWeeks = superSixLookups.get( SuperSixDomainLookupEnum.DAY_IN_WEEK); 
        //dt.nightPlayGamesLookupCell.setLOV(dayInWeeks);
        List seasonYears = superSixLookups.get( SuperSixDomainLookupEnum.SEASON_YEAR);
        dt.seasonYearLookupCellTwo.setLOV( seasonYears);
        
        List divisions = superSixLookups.get( SuperSixDomainLookupEnum.DIVISION); 
        dt.divisionLookupCell.setLOV(divisions);
        List kickOffTimes = superSixLookups.get( SuperSixDomainLookupEnum.KICK_OFF_TIME);
        dt.kickOffTimeLookupCell.setLOV(kickOffTimes);
        List pitches = superSixLookups.get( SuperSixDomainLookupEnum.PITCH_CLOSED);
        dt.pitchLookupCell.setLOV(pitches);                
        
        //Not all LOVs can be done at this early stage. Here we don't know the division
        //which will change for different games, so we will continue to need to do this
        //lookup from other triggers
        divisionTeamLookup.performLookup(false, division,  this.getClass().getName() + ".setLOVs()");
    }

    class InsteadOfAddRemoveMeetT implements InsteadOfAddRemoveTrigger
    {
        private CompetitionSeason getCurrentSeason()
        {
            CompetitionSeason result = null;
            result = SuperSixManagerUtils.getCurrentCompetitionSeason();
            return result;
        }

        public void add(Object collection, Object obj, int index)
        {
            Meet meet = (Meet)obj;
            CompetitionSeason competitionSeason = getCurrentSeason();
            if(collection != competitionSeason.getMeets())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( competitionSeason.getMeets(), "competitionSeason.getMeets()");
                Err.error( "collection != competitionSeason.getMeets()");
            }
            competitionSeason.addMeet( meet, index);
        }

        public boolean remove(Object collection, Object obj)
        {
            Meet meet = (Meet)obj;
            CompetitionSeason competitionSeason = getCurrentSeason();
            if(collection != competitionSeason.getMeets())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( competitionSeason.getMeets(), "competitionSeason.getMeets()");
                Err.error( "collection != competitionSeason.getMeets()");
            }
            return competitionSeason.removeMeet( meet);
        }
    }

    class InsteadOfAddRemoveGameT implements InsteadOfAddRemoveTrigger
    {
        public void add(Object collection, Object obj, int index)
        {
            Game game = (Game)obj;
            Meet meet = (Meet)dt.meetsCell.getLastNavigatedToDO();
            if(collection != meet.getGames())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( meet.getGames(), "meet.getGames()");
                Err.error( "collection != meet.getGames()");
            }
            meet.addMatch( game, index);
        }

        public boolean remove(Object collection, Object obj)
        {
            Game game = (Game)obj;
            Meet meet = (Meet)dt.meetsCell.getLastNavigatedToDO();
            if(collection != meet.getGames())
            {
                Print.prList( (List)collection, "collection");
                Print.prList( meet.getGames(), "meet.getGames()");
                Err.pr( "Current Meet: " + meet);
                Err.error( "collection != meet.getGames()");
            }
            return meet.removeMatch( game);
        }
    }
    
    private Meet getCurrentMeet()
    {
        Meet result = null;
        VisibleExtent ve = dt.meetsCell.getDataRecords();
        int row = dt.meetsListDetailNode.getRow();
        //Err.pr( "row using to getCurrentMeet(): " + row);
        if(row != -1) //when stateId == StateEnum.FROZEN/UNKNOWN row will be -1
        {
            result = (Meet)ve.get(row);
        }
        else
        {
            Err.error( "Unable to obtain the current Meet");
        }
        return result;
    }
    
    public class PostOperationTrigger
        implements PostOperationPerformedTrigger
    {
        public void postOperationPerformed(OutNodeControllerEvent evt)
        {
            if(evt.getNode() == dt.meetsListDetailNode
                && evt.getID() == OperationEnum.GOTO_NODE
                && evt.getNode().getState() != StateEnum.FROZEN)
            {
                recentlyNavigatedToScreenMeet = (Meet)dt.meetsCell.getItemValue();
                //Doing internally
                //recentlyNavigatedToMeet = getCurrentMeet();
            }
            if(evt.getNode() == dt.gamesListDetailNode
                && evt.getID() == OperationEnum.GOTO_NODE)
            {
                if(evt.getNode().getState() != StateEnum.FROZEN)
                {
                    teamCopier.copyTeam();
                    divisionTeamLookup.performLookup( true, this.getClass().getName() + ".postOperationPerformed() GOTO_NODE gamesListDetailNode");
                }
                else
                {
                    Err.pr( "Gone to GamesListDetailNode that is frozen");
                }
            }
        }
    }
    
    class MeetsNavigationT implements NavigationTrigger
    {
        public void navigated(OperationEvent evt)
        {
            if(dt.strand.getCurrentNode().getState() != StateEnum.FROZEN)
            {
                recentlyNavigatedToScreenMeet = (Meet)dt.meetsCell.getItemValue();
                //Doing internally
                //recentlyNavigatedToMeet = getCurrentMeet();
            }
            //Err.pr( "Navigating on " + dt.strand.getCurrentNode() + " which is " + dt.strand.getCurrentNode().getState());
        }
    }
    
    class GamesNavigationT implements NavigationTrigger
    {
        public void navigated(OperationEvent evt)
        {
            if(dt.strand.getCurrentNode().getState() != StateEnum.FROZEN)
            {
                teamCopier.copyTeam();
                divisionTeamLookup.performLookup( true, this.getClass().getName() + ".navigated()");
            }
        }
    }

    class MeetNodeDefaultT implements NodeDefaultTrigger
    {
        public void nodeChange(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_INSERT)
            {
                Integer newOrdinal = recentlyNavigatedToScreenMeet.getOrdinal() + 1;
                Date newDate = TimeUtils.addWeeks( recentlyNavigatedToScreenMeet.getDate(), 1);
                Meet m = new Meet();
                m.setDate( newDate);
                m.setOrdinal( newOrdinal);
                dt.meetsCell.setDefaultElement( m);
                dt.seasonYearLookupCellTwo.setDefaultElement( SuperSixManagerUtils.getCurrentCompetitionSeason().getSeasonYear());
            }
        }
    }

    class GameNodeDefaultT implements NodeDefaultTrigger
    {
        public void nodeChange(DataFlowEvent evt)
        {
            if(evt.getID() == DataFlowEvent.PRE_INSERT)
            {
                Integer newOrdinal = ((Meet)dt.meetsCell.getLastNavigatedToDO()).getOrdinal();
                Meet m = new Meet();
                m.setOrdinal( newOrdinal);
                dt.meetLookupCell.setDefaultElement( m);
            }
        }
    }
        
    public class PreOperationTrigger implements PreOperationPerformedTrigger
    {
        public void preOperationPerformed(OutNodeControllerEvent evt) throws ValidationException
        {
            if(evt.getNode() == dt.meetsListDetailNode && evt.getID() == OperationEnum.REMOVE)
            {
                int numGames = ((Meet)dt.meetsCell.getLastNavigatedToDO()).getGames().size();
                if(numGames > 0)
                {
                    String msgs[] = new String[]{
                            "Round " + ((Meet)dt.meetsCell.getLastNavigatedToDO()).getOrdinal() + " has " + numGames + " games.",
                            //Possible but silly, you don't use this program whilst games are being played
                            //"Of these " + recentlyNavigatedToMeet.getNumPlayed() + " have already been played.",
                            "Are you sure you want to delete this round?"};
                    int ret = MessageDlg.showConfirmDialog( msgs, "Delete Games Confirmation");
                    if(ret != JOptionPane.YES_OPTION)
                    {
                        throw new ValidationException();
                    }
                }
            }
        }
    }    
}