import java.awt.Color;


public class javaRef 
{
	
	public static Color normalCol = new Color(33, 96, 255,100);
	public static Color extCol = new Color(0, 135, 0);
	public static Color interCol = new Color(214, 157, 0);
	
	public static boolean[] enabled = new boolean[3];
	
	static
	{
		for(int i = 0; i < enabled.length; i++)
		{
			enabled[i] = true;
		}
	}
	
	JavaClass start;
	JavaClass end;
	Color color = normalCol;
	boolean thick = false;
	int type = 0;
	
	public javaRef(JavaClass s, JavaClass e)
	{
		start = s;
		end = e;
		
		if(start.extendsClass.equals(end.className)) 
		{
			color = extCol;
			//thick = true;
			type = 1;
		}
		
		for(String inter: start.implementsInterfaces)
		{
			if(inter.equals(end.className))
			{
				color = interCol;
				//thick = true;
				type = 2;
				break;
			}
		}
		
	}
}
