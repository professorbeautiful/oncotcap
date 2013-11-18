# USAGE: Rscript --vanilla sortOntologyFiles.R [FileChoice] [FolderChoice]
# Typically this will be run after saving the ontology,  twice:
#  Rscript --vanilla sortOntologyFiles.R pins .
#  Rscript --vanilla sortOntologyFiles.R pont .
# in the directory "." where the files were saved.
# The unsorted files are backed up, then overwritten with the sorted versions.
#options(error=recover)

#folder = "~/Dropbox/OncoTCap/oncotcap/TcapData-demos"
#folder = "~/Dropbox/OncoTCap/oncotcap/TcapData-Roger"
folder = "."
filename = "oncotcap.pins"
if(!interactive()) {
  args = commandArgs(trailingOnly=T)  ### AFTER "--args"
  cat("args are ", args, "\n")
  if(length(args) == 0) {
    cat("No arguments; folder and filename set to defaults.\n")
  }
  else {
    FileChoice = args[1]
    filename = switch(FileChoice,
                    oncotcap.pins="oncotcap.pins",
                    oncotcap.pont="oncotcap.pont",
                    pins="oncotcap.pins",
                    pont="oncotcap.pont",
                    "no file named")
    if(filename=="no file named") {
      stop("Error: file name ", FileChoice, " is unrecognized. Exiting\n")
    }
    if(length(args) >= 2) {
      FolderChoice = args[2]
      if(file.exists(FolderChoice) ) 
        if(file.info(FolderChoice)$isdir )  {
          folder = FolderChoice
        } else stop("folder ", FolderChoice, " is not actually a folder")
      else
        stop("folder ", FolderChoice, " does not exist")
    }
  }
}

fileIn =  paste0(folder, "/", filename)
#fileOut = paste0(fileIn, "_sorted")
fileOut = fileIn  ## We need to overwrite the input file.
cat("AFTER NONINTERACTIVE PROCESSING, filename is ", filename,
    "  folder is ", folder, "  fileIn is ", fileIn, 
    "  fileOut is ", fileOut,"\n")

######

# first make a backup.
system(paste0("cp ", fileIn, " ", fileIn, "_unsorted" ))

protege=readLines(f<-file(fileIn))
close(f)
cat( "length of protege file is ", length(protege), " lines.\n")
head(protege)

# ntabs = attr(regexpr("\t*", gsub(";\\+","",protege)), "match.length")
# table(ntabs)
# ntabs.rle = rle(ntabs)
# ntabs.cumLengths = c(1, 1+cumsum(ntabs.rle$lengths[-length(ntabs.rle$lengths)]))
# ntabs.cumLengths.end = c(ntabs.cumLengths[-1]-1, length(protege))
# rbind(start=(ntabs.cumLengths), end=(ntabs.cumLengths.end),
#       depth=(ntabs.rle$values))  [ , 1:6 ]               

#' separateWithinTabGroup
#' @param lines Input lines (character vector)
#' @param tablevel The depth number.
#' @param pr Print detail?
#' @param doSort Sort inside?
#' @return Broken into a list according to depth.
separateWithinTabGroup = function(lines, tablevel, pr=FALSE, doSort=FALSE){
  ntabs = attr(regexpr("\t*", gsub(";\\+","",lines)), "match.length")
#  ntabs.rle = rle(ntabs)
  pos.0 = which(ntabs==tablevel)
  pos.0.end = c(pos.0[-1]-1, length(ntabs))
  if(length(pos.0)!=length(pos.0.end)) 
    browser("OOPS")
  top = min(20, length(pos.0))
  if(pr) print(rbind(start=pos.0, end=pos.0.end)[ , 1:top])
  if(pr) print(rbind(start=rev(pos.0), end=rev(pos.0.end))[ , 1:top])
  result = lapply(1:length(pos.0), 
                  function(p) {
                    if(pos.0[p]==pos.0.end[p]) result = (NULL)
                    else { 
                      result = (lines[(pos.0[p]+1):pos.0.end[p]])
                      if(doSort) result=sort(result)
                    }
                    c(lines[pos.0[p]], result)
                  })
  names(result) = lines[pos.0]
  result = result[order(names(result))]
  result
}


