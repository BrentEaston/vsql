package VASL.build.module.map;

import java.awt.Dimension;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import VASSAL.Info;
import VASSAL.launch.TilingHandler;
import VASSAL.build.GameModule;
import VASSAL.tools.DataArchive;
import VASSAL.tools.image.ImageUtils;
import VASSAL.tools.image.tilecache.TileUtils;
import VASSAL.tools.io.FileArchive;
import VASSAL.tools.io.FileStore;
import VASSAL.tools.lang.Pair;

public class ASLTilingHandler extends VASSAL.launch.TilingHandler {
  public ASLTilingHandler(
    String aname,
    File cdir,
    Dimension tdim,
    int mhlim)
  {
    super(aname, cdir, tdim, mhlim);
  }

  @Override
  protected Pair<Integer,Integer> findImages(
    DataArchive archive,
    FileStore tcache,
    List<String> multi,
    List<Pair<String,IOException>> failed) throws IOException
  {
    int maxpix = 0; // number of pixels in the largest image
    int tcount = 0; // tile count

    final FileArchive fa = archive.getArchive();
    final String iname = fa.getFile().getName() + ".gif";

    // look at the first 1:1 tile
    final String tpath = TileUtils.tileName(iname, 0, 0, 1);

    // check whether the image is older than the tile
    final long imtime = fa.getMTime(iname);

    // skip images with fresh tiles
    if (imtime <= 0 || // time in archive might be goofy
        imtime > tcache.getMTime(tpath)) {
      final Dimension idim;
      // skip images with fresh tiles
      try {
        idim = getImageSize(archive, iname);
      }
      catch (IOException e) {
        // skip images we can't read
        failed.add(Pair.of(iname, e));
        return new Pair<>(0, 0);
      }

      // count the tiles at all sizes if we have more than one tile at 1:1
      final int t = TileUtils.tileCountAtScale(idim, tdim, 1) > 1 ?
                    TileUtils.tileCount(idim, tdim) : 0;

      if (t > 0) {
        tcount = t;
        multi.add(iname);
        maxpix = idim.width * idim.height;
      }
    }

    return new Pair<>(tcount, maxpix);
  }

  @Override
  protected StateMachineHandler createStateMachineHandler(int tcount, Future<Integer> fut) {
    return new StateMachineHandler() {
      @Override
      public void handleStart() {
      }

      @Override
      public void handleStartingImageState(String ipath) {
      }

      @Override
      public void handleTileWrittenState() {
      }

      @Override
      public void handleTilingFinishedState() {
      }

      @Override
      public void handleSuccess() {
      }

      @Override
      public void handleFailure() {
      }
    };
  }
}
