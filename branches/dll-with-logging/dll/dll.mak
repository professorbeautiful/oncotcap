# Microsoft Developer Studio Generated NMAKE File, Based on dll.dsp
!IF "$(CFG)" == ""
CFG=dll - Win32 Release
!MESSAGE No configuration specified. Defaulting to dll - Win32 Release.
!ENDIF 

!IF "$(CFG)" != "dll - Win32 Release" && "$(CFG)" != "dll - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "dll.mak" CFG="dll - Win32 Release"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "dll - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "dll - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "dll - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release
# Begin Custom Macros
OutDir=.\.\Release
# End Custom Macros

!IF "$(RECURSE)" == "0" 

ALL : ".\treat.dll" "$(OUTDIR)\dll.bsc"

!ELSE 

ALL : ".\treat.dll" "$(OUTDIR)\dll.bsc"

!ENDIF 

CLEAN :
	-@erase "$(INTDIR)\AddQIndex.obj"
	-@erase "$(INTDIR)\AddQIndex.sbr"
	-@erase "$(INTDIR)\AddToCellQ.obj"
	-@erase "$(INTDIR)\AddToCellQ.sbr"
	-@erase "$(INTDIR)\AddToEventQ.obj"
	-@erase "$(INTDIR)\AddToEventQ.sbr"
	-@erase "$(INTDIR)\Applyrul.obj"
	-@erase "$(INTDIR)\Applyrul.sbr"
	-@erase "$(INTDIR)\ApplyTrialSched.obj"
	-@erase "$(INTDIR)\ApplyTrialSched.sbr"
	-@erase "$(INTDIR)\ariths.obj"
	-@erase "$(INTDIR)\ariths.sbr"
	-@erase "$(INTDIR)\buildkill.obj"
	-@erase "$(INTDIR)\buildkill.sbr"
	-@erase "$(INTDIR)\buildsche.obj"
	-@erase "$(INTDIR)\buildsche.sbr"
	-@erase "$(INTDIR)\CalculateTimeKillSurv.obj"
	-@erase "$(INTDIR)\CalculateTimeKillSurv.sbr"
	-@erase "$(INTDIR)\cellcount.obj"
	-@erase "$(INTDIR)\cellcount.sbr"
	-@erase "$(INTDIR)\cellp.obj"
	-@erase "$(INTDIR)\cellp.sbr"
	-@erase "$(INTDIR)\checkevents.obj"
	-@erase "$(INTDIR)\checkevents.sbr"
	-@erase "$(INTDIR)\CheckForTox.obj"
	-@erase "$(INTDIR)\CheckForTox.sbr"
	-@erase "$(INTDIR)\CheckForTreatment.obj"
	-@erase "$(INTDIR)\CheckForTreatment.sbr"
	-@erase "$(INTDIR)\CreateParaStruct.obj"
	-@erase "$(INTDIR)\CreateParaStruct.sbr"
	-@erase "$(INTDIR)\dround.obj"
	-@erase "$(INTDIR)\dround.sbr"
	-@erase "$(INTDIR)\evalshed.obj"
	-@erase "$(INTDIR)\evalshed.sbr"
	-@erase "$(INTDIR)\GenerateInitCellCount.obj"
	-@erase "$(INTDIR)\GenerateInitCellCount.sbr"
	-@erase "$(INTDIR)\getenvir.obj"
	-@erase "$(INTDIR)\getenvir.sbr"
	-@erase "$(INTDIR)\getstring.obj"
	-@erase "$(INTDIR)\getstring.sbr"
	-@erase "$(INTDIR)\grand.obj"
	-@erase "$(INTDIR)\grand.sbr"
	-@erase "$(INTDIR)\incondition.obj"
	-@erase "$(INTDIR)\incondition.sbr"
	-@erase "$(INTDIR)\infile.obj"
	-@erase "$(INTDIR)\infile.sbr"
	-@erase "$(INTDIR)\InitDoseFactor.obj"
	-@erase "$(INTDIR)\InitDoseFactor.sbr"
	-@erase "$(INTDIR)\InitNextToxTime.obj"
	-@erase "$(INTDIR)\InitNextToxTime.sbr"
	-@erase "$(INTDIR)\InitTox.obj"
	-@erase "$(INTDIR)\InitTox.sbr"
	-@erase "$(INTDIR)\InterpretEvents.obj"
	-@erase "$(INTDIR)\InterpretEvents.sbr"
	-@erase "$(INTDIR)\jointpgf.obj"
	-@erase "$(INTDIR)\jointpgf.sbr"
	-@erase "$(INTDIR)\jpgfcond.obj"
	-@erase "$(INTDIR)\jpgfcond.sbr"
	-@erase "$(INTDIR)\jpgfen.obj"
	-@erase "$(INTDIR)\jpgfen.sbr"
	-@erase "$(INTDIR)\jpgfit.obj"
	-@erase "$(INTDIR)\jpgfit.sbr"
	-@erase "$(INTDIR)\jpgfstep.obj"
	-@erase "$(INTDIR)\jpgfstep.sbr"
	-@erase "$(INTDIR)\lognorm.obj"
	-@erase "$(INTDIR)\lognorm.sbr"
	-@erase "$(INTDIR)\makeoutfile.obj"
	-@erase "$(INTDIR)\makeoutfile.sbr"
	-@erase "$(INTDIR)\messages.obj"
	-@erase "$(INTDIR)\messages.sbr"
	-@erase "$(INTDIR)\MultiSim.obj"
	-@erase "$(INTDIR)\MultiSim.sbr"
	-@erase "$(INTDIR)\ncells.obj"
	-@erase "$(INTDIR)\ncells.sbr"
	-@erase "$(INTDIR)\nlogs.obj"
	-@erase "$(INTDIR)\nlogs.sbr"
	-@erase "$(INTDIR)\norm.obj"
	-@erase "$(INTDIR)\norm.sbr"
	-@erase "$(INTDIR)\outfile.obj"
	-@erase "$(INTDIR)\outfile.sbr"
	-@erase "$(INTDIR)\pgf1.pure.obj"
	-@erase "$(INTDIR)\pgf1.pure.sbr"
	-@erase "$(INTDIR)\pgfint.obj"
	-@erase "$(INTDIR)\pgfint.sbr"
	-@erase "$(INTDIR)\pgfmain.obj"
	-@erase "$(INTDIR)\pgfmain.sbr"
	-@erase "$(INTDIR)\pgfxcm.obj"
	-@erase "$(INTDIR)\pgfxcm.sbr"
	-@erase "$(INTDIR)\phaseii.obj"
	-@erase "$(INTDIR)\phaseii.sbr"
	-@erase "$(INTDIR)\probeprob.obj"
	-@erase "$(INTDIR)\probeprob.sbr"
	-@erase "$(INTDIR)\probzeros.obj"
	-@erase "$(INTDIR)\probzeros.sbr"
	-@erase "$(INTDIR)\Readrul.obj"
	-@erase "$(INTDIR)\Readrul.sbr"
	-@erase "$(INTDIR)\realprompt.obj"
	-@erase "$(INTDIR)\realprompt.sbr"
	-@erase "$(INTDIR)\registry.obj"
	-@erase "$(INTDIR)\registry.sbr"
	-@erase "$(INTDIR)\RemoveTreatments.obj"
	-@erase "$(INTDIR)\RemoveTreatments.sbr"
	-@erase "$(INTDIR)\ResetCellP.obj"
	-@erase "$(INTDIR)\ResetCellP.sbr"
	-@erase "$(INTDIR)\ResetCellQ.obj"
	-@erase "$(INTDIR)\ResetCellQ.sbr"
	-@erase "$(INTDIR)\ResetEventQ.obj"
	-@erase "$(INTDIR)\ResetEventQ.sbr"
	-@erase "$(INTDIR)\resetevents.obj"
	-@erase "$(INTDIR)\resetevents.sbr"
	-@erase "$(INTDIR)\ResetMultiCellP.obj"
	-@erase "$(INTDIR)\ResetMultiCellP.sbr"
	-@erase "$(INTDIR)\setInitialCellCounts.obj"
	-@erase "$(INTDIR)\setInitialCellCounts.sbr"
	-@erase "$(INTDIR)\setmem.obj"
	-@erase "$(INTDIR)\setmem.sbr"
	-@erase "$(INTDIR)\setrates.obj"
	-@erase "$(INTDIR)\setrates.sbr"
	-@erase "$(INTDIR)\SetSpeed.obj"
	-@erase "$(INTDIR)\SetSpeed.sbr"
	-@erase "$(INTDIR)\SingleCellp.obj"
	-@erase "$(INTDIR)\SingleCellp.sbr"
	-@erase "$(INTDIR)\sortsched.obj"
	-@erase "$(INTDIR)\sortsched.sbr"
	-@erase "$(INTDIR)\strings.obj"
	-@erase "$(INTDIR)\strings.sbr"
	-@erase "$(INTDIR)\ThdCellp.obj"
	-@erase "$(INTDIR)\ThdCellp.sbr"
	-@erase "$(INTDIR)\ThdMultiCellP.obj"
	-@erase "$(INTDIR)\ThdMultiCellP.sbr"
	-@erase "$(INTDIR)\timecours.obj"
	-@erase "$(INTDIR)\timecours.sbr"
	-@erase "$(INTDIR)\TreatmentExists.obj"
	-@erase "$(INTDIR)\TreatmentExists.sbr"
	-@erase "$(INTDIR)\TreatmentExistsInt.obj"
	-@erase "$(INTDIR)\TreatmentExistsInt.sbr"
	-@erase "$(INTDIR)\typeconds.obj"
	-@erase "$(INTDIR)\typeconds.sbr"
	-@erase "$(INTDIR)\typemask.obj"
	-@erase "$(INTDIR)\typemask.sbr"
	-@erase "$(INTDIR)\typestrin.obj"
	-@erase "$(INTDIR)\typestrin.sbr"
	-@erase "$(INTDIR)\typevec.obj"
	-@erase "$(INTDIR)\typevec.sbr"
	-@erase "$(INTDIR)\vbc.obj"
	-@erase "$(INTDIR)\vbc.sbr"
	-@erase "$(INTDIR)\vc50.idb"
	-@erase "$(OUTDIR)\dll.bsc"
	-@erase "$(OUTDIR)\treat.exp"
	-@erase "$(OUTDIR)\treat.lib"
	-@erase ".\treat.dll"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP_PROJ=/nologo /MT /W3 /GX /O2 /D "NDEBUG" /D "WIN32" /D "_WINDOWS" /FAs\
 /Fa"$(INTDIR)\\" /Fr"$(INTDIR)\\" /Fp"$(INTDIR)\dll.pch" /YX /Fo"$(INTDIR)\\"\
 /Fd"$(INTDIR)\\" /FD /c 
