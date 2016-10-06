package org.modelsphere.plugins.html.browser.htmlgeneration.images;

public class ThemeManager
{
	private int currentTheme;

	//////
	//singleton design pattern
	private static ThemeManager themeManager = null;

	private ThemeManager()
	{
		currentTheme = WINDOWS_XP; //by default
	}

	public static ThemeManager getThemeManager()
	{
		if (themeManager == null)
		{
			themeManager = new ThemeManager();
		}

		return themeManager;
	}

	//
	//////

	//the defined themes
	public static final int WINDOWS_XP = 0;
	public static final int WINDOWS_VISTA = 1;

	//the thematic properties
	private static final int MAXIMIZE_ICON = 0;
	private static final int MINIMIZE_ICON = 1;
	private static final int TITLE_BG_ICON = 2;
	private static final int TITLE_FG_COLOR = 3;
	private static final int BORDER_COLOR = 4;

	//the thematic values
	private String[][] themeValues = new String[][] {
			//              Windows-XP      Windows-Vista
			new String[] { "maximize.png", "maximize-vista.png" }, //MAXIMIZE_ICON
			new String[] { "minimize.png", "minimize-vista.png" }, //MINIMIZE_ICON
			new String[] { "title.jpg", "title-vista.jpg" }, //TITLE_BG_ICON
			new String[] { "#ffffff", "#000000" }, //TITLE_FG_COLOR
			new String[] { "#0734da", "#aaaaaa" }, //BORDER_COLOR		 
	};

	public void setCurrentTheme(int theme)
	{
		this.currentTheme = theme;
	}

	public String getMaximizeIcon()
	{
		String icon = themeValues[MAXIMIZE_ICON][this.currentTheme];
		return icon;
	}

	public String getMinimizeIcon()
	{
		String icon = themeValues[MINIMIZE_ICON][this.currentTheme];
		return icon;
	}

	public String getTitleBgIcon()
	{
		String icon = themeValues[TITLE_BG_ICON][this.currentTheme];
		return icon;
	}

	public String getTitleFgColor()
	{
		String color = themeValues[TITLE_FG_COLOR][this.currentTheme];
		return color;
	}

	public String getBorderColor()
	{
		String color = themeValues[BORDER_COLOR][this.currentTheme];
		return color;
	}

}
