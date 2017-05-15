package com.ecochain.ledger.util;

import java.awt.Color;
/** 
 * 类名称：ColorTransfer
 * 创建人：zhangchunming
 * 创建时间：2015年04月24日
 * 功能：将rgb（）颜色值转为16进制颜色
 * @version
 */
public class ColorTransfer extends Color {

	public ColorTransfer(int r, int g, int b) {
		super(r, g, b);
		// TODO Auto-generated constructor stub
	}
	/** 
	* * Returns the HEX value representing the colour in the default sRGB 
	* ColorModel. * * @return the HEX value of the colour in the default sRGB 
	* ColorModel 
	*/ 
	public String getHex() { 
	return toHex(getRed(), getGreen(), getBlue()); 
	} 

	/** 
	* * Returns a web browser-friendly HEX value representing the colour in the 
	* default sRGB * ColorModel. * * @param r red * @param g green * @param b 
	* blue * @return a browser-friendly HEX value 
	*/ 
	public static String toHex(int r, int g, int b) { 
	return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) 
	+ toBrowserHexValue(b); 
	} 

	private static String toBrowserHexValue(int number) { 
	StringBuilder builder = new StringBuilder( 
	Integer.toHexString(number & 0xff)); 
	while (builder.length() < 2) { 
	builder.append("0"); 
	} 
	return builder.toString().toUpperCase(); 
	} 
}
