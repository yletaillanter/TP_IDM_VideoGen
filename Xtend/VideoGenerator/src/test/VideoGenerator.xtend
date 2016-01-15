package test import java.util.HashMap
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.xtext.example.videogen.YogoStandaloneSetupGenerated
import org.xtext.example.videogen.yogo.VideoGen
import org.xtext.example.videogen.yogo.Mandatory
import org.junit.Test
import org.junit.Assert

class VideoGenerator {
	
	def loadVideoGenerator(URI uri) {
		new YogoStandaloneSetupGenerated().createInjectorAndDoEMFRegistration()
		var res = new ResourceSetImpl().getResource(uri, true);
		res.contents.get(0) as VideoGen
	}
	
	def saveVideoGenerator(URI uri, VideoGen pollS) {
		var Resource rs = new ResourceSetImpl().createResource(uri); 
		rs.getContents.add(pollS); 
		rs.save(new HashMap());
	}
	
	
	@Test
	def void test1() {
		// loading du fichier source
		var videoGen = loadVideoGenerator(URI.createURI("foo.yogo"))
		
		// On recup la premiere video et la CAST en mandatory
		var vidMandatory = videoGen.videoseqs.get(0) as Mandatory;
		
		//Test des attributs de la video
		Assert.assertEquals("The first video", vidMandatory.desc.description)
		Assert.assertEquals("video/1.mp4", vidMandatory.desc.path)
		Assert.assertEquals("v1", vidMandatory.desc.id)	
	}
}
