    #include "build.h"
    #include "defines.h"
    
    #define LN10          2.3025850929
    
    		/**@ buildkill.p **/
    void /*PROCEDURE*/ buildkill(int itype, int ienv)  /***COMPILED***/   /*COMPILED*/
    	/** purpose: to compute timekill and nallts and timevec **/
    	/** called by inschedule, indrugs, inkill **/
    	/** calls buildsched and sortsched first **/
    /*VAR*/
     {
    	 integer itime, isched;
    	 struct drugandtime s; /*** DECLARATOR **/
    
    	buildsched();
    	sortsched();
        
  		if ( itype > 0 ) {
    	nallts = 1;
    	for (itime = 0; itime  <= MAXTIMES; itime ++)  {
    		timevec [itime] = ZERO;
  		timekill (itime, itype,ienv) = ZERO;
  		timesurv (itime, itype,ienv) = ONE;
  	}
  
  	drugkill (0, itype,ienv) = ZERO;
  
    	for (isched = 1; isched  <= nsched; isched ++) {
    		s=sched [isched] ;
    		if ( s.t != timevec [nallts])  {
    			nallts = nallts + 1;
    			timevec [nallts] = s.t;
    		}
  		timekill (nallts, itype,ienv) =  1 -
  				(1 - timekill (nallts, itype,ienv))
  				*expo (-drugkill (s.d,itype,ienv)* s.df  * 2.3025850929);
    /* added the dose factor to implement the reduction of dosage
    				*expo (-drugkill [s.d][ itype]* dosefactor[s.d]* 2.3025850929);*/
  			timesurv (nallts,itype,ienv) = 
  				timesurv (nallts, itype,ienv) *
 				expo (-drugkill(s.d, itype,ienv) * s.df * 2.3025850929);
    /* Made the above two changes on 14 Aug 1996 -- Sai
    				expo (-drugkill [s.d][ itype]* dosefactor[s.d] * 2.3025850929);*/
    	}
    

    /** the following assures that jointpgf is computed at the last time
      indicated in the EVAL schedule (except for TIMECOURSE and DOSERESPONSE) **/
    	nt = nallts;
    	if ( (ndoses [EVAL] >0) )
    		while ((doselist [EVAL][ ndoses [EVAL]] < timevec [nt])
    			&& (nt >= 0) )
    				nt = nt - 1;
 	}
    }