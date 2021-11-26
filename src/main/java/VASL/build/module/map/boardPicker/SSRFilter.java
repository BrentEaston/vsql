/*
 * $Id: SSRFilter.java 8529 2012-12-26 04:36:44Z uckelman $
 *
 * Copyright (c) 2000-2003 by Rodney Kinney
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
package VASL.build.module.map.boardPicker;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Map.Entry;

import VASSAL.build.GameModule;
import VASSAL.tools.DataArchive;

public class SSRFilter extends RGBImageFilter {
  /*
   * * A class to swap colors according to specified rules * A set of color names is read from an input file with the
   * format * White 255,255,255 * Black 0,0,0 * * The swapping rules are read from an input file with the format: *
   * <key> * <color>=<color> * <color>=<color> * <color>=<color> * There can be any number of color entries per key. *
   * The color entries are names of colors as defined in the color file * Example: * * WoodsToBrush * WoodsGreen=BrushL0 *
   * WoodsBlack=BrushL0
   */
  private Map<Integer, Integer> mappings;
  private String saveRules;
  private Map<String, Integer> colorValues;
  private List<SSROverlay> overlays;
  private File archiveFile;
  private DataArchive archive;
  private ASLBoard board;

  public SSRFilter(String listOfRules, File archiveFile, ASLBoard board) throws BoardException {
    canFilterIndexColorModel = true;
    saveRules = listOfRules;
    this.archiveFile = archiveFile;

    try {
      archive = new DataArchive(archiveFile.getPath());
      if (!archive.contains("data")) {
        throw new FileNotFoundException("data");
      }
    }
    catch (IOException ex) {
      throw new BoardException("Board does not support terrain alterations");
    }

    this.board = board;
    readAllRules();
  }

  private static InputStream getStream(String name) {
    try {
      return GameModule.getGameModule().getDataArchive().getInputStream(name);
    }
    catch (IOException ex) {
      return null;
    }
  }

  public Iterable<SSROverlay> getOverlays() {
    return overlays;
  }

  public int filterRGB(int x, int y, int rgb) {
    return ((0xff000000 & rgb) | newValue(rgb & 0xffffff));
  }

  private int newValue(int rgb) {
    /*
     * * Maps the color to it's transformed value by going through * the rules. All rules are applied in sequence.
     */
    int rval = rgb;
    Integer mappedValue = mappings.get(rgb);
    if (mappedValue != null) {
      rval = mappedValue;
    }
    return rval;
  }

  public String toString() {
    return saveRules;
  }

  private int parseRGB(String s) {
    /*
     * * Calculate integer value from rr,gg,bb or 40a38f format
     */
    int rval = -1;
    try {
      Integer test = (Integer) colorValues.get(s);
      if (test != null) {
        rval = test.intValue();
      }
      else if (s.indexOf(',') >= 0) {
        StringTokenizer st = new StringTokenizer(s, ",");
        if (st.countTokens() == 3) {
          int red, green, blue;
          red = Integer.parseInt(st.nextToken());
          green = Integer.parseInt(st.nextToken());
          blue = Integer.parseInt(st.nextToken());
          if ((red >= 0 && red <= 255) && (green >= 0 && green <= 255) && (blue >= 0 && blue <= 255)) {
            rval = (red << 16) + (green << 8) + blue;
          }
        }
      }
      else if (s.length() == 6) {
        rval = Integer.parseInt(s, 16);
      }
    }
    catch (Exception e) {
      rval = -1;
    }
    return rval;
  }

  public void readAllRules() {
    // Build the list of rules in use
    Vector rules = new Vector();
    StringTokenizer st = new StringTokenizer(saveRules);
    while (st.hasMoreTokens()) {
      String s = st.nextToken();
      if (!rules.contains(s)) {
        rules.addElement(s);
      }
    }

    mappings = new HashMap<Integer, Integer>();
    colorValues = new HashMap<String, Integer>();
    overlays = new Vector();

    final DataArchive da = GameModule.getGameModule().getDataArchive();

    // Read board-specific colors last to override defaults
    try (InputStream in = da.getInputStream("boardData/colors")) {
      readColorValues(in);
    }
    catch (IOException ignore) {
    }

    try (InputStream in = archive.getInputStream("colors")) {
      readColorValues(in);
    }
    catch (IOException ignore) {
    }

    // Read board-specific rules first to be applied before defaults
    try (InputStream in = archive.getInputStream("colorSSR")) {
      readColorRules(in, rules);
    }
    catch (IOException ignore) {
    }

    try (InputStream in = da.getInputStream("boardData/colorSSR")) {
      readColorRules(in, rules);
    }
    catch (IOException ignore) {
    }

    overlays.clear();
    // SSR Overlays are applied in reverse order to the order they're listed
    // in the overlaySSR file. Therefore, reading board-specific
    // overlay rules first will override defaults
    try (InputStream in = archive.getInputStream("overlaySSR")) {
      readOverlayRules(in);
    }
    catch (IOException ignore) {
    }

    try (InputStream in = da.getInputStream("boardData/overlaySSR")) {
      readOverlayRules(in);
    }
    catch (IOException ignore) {
    }
  }

  protected void readColorValues(InputStream in) {
    /*
     * * Add to the list of color definitions, as read from input file
     */
    if (in == null) {
      return;
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    StreamTokenizer st = new StreamTokenizer(reader);
    st.resetSyntax();
    st.wordChars((int) ' ', 0xff);
    st.commentChar((int) '/');
    st.whitespaceChars((int) ' ', (int) ' ');
    st.whitespaceChars((int) '\n', (int) '\n');
    st.whitespaceChars((int) '\t', (int) '\t');
    st.slashSlashComments(true);
    st.slashStarComments(true);
    st.eolIsSignificant(false);
    try {
      for (String s = reader.readLine(); s != null; s = reader.readLine()) {
        if (s.startsWith("/")) {
          continue;
        }
        StringTokenizer st2 = new StringTokenizer(s);
        if (st2.countTokens() < 2) {
          continue;
        }
        String s1 = st2.nextToken();
        int rgb = parseRGB(st2.nextToken());
        if (rgb >= 0) {
          colorValues.put(s1, rgb);
        }
        else {
          System.err.println("Invalid color alias: " + s);
        }
      }
    }
    catch (Exception e) {
      System.err.println("Caught " + e + " reading colors");
    }
  }

  public void readColorRules(InputStream in, Vector rules) {
    /*
     * * Define the color transformations defined by each rule * as read in from input file
     */
    if (in == null) {
      return;
    }

    StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(in)));
    st.resetSyntax();
    st.wordChars((int) ' ', 0xff);
    st.commentChar((int) '/');
    st.whitespaceChars((int) ' ', (int) ' ');
    st.whitespaceChars((int) '\n', (int) '\n');
    st.whitespaceChars((int) '\t', (int) '\t');
    st.slashSlashComments(true);
    st.slashStarComments(true);
    st.eolIsSignificant(false);
    boolean inCategory = false; /* are we in a "selected" category */
    try {
      while (st.nextToken() != StreamTokenizer.TT_EOF) {
        String s = st.sval;
        if (s == null) {
          continue;
        }
        int n = s.indexOf('=');
        if (n < 0) {
          if (s.charAt(0) == '+') {
            inCategory = rules.contains(s.substring(1));
          }
          else {
            inCategory = rules.removeElement(s);
          }
        }
        else if (inCategory) {
          int len = s.length();
          boolean valid = true;
          if (n + 1 == len) {
            valid = false;
          }
          else {
            String sfrom = s.substring(0, n);
            String sto = s.substring(n + 1, len);
            int ifrom = parseRGB(sfrom);
            int ito = parseRGB(sto);
            if (ifrom >= 0 && ito >= 0) {

              if (!mappings.containsKey(ifrom))
            	  mappings.put(ifrom, ito);

              /*
               * Also apply this mapping to previous mappings
               */
              if (mappings.containsValue(ifrom)) {
	              for(Iterator<Entry<Integer,Integer>> it = mappings.entrySet().iterator(); it.hasNext(); ) {
	            	  Entry<Integer,Integer> e = it.next();
	            	  if (e.getValue() == ifrom)
	            		  e.setValue(ito);
	              }
              }
            }
            else {
              valid = false;
              System.err.println("Invalid color mapping: " + s + " mapped to " + ifrom + "=" + ito);
            }
          }
          if (!valid) {
            System.err.println("Invalid color mapping: " + s);
          }
        }
      }
    }
    catch (Exception e) {
    }
  }

  public void readOverlayRules(InputStream in) {
    if (in == null) {
      return;
    }

    try {
      BufferedReader file;
      file = new BufferedReader(new InputStreamReader(in));
      String s;
      while ((s = file.readLine()) != null) {
        if (s.trim().length() == 0) {
          continue;
        }
        if (saveRules.indexOf(s.trim()) >= 0) {
          while ((s = file.readLine()) != null) {
            if (s.length() == 0) {
              break;
            }
            else if (s.toLowerCase().startsWith("underlay")) {
              try {
                StringTokenizer st = new StringTokenizer(s);
                st.nextToken();
                String underImage = st.nextToken();
                st = new StringTokenizer(st.nextToken(), ",");
                int trans[] = new int[st.countTokens()];
                int n = 0;
                while (st.hasMoreTokens()) {
                  trans[n++] = ((Integer) colorValues.get(st.nextToken())).intValue();
                }
                overlays.add(new Underlay(underImage, trans, archive, board));
              }
              catch (NoSuchElementException end) {
              }
            }
            else {
              overlays.add(new SSROverlay(s.trim(),archiveFile));
            }
          }
        }
      }
    }
    catch (Exception e) {
      System.err.println("Error opening rules file " + e);
    }
  }

  public Image recolor(Image oldImage, Component observer) {
    return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(oldImage.getSource(), this));
  }

  public void transform(BufferedImage image) {
    if (!mappings.isEmpty()) {
      final int h = image.getHeight();
      final int[] row = new int[image.getWidth()];
      for (int y = 0; y < h; ++y) {
        image.getRGB(0, y, row.length, 1, row, 0, row.length);
        for (int x = 0; x < row.length; ++x) {
          row[x] = filterRGB(x, y, row[x]);
        }
        image.setRGB(0, y, row.length, 1, row, 0, row.length);
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof SSRFilter && saveRules.equals(((SSRFilter) obj).saveRules);
  }

  @Override
  public int hashCode() {
    return saveRules.hashCode();
  }
}
