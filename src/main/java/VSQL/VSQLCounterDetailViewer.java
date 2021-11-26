package VSQL;

import java.awt.Graphics;
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

    for (int i = 0; i < pieces.size(); i++) {
      GamePiece piece = (GamePiece) pieces.get(i);
      Rectangle pieceBounds = piece.getShape().getBounds();
      bounds.width += (int) (pieceBounds.width * graphicsZoomLevel) + borderWidth;
      bounds.height = Math.max(bounds.height, (int) (pieceBounds.height * graphicsZoomLevel) + borderWidth * 2);
    }
    bounds.width += borderWidth;
    bounds.y -= bounds.height;

    if (bounds.width > 0) {

      Rectangle visibleRect = comp.getVisibleRect();
      bounds.x = Math.min(bounds.x, visibleRect.x + visibleRect.width - bounds.width);
      if (bounds.x < visibleRect.x)
        bounds.x = visibleRect.x;
      bounds.y = Math.min(bounds.y, visibleRect.y + visibleRect.height - bounds.height) - (isTextUnderCounters() ? 15 : 0);
      int minY = visibleRect.y + (textVisible ? g.getFontMetrics().getHeight() + 6 : 0);
      if (bounds.y < minY)
        bounds.y = minY;

      g.setColor(bgColor);
      g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
      g.setColor(fgColor);
      g.drawRect(bounds.x - 1, bounds.y - 1, bounds.width + 1, bounds.height + 1);
      g.drawRect(bounds.x - 2, bounds.y - 2, bounds.width + 3, bounds.height + 3);
      Shape oldClip = g.getClip();

      int borderOffset = borderWidth;
      double graphicsZoom = graphicsZoomLevel;
      for (int i = 0; i < pieces.size(); i++) {
        // Draw the next piece
        // pt is the location of the left edge of the piece
        GamePiece piece = (GamePiece) pieces.get(i);
        Rectangle pieceBounds = piece.getShape().getBounds();
        g.setClip(bounds.x - 3, bounds.y - 15, bounds.width + 5, bounds.height + 17);
        piece.draw(g, bounds.x - (int) (pieceBounds.x * graphicsZoom) + borderOffset, bounds.y - (int) (pieceBounds.y * graphicsZoom) + borderWidth, comp,
            graphicsZoom);
        g.setClip(oldClip);

        if (isTextUnderCounters()) {
          String text = counterReportFormat.getText(piece);
          int x = bounds.x - (int) (pieceBounds.x * graphicsZoom) + borderOffset;
          int y = bounds.y + bounds.height + 10;
          drawLabel(g, new Point(x, y), text, LabelUtils.CENTER, LabelUtils.CENTER);
        }

        bounds.translate((int) (pieceBounds.width * graphicsZoom), 0);
        borderOffset += borderWidth;
      }

    }
  }
  
}
