package oncotcap.display.common;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import oncotcap.datalayer.*;
import oncotcap.util.*;
import oncotcap.protege.plugins.*;
import oncotcap.datalayer.autogenpersistible.Article;
import oncotcap.datalayer.autogenpersistible.Author;
import oncotcap.datalayer.autogenpersistible.Journal;
import oncotcap.display.editor.autogeneditorpanel.*;

public class PMIDPanel  extends JPanel {
		String pmid = new String();;
		String pubMedURL;
		ArticlePanel articlePanel = null;
		public static String pubMedURLPrefix =	
				"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids=" ;
		public static String pubMedURLSuffix = "&dopt=Abstract";
		public PMIDPanel(ArticlePanel a) {
				super();
				setLayout(new FlowLayout());
				articlePanel = a;
				init();
		}
		private void init() {
				//add(pmidField);
				// Object editObj = articlePanel.getEditObject();
// 				System.out.println("editObj " + editObj);
						
// 				if ( editObj != null && editObj instanceof Article) {
// 						Integer pmid = ((Article)editObj).getPMID();
// 						System.out.println("pmid " + pmid);
// 						pmidField.setText(String.valueOf(pmid));
// 				}
				JButton autoFillBtn = new JButton("Auto Fill");
				autoFillBtn.setMargin(new Insets(0,0,0,0));
				autoFillBtn.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
// 										System.out.println("AutoFill");
										autoFill();
								}	
						});

				JButton pubMedBtn = new JButton("PubMed");
				pubMedBtn.setMargin(new Insets(0,0,0,0));
				pubMedBtn.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
