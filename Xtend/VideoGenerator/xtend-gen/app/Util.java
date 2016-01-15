package app;

import com.google.common.base.Objects;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.xtext.example.videogen.YogoStandaloneSetupGenerated;
import org.xtext.example.videogen.yogo.Alternatives;
import org.xtext.example.videogen.yogo.Mandatory;
import org.xtext.example.videogen.yogo.Optional;
import org.xtext.example.videogen.yogo.VideoDescription;
import org.xtext.example.videogen.yogo.VideoGen;
import org.xtext.example.videogen.yogo.VideoSeq;

@SuppressWarnings("all")
public class Util {
  private Random random;
  
  private final String PATH_VIDEO = "data/";
  
  private Runtime rt = Runtime.getRuntime();
  
  public VideoGen loadVideoGenerator(final URI uri) {
    VideoGen _xblockexpression = null;
    {
      YogoStandaloneSetupGenerated _yogoStandaloneSetupGenerated = new YogoStandaloneSetupGenerated();
      _yogoStandaloneSetupGenerated.createInjectorAndDoEMFRegistration();
      ResourceSetImpl _resourceSetImpl = new ResourceSetImpl();
      Resource res = _resourceSetImpl.getResource(uri, true);
      EList<EObject> _contents = res.getContents();
      EObject _get = _contents.get(0);
      _xblockexpression = ((VideoGen) _get);
    }
    return _xblockexpression;
  }
  
  public void saveVideoGenerator(final URI uri, final VideoGen pollS) {
    try {
      ResourceSetImpl _resourceSetImpl = new ResourceSetImpl();
      Resource rs = _resourceSetImpl.createResource(uri);
      EList<EObject> _contents = rs.getContents();
      _contents.add(pollS);
      HashMap<Object, Object> _hashMap = new HashMap<Object, Object>();
      rs.save(_hashMap);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public boolean getOptional() {
    Random _random = new Random();
    this.random = _random;
    return this.random.nextBoolean();
  }
  
  public double randomWithRange(final int min, final int max) {
    int range = ((max - min) + 1);
    double _random = Math.random();
    double _multiply = (_random * range);
    return (_multiply + ((int) min));
  }
  
  public String getDuration(final String videoName) {
    try {
      String path = "";
      Process proc = this.rt.exec((("/usr/local/bin/ffprobe -v error -select_streams v:0 -show_entries stream=duration -of default=noprint_wrappers=1:nokey=1 " + this.PATH_VIDEO) + videoName));
      InputStream _inputStream = proc.getInputStream();
      InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
      BufferedReader reader = new BufferedReader(_inputStreamReader);
      String line = "";
      while ((!Objects.equal((line = reader.readLine()), null))) {
        path = line;
      }
      return path;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String videoToTS(final String videoName) {
    try {
      Process proc = this.rt.exec(((((((("/usr/local/bin/ffmpeg -i " + this.PATH_VIDEO) + videoName) + " -c copy -bsf h264_mp4toannexb ") + this.PATH_VIDEO) + "ts/ ") + videoName) + ".ts"));
      InputStream _inputStream = proc.getInputStream();
      InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
      BufferedReader reader = new BufferedReader(_inputStreamReader);
      String line = "";
      String result = "";
      while ((!Objects.equal((line = reader.readLine()), null))) {
        result = line;
      }
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void checkErrors(final VideoGen videogen) {
    EList<VideoSeq> _videoseqs = videogen.getVideoseqs();
    int nbVideos = ((Object[])Conversions.unwrapArray(_videoseqs, Object.class)).length;
    HashMap<String, String> path = new HashMap<String, String>();
    HashMap<String, String> ids = new HashMap<String, String>();
    for (int i = 0; (i < nbVideos); i++) {
      {
        EList<VideoSeq> _videoseqs_1 = videogen.getVideoseqs();
        VideoSeq _get = _videoseqs_1.get(i);
        EClass _eClass = _get.eClass();
        String type = _eClass.getName();
        boolean _equals = type.equals("Mandatory");
        if (_equals) {
          EList<VideoSeq> _videoseqs_2 = videogen.getVideoseqs();
          VideoSeq _get_1 = _videoseqs_2.get(i);
          Mandatory video = ((Mandatory) _get_1);
          VideoDescription _desc = video.getDesc();
          String _path = _desc.getPath();
          boolean _containsKey = ids.containsKey(_path);
          if (_containsKey) {
            VideoDescription _desc_1 = video.getDesc();
            String _path_1 = _desc_1.getPath();
            String _get_2 = ids.get(_path_1);
            VideoDescription _desc_2 = video.getDesc();
            String _id = _desc_2.getId();
            boolean _equals_1 = _get_2.equals(_id);
            if (_equals_1) {
              System.out.println("Error id must be unique");
            } else {
              VideoDescription _desc_3 = video.getDesc();
              String _path_2 = _desc_3.getPath();
              VideoDescription _desc_4 = video.getDesc();
              String _id_1 = _desc_4.getId();
              ids.put(_path_2, _id_1);
            }
          } else {
            VideoDescription _desc_5 = video.getDesc();
            String _path_3 = _desc_5.getPath();
            VideoDescription _desc_6 = video.getDesc();
            String _id_2 = _desc_6.getId();
            ids.put(_path_3, _id_2);
          }
        } else {
          boolean _equals_2 = type.equals("Optional");
          if (_equals_2) {
            boolean _optional = this.getOptional();
            if (_optional) {
              EList<VideoSeq> _videoseqs_3 = videogen.getVideoseqs();
              VideoSeq _get_3 = _videoseqs_3.get(i);
              Optional vidOptional = ((Optional) _get_3);
              VideoDescription _desc_7 = vidOptional.getDesc();
              String _path_4 = _desc_7.getPath();
              boolean _containsKey_1 = ids.containsKey(_path_4);
              if (_containsKey_1) {
                VideoDescription _desc_8 = vidOptional.getDesc();
                String _path_5 = _desc_8.getPath();
                String _get_4 = ids.get(_path_5);
                VideoDescription _desc_9 = vidOptional.getDesc();
                String _id_3 = _desc_9.getId();
                boolean _equals_3 = _get_4.equals(_id_3);
                if (_equals_3) {
                  System.out.println("Error id must be unique");
                } else {
                  VideoDescription _desc_10 = vidOptional.getDesc();
                  String _path_6 = _desc_10.getPath();
                  VideoDescription _desc_11 = vidOptional.getDesc();
                  String _id_4 = _desc_11.getId();
                  ids.put(_path_6, _id_4);
                }
              } else {
                VideoDescription _desc_12 = vidOptional.getDesc();
                String _path_7 = _desc_12.getPath();
                VideoDescription _desc_13 = vidOptional.getDesc();
                String _id_5 = _desc_13.getId();
                ids.put(_path_7, _id_5);
              }
            }
          } else {
            EList<VideoSeq> _videoseqs_4 = videogen.getVideoseqs();
            VideoSeq _get_5 = _videoseqs_4.get(i);
            Alternatives vidAlternatives = ((Alternatives) _get_5);
          }
        }
      }
    }
  }
}
