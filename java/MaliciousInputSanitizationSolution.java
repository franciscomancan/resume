package _03_sanitizationExercise;

/** 
 * @author donjuanson 20180912
 * 
 * This is related to the exercise on a servlet. 
 */
public class MaliciousInputSanitizationSolution {

	/** This was a solution used in a servlet to remove 
	 *  potentially malicious characters.
	 *  
	 *  Another problem with using regular expressions (i've found)
	 *  is that they can take up quite a bit of computational resources 
	 *  to evaluate strings.  There are also certain types of strings which
	 *  can lead regexs to DoS type of attacks, which pin CPUs for some
	 *  amount of time.  As an option, just remove/blacklist specific characters
	 *  or whitelist certain strings... 
	 *  
	 *  	Citation: https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS
	 *  
	 *  
	 *    
	 * @param s
	 * @return
	 */
	public String normalizeAndSanitize(String s)
	{
		// Deletes non-character code points
		s = s.replaceAll("[\\p{Cn}]", "");
		// Replace anything that is not alphanumeric
		s = s.replaceAll("[^A-Za-z_]", "_");
		return s;
	}
	
	public static void main(String[] argv)
	{
		String[] tests = 
		{
				"<img src=\"file:///etc/bash.bashrc\">",
				"<div someCrossSiteScriptingInjections>",
				"dingdangdoodey@theblackhat.com"
		};
		
		MaliciousInputSanitizationSolution solution = new MaliciousInputSanitizationSolution();
		for(String test : tests)
			System.out.println(solution.normalizeAndSanitize(test));
		
	}
}
