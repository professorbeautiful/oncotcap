package oncotcap.process.cells;

import java.util.*;

class CellPopulation extends HashMap{
	CellPopulation(){
		super();
		this.label = new CellPopLabel("cancer");  //default
	}
	CellPopulation(String label){
		this.label = new CellPopLabel(label);
	}
	CellPopulation(CellPopLabel label){
		this.label = label;
	}
	CellPopLabel label(){
		return(this.label);
	}
	void setCellCount(Double n, AbstractCell ct){
		CellCount cc = new CellCount(n);
		put((Object)ct,(Object)cc);
	}
	void changeCellCount(Double n, AbstractCell ct){
		// add or subtract cells of a particular CellType ct
		CellCount cc = (CellCount)get((Object)ct);
		cc.change(n);
	}
	CellCount getCellCount(AbstractCell ct){
		// return number of cells of CellType ct.
		return((CellCount)get((Object)ct));
	}

	/* Implementation
	 */
	CellPopLabel label;
}