if(1==length(grep("pont$", filename))){ #  "pont" file. 
  ### We have to sort ONLY in groups of single-slot and multislot,
  ## not at the top level (which is NOT written in stochastic order,
  ## but in a meaningful order),
  ## and not at the third level, which must be fixed also.
  protegeHeader = protege[(1:6)]
  protegeFixed = protege[-(1:6)]
  defclass = grep("^\\(defclass", protegeFixed)
  isa = grep("^\t\\(is-a", protegeFixed)
  role = grep("^\t\\(role", protegeFixed)
  table(isa - defclass)   #all = 1. The first line is always "isa".
  table(role - defclass)  #all = 2. The second line is always "role".
  ## Therefore the first and second lines must not be included in the sort.
  
  ranges = rbind(defclass+3, c(defclass[-1]-2, length(protegeFixed)))
  ## The line number ranges over which the sorting will take place.
  ## The first row is the starting line, the second row is the ending line.

  for(r in 1:ncol(ranges)) {
    if( ranges[1,r] < ranges[2,r] ) {  
      fixThis = protegeFixed[ ranges[1,r]:ranges[2,r]]
      fixThis[length(fixThis)] = gsub(")$", "", fixThis[length(fixThis)])
      # handling the completing parenthesis. Remove before sorting.
      separatedSorted = separateWithinTabGroup(fixThis, 1)
      # Separate (into a list) and sort at tab level 1.
      unListed = unlist(separatedSorted, recursive=TRUE, use.names=FALSE)
      # Now we re-assemble as a character vector.
      unListed[length(unListed)] = paste0(unListed[length(unListed)], ")")
      # handling the completing parenthesis. Replacing after sorting.
      protegeFixed[ ranges[1,r]:ranges[2,r]] = unListed
      # Replace the chunk to be fixed.
    }
  }
  ## No sorting is done at level 3.
}

if(1==length(grep("pins$", filename))){  #### "pins" file: 
  
  protegeHeader = protege[(1:5)]
  protegeFixed = protege[-(1:5)]
  
  instanceStarts = grep("^\\(", protegeFixed)  
  # grep '^(' TcapData/*pins | cut -c1-2 | sort | uniq -c
  #  27244 instances from this unix pipeline
  ranges = rbind(instanceStarts, 
                 c(instanceStarts[-1]-2, length(protegeFixed)))
  # The ranges of chunks to be processed: each instance.
  # The second row is the end of the range. 
  for(r in 1:ncol(ranges)) {
    if( ranges[1,r] < ranges[2,r] ) {  
      fixThis = protegeFixed[ (ranges[1,r]+1):ranges[2,r]]
      fixThis[length(fixThis)] = gsub(")$", "", fixThis[length(fixThis)])
      ## Handle the closing parenthesis.  Remove it for now.
      separatedSorted = separateWithinTabGroup(fixThis, 1, doSort=F)
      ## Within this chunk, separate the tab groups. A new group starts with a line with one tab.
      separatedSorted = separatedSorted[order(names(separatedSorted))]
      ## Sort the one-tab groups.
      unListed = unlist(separatedSorted, recursive=TRUE, use.names=FALSE)
      ## Turn it back into a vector of character strings with unlist().
      unListed[length(unListed)] = paste0(unListed[length(unListed)], ")")
      ## Handle the closing parenthesis.  Replace it .
      protegeFixed[ (ranges[1,r]+1):ranges[2,r]] = unListed
      ## Replace the fixed vectors.
    }  
  }  
}
writeLines(c(protegeHeader, protegeFixed),
           f<-file(fileOut))
close(f)
