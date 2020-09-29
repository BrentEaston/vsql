/*
 * Copyright (c) 2000-2003 by David Sullivan
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package CASL.MapBuilder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class MapEditFrameBeanInfo extends SimpleBeanInfo {
	Class beanClass = MapEditFrame.class;
	String iconColor16x16Filename;
	String iconColor32x32Filename;
	String iconMono16x16Filename;
	String iconMono32x32Filename;

	public MapEditFrameBeanInfo() {
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try  {
			PropertyDescriptor _map = new PropertyDescriptor("map", beanClass, "getMap", "setMap");
			_map.setDisplayName("map");
			_map.setShortDescription("map");
			PropertyDescriptor _mapImage = new PropertyDescriptor("mapImage", beanClass, "getMapImage", "setMapImage");
			_mapImage.setDisplayName("mapImage");
			_mapImage.setShortDescription("mapImage");
			PropertyDescriptor[] pds = new PropertyDescriptor[] {
				_map,
				_mapImage,};
			return pds;


		}
		catch(IntrospectionException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public java.awt.Image getIcon(int iconKind) {
		switch (iconKind) {
			case BeanInfo.ICON_COLOR_16x16:
			  return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
			case BeanInfo.ICON_COLOR_32x32:
			  return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
			case BeanInfo.ICON_MONO_16x16:
			  return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
			case BeanInfo.ICON_MONO_32x32:
			  return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
		}
		return null;
	}

	public BeanInfo[] getAdditionalBeanInfo() {
		Class superclass = beanClass.getSuperclass();
		try  {
			BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
			return new BeanInfo[] { superBeanInfo };
		}
		catch(IntrospectionException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
 