CPP_OBJS=.\Release/
CPP_SBRS=.\Release/
MTL_PROJ=/nologo /D "NDEBUG" /mktyplib203 /win32 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\dll.bsc" 
BSC32_SBRS= \
	"$(INTDIR)\AddQIndex.sbr" \
	"$(INTDIR)\AddToCellQ.sbr" \
	"$(INTDIR)\AddToEventQ.sbr" \
	"$(INTDIR)\Applyrul.sbr" \
	"$(INTDIR)\ApplyTrialSched.sbr" \
	"$(INTDIR)\ariths.sbr" \
	"$(INTDIR)\buildkill.sbr" \
	"$(INTDIR)\buildsche.sbr" \
	"$(INTDIR)\CalculateTimeKillSurv.sbr" \
	"$(INTDIR)\cellcount.sbr" \
	"$(INTDIR)\cellp.sbr" \
	"$(INTDIR)\checkevents.sbr" \
	"$(INTDIR)\CheckForTox.sbr" \
	"$(INTDIR)\CheckForTreatment.sbr" \
	"$(INTDIR)\CreateParaStruct.sbr" \
	"$(INTDIR)\dround.sbr" \
	"$(INTDIR)\evalshed.sbr" \
	"$(INTDIR)\GenerateInitCellCount.sbr" \
	"$(INTDIR)\getenvir.sbr" \
	"$(INTDIR)\getstring.sbr" \
	"$(INTDIR)\grand.sbr" \
	"$(INTDIR)\incondition.sbr" \
	"$(INTDIR)\infile.sbr" \
	"$(INTDIR)\InitDoseFactor.sbr" \
	"$(INTDIR)\InitNextToxTime.sbr" \
	"$(INTDIR)\InitTox.sbr" \
	"$(INTDIR)\InterpretEvents.sbr" \
	"$(INTDIR)\jointpgf.sbr" \
	"$(INTDIR)\jpgfcond.sbr" \
	"$(INTDIR)\jpgfen.sbr" \
	"$(INTDIR)\jpgfit.sbr" \
	"$(INTDIR)\jpgfstep.sbr" \
	"$(INTDIR)\lognorm.sbr" \
	"$(INTDIR)\makeoutfile.sbr" \
	"$(INTDIR)\messages.sbr" \
	"$(INTDIR)\MultiSim.sbr" \
	"$(INTDIR)\ncells.sbr" \
	"$(INTDIR)\nlogs.sbr" \
	"$(INTDIR)\norm.sbr" \
	"$(INTDIR)\outfile.sbr" \
	"$(INTDIR)\pgf1.pure.sbr" \
	"$(INTDIR)\pgfint.sbr" \
	"$(INTDIR)\pgfmain.sbr" \
	"$(INTDIR)\pgfxcm.sbr" \
	"$(INTDIR)\phaseii.sbr" \
	"$(INTDIR)\probeprob.sbr" \
	"$(INTDIR)\probzeros.sbr" \
	"$(INTDIR)\Readrul.sbr" \
	"$(INTDIR)\realprompt.sbr" \
	"$(INTDIR)\registry.sbr" \
	"$(INTDIR)\RemoveTreatments.sbr" \
	"$(INTDIR)\ResetCellP.sbr" \
	"$(INTDIR)\ResetCellQ.sbr" \
	"$(INTDIR)\ResetEventQ.sbr" \
	"$(INTDIR)\resetevents.sbr" \
	"$(INTDIR)\ResetMultiCellP.sbr" \
	"$(INTDIR)\setInitialCellCounts.sbr" \
	"$(INTDIR)\setmem.sbr" \
	"$(INTDIR)\setrates.sbr" \
	"$(INTDIR)\SetSpeed.sbr" \
	"$(INTDIR)\SingleCellp.sbr" \
	"$(INTDIR)\sortsched.sbr" \
	"$(INTDIR)\strings.sbr" \
	"$(INTDIR)\ThdCellp.sbr" \
	"$(INTDIR)\ThdMultiCellP.sbr" \
	"$(INTDIR)\timecours.sbr" \
	"$(INTDIR)\TreatmentExists.sbr" \
	"$(INTDIR)\TreatmentExistsInt.sbr" \
	"$(INTDIR)\typeconds.sbr" \
	"$(INTDIR)\typemask.sbr" \
	"$(INTDIR)\typestrin.sbr" \
	"$(INTDIR)\typevec.sbr" \
	"$(INTDIR)\vbc.sbr"

