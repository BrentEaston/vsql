package VSQL;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.KeyStroke;

import VASSAL.build.AutoConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.map.Zoomer;
import VASSAL.configure.Configurer;
import VASSAL.configure.ConfigurerFactory;
import VASSAL.configure.IconConfigurer;
import VASSAL.preferences.Prefs;
import VASSAL.tools.LaunchButton;

/** 
 * @deprecated
 */
public class VSQLZoomer extends Zoomer {

  protected LaunchButton zoomMinButton;
  protected LaunchButton zoomMaxButton;
  
  protected static final String ZOOM_MAX = "zoomMaxKey";
  protected static final String MAX_TOOLTIP = "maxTooltip";
  protected static final String MAX_BUTTON_TEXT = "maxButtonText";
  protected static final String MAX_ICON_NAME = "maxIconName";
  protected static final String MAX_DEFAULT_ICON = null;
  
  protected static final String ZOOM_MIN = "zoomMinKey";
  protected static final String MIN_TOOLTIP = "minTooltip";
  protected static final String MIN_BUTTON_TEXT = "minButtonText";
  protected static final String MIN_ICON_NAME = "minIconName";
  protected static final String MIN_DEFAULT_ICON = null;
  
  public VSQLZoomer() {
    super();
    
    ActionListener zoomMin = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        zoomMin();
      }
    };
    
    ActionListener zoomMax = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        zoomMax();
      }
    };
    
    zoomMinButton = new LaunchButton(null, MIN_TOOLTIP, MIN_BUTTON_TEXT, ZOOM_MIN, MIN_ICON_NAME, zoomMin);
    zoomMinButton.setAttribute(MIN_TOOLTIP, "Zoom right out");
    zoomMinButton.setAttribute(MIN_ICON_NAME, MIN_DEFAULT_ICON);

    zoomMaxButton = new LaunchButton(null, MAX_TOOLTIP, MAX_BUTTON_TEXT, ZOOM_MAX, MAX_ICON_NAME, zoomMax);
    zoomMaxButton.setAttribute(MAX_TOOLTIP, "Zoom right in");
    zoomMaxButton.setAttribute(MAX_ICON_NAME, MAX_DEFAULT_ICON);
    
  }
  
  public void zoomIn() {
    super.zoomIn();
    setZoomButtons();
  }

  public void zoomOut() {
    super.zoomOut();
    setZoomButtons();
  }
  
  public void zoomMin() {
    if (zoomMinButton.isEnabled()) {
      Rectangle r = map.getView().getVisibleRect();
      Point center = new Point(r.x + r.width / 2, r.y + r.height / 2);
      center = map.mapCoordinates(center);

      setZoomLevel(0);
      setZoomButtons();

      map.centerAt(center);

      map.repaint(true);
      map.getView().revalidate();
    }
  }

  public void zoomMax() {
    if (zoomMaxButton.isEnabled()) {
      Rectangle r = map.getView().getVisibleRect();
      Point center = new Point(r.x + r.width / 2, r.y + r.height / 2);
      center = map.mapCoordinates(center);

      setZoomLevel(state.getLevelCount()-1);
      setZoomButtons();

      map.centerAt(center);

      map.repaint(true);
      map.getView().revalidate();
    }
  }
  
  public void setZoomButtons() {
    zoomInButton.setEnabled(state.hasHigherLevel());
    zoomOutButton.setEnabled(state.hasLowerLevel());
    zoomMinButton.setEnabled(state.hasLowerLevel());
    zoomMaxButton.setEnabled(state.hasHigherLevel());
  }
  
  public void setup(boolean gameStarting) {
    super.setup(gameStarting);

    if (gameStarting) {
      
      Prefs prefs = GameModule.getGameModule().getPrefs();
      
      int newZoomLevel = ((Integer) prefs.getValue(VSQLProperties.ZOOM_LEVELS)).intValue();
      int newZoomStart = ((Integer) prefs.getValue(VSQLProperties.ZOOM_START)).intValue();
      double newZoomFactor = ((Double) prefs.getValue(VSQLProperties.ZOOM_FACTOR)).doubleValue();
      
      final List<VSQLScenInfo> l = GameModule.getGameModule().getComponentsOf(VSQLScenInfo.class);
      if (!l.isEmpty()) {
        final VSQLScenInfo info = l.get(0);
        if (newZoomLevel == 0) {
          newZoomLevel = info.getDefaultZoomLevels();
        }
        if (newZoomStart == 0) {
          newZoomStart = info.getDefaultZoomStart();
        }
        if (newZoomFactor == 0.0) {
          newZoomFactor = info.getDefaultMagnification();
        }
      }  
      
      if (newZoomLevel != 0) {
        setAttribute("max", String.valueOf(newZoomLevel));
      }
      if (newZoomStart != 0) {
        setAttribute(ZOOM_START, String.valueOf(newZoomStart));
      }
      if (newZoomFactor != 0.0) {
        setAttribute("factor", String.valueOf(newZoomFactor));
      }
    }
    else {
      setZoomLevel(state.getInitialLevel());
    }

    setZoomButtons();       
  }
  
  public void addTo(Buildable b) {
    super.addTo(b);
    map.getToolBar().add(zoomMinButton);
    map.getToolBar().add(zoomMaxButton);
  }
  
  public void removeFrom(Buildable b) {
    super.removeFrom(b);
    map.getToolBar().remove(zoomMinButton);
    map.getToolBar().remove(zoomMaxButton);
  }
    
  public String getAttributeValueString(String key) {
    if (zoomInButton.getAttributeValueString(key) != null) {
      return zoomInButton.getAttributeValueString(key);
    }
    else if (zoomOutButton.getAttributeValueString(key) != null) {
      return zoomOutButton.getAttributeValueString(key);
    }
    else if (zoomMinButton.getAttributeValueString(key) != null) {
      return zoomMinButton.getAttributeValueString(key);
    }
    else if (zoomMaxButton.getAttributeValueString(key) != null) {
      return zoomMaxButton.getAttributeValueString(key);
    }    
    else {
      return super.getAttributeValueString(key);
    }
  }

  public void setAttribute(String key, Object val) {
    super.setAttribute(key, val);
    zoomMinButton.setAttribute(key, val);
    zoomMaxButton.setAttribute(key, val);
  }
  
  public static class MinIconConfig implements ConfigurerFactory {
    public Configurer getConfigurer(AutoConfigurable c, String key, String name) {
      return new IconConfigurer(key, name, MIN_DEFAULT_ICON);
    }
  }
  
  public static class MaxIconConfig implements ConfigurerFactory {
    public Configurer getConfigurer(AutoConfigurable c, String key, String name) {
      return new IconConfigurer(key, name, MAX_DEFAULT_ICON);
    }
  }
  
  public String[] getAttributeNames() {
    return new String[]{"factor", "max", ZOOM_START, 
        IN_TOOLTIP, IN_BUTTON_TEXT, IN_ICON_NAME, ZOOM_IN, 
        OUT_TOOLTIP, OUT_BUTTON_TEXT, OUT_ICON_NAME, ZOOM_OUT,
        MIN_TOOLTIP, MIN_BUTTON_TEXT, MIN_ICON_NAME, ZOOM_MIN, 
        MAX_TOOLTIP, MAX_BUTTON_TEXT, MAX_ICON_NAME, ZOOM_MAX};
  }

  public String[] getAttributeDescriptions() {
    return new String[]{"Magnification factor",
                        "Number of zoom levels",
                        "Starting zoom level",
                        "Zoom in tooltip text",
                        "Zoom in button text",
                        "Zoom in Icon",
                        "Zoom in hotkey",
                        "Zoom out tooltip text",
                        "Zoom out button text",
                        "Zoom out Icon",
                        "Zoom out hotkey",
                        "Zoom right out tooltip text",
                        "Zoom right out button text",
                        "Zoom right out Icon",
                        "Zoom right out hotkey",
                        "Zoom right in tooltip text",
                        "Zoom right in button text",
                        "Zoom right in Icon",
                        "Zoom right in hotkey",
                        };
  }

  public Class[] getAttributeTypes() {
    return new Class[]{Double.class,
                       Integer.class,
                       Integer.class,
                       String.class,
                       String.class,
                       InIconConfig.class,
                       KeyStroke.class,
                       String.class,
                       String.class,
                       OutIconConfig.class,
                       KeyStroke.class,
                       String.class,
                       String.class,
                       OutIconConfig.class,
                       KeyStroke.class,
                       String.class,
                       String.class,
                       OutIconConfig.class,
                       KeyStroke.class};
  }
  
}
