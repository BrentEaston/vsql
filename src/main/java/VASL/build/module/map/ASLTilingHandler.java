package VASL.build.module.map;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import VASSAL.launch.TilingHandler;
import VASSAL.tools.DataArchive;
import VASSAL.tools.io.FileArchive;

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
  protected Iterable<String> getImagePaths(DataArchive archive) throws IOException {
    final FileArchive fa = archive.getArchive();
    return List.of(fa.getFile().getName() + ".gif");
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
