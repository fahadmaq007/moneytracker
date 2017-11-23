package com.maqs.moneytracker.common;

/**
 *
 * The MIT License
 *
 * Copyright (c) 2011 the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * RGB Color for all charts. There are many predefined colors defined herein
 * (e.g. Color.AQUA). You can also construct a color with the usual hexdecimal
 * notation (e.g. F0F8FF). You can also set the opacity.
 * 
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public class Colors {

	/** ALICEBLUE. */
	public static final Color ALICEBLUE = Color.decode("#F0F8FF");

	/** ANTIQUEWHITE. */
	public static final Color ANTIQUEWHITE = Color.decode("#FAEBD7");

	/** AQUA. */
	public static final Color AQUA = Color.decode("#00FFFF");

	/** AQUAMARINE. */
	public static final Color AQUAMARINE = Color.decode("#7FFFD4");

	/** AZURE. */
	public static final Color AZURE = Color.decode("#F0FFFF");

	/** BEIGE. */
	public static final Color BEIGE = Color.decode("#F5F5DC");

	/** BISQUE. */
	public static final Color BISQUE = Color.decode("#FFE4C4");

	/** BLACK. */
	public static final Color BLACK = Color.decode("#000000");

	/** BLANCHEDALMOND. */
	public static final Color BLANCHEDALMOND = Color.decode("#FFEBCD");

	/** BLUE. */
	public static final Color BLUE = Color.decode("#0000FF");

	/** BLUEVIOLET. */
	public static final Color BLUEVIOLET = Color.decode("#8A2BE2");

	/** BROWN. */
	public static final Color BROWN = Color.decode("#A52A2A");

	/** BURLYWOOD. */
	public static final Color BURLYWOOD = Color.decode("#DEB887");

	/** CADETBLUE. */
	public static final Color CADETBLUE = Color.decode("#5F9EA0");

	/** CHARTREUSE. */
	public static final Color CHARTREUSE = Color.decode("#7FFF00");

	/** CHOCOLATE. */
	public static final Color CHOCOLATE = Color.decode("#D2691E");

	/** CORAL. */
	public static final Color CORAL = Color.decode("#FF7F50");

	/** CORNFLOWERBLUE. */
	public static final Color CORNFLOWERBLUE = Color.decode("#6495ED");

	/** CORNSILK. */
	public static final Color CORNSILK = Color.decode("#FFF8DC");

	/** CRIMSON. */
	public static final Color CRIMSON = Color.decode("#DC143C");

	/** CYAN. */
	public static final Color CYAN = Color.decode("#00FFFF");

	/** DARKBLUE. */
	public static final Color DARKBLUE = Color.decode("#00008B");

	/** DARKCYAN. */
	public static final Color DARKCYAN = Color.decode("#008B8B");

	/** DARKGOLDENROD. */
	public static final Color DARKGOLDENROD = Color.decode("#B8860B");

	/** DARKGRAY. */
	public static final Color DARKGRAY = Color.decode("#A9A9A9");

	/** DARKGREEN. */
	public static final Color DARKGREEN = Color.decode("#006400");

	/** DARKKHAKI. */
	public static final Color DARKKHAKI = Color.decode("#BDB76B");

	/** DARKMAGENTA. */
	public static final Color DARKMAGENTA = Color.decode("#8B008B");

	/** DARKOLIVEGREEN. */
	public static final Color DARKOLIVEGREEN = Color.decode("#556B2F");

	/** DARKORANGE. */
	public static final Color DARKORANGE = Color.decode("#FF8C00");

	/** DARKORCHID. */
	public static final Color DARKORCHID = Color.decode("#9932CC");

	/** DARKRED. */
	public static final Color DARKRED = Color.decode("#8B0000");

	/** DARKSALMON. */
	public static final Color DARKSALMON = Color.decode("#E9967A");

	/** DARKSEAGREEN. */
	public static final Color DARKSEAGREEN = Color.decode("#8FBC8F");

	/** DARKSLATEBLUE. */
	public static final Color DARKSLATEBLUE = Color.decode("#483D8B");

	/** DARKSLATEGRAY. */
	public static final Color DARKSLATEGRAY = Color.decode("#2F4F4F");

	/** DARKTURQUOISE. */
	public static final Color DARKTURQUOISE = Color.decode("#00CED1");

	/** DARKVIOLET. */
	public static final Color DARKVIOLET = Color.decode("#9400D3");

	/** DEEPPINK. */
	public static final Color DEEPPINK = Color.decode("#FF1493");

	/** DEEPSKYBLUE. */
	public static final Color DEEPSKYBLUE = Color.decode("#00BFFF");

	/** DIMGRAY. */
	public static final Color DIMGRAY = Color.decode("#696969");

	/** DODGERBLUE. */
	public static final Color DODGERBLUE = Color.decode("#1E90FF");

	/** FIREBRICK. */
	public static final Color FIREBRICK = Color.decode("#B22222");

	/** FLORALWHITE. */
	public static final Color FLORALWHITE = Color.decode("#FFFAF0");

	/** FORESTGREEN. */
	public static final Color FORESTGREEN = Color.decode("#228B22");

	/** FUCHSIA. */
	public static final Color FUCHSIA = Color.decode("#FF00FF");

	/** GAINSBORO. */
	public static final Color GAINSBORO = Color.decode("#DCDCDC");

	/** GHOSTWHITE. */
	public static final Color GHOSTWHITE = Color.decode("#F8F8FF");

	/** GOLD. */
	public static final Color GOLD = Color.decode("#FFD700");

	/** GOLDENROD. */
	public static final Color GOLDENROD = Color.decode("#DAA520");

	/** GRAY. */
	public static final Color GRAY = Color.decode("#808080");

	/** GREEN. */
	public static final Color GREEN = Color.decode("#008000");

	/** GREENYELLOW. */
	public static final Color GREENYELLOW = Color.decode("#ADFF2F");

	/** HONEYDEW. */
	public static final Color HONEYDEW = Color.decode("#F0FFF0");

	/** HOTPINK. */
	public static final Color HOTPINK = Color.decode("#FF69B4");

	/** INDIANRED. */
	public static final Color INDIANRED = Color.decode("#CD5C5C");

	/** INDIGO. */
	public static final Color INDIGO = Color.decode("#4B0082");

	/** IVORY. */
	public static final Color IVORY = Color.decode("#FFFFF0");

	/** KHAKI. */
	public static final Color KHAKI = Color.decode("#F0E68C");

	/** LAVENDER. */
	public static final Color LAVENDER = Color.decode("#E6E6FA");

	/** LAVENDERBLUSH. */
	public static final Color LAVENDERBLUSH = Color.decode("#FFF0F5");

	/** LAWNGREEN. */
	public static final Color LAWNGREEN = Color.decode("#7CFC00");

	/** LEMONCHIFFON. */
	public static final Color LEMONCHIFFON = Color.decode("#FFFACD");

	/** LIGHTBLUE. */
	public static final Color LIGHTBLUE = Color.decode("#ADD8E6");

	/** LIGHTCORAL. */
	public static final Color LIGHTCORAL = Color.decode("#F08080");

	/** LIGHTCYAN. */
	public static final Color LIGHTCYAN = Color.decode("#E0FFFF");

	/** LIGHTGOLDENRODYELLOW. */
	public static final Color LIGHTGOLDENRODYELLOW = Color.decode("#FAFAD2");

	/** LIGHTGREEN. */
	public static final Color LIGHTGREEN = Color.decode("#90EE90");

	/** LIGHTGREY. */
	public static final Color LIGHTGREY = Color.decode("#D3D3D3");

	/** LIGHTPINK. */
	public static final Color LIGHTPINK = Color.decode("#FFB6C1");

	/** LIGHTSALMON. */
	public static final Color LIGHTSALMON = Color.decode("#FFA07A");

	/** LIGHTSEAGREEN. */
	public static final Color LIGHTSEAGREEN = Color.decode("#20B2AA");

	/** LIGHTSKYBLUE. */
	public static final Color LIGHTSKYBLUE = Color.decode("#87CEFA");

	/** LIGHTSLATEGRAY. */
	public static final Color LIGHTSLATEGRAY = Color.decode("#778899");

	/** LIGHTSTEELBLUE. */
	public static final Color LIGHTSTEELBLUE = Color.decode("#B0C4DE");

	/** LIGHTYELLOW. */
	public static final Color LIGHTYELLOW = Color.decode("#FFFFE0");

	/** LIME. */
	public static final Color LIME = Color.decode("#00FF00");

	/** LIMEGREEN. */
	public static final Color LIMEGREEN = Color.decode("#32CD32");

	/** LINEN. */
	public static final Color LINEN = Color.decode("#FAF0E6");

	/** MAGENTA. */
	public static final Color MAGENTA = Color.decode("#FF00FF");

	/** MAROON. */
	public static final Color MAROON = Color.decode("#800000");

	/** MEDIUMAQUAMARINE. */
	public static final Color MEDIUMAQUAMARINE = Color.decode("#66CDAA");

	/** MEDIUMBLUE. */
	public static final Color MEDIUMBLUE = Color.decode("#0000CD");

	/** MEDIUMORCHID. */
	public static final Color MEDIUMORCHID = Color.decode("#BA55D3");

	/** MEDIUMPURPLE. */
	public static final Color MEDIUMPURPLE = Color.decode("#9370DB");

	/** MEDIUMSEAGREEN. */
	public static final Color MEDIUMSEAGREEN = Color.decode("#3CB371");

	/** MEDIUMSLATEBLUE. */
	public static final Color MEDIUMSLATEBLUE = Color.decode("#7B68EE");

	/** MEDIUMSPRINGGREEN. */
	public static final Color MEDIUMSPRINGGREEN = Color.decode("#00FA9A");

	/** MEDIUMTURQUOISE. */
	public static final Color MEDIUMTURQUOISE = Color.decode("#48D1CC");

	/** MEDIUMVIOLETRED. */
	public static final Color MEDIUMVIOLETRED = Color.decode("#C71585");

	/** MIDNIGHTBLUE. */
	public static final Color MIDNIGHTBLUE = Color.decode("#191970");

	/** MINTCREAM. */
	public static final Color MINTCREAM = Color.decode("#F5FFFA");

	/** MISTYROSE. */
	public static final Color MISTYROSE = Color.decode("#FFE4E1");

	/** MOCCASIN. */
	public static final Color MOCCASIN = Color.decode("#FFE4B5");

	/** NAVAJOWHITE. */
	public static final Color NAVAJOWHITE = Color.decode("#FFDEAD");

	/** NAVY. */
	public static final Color NAVY = Color.decode("#000080");

	/** OLDLACE. */
	public static final Color OLDLACE = Color.decode("#FDF5E6");

	/** OLIVE. */
	public static final Color OLIVE = Color.decode("#808000");

	/** OLIVEDRAB. */
	public static final Color OLIVEDRAB = Color.decode("#6B8E23");

	/** ORANGE. */
	public static final Color ORANGE = Color.decode("#FFA500");

	/** ORANGERED. */
	public static final Color ORANGERED = Color.decode("#FF4500");

	/** ORCHID. */
	public static final Color ORCHID = Color.decode("#DA70D6");

	/** PALEGOLDENROD. */
	public static final Color PALEGOLDENROD = Color.decode("#EEE8AA");

	/** PALEGREEN. */
	public static final Color PALEGREEN = Color.decode("#98FB98");

	/** PALETURQUOISE. */
	public static final Color PALETURQUOISE = Color.decode("#AFEEEE");

	/** PALEVIOLETRED. */
	public static final Color PALEVIOLETRED = Color.decode("#DB7093");

	/** PAPAYAWHIP. */
	public static final Color PAPAYAWHIP = Color.decode("#FFEFD5");

	/** PEACHPUFF. */
	public static final Color PEACHPUFF = Color.decode("#FFDAB9");

	/** PERU. */
	public static final Color PERU = Color.decode("#CD853F");

	/** PINK. */
	public static final Color PINK = Color.decode("#FFC0CB");

	/** PLUM. */
	public static final Color PLUM = Color.decode("#DDA0DD");

	/** POWDERBLUE. */
	public static final Color POWDERBLUE = Color.decode("#B0E0E6");

	/** PURPLE. */
	public static final Color PURPLE = Color.decode("#800080");

	/** RED. */
	public static final Color RED = Color.decode("#FF0000");

	/** ROSYBROWN. */
	public static final Color ROSYBROWN = Color.decode("#BC8F8F");

	/** ROYALBLUE. */
	public static final Color ROYALBLUE = Color.decode("#4169E1");

	/** SADDLEBROWN. */
	public static final Color SADDLEBROWN = Color.decode("#8B4513");

	/** SALMON. */
	public static final Color SALMON = Color.decode("#FA8072");

	/** SANDYBROWN. */
	public static final Color SANDYBROWN = Color.decode("#F4A460");

	/** SEAGREEN. */
	public static final Color SEAGREEN = Color.decode("#2E8B57");

	/** SEASHELL. */
	public static final Color SEASHELL = Color.decode("#FFF5EE");

	/** SIENNA. */
	public static final Color SIENNA = Color.decode("#A0522D");

	/** SILVER. */
	public static final Color SILVER = Color.decode("#C0C0C0");

	/** SKYBLUE. */
	public static final Color SKYBLUE = Color.decode("#87CEEB");

	/** SLATEBLUE. */
	public static final Color SLATEBLUE = Color.decode("#6A5ACD");

	/** SLATEGRAY. */
	public static final Color SLATEGRAY = Color.decode("#708090");

	private static final List<Color> colors = new ArrayList<Color>();

	static {
		Field[] declaredFields = Colors.class.getDeclaredFields();
		for (Field field : declaredFields) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				try {
					Object o = field.get(null);
					if (o instanceof Color) {
						Color c = (Color) field.get(null);
						colors.add(c);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static int length() {
		int size = colors.size();
		return size;
	}
	public static Color getColor(int index) {
		int size = colors.size();
		if (index < size) {
			return colors.get(index);
		}		
		return null;
	}
	
}
