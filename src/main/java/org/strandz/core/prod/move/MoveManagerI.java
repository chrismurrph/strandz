package org.strandz.core.prod.move;

import org.strandz.core.domain.ValidationContext;
import org.strandz.core.domain.ItemAdapter;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.MoveBlockI;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.domain.constants.OperationEnum;

import java.util.List;

/**
 * Will be used to make Sdz independent from MoveTracker, which
 * will not be required for all applications. Will also facilitate
 * a rewrite/rethink of MoveTracker, which is overly complex for
 * validation. For instance for field validation take a look at
 * using a lightweight approach such as done by
 * AttributeChangesMonitor
 *
 * User: Chris
 * Date: 17/12/2008
 * Time: 11:56:55 PM
 */
public interface MoveManagerI extends NodeChangeListener
{
    void updateValidationContext(List list);
    void updateValidationContext(Object obj);
    ValidationContext getValidationContext();
    boolean readyNextStep();
    ItemAdapter getConfirmedErrorAdapter();
    void forgetWhereCameFrom();
    void enter(ItemAdapter itemAdapter, EntrySiteEnum where);
    void enterWithoutValidation(ItemAdapter itemAdapter, EntrySiteEnum where);
    void exitEnter();
    void errorSite(ErrorSiteEnum whereError, OperationEnum enumId);
    void setSomeRowsQueried(boolean b);
    ItemAdapter getEnteredAdapter();
    void createNewValidationContext();
    void closeTo(EntrySiteEnum where);
    boolean initialPoint(List blocks);
    void setBlock(BlockForMoveBlockI block);
    void openAgainTo(EntrySiteEnum where);
    boolean error(ErrorSiteEnum whereError);
    void b4HandleErrorProcessing(ApplicationError err, MoveBlockI mBlock, OperationEnum reason);
    void postValidationProcessing( boolean success, ApplicationError err);
}
