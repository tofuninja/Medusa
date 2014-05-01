

class baseClass
{
	int x;
	int y;
	int z;
	public void doStuff(){}
	public void fooBar(int x, int y){}
	public void dance(int x, int y){}
}


class childClassA extends baseClass
{
	String hat;
	public void putOnHat(){}
}
class childClassB extends baseClass
{
	String shirt;
	public void putOnShirt(){}
}
class childClassC extends baseClass
{
	String pants;
	public void putOnPants(){}
}
class someOtherClass extends baseClass
{
	ArrayList<String> thingstodo;
}

class classesAreCool extends baseClass
{
	public String getReallyCoolString(){}
	public int getReallyCoolInt(){}
	public char getReallyCoolChar(){}
}

class javaThing extends baseClass
{
	public int doSomethingAJavaThingWouldDo(String x);
}


class userClass
{
	ArrayList<baseClass> list;
}

/**
*<h3> server </h3>
* A simple server interface... doesnt really do anything... 
*/
interface server
{
	void reciveMessage(message m);
}


class message
{

}

class network
{
	String ip;
	String username;
	String password;
}

class html
{
	String sillyHtmlStuff;
}

class log
{
	public void writeLogln(String s){}
	public void flushLog(){}
	String logContents;
	boolean canLog;
}