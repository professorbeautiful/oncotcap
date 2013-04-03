/**
 * 
 */
package oncotcap.process.cells;

//import com.sun.org.apache.xml.internal.utils.StopParseException;

import oncotcap.process.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;

/**
 * @author day
 *
 */
public class OrderedConversionEvent extends Conversion{

	/**
	 * 
	 */
	public OrderedConversionEvent(OncParent parent)
	{
		this(parent, 1);
	}
	public OrderedConversionEvent(OncParent parent, boolean stopAtBoundary)
	{
		this(parent, 1);
		setStopAtBoundary(stopAtBoundary);
	}
	public OrderedConversionEvent(OncParent parent, int jumpSize, boolean stopAtBoundary) {
		this(parent, jumpSize);
		setStopAtBoundary(stopAtBoundary);
	}
	public OrderedConversionEvent(OncParent parent, int jumpSize){
		super(parent);
		setDestLevel ((AbstractCell) parent);
	}
	/**
	 * @param args
	 */
	public OrderedConversionEvent clone(){return null;}
	public OrderedConversionEvent clone(oncotcap.util.OncEnum[] o){return null;}
	public OrderedConversionEvent clone(oncotcap.util.OncEnum o){return null;}
	public oncotcap.process.OncCollection newCollectionInstance(){return null;}


	public static void main(String[] args) {
		TopOncParent parent = new TopOncParent(); 
		OrderedConversionEvent event = new OrderedConversionEvent(parent, 1);
		Cell c = event.new Cell(parent);
		c.addKinEvent(event);
		System.out.println(event.getJumpSize());
		event.setDestLevel(c);
	}
	OncEnum testEnum = new TestEnum("test");
	class Cell extends AbstractCell {
		public Cell(OncParent parent){
			super(parent);
			super.myIdEnums = new OncIDEnum[]{ new TestEnum("test")};
		}
		public Cell clone(){return null;}
		public Cell clone(oncotcap.util.OncEnum[] o){return null;}
		public Cell clone(oncotcap.util.OncEnum o){return null;}
		public oncotcap.process.OncCollection newCollectionInstance(){return null;}
	};
	class TestEnum extends OncIDEnum {
		EnumLevelList ell = new EnumLevelList();
		final TestEnum [] VALUES;
		public TestEnum(String name) {
			super(name);
			ell.setMin("1");
			ell.setMax("5");
			ell.setIncrement("1");
			java.util.Iterator it = ell.getLevels().iterator();
			String level = null;
			VALUES = new TestEnum[ell.getLevels().size()];
			int index=0;
			for(; it.hasNext(); level = (String)it.next()){
				VALUES[index++] = new TestEnum(level);
			}
		}
		public OncEnum[] getValues() {return VALUES;}
	}
	
	//  THE ABOVE IS ONLY FOR TESTING!!
	
	private boolean stopAtBoundary = true;  // false means to wrap around.
	
	public void setStopAtBoundary(boolean b) {
		stopAtBoundary = b;
	}
	private oncotcap.util.OncEnum whichEnumToChange(AbstractCell ct) {
		for(int i=0; i<ct.getIdEnums().length; i++){
			if (ct.getIdEnums().clone()[i].getClass() == destLevels[0].getClass()){
				return (ct.getIdEnums().clone()[i]);
			}
		}
		return null;
	}
	
	int jumpSize = 1;
	
	public int getJumpSize() {  // override to get stochastic behavior
		return(jumpSize);
	}
	public void setDestLevel (AbstractCell ct){
		// In the CB, we have assigned destLevels to the OncEnum in ct that we want to change. 
		long elevel = whichEnumToChange(ct).getValue();
		elevel = elevel + getJumpSize();
		int len = destLevels[0].getValues().length;
		if(elevel > len ) {
			if (stopAtBoundary)
				elevel = len;
			else
				elevel = elevel % len;
		}
		if(elevel < 1 ) {
			if (stopAtBoundary)
				elevel = 1;
			else
				elevel = elevel % len;
		}
		//destLevels[0].setValue(elevel + jumpSize);
		// TODO:  fix this-  setValue is protected in OncEnum.
	}
	
	public CellPopulation daughters(CellCollection myCollection, double nParentCells, AbstractCell ct)
	{
		CellPopulation cpop = new CellPopulation();
		AbstractCell new_ct = myCollection.foundCellType(ct, destLevels);
		if(new_ct == null) {
			new_ct = (AbstractCell) ct.clone(destLevels);
//			mutct.changeProp( destLevels);
//			myCollection.add(mutct);
		}
		cpop.setCellCount(new Double(nParentCells), new_ct);
		return (cpop);
	}
}
