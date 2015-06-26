package org.strandz.applic.books;import org.strandz.core.interf.SdzBagI;import org.strandz.core.interf.RuntimeAttribute;import org.strandz.core.interf.Cell;import org.strandz.core.interf.Node;import org.strandz.core.interf.Strand;import org.strandz.lgpl.util.Err;import org.strandz.view.books.MainExpensePanel;

public class ExampleExpensesDT
{
    public MainExpensePanel ui0;
    public Strand strand;

    public Node expenseNode;
    public Cell expenseCell;
    public RuntimeAttribute expenseamountAttribute;
    public RuntimeAttribute expensedateAttribute;
    public RuntimeAttribute expenseexpenseTypeAttribute;
    public RuntimeAttribute expensedescriptionAttribute;

    public Cell expenseTypeLookupCell;
    public RuntimeAttribute expenseTypenameAttribute;

    public ExampleExpensesDT( SdzBagI sdzBag)
    {
        try
        {
            ui0 = (MainExpensePanel)sdzBag.getPane( 0);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }

        strand = sdzBag.getStrand();

        expenseNode = strand.getNodeByName( "Expense Node");

        expenseCell = expenseNode.getCell();
        expenseamountAttribute = expenseCell.getAttributeByName( "expense amount");
        expensedateAttribute = expenseCell.getAttributeByName( "expense date");
        expenseexpenseTypeAttribute = expenseCell.getAttributeByName( "expense expenseType");
        expensedescriptionAttribute = expenseCell.getAttributeByName( "expense description");

        expenseTypeLookupCell = expenseCell.getCellByName( "expenseType Lookup Cell");
        expenseTypenameAttribute = expenseTypeLookupCell.getAttributeByName( "expenseType name");


    }
}