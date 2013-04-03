package oncotcap.display.browser;

public class AnEvent extends DependencyNoun {
				public AnEvent(PreprocessedCodeBundle cb, String varName) {
						super(cb, varName);
				}
				public AnEvent(PreprocessedCodeBundle cb, String varName, boolean noClass) {
						super(cb, varName, noClass);
				}
				public AnEvent(String className, String varName) {
						super(className, varName);
				}
				public AnEvent(String varName) {
						super((String)null, varName);
				}

		}