"$(OUTDIR)\dll.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib\
 advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib /nologo\
 /subsystem:windows /dll /profile /debug /machine:I386 /def:".\Treat.def"\
 /out:".\treat.dll" /implib:"$(OUTDIR)\treat.lib" 
DEF_FILE= \
	".\Treat.def"
LINK32_OBJS= \
	"$(INTDIR)\AddQIndex.obj" \
	"$(INTDIR)\AddToCellQ.obj" \
	"$(INTDIR)\AddToEventQ.obj" \
	"$(INTDIR)\Applyrul.obj" \
	"$(INTDIR)\ApplyTrialSched.obj" \
	"$(INTDIR)\ariths.obj" \
	"$(INTDIR)\buildkill.obj" \
	"$(INTDIR)\buildsche.obj" \
	"$(INTDIR)\CalculateTimeKillSurv.obj" \
	"$(INTDIR)\cellcount.obj" \
	"$(INTDIR)\cellp.obj" \
	"$(INTDIR)\checkevents.obj" \
	"$(INTDIR)\CheckForTox.obj" \
	"$(INTDIR)\CheckForTreatment.obj" \
	"$(INTDIR)\CreateParaStruct.obj" \
	"$(INTDIR)\dround.obj" \
	"$(INTDIR)\evalshed.obj" \
	"$(INTDIR)\GenerateInitCellCount.obj" \
	"$(INTDIR)\getenvir.obj" \
	"$(INTDIR)\getstring.obj" \
	"$(INTDIR)\grand.obj" \
	"$(INTDIR)\incondition.obj" \
	"$(INTDIR)\infile.obj" \
	"$(INTDIR)\InitDoseFactor.obj" \
	"$(INTDIR)\InitNextToxTime.obj" \
	"$(INTDIR)\InitTox.obj" \
	"$(INTDIR)\InterpretEvents.obj" \
	"$(INTDIR)\jointpgf.obj" \
	"$(INTDIR)\jpgfcond.obj" \
	"$(INTDIR)\jpgfen.obj" \
	"$(INTDIR)\jpgfit.obj" \
	"$(INTDIR)\jpgfstep.obj" \
	"$(INTDIR)\lognorm.obj" \
	"$(INTDIR)\makeoutfile.obj" \
	"$(INTDIR)\messages.obj" \
	"$(INTDIR)\MultiSim.obj" \
	"$(INTDIR)\ncells.obj" \
	"$(INTDIR)\nlogs.obj" \
	"$(INTDIR)\norm.obj" \
	"$(INTDIR)\outfile.obj" \
	"$(INTDIR)\pgf1.pure.obj" \
	"$(INTDIR)\pgfint.obj" \
	"$(INTDIR)\pgfmain.obj" \
	"$(INTDIR)\pgfxcm.obj" \
	"$(INTDIR)\phaseii.obj" \
	"$(INTDIR)\probeprob.obj" \
	"$(INTDIR)\probzeros.obj" \
	"$(INTDIR)\Readrul.obj" \
	"$(INTDIR)\realprompt.obj" \
	"$(INTDIR)\registry.obj" \
	"$(INTDIR)\RemoveTreatments.obj" \
	"$(INTDIR)\ResetCellP.obj" \
	"$(INTDIR)\ResetCellQ.obj" \
	"$(INTDIR)\ResetEventQ.obj" \
	"$(INTDIR)\resetevents.obj" \
	"$(INTDIR)\ResetMultiCellP.obj" \
	"$(INTDIR)\setInitialCellCounts.obj" \
	"$(INTDIR)\setmem.obj" \
	"$(INTDIR)\setrates.obj" \
	"$(INTDIR)\SetSpeed.obj" \
	"$(INTDIR)\SingleCellp.obj" \
	"$(INTDIR)\sortsched.obj" \
	"$(INTDIR)\strings.obj" \
	"$(INTDIR)\ThdCellp.obj" \
	"$(INTDIR)\ThdMultiCellP.obj" \
	"$(INTDIR)\timecours.obj" \
	"$(INTDIR)\TreatmentExists.obj" \
	"$(INTDIR)\TreatmentExistsInt.obj" \
	"$(INTDIR)\typeconds.obj" \
	"$(INTDIR)\typemask.obj" \
	"$(INTDIR)\typestrin.obj" \
	"$(INTDIR)\typevec.obj" \
	"$(INTDIR)\vbc.obj"

".\treat.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

OUTDIR=.\Debug
INTDIR=.\Debug

!IF "$(RECURSE)" == "0" 

ALL : ".\treat.dll"

!ELSE 

ALL : ".\treat.dll"

!ENDIF 

