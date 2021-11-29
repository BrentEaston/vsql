package VSQL;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.List;

import javax.swing.JComponent;

import VASSAL.build.module.map.CounterDetailViewer;
import VASSAL.counters.GamePiece;
import VASSAL.tools.image.LabelUtils;

public class VSQLCounterDetailViewer extends CounterDetailViewer {

  public VSQLCounterDetailViewer() {
    super();
  }
  
  protected void drawGraphics(Graphics g, Point pt, JComponent comp, List<GamePiece> pieces) {
    final Graphics2D g2d = (Graphics2D) g;
    final double os_scale = g2d.getDeviceConfiguration().getDefaultTransform().getScaleX();

    for (int i = 0; i < pieces.size(); i++) {
      GamePiece piece = (GamePiece) pieces.get(i);
      Rectangle pieceBounds = piece.getShape().getBounds();
      bounds.width += (int) (pieceBounds.width * graphicsZoomLevel) + borderWidth;
      bounds.height = Math.max(bounds.height, (int) (pieceBounds.height * graphicsZoomLevel) + borderWidth * 2);
    }
    bounds.width += borderWidth;
    bounds.y -= bounds.height;

    final Rectangle dbounds = new Rectangle(bounds);
    dbounds.x *= os_scale;
    dbounds.y *= os_scale;
    dbounds.width *= os_scale;
    dbounds.height *= os_scale;

    if (bounds.width > 0) {

      Rectangle visibleRect = comp.getVisibleRect();
      visibleRect.x *= os_scale;
      visibleRect.y *= os_scale;
      visibleRect.width *= os_scale;
      visibleRect.height *= os_scale;

      dbounds.x = Math.min(dbounds.x, visibleRect.x + visibleRect.width - dbounds.width);
      if (dbounds.x < visibleRect.x) {
        dbounds.x = visibleRect.x;
      }
      dbounds.y = Math.min(dbounds.y, visibleRect.y + visibleRect.height - dbounds.height) - (isTextUnderCounters() ? 15 : 0);
      int minY = visibleRect.y + (textVisible ? g.getFontMetrics().getHeight() + 6 : 0);
      if (dbounds.y < minY) {
        dbounds.y = minY;
      }

      g.setColor(bgColor);
      g.fillRect(dbounds.x, dbounds.y, dbounds.width, dbounds.height);
      g.setColor(fgColor);
      g.drawRect(dbounds.x - 1, dbounds.y - 1, dbounds.width + 1, dbounds.height + 1);
      g.drawRect(dbounds.x - 2, dbounds.y - 2, dbounds.width + 3, dbounds.height + 3);
      Shape oldClip = g.getClip();

      int borderOffset = borderWidth;
      double graphicsZoom = graphicsZoomLevel;
      for (int i = 0; i < pieces.size(); i++) {
        // Draw the next piece
        // pt is the location of the left edge of the piece
        GamePiece piece = (GamePiece) pieces.get(i);
        Rectangle pieceBounds = piece.getShape().getBounds();
        g.setClip(dbounds.x - 3, dbounds.y - 15, dbounds.width + 5, dbounds.height + 17);
        piece.draw(g, dbounds.x - (int) (pieceBounds.x * graphicsZoom * os_scale) + borderOffset, dbounds.y - (int) (pieceBounds.y * graphicsZoom * os_scale) + borderWidth, comp,
            graphicsZoom * os_scale);
        g.setClip(oldClip);

        if (isTextUnderCounters()) {
          String text = counterReportFormat.getText(piece);
          int x = bounds.x - (int) (pieceBounds.x * graphicsZoom) + borderOffset;
          int y = bounds.y + bounds.height + 10;
          drawLabel(g, new Point(x, y), text, LabelUtils.CENTER, LabelUtils.CENTER);
        }

        dbounds.translate((int) (pieceBounds.width * graphicsZoom * os_scale), 0);
        borderOffset += borderWidth;
      }
    }
  }
}
