/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Gal�n y Martins
 * Author: Daniel Gal�n y Martins
 * Creation date: 02.05.2003
 * Revision: $Revision: 1.8 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:59 $
 * $Log: VT100TerminalIO.java,v $
 * Revision 1.8  2009/12/02 21:53:59  danielgalan
 * naming
 *
 * Revision 1.7 2009/03/30 09:50:45 ilgian
 * Fixed Beeper Thread bug (not closing with IO)
 * Added Thread name (useful for remote debugging purposes)
 * Revision 1.6 2009/03/06 11:17:52 ilgian
 * disabled echo during beeps
 * Revision 1.5 2009/03/04 11:36:17 ilgian
 * Enhanced support for beeps
 * Added width/height members
 * Revision 1.4 2009/02/26 16:50:28 ilgian
 * Added beep with beepCount parameter
 * Revision 1.3 2005/05/23 18:10:20 danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 * Revision 1.2 2004/12/05 17:53:49 danielgalan
 * Refactored this damn beepError thing
 * Revision 1.1 2004/07/26 21:40:28 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;



/**
 * a VT100-compatible terminal
 * 
 * @author Daniel Gal�n y Martins
 * @version $Revision: 1.8 $
 */

public class VT100TerminalIO extends BasicTerminalIO {

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** LowLevel Out-Access */
	private final VT100Writer out;

	/** LowLevel In-Access */
	private final VT100Reader in;

	private final Beeper beeper;

	private final IACHandler IACHandler;

	private int terminalWidth;

	private int terminalHeight;


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates a new VT100 object */
	public VT100TerminalIO(Socket socket) throws IOException {
		super();
		log.debug("Creating instance of VT100Terminal");
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		IACHandler = new IACHandler(bis, bos);
		out = new VT100Writer(bos);
		in = new VT100Reader(bis);
		in.setIACHandler(new IACHandler(bis, bos));
		beeper = new Beeper(socket.getOutputStream());
		beeper.start();

		this.socket = socket;
	}


	//--------------------------------------------------------------------------
	// implementation of the abstract class BasicTerminal
	//--------------------------------------------------------------------------

	/** Returns a VT100Reader */
	@Override
	protected Reader getReader() {
		return in;
	}


	/** Returns a VT100Writer */
	@Override
	protected Writer getWriter() {
		return out;
	}


	//--------------------------------------------------------------------------
	// implementation of the interface TerminalInterface
	//--------------------------------------------------------------------------

	/** absolut cursor movement on a terminal display */
	public void cursorMoveAbsolut(int line, int column) throws IOException {
		out.cursorMoveAbsolut(line, column);
	}


	/** relative cursor movement on a terminal display */
	public void cursorMoveRelative(int direction, int steps) throws IOException {
		out.cursorMoveRelative(direction, steps);
	}


	/** writes <code>text</code> to the current cursor position */
	public void writeText(String text) throws IOException {
		out.writeText(text);
	}


	/** writes <code>text</code> to the specific position */
	public void writeText(String text, int posLine, int posColumn) throws IOException {
		out.writeText(text, posLine, posColumn);
	}


	/** writes <code>text</code> invers to the current cursor position */
	public void writeInverseText(String text) throws IOException {
		out.writeInverseText(text);
	}


	/** writes <code>text</code> invers to the specific position */
	public void writeInverseText(String text, int posLine, int posColumn) throws IOException {
		out.writeInverseText(text, posLine, posColumn);
	}


	/** sets the bold text style */
	public void setBold(boolean bold) throws IOException {
		out.setBold(bold);
	}


	/** sets the underline text style */
	public void setUnderlined(boolean underlined) throws IOException {
		out.setUnderscore(underlined);
	}


	/** sets the blink text style */
	public void setBlink(boolean blink) throws IOException {
		out.setBlink(blink);
	}


	/** sets the invers text style */
	public void setReverse(boolean reverse) throws IOException {
		out.setReverse(reverse);
	}


	/** sets the foreground color according to it's constants */
	public void setForeground(int color) throws IOException {
		out.setForeground(color);
	}


	/** sets the background color according to it's constants */
	public void setBackground(int color) throws IOException {
		out.setBackground(color);
	}


	/** creates manually line break */
	public void newLine() throws IOException {
		out.newLine();
	}


	/** clears the screen */
	public void clearScreen() throws IOException {
		out.clearScreen();
	}


	/** draws a line */
	public void drawLine(int line, int column, int orientation, int length) throws IOException {
		out.drawLine(line, column, orientation, length);
	}


	/** draws an not filled rectangle */
	public void drawRectangle(int fromLine, int fromColumn, int toLine, int toColumn) throws IOException {
		out.drawRectangle(fromLine, fromColumn, toLine, toColumn);
	}


	/** clears a line */
	public void clearLine(int line, int column, int orientation, int length) throws IOException {
		out.clearLine(line, column, orientation, length);
	}


	/** writes content of the writer buffer */
	public synchronized void flush() throws IOException {
		out.flush();
	}


	/** reads the next event from the reader */
	public TerminalEvent readNextEvent() throws IOException {
		return in.readNextEvent();
	}


	/** Makes an errortone */
	public void beepError() throws IOException {
		beeper.beep();
	}


	/** Makes an errortone */
	public void beepError(int number) throws IOException {
		for (int i = 0; i < number; i++)
			beeper.beep();
	}


	@Override
	public void close() throws IOException {
		beeper.disable();
		super.close();
	}

	/**
	 * Thread to write the beep chars directly to the output stream
	 * with a sufficient delay within beeps. The beeps are written
	 * on the socket stream without buffering.
	 */
	private class Beeper extends Thread {

		private int beepCount = 0;
		private final OutputStreamWriter osw;
		private boolean enabled = true;
		private boolean isNameSet = false;


		public Beeper(OutputStream outStream) {
			osw = new OutputStreamWriter(outStream);
		}


		public synchronized void beep() {
			beepCount = getBeepCount() + 1;
		}


		private synchronized int getBeepCount() {
			return beepCount;
		}


		private synchronized void sendBeep() {
			beepCount = getBeepCount() - 1;
			try {
				osw.write(VT100Constants.BEEP_ERROR);
				osw.flush();
				try {
					Thread.sleep(300);
				}
				catch (InterruptedException e) {
					//ignore
				}
			}
			catch (IOException e) {
				log.error(e);
			}
		}


		public void disable() {
			enabled = false;
		}


		@Override
		public void run() {
			while (enabled) {
				if (!isNameSet) {
					try {
						setName(socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ".Beeper");
						isNameSet = true;
					}
					catch (Throwable e) {
						isNameSet = false;
					}
				}
				if (getBeepCount() > 0)
					sendBeep();
				try {
					sleep(200);
				}
				catch (InterruptedException e) {
					//ignore
				}
			}
		}

	}

}