CLEAN :
	-@erase "$(INTDIR)\AddQIndex.obj"
	-@erase "$(INTDIR)\AddToCellQ.obj"
	-@erase "$(INTDIR)\AddToEventQ.obj"
	-@erase "$(INTDIR)\Applyrul.obj"
	-@erase "$(INTDIR)\ApplyTrialSched.obj"
	-@erase "$(INTDIR)\ariths.obj"
	-@erase "$(INTDIR)\buildkill.obj"
	-@erase "$(INTDIR)\buildsche.obj"
	-@erase "$(INTDIR)\CalculateTimeKillSurv.obj"
	-@erase "$(INTDIR)\cellcount.obj"
	-@erase "$(INTDIR)\cellp.obj"
	-@erase "$(INTDIR)\checkevents.obj"
	-@erase "$(INTDIR)\CheckForTox.obj"
	-@erase "$(INTDIR)\CheckForTreatment.obj"
	-@erase "$(INTDIR)\CreateParaStruct.obj"
	-@erase "$(INTDIR)\dround.obj"
	-@erase "$(INTDIR)\evalshed.obj"
	-@erase "$(INTDIR)\GenerateInitCellCount.obj"
	-@erase "$(INTDIR)\getenvir.obj"
	-@erase "$(INTDIR)\getstring.obj"
	-@erase "$(INTDIR)\grand.obj"
	-@erase "$(INTDIR)\incondition.obj"
	-@erase "$(INTDIR)\infile.obj"
	-@erase "$(INTDIR)\InitDoseFactor.obj"
	-@erase "$(INTDIR)\InitNextToxTime.obj"
	-@erase "$(INTDIR)\InitTox.obj"
	-@erase "$(INTDIR)\InterpretEvents.obj"
	-@erase "$(INTDIR)\jointpgf.obj"
	-@erase "$(INTDIR)\jpgfcond.obj"
	-@erase "$(INTDIR)\jpgfen.obj"
	-@erase "$(INTDIR)\jpgfit.obj"
	-@erase "$(INTDIR)\jpgfstep.obj"
	-@erase "$(INTDIR)\lognorm.obj"
	-@erase "$(INTDIR)\makeoutfile.obj"
	-@erase "$(INTDIR)\messages.obj"
	-@erase "$(INTDIR)\MultiSim.obj"
	-@erase "$(INTDIR)\ncells.obj"
	-@erase "$(INTDIR)\nlogs.obj"
	-@erase "$(INTDIR)\norm.obj"
	-@erase "$(INTDIR)\outfile.obj"
	-@erase "$(INTDIR)\pgf1.pure.obj"
	-@erase "$(INTDIR)\pgfint.obj"
	-@erase "$(INTDIR)\pgfmain.obj"
	-@erase "$(INTDIR)\pgfxcm.obj"
	-@erase "$(INTDIR)\phaseii.obj"
	-@erase "$(INTDIR)\probeprob.obj"
	-@erase "$(INTDIR)\probzeros.obj"
	-@erase "$(INTDIR)\Readrul.obj"
	-@erase "$(INTDIR)\realprompt.obj"
	-@erase "$(INTDIR)\registry.obj"
	-@erase "$(INTDIR)\RemoveTreatments.obj"
	-@erase "$(INTDIR)\ResetCellP.obj"
	-@erase "$(INTDIR)\ResetCellQ.obj"
	-@erase "$(INTDIR)\ResetEventQ.obj"
	-@erase "$(INTDIR)\resetevents.obj"
	-@erase "$(INTDIR)\ResetMultiCellP.obj"
	-@erase "$(INTDIR)\setInitialCellCounts.obj"
	-@erase "$(INTDIR)\setmem.obj"
	-@erase "$(INTDIR)\setrates.obj"
	-@erase "$(INTDIR)\SetSpeed.obj"
	-@erase "$(INTDIR)\SingleCellp.obj"
	-@erase "$(INTDIR)\sortsched.obj"
	-@erase "$(INTDIR)\strings.obj"
	-@erase "$(INTDIR)\ThdCellp.obj"
	-@erase "$(INTDIR)\ThdMultiCellP.obj"
	-@erase "$(INTDIR)\timecours.obj"
	-@erase "$(INTDIR)\TreatmentExists.obj"
	-@erase "$(INTDIR)\TreatmentExistsInt.obj"
	-@erase "$(INTDIR)\typeconds.obj"
	-@erase "$(INTDIR)\typemask.obj"
	-@erase "$(INTDIR)\typestrin.obj"
	-@erase "$(INTDIR)\typevec.obj"
	-@erase "$(INTDIR)\vbc.obj"
	-@erase "$(INTDIR)\vc50.idb"
	-@erase "$(INTDIR)\vc50.pdb"
	-@erase "$(OUTDIR)\treat.exp"
	-@erase "$(OUTDIR)\treat.lib"
	-@erase ".\treat.dll"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP_PROJ=/nologo /MTd /W3 /GX /Zi /Od /D "_DEBUG" /D "WIN32" /D "_WINDOWS" /FAs\
 /Fa"$(INTDIR)\\" /Fp"$(INTDIR)\dll.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\"\
 /FD /c 
CPP_OBJS=.\Debug/
CPP_SBRS=.
MTL_PROJ=/nologo /D "_DEBUG" /mktyplib203 /win32 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\dll.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib\
 advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib /nologo\
 /subsystem:windows /dll /profile /debug /machine:I386 /def:".\Treat.def"\
 /out:".\treat.dll" /implib:"$(OUTDIR)\treat.lib" 
DEF_FILE= \
	".\Treat.def"
LINK32_OBJS= \
	"$(INTDIR)\AddQIndex.obj" \
	"$(INTDIR)\AddToCellQ.obj" \
	"$(INTDIR)\AddToEventQ.obj" \
	"$(INTDIR)\Applyrul.obj" \
	"$(INTDIR)\ApplyTrialSched.obj" \
	"$(INTDIR)\ariths.obj" \
	"$(INTDIR)\buildkill.obj" \
	"$(INTDIR)\buildsche.obj" \
	"$(INTDIR)\CalculateTimeKillSurv.obj" \
	"$(INTDIR)\cellcount.obj" \
	"$(INTDIR)\cellp.obj" \
	"$(INTDIR)\checkevents.obj" \
	"$(INTDIR)\CheckForTox.obj" \
	"$(INTDIR)\CheckForTreatment.obj" \
	"$(INTDIR)\CreateParaStruct.obj" \
	"$(INTDIR)\dround.obj" \
	"$(INTDIR)\evalshed.obj" \
	"$(INTDIR)\GenerateInitCellCount.obj" \
	"$(INTDIR)\getenvir.obj" \
	"$(INTDIR)\getstring.obj" \
	"$(INTDIR)\grand.obj" \
	"$(INTDIR)\incondition.obj" \
	"$(INTDIR)\infile.obj" \
	"$(INTDIR)\InitDoseFactor.obj" \
	"$(INTDIR)\InitNextToxTime.obj" \
	"$(INTDIR)\InitTox.obj" \
	"$(INTDIR)\InterpretEvents.obj" \
	"$(INTDIR)\jointpgf.obj" \
	"$(INTDIR)\jpgfcond.obj" \
	"$(INTDIR)\jpgfen.obj" \
	"$(INTDIR)\jpgfit.obj" \
	"$(INTDIR)\jpgfstep.obj" \
	"$(INTDIR)\lognorm.obj" \
	"$(INTDIR)\makeoutfile.obj" \
	"$(INTDIR)\messages.obj" \
	"$(INTDIR)\MultiSim.obj" \
	"$(INTDIR)\ncells.obj" \
	"$(INTDIR)\nlogs.obj" \
	"$(INTDIR)\norm.obj" \
	"$(INTDIR)\outfile.obj" \
	"$(INTDIR)\pgf1.pure.obj" \
	"$(INTDIR)\pgfint.obj" \
	"$(INTDIR)\pgfmain.obj" \
	"$(INTDIR)\pgfxcm.obj" \
	"$(INTDIR)\phaseii.obj" \
	"$(INTDIR)\probeprob.obj" \
	"$(INTDIR)\probzeros.obj" \
	"$(INTDIR)\Readrul.obj" \
	"$(INTDIR)\realprompt.obj" \
	"$(INTDIR)\registry.obj" \
	"$(INTDIR)\RemoveTreatments.obj" \
	"$(INTDIR)\ResetCellP.obj" \
	"$(INTDIR)\ResetCellQ.obj" \
	"$(INTDIR)\ResetEventQ.obj" \
	"$(INTDIR)\resetevents.obj" \
	"$(INTDIR)\ResetMultiCellP.obj" \
	"$(INTDIR)\setInitialCellCounts.obj" \
	"$(INTDIR)\setmem.obj" \
	"$(INTDIR)\setrates.obj" \
	"$(INTDIR)\SetSpeed.obj" \
	"$(INTDIR)\SingleCellp.obj" \
	"$(INTDIR)\sortsched.obj" \
	"$(INTDIR)\strings.obj" \
	"$(INTDIR)\ThdCellp.obj" \
	"$(INTDIR)\ThdMultiCellP.obj" \
	"$(INTDIR)\timecours.obj" \
	"$(INTDIR)\TreatmentExists.obj" \
	"$(INTDIR)\TreatmentExistsInt.obj" \
	"$(INTDIR)\typeconds.obj" \
	"$(INTDIR)\typemask.obj" \
	"$(INTDIR)\typestrin.obj" \
	"$(INTDIR)\typevec.obj" \
	"$(INTDIR)\vbc.obj"

