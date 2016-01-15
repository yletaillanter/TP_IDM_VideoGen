package app

import java.util.Random
import java.io.BufferedReader
import java.io.InputStreamReader
import org.xtext.example.videogen.YogoStandaloneSetupGenerated
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.xtext.example.videogen.yogo.VideoGen
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.common.util.URI
import java.util.HashMap
import org.xtext.example.videogen.yogo.Mandatory
import org.xtext.example.videogen.yogo.Optional
import org.xtext.example.videogen.yogo.Alternatives

class Util {
	
	private Random random;
	private final String PATH_VIDEO = "data/"
	var Runtime rt = Runtime.getRuntime()
	
	def loadVideoGenerator(URI uri) {
		new YogoStandaloneSetupGenerated().createInjectorAndDoEMFRegistration()
		var res = new ResourceSetImpl().getResource(uri, true)
		res.contents.get(0) as VideoGen
	}
	
	def saveVideoGenerator(URI uri, VideoGen pollS) {
		var Resource rs = new ResourceSetImpl().createResource(uri) 
		rs.getContents.add(pollS)
		rs.save(new HashMap())
	}
	
	def getOptional(){
		random = new Random()
		return random.nextBoolean;
	}
	
	def randomWithRange(int min, int max) {
   		var range = (max - min) + 1
   		return (Math.random() * range) + min as int
	}
	
	def getDuration(String videoName) {
		var path = ''
		var Process proc = rt.exec("/usr/local/bin/ffprobe -v error -select_streams v:0 -show_entries stream=duration -of default=noprint_wrappers=1:nokey=1 "+ PATH_VIDEO + videoName)
		var reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	    var line = "";
			    
	    while ((line = reader.readLine()) != null) {
			path = line
		}
		
		return path
	}
	
	def String videoToTS(String videoName){
		var Process proc = rt.exec("/usr/local/bin/ffmpeg -i "+  PATH_VIDEO + videoName +" -c copy -bsf h264_mp4toannexb " + PATH_VIDEO + "ts/ " + videoName +".ts")
		var reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))
	    var line = "";
	    var result ="";
			    
	    while ((line = reader.readLine()) != null) {
			result = line
		}
		return result
	}	
	
	def checkErrors(VideoGen videogen){
		/* 
		videogen.videoseqs.forEach[ video | 
			System.out.println(video.class.name)
		]
		*/
		
		//var vid = videogen.videoseqs.get(0) as Mandatory
		
		var nbVideos = videogen.videoseqs.length
		
		// Store existing path
		var path = new HashMap<String, String>
		// Store ID
		var ids = new HashMap<String, String>
		
		for (var i =0; i<nbVideos; i++) {
			var type = videogen.videoseqs.get(i).eClass.name
			if (type.equals("Mandatory")){
				var video = videogen.videoseqs.get(i) as Mandatory	

				if(ids.containsKey(video.desc.path)) {
					if(ids.get(video.desc.path).equals(video.desc.id)) {
						System.out.println("Error id must be unique")
					}
					else {
						ids.put(video.desc.path, video.desc.id)	
					}
				} 
				else {
					ids.put(video.desc.path, video.desc.id)	
				}
			}
			else if (type.equals("Optional")){
				if(getOptional) {
					var vidOptional = videogen.videoseqs.get(i) as Optional
					
					if(ids.containsKey(vidOptional.desc.path)) {
						if(ids.get(vidOptional.desc.path).equals(vidOptional.desc.id)) {
							System.out.println("Error id must be unique")
						}
						else {
							ids.put(vidOptional.desc.path, vidOptional.desc.id)	
						}
					} 
					else {
						ids.put(vidOptional.desc.path, vidOptional.desc.id)	
					}
				}
			}
			else {
				var vidAlternatives = videogen.videoseqs.get(i) as Alternatives
				//var nombreAleatoire = randomWithRange(0, vidAlternatives.videoDescriptions.length-1) as int
				/* 
				for (i=0; i<vidAlternatives.videoDescriptions.size; i++) {
					var vid = vidAlternatives.videoDescriptions.get(i) as Video
					if(ids.containsKey(vid.description.)) {
						if(ids.get(vidAlternatives. ).equals(vidAlternatives.desc.id)) {
							System.out.println("Error id must be unique")
						}
					else {
						ids.put(vidAlternatives.desc.path, vidAlternatives.desc.id)	
					}
				} 
				else {
					ids.put(vidAlternatives.desc.path, vidAlternatives.desc.id)	
				}
				}
*/
			}
		}
	}
}