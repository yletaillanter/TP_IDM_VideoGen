package test;

import java.util.HashMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Assert;
import org.junit.Test;
import org.xtext.example.videogen.YogoStandaloneSetupGenerated;
import org.xtext.example.videogen.yogo.Mandatory;
import org.xtext.example.videogen.yogo.VideoDescription;
import org.xtext.example.videogen.yogo.VideoGen;
import org.xtext.example.videogen.yogo.VideoSeq;

@SuppressWarnings("all")
public class VideoGenerator {
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
  
  @Test
  public void test1() {
    URI _createURI = URI.createURI("foo.yogo");
    VideoGen videoGen = this.loadVideoGenerator(_createURI);
    EList<VideoSeq> _videoseqs = videoGen.getVideoseqs();
    VideoSeq _get = _videoseqs.get(0);
    Mandatory vidMandatory = ((Mandatory) _get);
    VideoDescription _desc = vidMandatory.getDesc();
    String _description = _desc.getDescription();
    Assert.assertEquals("The first video", _description);
    VideoDescription _desc_1 = vidMandatory.getDesc();
    String _path = _desc_1.getPath();
    Assert.assertEquals("video/1.mp4", _path);
    VideoDescription _desc_2 = vidMandatory.getDesc();
    String _id = _desc_2.getId();
    Assert.assertEquals("v1", _id);
  }
}