".\treat.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 

.c{$(CPP_OBJS)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(CPP_OBJS)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(CPP_OBJS)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(CPP_SBRS)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(CPP_SBRS)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(CPP_SBRS)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<


!IF "$(CFG)" == "dll - Win32 Release" || "$(CFG)" == "dll - Win32 Debug"
SOURCE=.\AddQIndex.c
DEP_CPP_ADDQI=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\AddQIndex.obj"	"$(INTDIR)\AddQIndex.sbr" : $(SOURCE)\
 $(DEP_CPP_ADDQI) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\AddQIndex.obj" : $(SOURCE) $(DEP_CPP_ADDQI) "$(INTDIR)"


!ENDIF 

SOURCE=.\AddToCellQ.c
DEP_CPP_ADDTO=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\AddToCellQ.obj"	"$(INTDIR)\AddToCellQ.sbr" : $(SOURCE)\
 $(DEP_CPP_ADDTO) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\AddToCellQ.obj" : $(SOURCE) $(DEP_CPP_ADDTO) "$(INTDIR)"


!ENDIF 

SOURCE=.\AddToEventQ.c
DEP_CPP_ADDTOE=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\AddToEventQ.obj"	"$(INTDIR)\AddToEventQ.sbr" : $(SOURCE)\
 $(DEP_CPP_ADDTOE) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\AddToEventQ.obj" : $(SOURCE) $(DEP_CPP_ADDTOE) "$(INTDIR)"


!ENDIF 

SOURCE=.\Applyrul.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_APPLY=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\Applyrul.obj"	"$(INTDIR)\Applyrul.sbr" : $(SOURCE) $(DEP_CPP_APPLY)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_APPLY=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\Applyrul.obj" : $(SOURCE) $(DEP_CPP_APPLY) "$(INTDIR)"


!ENDIF 

SOURCE=.\ApplyTrialSched.c
DEP_CPP_APPLYT=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\ApplyTrialSched.obj"	"$(INTDIR)\ApplyTrialSched.sbr" : $(SOURCE)\
 $(DEP_CPP_APPLYT) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\ApplyTrialSched.obj" : $(SOURCE) $(DEP_CPP_APPLYT) "$(INTDIR)"


!ENDIF 

SOURCE=.\ariths.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_ARITH=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\ariths.obj"	"$(INTDIR)\ariths.sbr" : $(SOURCE) $(DEP_CPP_ARITH)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_ARITH=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\ariths.obj" : $(SOURCE) $(DEP_CPP_ARITH) "$(INTDIR)"


!ENDIF 

SOURCE=.\buildkill.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_BUILD=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\buildkill.obj"	"$(INTDIR)\buildkill.sbr" : $(SOURCE)\
 $(DEP_CPP_BUILD) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_BUILD=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\buildkill.obj" : $(SOURCE) $(DEP_CPP_BUILD) "$(INTDIR)"


!ENDIF 

SOURCE=.\buildsche.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_BUILDS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\buildsche.obj"	"$(INTDIR)\buildsche.sbr" : $(SOURCE)\
 $(DEP_CPP_BUILDS) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_BUILDS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\buildsche.obj" : $(SOURCE) $(DEP_CPP_BUILDS) "$(INTDIR)"


!ENDIF 

SOURCE=.\CalculateTimeKillSurv.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_CALCU=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\CalculateTimeKillSurv.obj"	"$(INTDIR)\CalculateTimeKillSurv.sbr" : \
$(SOURCE) $(DEP_CPP_CALCU) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_CALCU=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\CalculateTimeKillSurv.obj" : $(SOURCE) $(DEP_CPP_CALCU) "$(INTDIR)"


!ENDIF 

SOURCE=.\cellcount.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_CELLC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\cellcount.obj"	"$(INTDIR)\cellcount.sbr" : $(SOURCE)\
 $(DEP_CPP_CELLC) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_CELLC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\cellcount.obj" : $(SOURCE) $(DEP_CPP_CELLC) "$(INTDIR)"


!ENDIF 

SOURCE=.\cellp.c
DEP_CPP_CELLP=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\cellp.obj"	"$(INTDIR)\cellp.sbr" : $(SOURCE) $(DEP_CPP_CELLP)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\cellp.obj" : $(SOURCE) $(DEP_CPP_CELLP) "$(INTDIR)"


!ENDIF 

SOURCE=.\checkevents.c
DEP_CPP_CHECK=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\checkevents.obj"	"$(INTDIR)\checkevents.sbr" : $(SOURCE)\
 $(DEP_CPP_CHECK) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\checkevents.obj" : $(SOURCE) $(DEP_CPP_CHECK) "$(INTDIR)"


!ENDIF 

SOURCE=.\CheckForTox.c
DEP_CPP_CHECKF=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\CheckForTox.obj"	"$(INTDIR)\CheckForTox.sbr" : $(SOURCE)\
 $(DEP_CPP_CHECKF) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\CheckForTox.obj" : $(SOURCE) $(DEP_CPP_CHECKF) "$(INTDIR)"


!ENDIF 

SOURCE=.\CheckForTreatment.c
DEP_CPP_CHECKFO=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\CheckForTreatment.obj"	"$(INTDIR)\CheckForTreatment.sbr" : $(SOURCE)\
 $(DEP_CPP_CHECKFO) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\CheckForTreatment.obj" : $(SOURCE) $(DEP_CPP_CHECKFO) "$(INTDIR)"


!ENDIF 

SOURCE=.\CreateParaStruct.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_CREAT=\
	".\build.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\Pheno.h"\
	".\procdecs.h"\
	

"$(INTDIR)\CreateParaStruct.obj"	"$(INTDIR)\CreateParaStruct.sbr" : $(SOURCE)\
 $(DEP_CPP_CREAT) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_CREAT=\
	".\build.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\Pheno.h"\
	".\procdecs.h"\
	

"$(INTDIR)\CreateParaStruct.obj" : $(SOURCE) $(DEP_CPP_CREAT) "$(INTDIR)"


!ENDIF 

SOURCE=.\dround.c

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\dround.obj"	"$(INTDIR)\dround.sbr" : $(SOURCE) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\dround.obj" : $(SOURCE) "$(INTDIR)"


!ENDIF 

SOURCE=.\evalshed.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_EVALS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\evalshed.obj"	"$(INTDIR)\evalshed.sbr" : $(SOURCE) $(DEP_CPP_EVALS)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_EVALS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\evalshed.obj" : $(SOURCE) $(DEP_CPP_EVALS) "$(INTDIR)"


!ENDIF 

SOURCE=.\GenerateInitCellCount.c
DEP_CPP_GENER=\
	".\build.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\GenerateInitCellCount.obj"	"$(INTDIR)\GenerateInitCellCount.sbr" : \
$(SOURCE) $(DEP_CPP_GENER) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\GenerateInitCellCount.obj" : $(SOURCE) $(DEP_CPP_GENER) "$(INTDIR)"


!ENDIF 

SOURCE=.\getenvir.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_GETEN=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\getenvir.obj"	"$(INTDIR)\getenvir.sbr" : $(SOURCE) $(DEP_CPP_GETEN)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_GETEN=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\getenvir.obj" : $(SOURCE) $(DEP_CPP_GETEN) "$(INTDIR)"


!ENDIF 

SOURCE=.\getstring.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_GETST=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\getstring.obj"	"$(INTDIR)\getstring.sbr" : $(SOURCE)\
 $(DEP_CPP_GETST) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_GETST=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\getstring.obj" : $(SOURCE) $(DEP_CPP_GETST) "$(INTDIR)"


!ENDIF 

SOURCE=.\grand.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_GRAND=\
	".\build.h"\
	".\Const.h"\
	".\grand.h"\
	

"$(INTDIR)\grand.obj"	"$(INTDIR)\grand.sbr" : $(SOURCE) $(DEP_CPP_GRAND)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_GRAND=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\grand.h"\
	".\procdecs.h"\
	

"$(INTDIR)\grand.obj" : $(SOURCE) $(DEP_CPP_GRAND) "$(INTDIR)"


!ENDIF 

SOURCE=.\incondition.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_INCON=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\incondition.obj"	"$(INTDIR)\incondition.sbr" : $(SOURCE)\
 $(DEP_CPP_INCON) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_INCON=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\incondition.obj" : $(SOURCE) $(DEP_CPP_INCON) "$(INTDIR)"


!ENDIF 

SOURCE=.\infile.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_INFIL=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\infile.obj"	"$(INTDIR)\infile.sbr" : $(SOURCE) $(DEP_CPP_INFIL)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_INFIL=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\infile.obj" : $(SOURCE) $(DEP_CPP_INFIL) "$(INTDIR)"


!ENDIF 

SOURCE=.\InitDoseFactor.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_INITD=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\InitDoseFactor.obj"	"$(INTDIR)\InitDoseFactor.sbr" : $(SOURCE)\
 $(DEP_CPP_INITD) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_INITD=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\InitDoseFactor.obj" : $(SOURCE) $(DEP_CPP_INITD) "$(INTDIR)"


!ENDIF 

SOURCE=.\InitNextToxTime.c
DEP_CPP_INITN=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\InitNextToxTime.obj"	"$(INTDIR)\InitNextToxTime.sbr" : $(SOURCE)\
 $(DEP_CPP_INITN) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\InitNextToxTime.obj" : $(SOURCE) $(DEP_CPP_INITN) "$(INTDIR)"


!ENDIF 

SOURCE=.\InitTox.c
DEP_CPP_INITT=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\InitTox.obj"	"$(INTDIR)\InitTox.sbr" : $(SOURCE) $(DEP_CPP_INITT)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\InitTox.obj" : $(SOURCE) $(DEP_CPP_INITT) "$(INTDIR)"


!ENDIF 

SOURCE=.\InterpretEvents.c
DEP_CPP_INTER=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\InterpretEvents.obj"	"$(INTDIR)\InterpretEvents.sbr" : $(SOURCE)\
 $(DEP_CPP_INTER) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\InterpretEvents.obj" : $(SOURCE) $(DEP_CPP_INTER) "$(INTDIR)"


!ENDIF 

SOURCE=.\jointpgf.c
DEP_CPP_JOINT=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\jointpgf.obj"	"$(INTDIR)\jointpgf.sbr" : $(SOURCE) $(DEP_CPP_JOINT)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\jointpgf.obj" : $(SOURCE) $(DEP_CPP_JOINT) "$(INTDIR)"


!ENDIF 

SOURCE=.\jpgfcond.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_JPGFC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfcond.obj"	"$(INTDIR)\jpgfcond.sbr" : $(SOURCE) $(DEP_CPP_JPGFC)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_JPGFC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfcond.obj" : $(SOURCE) $(DEP_CPP_JPGFC) "$(INTDIR)"


!ENDIF 

SOURCE=.\jpgfen.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_JPGFE=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfen.obj"	"$(INTDIR)\jpgfen.sbr" : $(SOURCE) $(DEP_CPP_JPGFE)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_JPGFE=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfen.obj" : $(SOURCE) $(DEP_CPP_JPGFE) "$(INTDIR)"


!ENDIF 

SOURCE=.\jpgfit.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_JPGFI=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfit.obj"	"$(INTDIR)\jpgfit.sbr" : $(SOURCE) $(DEP_CPP_JPGFI)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_JPGFI=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfit.obj" : $(SOURCE) $(DEP_CPP_JPGFI) "$(INTDIR)"


!ENDIF 

SOURCE=.\jpgfstep.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_JPGFS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfstep.obj"	"$(INTDIR)\jpgfstep.sbr" : $(SOURCE) $(DEP_CPP_JPGFS)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_JPGFS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\jpgfstep.obj" : $(SOURCE) $(DEP_CPP_JPGFS) "$(INTDIR)"


!ENDIF 

SOURCE=.\lognorm.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_LOGNO=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\lognorm.obj"	"$(INTDIR)\lognorm.sbr" : $(SOURCE) $(DEP_CPP_LOGNO)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_LOGNO=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\lognorm.obj" : $(SOURCE) $(DEP_CPP_LOGNO) "$(INTDIR)"


!ENDIF 

SOURCE=.\makeoutfile.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_MAKEO=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\makeoutfile.obj"	"$(INTDIR)\makeoutfile.sbr" : $(SOURCE)\
 $(DEP_CPP_MAKEO) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_MAKEO=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\makeoutfile.obj" : $(SOURCE) $(DEP_CPP_MAKEO) "$(INTDIR)"


!ENDIF 

SOURCE=.\messages.c
DEP_CPP_MESSA=\
	".\build.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\messages.obj"	"$(INTDIR)\messages.sbr" : $(SOURCE) $(DEP_CPP_MESSA)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\messages.obj" : $(SOURCE) $(DEP_CPP_MESSA) "$(INTDIR)"


!ENDIF 

SOURCE=.\MultiSim.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_MULTI=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	".\tox.h"\
	

"$(INTDIR)\MultiSim.obj"	"$(INTDIR)\MultiSim.sbr" : $(SOURCE) $(DEP_CPP_MULTI)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_MULTI=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	".\tox.h"\
	

"$(INTDIR)\MultiSim.obj" : $(SOURCE) $(DEP_CPP_MULTI) "$(INTDIR)"


!ENDIF 

SOURCE=.\ncells.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_NCELL=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\ncells.obj"	"$(INTDIR)\ncells.sbr" : $(SOURCE) $(DEP_CPP_NCELL)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_NCELL=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\ncells.obj" : $(SOURCE) $(DEP_CPP_NCELL) "$(INTDIR)"


!ENDIF 

SOURCE=.\nlogs.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_NLOGS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\nlogs.obj"	"$(INTDIR)\nlogs.sbr" : $(SOURCE) $(DEP_CPP_NLOGS)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_NLOGS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\nlogs.obj" : $(SOURCE) $(DEP_CPP_NLOGS) "$(INTDIR)"


!ENDIF 

SOURCE=.\norm.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_NORM_=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\norm.obj"	"$(INTDIR)\norm.sbr" : $(SOURCE) $(DEP_CPP_NORM_)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_NORM_=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\norm.obj" : $(SOURCE) $(DEP_CPP_NORM_) "$(INTDIR)"


!ENDIF 

SOURCE=.\outfile.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_OUTFI=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\outfile.obj"	"$(INTDIR)\outfile.sbr" : $(SOURCE) $(DEP_CPP_OUTFI)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_OUTFI=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\outfile.obj" : $(SOURCE) $(DEP_CPP_OUTFI) "$(INTDIR)"


!ENDIF 

SOURCE=.\pgf1.pure.c
DEP_CPP_PGF1_=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\pgf1.pure.obj"	"$(INTDIR)\pgf1.pure.sbr" : $(SOURCE)\
 $(DEP_CPP_PGF1_) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\pgf1.pure.obj" : $(SOURCE) $(DEP_CPP_PGF1_) "$(INTDIR)"


!ENDIF 

SOURCE=.\pgfint.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_PGFIN=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\pgfint.obj"	"$(INTDIR)\pgfint.sbr" : $(SOURCE) $(DEP_CPP_PGFIN)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_PGFIN=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\pgfint.obj" : $(SOURCE) $(DEP_CPP_PGFIN) "$(INTDIR)"


!ENDIF 

SOURCE=.\pgfmain.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_PGFMA=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	

"$(INTDIR)\pgfmain.obj"	"$(INTDIR)\pgfmain.sbr" : $(SOURCE) $(DEP_CPP_PGFMA)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_PGFMA=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	

"$(INTDIR)\pgfmain.obj" : $(SOURCE) $(DEP_CPP_PGFMA) "$(INTDIR)"


!ENDIF 

SOURCE=.\pgfxcm.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_PGFXC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\pgfxcm.obj"	"$(INTDIR)\pgfxcm.sbr" : $(SOURCE) $(DEP_CPP_PGFXC)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_PGFXC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\pgfxcm.obj" : $(SOURCE) $(DEP_CPP_PGFXC) "$(INTDIR)"


!ENDIF 

SOURCE=.\phaseii.c
DEP_CPP_PHASE=\
	".\phaseii.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\phaseii.obj"	"$(INTDIR)\phaseii.sbr" : $(SOURCE) $(DEP_CPP_PHASE)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\phaseii.obj" : $(SOURCE) $(DEP_CPP_PHASE) "$(INTDIR)"


!ENDIF 

SOURCE=.\probeprob.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_PROBE=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\probeprob.obj"	"$(INTDIR)\probeprob.sbr" : $(SOURCE)\
 $(DEP_CPP_PROBE) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_PROBE=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\probeprob.obj" : $(SOURCE) $(DEP_CPP_PROBE) "$(INTDIR)"


!ENDIF 

SOURCE=.\probzeros.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_PROBZ=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\probzeros.obj"	"$(INTDIR)\probzeros.sbr" : $(SOURCE)\
 $(DEP_CPP_PROBZ) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_PROBZ=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\probzeros.obj" : $(SOURCE) $(DEP_CPP_PROBZ) "$(INTDIR)"


!ENDIF 

SOURCE=.\Readrul.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_READR=\
	".\build.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\Pheno.h"\
	".\procdecs.h"\
	

"$(INTDIR)\Readrul.obj"	"$(INTDIR)\Readrul.sbr" : $(SOURCE) $(DEP_CPP_READR)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_READR=\
	".\build.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\Pheno.h"\
	".\procdecs.h"\
	

"$(INTDIR)\Readrul.obj" : $(SOURCE) $(DEP_CPP_READR) "$(INTDIR)"


!ENDIF 

SOURCE=.\realprompt.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_REALP=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\realprompt.obj"	"$(INTDIR)\realprompt.sbr" : $(SOURCE)\
 $(DEP_CPP_REALP) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_REALP=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\realprompt.obj" : $(SOURCE) $(DEP_CPP_REALP) "$(INTDIR)"


!ENDIF 

SOURCE=.\registry.c
DEP_CPP_REGIS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\registry.obj"	"$(INTDIR)\registry.sbr" : $(SOURCE) $(DEP_CPP_REGIS)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\registry.obj" : $(SOURCE) $(DEP_CPP_REGIS) "$(INTDIR)"


!ENDIF 

SOURCE=.\RemoveTreatments.c
DEP_CPP_REMOV=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\RemoveTreatments.obj"	"$(INTDIR)\RemoveTreatments.sbr" : $(SOURCE)\
 $(DEP_CPP_REMOV) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\RemoveTreatments.obj" : $(SOURCE) $(DEP_CPP_REMOV) "$(INTDIR)"


!ENDIF 

SOURCE=.\ResetCellP.c
DEP_CPP_RESET=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\ResetCellP.obj"	"$(INTDIR)\ResetCellP.sbr" : $(SOURCE)\
 $(DEP_CPP_RESET) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\ResetCellP.obj" : $(SOURCE) $(DEP_CPP_RESET) "$(INTDIR)"


!ENDIF 

SOURCE=.\ResetCellQ.c
DEP_CPP_RESETC=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\ResetCellQ.obj"	"$(INTDIR)\ResetCellQ.sbr" : $(SOURCE)\
 $(DEP_CPP_RESETC) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\ResetCellQ.obj" : $(SOURCE) $(DEP_CPP_RESETC) "$(INTDIR)"


!ENDIF 

SOURCE=.\ResetEventQ.c
DEP_CPP_RESETE=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\ResetEventQ.obj"	"$(INTDIR)\ResetEventQ.sbr" : $(SOURCE)\
 $(DEP_CPP_RESETE) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\ResetEventQ.obj" : $(SOURCE) $(DEP_CPP_RESETE) "$(INTDIR)"


!ENDIF 

SOURCE=.\resetevents.c
DEP_CPP_RESETEV=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\resetevents.obj"	"$(INTDIR)\resetevents.sbr" : $(SOURCE)\
 $(DEP_CPP_RESETEV) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\resetevents.obj" : $(SOURCE) $(DEP_CPP_RESETEV) "$(INTDIR)"


!ENDIF 

SOURCE=.\ResetMultiCellP.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_RESETM=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	

"$(INTDIR)\ResetMultiCellP.obj"	"$(INTDIR)\ResetMultiCellP.sbr" : $(SOURCE)\
 $(DEP_CPP_RESETM) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_RESETM=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	

"$(INTDIR)\ResetMultiCellP.obj" : $(SOURCE) $(DEP_CPP_RESETM) "$(INTDIR)"


!ENDIF 

SOURCE=.\setInitialCellCounts.c
DEP_CPP_SETIN=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\setInitialCellCounts.obj"	"$(INTDIR)\setInitialCellCounts.sbr" : \
$(SOURCE) $(DEP_CPP_SETIN) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\setInitialCellCounts.obj" : $(SOURCE) $(DEP_CPP_SETIN) "$(INTDIR)"


!ENDIF 

SOURCE=.\setmem.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_SETME=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\setmem.obj"	"$(INTDIR)\setmem.sbr" : $(SOURCE) $(DEP_CPP_SETME)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_SETME=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\setmem.obj" : $(SOURCE) $(DEP_CPP_SETME) "$(INTDIR)"


!ENDIF 

SOURCE=.\setrates.c
DEP_CPP_SETRA=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\setrates.obj"	"$(INTDIR)\setrates.sbr" : $(SOURCE) $(DEP_CPP_SETRA)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\setrates.obj" : $(SOURCE) $(DEP_CPP_SETRA) "$(INTDIR)"


!ENDIF 

SOURCE=.\SetSpeed.c
DEP_CPP_SETSP=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\SetSpeed.obj"	"$(INTDIR)\SetSpeed.sbr" : $(SOURCE) $(DEP_CPP_SETSP)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\SetSpeed.obj" : $(SOURCE) $(DEP_CPP_SETSP) "$(INTDIR)"


!ENDIF 

SOURCE=.\SingleCellp.c
DEP_CPP_SINGL=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\SingleCellp.obj"	"$(INTDIR)\SingleCellp.sbr" : $(SOURCE)\
 $(DEP_CPP_SINGL) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\SingleCellp.obj" : $(SOURCE) $(DEP_CPP_SINGL) "$(INTDIR)"


!ENDIF 

SOURCE=.\sortsched.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_SORTS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\sortsched.obj"	"$(INTDIR)\sortsched.sbr" : $(SOURCE)\
 $(DEP_CPP_SORTS) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_SORTS=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\sortsched.obj" : $(SOURCE) $(DEP_CPP_SORTS) "$(INTDIR)"


!ENDIF 

SOURCE=.\strings.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_STRIN=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\strings.obj"	"$(INTDIR)\strings.sbr" : $(SOURCE) $(DEP_CPP_STRIN)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_STRIN=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\strings.obj" : $(SOURCE) $(DEP_CPP_STRIN) "$(INTDIR)"


!ENDIF 

SOURCE=.\ThdCellp.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_THDCE=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	".\tox.h"\
	

"$(INTDIR)\ThdCellp.obj"	"$(INTDIR)\ThdCellp.sbr" : $(SOURCE) $(DEP_CPP_THDCE)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_THDCE=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	".\tox.h"\
	

"$(INTDIR)\ThdCellp.obj" : $(SOURCE) $(DEP_CPP_THDCE) "$(INTDIR)"


!ENDIF 

SOURCE=.\ThdMultiCellP.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_THDMU=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	".\tox.h"\
	

"$(INTDIR)\ThdMultiCellP.obj"	"$(INTDIR)\ThdMultiCellP.sbr" : $(SOURCE)\
 $(DEP_CPP_THDMU) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_THDMU=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\msim.h"\
	".\procdecs.h"\
	".\tox.h"\
	

"$(INTDIR)\ThdMultiCellP.obj" : $(SOURCE) $(DEP_CPP_THDMU) "$(INTDIR)"


!ENDIF 

SOURCE=.\timecours.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_TIMEC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\timecourse.h"\
	

"$(INTDIR)\timecours.obj"	"$(INTDIR)\timecours.sbr" : $(SOURCE)\
 $(DEP_CPP_TIMEC) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_TIMEC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\timecourse.h"\
	

"$(INTDIR)\timecours.obj" : $(SOURCE) $(DEP_CPP_TIMEC) "$(INTDIR)"


!ENDIF 

SOURCE=.\TreatmentExists.c
DEP_CPP_TREAT=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\TreatmentExists.obj"	"$(INTDIR)\TreatmentExists.sbr" : $(SOURCE)\
 $(DEP_CPP_TREAT) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\TreatmentExists.obj" : $(SOURCE) $(DEP_CPP_TREAT) "$(INTDIR)"


!ENDIF 

SOURCE=.\TreatmentExistsInt.c
DEP_CPP_TREATM=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	".\tox.h"\
	

!IF  "$(CFG)" == "dll - Win32 Release"


"$(INTDIR)\TreatmentExistsInt.obj"	"$(INTDIR)\TreatmentExistsInt.sbr" : \
$(SOURCE) $(DEP_CPP_TREATM) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"


"$(INTDIR)\TreatmentExistsInt.obj" : $(SOURCE) $(DEP_CPP_TREATM) "$(INTDIR)"


!ENDIF 

SOURCE=.\typeconds.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_TYPEC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typeconds.obj"	"$(INTDIR)\typeconds.sbr" : $(SOURCE)\
 $(DEP_CPP_TYPEC) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_TYPEC=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typeconds.obj" : $(SOURCE) $(DEP_CPP_TYPEC) "$(INTDIR)"


!ENDIF 

SOURCE=.\typemask.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_TYPEM=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typemask.obj"	"$(INTDIR)\typemask.sbr" : $(SOURCE) $(DEP_CPP_TYPEM)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_TYPEM=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typemask.obj" : $(SOURCE) $(DEP_CPP_TYPEM) "$(INTDIR)"


!ENDIF 

SOURCE=.\typestrin.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_TYPES=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typestrin.obj"	"$(INTDIR)\typestrin.sbr" : $(SOURCE)\
 $(DEP_CPP_TYPES) "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_TYPES=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typestrin.obj" : $(SOURCE) $(DEP_CPP_TYPES) "$(INTDIR)"


!ENDIF 

SOURCE=.\typevec.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_TYPEV=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typevec.obj"	"$(INTDIR)\typevec.sbr" : $(SOURCE) $(DEP_CPP_TYPEV)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_TYPEV=\
	".\build.h"\
	".\Const.h"\
	".\defines.h"\
	".\procdecs.h"\
	

"$(INTDIR)\typevec.obj" : $(SOURCE) $(DEP_CPP_TYPEV) "$(INTDIR)"


!ENDIF 

SOURCE=.\vbc.c

!IF  "$(CFG)" == "dll - Win32 Release"

DEP_CPP_VBC_C=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\phaseii.h"\
	".\procdecs.h"\
	".\timecourse.h"\
	".\tox.h"\
	

"$(INTDIR)\vbc.obj"	"$(INTDIR)\vbc.sbr" : $(SOURCE) $(DEP_CPP_VBC_C)\
 "$(INTDIR)"


!ELSEIF  "$(CFG)" == "dll - Win32 Debug"

DEP_CPP_VBC_C=\
	".\build.h"\
	".\cellp.h"\
	".\classes.h"\
	".\Const.h"\
	".\defines.h"\
	".\phaseii.h"\
	".\procdecs.h"\
	".\timecourse.h"\
	".\tox.h"\
	

"$(INTDIR)\vbc.obj" : $(SOURCE) $(DEP_CPP_VBC_C) "$(INTDIR)"


!ENDIF 


!ENDIF 