// 										System.out.println("PubMed");
										// get the current PMID
										updatePubMedURL( getPMID());
										java.awt.Cursor curs = getCursor();
										setCursor(java.awt.Cursor.getPredefinedCursor
															(java.awt.Cursor.WAIT_CURSOR));
										try {
												System.out.println("Launching URL 4:\n" + pubMedURL);
												oncotcap.util.BrowserLauncher2.openURL(pubMedURL);
										} catch (java.net.MalformedURLException murle)
												{ return; }
										catch (java.io.IOException ioe)
												{ System.out.println("IOException"); return; }
										finally {
												setCursor(curs);
										}
								}	
						});
				
				JButton fullTextBtn = new JButton("Auto-Locate Full Text");
				fullTextBtn.setMargin(new Insets(0,0,0,0));
				fullTextBtn.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
										System.out.println("Full Text");
										java.awt.Cursor curs = getCursor();
										setCursor(java.awt.Cursor.getPredefinedCursor
															(java.awt.Cursor.WAIT_CURSOR));
										Article article = null;
										String location =  null;
										try {
												Object editObj = articlePanel.getEditObject();
												if ( editObj instanceof Article ) 
														article = (Article)editObj;
												// Persistible article = 
												// 			oncotcap.Oncotcap.getDataSource().find
												// 				(GUID.fromString
												//  			 ("888e669c00000005000000f54b6d1b8e"));
												location = 
														FullTextURLFinder.find
														(article);
												System.out.println("Launching URL 5:\n" + location);
												oncotcap.util.BrowserLauncher2.openURL(location);
										} catch (Exception ex) { 	
												JOptionPane.showMessageDialog
														((JFrame)null, 
														 "<html>"
														 + "Error launching " + 
														 location + ": " 
														 + ex.getMessage()
														 + "</html>");
															return; 
										}
										finally {
												setCursor(curs);
										}
								}	
						});
				add(autoFillBtn);
				add(pubMedBtn);
				add(fullTextBtn);
		}

		String getPMID() {
				pmid= String.valueOf(articlePanel.PMID.getValue());
// 				System.out.println("pmid " + pmid);
				if( pmid == null || pmid.equals("") || pmid.equals("null") )
						pmid = oncotcap.util.ClipboardHelper.get();
// 				System.out.println("pmid " + pmid);
				while(pmid.charAt(pmid.length()-1)==' ')
						pmid = pmid.substring(0,pmid.length()-1);
				articlePanel.PMID.setValue(pmid);
				return pmid;
		}

		void updatePubMedURL(String s) {
			
				pubMedURL = pubMedURLPrefix + s + pubMedURLSuffix;
		}
		

		void autoFill() {
				String pmid = getPMID();
				HashMap h = null;
				MedlineParserContentHandler handler = new MedlineParserContentHandler();
			
				updatePubMedURL(pmid);
				try { 
						h = (HashMap)MedlineParser.getContentsByPMID
								(new Integer(pmid), handler);
				} catch ( NumberFormatException nfe) {
						JOptionPane.showMessageDialog
								((JFrame)null, 
								 "<html>"
								 + "PMID must be a number"
								 + "</html>");
				}
				//System.out.println("hashmap: " + h);
				updateArticleFields(h);
	}

		private void clearFields() {
				// clear all fields first
				articlePanel.PMID.setValue(null);
				articlePanel.title.setValue(null);
				articlePanel.volume.setValue(null);
				articlePanel.issue.setValue(null);
				articlePanel.pages.setValue(null);
				articlePanel.journalAsString.setValue(null);
				articlePanel.year.setValue(null);
				articlePanel.authors.setValue(new Vector());					
		}

		private void updateArticleFields(HashMap h ) {
				// clear all fields first
// 				System.out.println( "Hashmap : " + h);
				clearFields();
				articlePanel.PMID.setValue(pmid);
				articlePanel.title.setValue(h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.Article.ArticleTitle"));
				articlePanel.volume.setValue(h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.Article.Journal.JournalIssue.Volume"));
				articlePanel.issue.setValue(h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.Article.Journal.JournalIssue.Issue"));
				articlePanel.pages.setValue(h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.Article.Pagination.MedlinePgn"));
				String journalString = 
						(String)h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.MedlineJournalInfo.MedlineTA");
				articlePanel.journalAsString.setValue(journalString);
				Journal journal = getJournal(journalString);
				articlePanel.journal.setValue(journal);
				articlePanel.year.setValue(h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.Article.Journal.JournalIssue.PubDate.Year"));
				// Get authors and transform into instances
				Object lnames = h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.Article.AuthorList.Author.LastName");
				Object initials = h.get("PubmedArticleSet.PubmedArticle.MedlineCitation.Article.AuthorList.Author.Initials");
				Vector authors = new Vector();
				if ( lnames instanceof Vector &&
						 initials instanceof Vector ) {
						authors = new Vector();
						Iterator lnamesIter = ((Vector)lnames).iterator();
						Iterator initialsIter = ((Vector)initials).iterator();
						// mutiple authors build author string
						while ( lnamesIter.hasNext() && initialsIter.hasNext() ) {
								String lname = (String)lnamesIter.next();
								String initial = (String)initialsIter.next();
								authors.addElement(getAuthor(lname, initial));
						}
				}
				else if ( lnames instanceof String &&
									initials instanceof String ) {
						authors.addElement(getAuthor((String)lnames, (String)initials));
				}
				articlePanel.authors.setValue(authors);	
		}

		public oncotcap.datalayer.autogenpersistible.Author getAuthor(String lname, String initial) {
				Vector matchedAuthors = null;
				String author = 
						lname + " " + initial;
								// does this author exist
				matchedAuthors = 	
						new Vector(oncotcap.Oncotcap.getDataSource().find
											 (oncotcap.datalayer.autogenpersistible.Author.class,
												author));
				if ( matchedAuthors.size() > 0 ) {
						Object authorPers = matchedAuthors.firstElement();
						return (Author)authorPers;
				}
				else {
						// Create a new author and add it
						oncotcap.datalayer.autogenpersistible.Author newAuthor = new oncotcap.datalayer.autogenpersistible.Author();
						newAuthor.setName(author);
						newAuthor.update();
						return newAuthor;
				}
		}

		public oncotcap.datalayer.autogenpersistible.Journal getJournal(String journal) {
				
				if ( journal == null ) 
						return null;
				
				Vector allJournals = null;
				// does this journal exist - get all journals
				allJournals = 	
						new Vector(oncotcap.Oncotcap.getDataSource().find
											 (oncotcap.datalayer.autogenpersistible.Journal.class));
				if ( allJournals.size() > 0 ) {
						// spin through the journals see if the name matches any
						Iterator i = allJournals.iterator();
						while ( i.hasNext() ) {
								Journal journalPers = (Journal)i.next();
								if ( journal.equalsIgnoreCase(journalPers.getAbbreviation()) ) {
										System.out.println("Found journal " + journal);
										return (Journal)journalPers;
								}
						}
				}
				// Create a new journal and add it
				oncotcap.datalayer.autogenpersistible.Journal newJournal = new oncotcap.datalayer.autogenpersistible.Journal();
				newJournal.setName(journal);
				newJournal.setAbbreviation(journal);
				System.out.println("Creating journal " + journal);
				newJournal.update();
				return newJournal;
		}
}
