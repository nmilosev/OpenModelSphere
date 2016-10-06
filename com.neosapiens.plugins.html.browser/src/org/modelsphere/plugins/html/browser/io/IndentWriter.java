/*************************************************************************

This file is part of Open ModelSphere HTML Reports Project.

Open ModelSphere HTML Reports is free software; you can redistribute
it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can contact us at :
http://www.javaforge.com/project/3219

 **********************************************************************/

package org.modelsphere.plugins.html.browser.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * This class is used to perform adequate indentation in a designated
 * OutputStreamWriter.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class IndentWriter extends OutputStreamWriter
{
	/** Static value that determine the size of the indentations */
	protected static final int DEFAULT_INDENTATION_WIDTH = 4;

	/** Parameters that determine the size of indentation */
	protected int width;
	/** Parameters that determine the level of indentation */
	protected int level;
	/** Boolean value that indicate the current line is new */
	protected boolean newline;

	/**
	 * Constructor
	 * 
	 * @param out
	 *            the OutputStream to which the IndentWriter is directed
	 */
	public IndentWriter(OutputStream out)
	{
		super(out);
		init(DEFAULT_INDENTATION_WIDTH);
	}

	/**
	 * Constructor
	 * 
	 * @param out
	 *            the OutputStream to which the IndentWriter is directed
	 * @param width
	 *            the desired size for the indentation
	 * @param charset
	 *            the desired encoding for the IndentWriter
	 * @throws UnsupportedEncodingException
	 */
	public IndentWriter(OutputStream out, int width, String charset) throws UnsupportedEncodingException
	{
		super(out, charset);
		init(width);
	}

	/**
	 * Initialize the IndentWriter.
	 * 
	 * @param indentationWidth
	 *            the size of the indentation for the writer
	 */
	protected void init(int indentationWidth)
	{
		this.width = indentationWidth;
		this.level = 0;
		this.newline = true;
	}

	/**
	 * Increases the indentation level by one.
	 */
	public void indent()
	{
		++this.level;
	}

	/**
	 * Decreases the indentation level by one.
	 */
	public void unindent()
	{
		if (--this.level < 0)
			this.level = 0;
	}

	/**
	 * Return the current level of identation.
	 * 
	 * @return the current indentation level
	 */
	public int getCurrentIndentation()
	{
		return this.level;
	}

	/**
	 * Print a new empty line.
	 * 
	 * @throws IOException
	 */
	public void println() throws IOException
	{
		super.write("\r\n");
		this.newline = true;
	}

	/**
	 * Print a new line containing a string.
	 * 
	 * @param str
	 *            the string to print
	 * @throws IOException
	 */
	public void println(String str) throws IOException
	{
		doIndent();
		super.write(str + "\r\n");
		this.newline = true;
	}

	/**
	 * Performs indentation by printing the correct number of tabs and spaces.
	 * 
	 * @throws IOException
	 */
	protected void doIndent() throws IOException
	{
		if (this.newline)
		{
			int spaces = this.level * this.width;
			while (spaces >= 8)
			{
				super.write("        ");
				spaces -= 8;
			}
			super.write("        ".substring(0, spaces));
			this.newline = false;
		}
	}
}