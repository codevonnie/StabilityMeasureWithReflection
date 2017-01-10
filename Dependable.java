/**
 * Interface with methods for checking dependencies on classes in Jar files
 */

package ie.gmit.sw;

public interface Dependable
{
	public void checkConstructors();
	public void checkFields();
	public void checkMethods();
}
